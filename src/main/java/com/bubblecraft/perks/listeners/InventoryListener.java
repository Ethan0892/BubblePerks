package com.bubblecraft.perks.listeners;

import com.bubblecraft.perks.BubblePerks;
import com.bubblecraft.perks.models.Perk;
import com.bubblecraft.perks.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

/**
 * Handles inventory click events for the perks GUI
 * Enhanced with robust perk detection and purchase handling
 */
public class InventoryListener implements Listener {
    
    private final BubblePerks plugin;
    
    // Hardcoded slot mappings for the new GUI layout
    private static final int[] HOME_TIER_SLOTS = {21, 22, 23};
    private static final int[] VAULT_TIER_SLOTS = {30, 31, 32};
    private static final int[] JOB_TIER_SLOTS = {39, 40, 41};
    private static final int CLOSE_BUTTON_SLOT = 49;
    
    public InventoryListener(BubblePerks plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        // Check if it's our GUI - check inventory size and if it's the top inventory
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;
        if (event.getView().getTopInventory().getSize() != 54) return;
        
        // Additional check: look for our GUI title pattern
        String title = ColorUtils.stripColor(ColorUtils.toLegacy(event.getView().title()));
        if (!title.toLowerCase().contains("bubble") && !title.contains("ᴘᴇʀᴋꜱ")) {
            return;
        }
        
        // ALWAYS log when GUI is clicked for debugging
        // Always prevent item movement within our GUI
        event.setCancelled(true);

        // Disallow all risky click types explicitly
        ClickType click = event.getClick();
        switch (click) {
            case DOUBLE_CLICK:
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
            case NUMBER_KEY:
            case MIDDLE:
            case SWAP_OFFHAND:
            case DROP:
            case CONTROL_DROP:
                return; // already cancelled
            default:
                // proceed to handle logical clicks on our items
        }
        
        int slot = event.getRawSlot();
        
        // Handle close button
        if (slot == CLOSE_BUTTON_SLOT) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
            return;
        }
        
        // Get clicked item
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        // Skip decorative items (glass panes that aren't upgrade tiers)
        Material material = clickedItem.getType();
        if (isDecorativeItem(material, slot)) {
            return;
        }
        
        // Determine which perk was clicked by slot
        String perkId = findPerkIdBySlot(slot);
        if (perkId == null) {
            return;
        }

        // Handle perk purchases by id
        handlePerkPurchase(player, perkId);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        // Check if it's our GUI - check inventory size first
        if (event.getView().getTopInventory().getSize() != 54) return;
        
        // Additional check: look for our GUI title pattern
        String title = ColorUtils.stripColor(ColorUtils.toLegacy(event.getView().title()));
        if (!title.toLowerCase().contains("bubble") && !title.contains("ᴘᴇʀᴋꜱ")) {
            return;
        }

        // If any dragged slot is in the top inventory, cancel
        int topSize = event.getView().getTopInventory().getSize();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < topSize) {
                event.setCancelled(true);
                return;
            }
        }
        // Also block dragging into bottom while GUI open to prevent inserting into GUI by distribution
        event.setCancelled(true);
    }
    
    /**
     * Handle perk purchase logic with enhanced error reporting
     */
    private void handlePerkPurchase(Player player, String perkId) {
        Perk perk = plugin.getPerkManager().getPerk(perkId);
        if (perk == null) {
            plugin.getLogger().warning("Perk not found: " + perkId);
            return;
        }
        
        // Check if already purchased
        if (plugin.getPerkManager().hasPlayerPurchased(player, perkId)) {
            String message = getFormattedMessage("perks.already-owned", "&cYou already own this perk!");
            player.sendMessage(ColorUtils.colorize(message));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            return;
        }
        
        // Check requirements first
        double cost = perk.getCost();
        double balance = plugin.getEconomyManager().getBalance(player);
        
        if (balance < cost) {
            String message = getFormattedMessage("economy.insufficient-funds", "&cYou need {amount} to purchase this perk!");
            message = message.replace("{amount}", plugin.getEconomyManager().format(cost));
            player.sendMessage(ColorUtils.colorize(message));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            return;
        }
        
        // Attempt purchase
        boolean success = plugin.getPerkManager().purchasePerk(player, perkId);
        
        if (success) {
            String message = getFormattedMessage("economy.purchase-success", "&a✓ Successfully purchased {perk}!");
            message = message.replace("{perk}", ColorUtils.stripColor(perk.getName()));
            player.sendMessage(ColorUtils.colorize(message));
            
            // Play success sound
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.1f);
            
            // Refresh GUI after a short delay to show the glow effect
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getPerksGUI().openGUI(player);
            }, 5L);
        } else {
            String message = getFormattedMessage("economy.purchase-failed", "&cFailed to purchase perk!");
            player.sendMessage(ColorUtils.colorize(message));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().warning("Purchase failed for " + player.getName() + " perk: " + perkId);
                plugin.getLogger().warning("Balance: " + balance + " Cost: " + cost);
            }
        }
    }

    /**
     * Check if an item is decorative (borders, separators, headers, etc.)
     */
    private boolean isDecorativeItem(Material material, int slot) {
        // Border slots (top and bottom rows, excluding certain functional slots)
        int[] borderSlots = {0, 1, 2, 3, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53};
        for (int borderSlot : borderSlots) {
            if (slot == borderSlot) return true;
        }
        
        // Separator slots (left side of content rows)
        int[] separatorSlots = {9, 18, 27, 36};
        for (int sepSlot : separatorSlots) {
            if (slot == sepSlot) return true;
        }
        
        // Header slots (category headers)
        int[] headerSlots = {10, 19, 28, 37};
        for (int headerSlot : headerSlots) {
            if (slot == headerSlot) return true;
        }
        
        // Status display slots (not purchasable)
        int[] statusSlots = {20, 29, 38};
        for (int statusSlot : statusSlots) {
            if (slot == statusSlot) return true;
        }
        
        // Filler glass panes
        if (material.name().contains("GLASS_PANE") && !isUpgradeTierSlot(slot)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if a slot is an upgrade tier slot
     */
    private boolean isUpgradeTierSlot(int slot) {
        for (int tierSlot : HOME_TIER_SLOTS) {
            if (slot == tierSlot) return true;
        }
        for (int tierSlot : VAULT_TIER_SLOTS) {
            if (slot == tierSlot) return true;
        }
        for (int tierSlot : JOB_TIER_SLOTS) {
            if (slot == tierSlot) return true;
        }
        return false;
    }
    
    /**
     * Find a perk ID by the GUI slot using the new hardcoded layout
     */
    private String findPerkIdBySlot(int slot) {
        // Utility command perks (slots 11-16)
        if (slot >= 11 && slot <= 16) {
            int index = slot - 11;
            String[] utilityPerkIds = {"grindstone", "workbench", "anvil", "enderchest", "cartographytable", "stonecutter"};
            if (index < utilityPerkIds.length) {
                return utilityPerkIds[index];
            }
        }
        
        // Home upgrades (slots 21-23)
        if (slot == 21) return "homes_tier1";
        if (slot == 22) return "homes_tier2";
        if (slot == 23) return "homes_tier3";
        
        // Vault upgrades (slots 30-32)
        if (slot == 30) return "vault_tier1";
        if (slot == 31) return "vault_tier2";
        if (slot == 32) return "vault_tier3";
        
        // Job upgrades (slots 39-41)
        if (slot == 39) return "job_tier1";
        if (slot == 40) return "job_tier2";
        if (slot == 41) return "job_tier3";
        
        return null;
    }
    
    /**
     * Get formatted message with prefix replacement
     */
    private String getFormattedMessage(String path, String defaultMessage) {
        String prefix = plugin.getConfigManager().getMessagesConfig().getString("prefix", "<gold><b>Bubble<aqua>Perks <dark_gray>»");
        String message = plugin.getConfigManager().getMessagesConfig().getString(path, defaultMessage);
        return message.replace("{prefix}", prefix);
    }
}
