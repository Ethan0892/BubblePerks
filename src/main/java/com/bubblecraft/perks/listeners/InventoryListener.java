package com.bubblecraft.perks.listeners;

import com.bubblecraft.perks.BubblePerks;
import com.bubblecraft.perks.models.Perk;
import com.bubblecraft.perks.utils.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Handles inventory click events for the perks GUI
 */
public class InventoryListener implements Listener {
    
    private final BubblePerks plugin;
    
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
        
    // Determine which perk (if any) was clicked by slot
    int rawSlot = event.getRawSlot();
    String perkId = findPerkIdBySlot(rawSlot);
    if (perkId == null) return;

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
     * Handle perk purchase logic
     */
    private void handlePerkPurchase(Player player, String perkId) {
        Perk perk = plugin.getPerkManager().getPerk(perkId);
        if (perk == null) return;
        
        // Check if already purchased
        if (plugin.getPerkManager().hasPlayerPurchased(player, perkId)) {
            player.sendMessage(ColorUtils.colorize("&cYou already own this perk!"));
            return;
        }
        
        // Attempt purchase
        if (plugin.getPerkManager().purchasePerk(player, perkId)) {
            player.sendMessage(ColorUtils.colorize("&a✓ Successfully purchased " + perk.getName() + "!"));
            // Play XP level-up sound on success
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.1f);
            
            // Refresh GUI
            plugin.getPerksGUI().openGUI(player);
        } else {
            player.sendMessage(ColorUtils.colorize("&cFailed to purchase perk! Check requirements and balance."));
        }
    }

    /**
     * Find a perk id by the configured GUI slot
     */
    private String findPerkIdBySlot(int slot) {
        // Iterate all perks and check their configured slot
        for (Perk perk : plugin.getPerkManager().getAllPerks()) {
            String id = perk.getId();
            String key = null;

            if (perk.getType().name().equalsIgnoreCase("COMMAND")) {
                key = "gui.slots.utilities." + id;
            } else if (id.startsWith("homes_")) {
                String suffix = id.substring("homes_".length());
                key = "gui.slots.homes." + suffix;
            } else if (id.startsWith("vault_")) {
                String suffix = id.substring("vault_".length());
                key = "gui.slots.vaults." + suffix;
            } else if (id.startsWith("job_")) {
                String suffix = id.substring("job_".length());
                key = "gui.slots.jobs." + suffix;
            }

            if (key == null) continue;

            int configured = plugin.getConfigManager().getConfig().getInt(key, -1);
            if (configured == slot) return id;
        }

        return null;
    }
    
    /**
     * Extract perk ID from display name
     */
    private String extractPerkId(String displayName) {
        // Simple mapping based on display names
        displayName = ColorUtils.stripColor(displayName).toLowerCase();
        
        if (displayName.contains("grindstone")) return "grindstone";
        if (displayName.contains("craft")) return "workbench";
        if (displayName.contains("anvil")) return "anvil";
        if (displayName.contains("echest") || displayName.contains("ender")) return "enderchest";
        if (displayName.contains("cartography")) return "cartographytable";
        if (displayName.contains("stonecutter")) return "stonecutter";
        if (displayName.contains("home")) return "homes_upgrade";
        if (displayName.contains("vault")) return "vault_upgrade";
        if (displayName.contains("job")) return "job_upgrade";
        
        return null;
    }
}
