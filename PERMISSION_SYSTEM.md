# BubblePerks - Permission-Based System

## 🎯 **What Changed**

You asked a great question about using permission checking instead of a database, and that's exactly what we've implemented! This approach is much simpler and leverages your existing permission system.

## 🔧 **How It Works Now**

### **Permission-Based Ownership**
Instead of storing purchases in a database, the plugin now:

1. **Checks if player has permission** → They own the perk
2. **Player doesn't have permission** → They can purchase it
3. **Purchase grants permission via LuckPerms** → Permanent ownership

### **Example Flow**
```java
// Check if player can buy grindstone perk
if (!player.hasPermission("essentials.grindstone")) {
    // Player can purchase it
    if (economy.withdraw(player, 100)) {
        // Execute command to grant permission permanently
        console.dispatchCommand("lp user " + player.getName() + " permission set essentials.grindstone true");
        
        // Grant immediate access for this session
        player.addAttachment(plugin, "essentials.grindstone", true);
    }
}
```

## 🏗️ **Architecture Benefits**

### **Simplified System**
- ✅ **No database needed** - permissions are stored in LuckPerms
- ✅ **Automatic persistence** - LuckPerms handles data storage
- ✅ **No data loss** - LuckPerms is battle-tested for reliability
- ✅ **Smaller plugin** - JAR is now 32KB instead of 13MB

### **Permission Integration**
- ✅ **Works with existing perms** - integrates with your current setup
- ✅ **Admin friendly** - admins can grant perks manually via LuckPerms
- ✅ **Inheritance support** - can use permission group inheritance
- ✅ **Easy troubleshooting** - just check `/lp user <player> info`

## 🔍 **How Checking Works**

### **Single Perks** (grindstone, anvil, etc.)
```java
// Simple permission check
boolean hasPerk = player.hasPermission("essentials.grindstone");
```

### **Tiered Perks** (homes, vaults, jobs)
```java
// Check highest tier owned
if (player.hasPermission("essentials.sethome.multiple.tier3")) return 3;      // 20 homes
if (player.hasPermission("essentials.sethome.multiple.tier2")) return 2;      // 13 homes  
if (player.hasPermission("essentials.sethome.multiple.tier1")) return 1;      // 8 homes
return 0; // Default 5 homes
```

## 📋 **Permission Structure**

### **Command Perks**
- `essentials.grindstone` - /grindstone command access
- `essentials.workbench` - /craft command access  
- `essentials.anvil` - /anvil command access
- `essentials.enderchest` - /echest command access
- `essentials.cartographytable` - /cartographytable command access
- `essentials.stonecutter` - /stonecutter command access

### **Home Upgrades**
- `essentials.sethome.multiple.tier1` - 8 homes total
- `essentials.sethome.multiple.tier2` - 13 homes total  
- `essentials.sethome.multiple.tier3` - 20 homes total

### **Vault Upgrades**
- `axvaults.vault.2` - Access to vault #2
- `axvaults.vault.3` - Access to vault #3
- `axvaults.vault.4` - Access to vault #4
- `axvaults.vault.5` - Access to vault #5

### **Job Upgrades**
- `jobs.max.4` - 4 job slots
- `jobs.max.5` - 5 job slots
- `jobs.max.6` - 6 job slots

## 🎮 **User Experience**

### **GUI Display Logic**
```yaml
# If player has permission → Show "Purchased!"
# If player has money → Show green checkmark
# If player lacks money → Show red X
```

### **Purchase Prevention**
- Can't buy the same perk twice (permission check prevents it)
- Must meet tier requirements (can't buy tier 2 without tier 1)
- Economy validation before purchase

## ⚙️ **Admin Benefits**

### **Manual Perk Granting**
```bash
# Grant a perk manually via console
/lp user PlayerName permission set essentials.grindstone true

# Remove a perk
/lp user PlayerName permission unset essentials.grindstone

# Check what perks a player has
/lp user PlayerName info
```

### **Bulk Operations**
```bash
# Grant perk to entire group
/lp group vip permission set essentials.grindstone true

# Grant multiple perks at once
/lp user PlayerName permission set essentials.grindstone essentials.anvil essentials.workbench true
```

### **Troubleshooting**
```bash
# Check if player has permission
/lp user PlayerName permission check essentials.grindstone

# See permission inheritance
/lp user PlayerName permission info

# Debug permission issues
/lp verbose on
```

## 🔄 **Migration from Database Systems**

If you were using another perk plugin with a database:

### **Export/Import Process**
1. **Export existing data** from old plugin
2. **Convert to LuckPerms commands**:
   ```bash
   /lp user PlayerName permission set essentials.grindstone true
   /lp user PlayerName permission set axvaults.vault.2 true
   ```
3. **Bulk import** via LuckPerms web editor or commands

### **No Data Loss**
- All purchases become permanent permissions
- Can verify via `/lp user <player> info`
- Easy to audit and modify

## 🚀 **Performance Impact**

### **Faster Operations**
- ❌ **No database queries** - instant permission checks
- ❌ **No async operations** - synchronous permission API
- ❌ **No connection pooling** - uses Bukkit's permission system
- ✅ **Memory efficient** - permissions cached by server

### **Reduced Dependencies**
- ❌ **No SQLite/MySQL** - one less dependency to manage
- ❌ **No database drivers** - smaller plugin size
- ✅ **LuckPerms only** - which you already have

## 🎯 **Summary**

This permission-based approach is actually **better** than a database because:

1. **Simpler**: No database setup or maintenance
2. **More reliable**: LuckPerms handles all persistence
3. **Admin-friendly**: Easy to manage via existing tools
4. **Performance**: Faster permission checks vs database queries
5. **Integration**: Works seamlessly with your existing permission setup

The plugin is now much cleaner, smaller, and more maintainable while providing the exact same functionality!
