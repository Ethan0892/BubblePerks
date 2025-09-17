# Server Setup Guide for BubblePerks

This guide will help you set up BubblePerks on your Minecraft server.

## Prerequisites

### Required Plugins
1. **Vault** - Economy API
   - Download: https://dev.bukkit.org/projects/vault
   - Used for: Economy integration

2. **LuckPerms** - Permission management
   - Download: https://luckperms.net/download
   - Used for: Granting permissions when perks are purchased

### Optional Plugins (for full functionality)
3. **PlaceholderAPI** - Placeholder support
   - Download: https://www.spigotmc.org/resources/placeholderapi.6245/
   - Used for: Custom placeholders

4. **Essentials** - Essential commands
   - Download: https://essentialsx.net/downloads.html
   - Used for: Virtual utility commands (grindstone, workbench, anvil, echest)

5. **Jobs** - Job system
   - Download: https://www.spigotmc.org/resources/jobs-reborn.4216/
   - Used for: Job slot upgrades

6. **AxVaults** - Vault system
   - Download: https://www.spigotmc.org/resources/axvaults.89893/
   - Used for: Vault upgrades

7. **CoinsEngine** or other economy plugin
   - Used for: Alternative economy support

## Installation Steps

### 1. Install Dependencies
1. Download and install the required plugins listed above
2. Start your server to generate configuration files
3. Stop your server

### 2. Install BubblePerks
1. Place the BubblePerks JAR file in your `plugins` folder
2. Start your server
3. Stop your server (this generates the default config files)

### 3. Configure Economy
Edit your economy plugin configuration to set up currency. For example, if using Essentials:

```yaml
# In Essentials config.yml
economy:
  currency-symbol: '🫧'
  currency-format: '%d %s'
```

### 4. Configure LuckPerms
Set up LuckPerms to work with your server:

```bash
# In console or in-game (as admin)
/lp creategroup default
/lp group default permission set essentials.home true
/lp group default permission set essentials.sethome true
```

### 5. Configure BubblePerks
Edit the configuration files in `plugins/BubblePerks/`:

#### config.yml
```yaml
# Enable debug mode for testing
debug: true

# Configure integrations based on your installed plugins
integrations:
  placeholderapi: true  # Set to false if not using PlaceholderAPI
  essentials: true      # Set to false if not using Essentials
  jobs: true           # Set to false if not using Jobs
  axvaults: true       # Set to false if not using AxVaults
  coinsengine: false   # Set to true if using CoinsEngine
```

#### perks.yml
Adjust perk costs and permissions according to your server's economy:

```yaml
perks:
  grindstone:
    cost: 100.0  # Adjust cost as needed
    permission: 'essentials.grindstone'
```

### 6. Set Up Permissions

#### Default Player Permissions
```bash
/lp group default permission set bubbleperks.use true
```

#### Admin Permissions
```bash
/lp group admin permission set bubbleperks.admin true
/lp group admin permission set bubbleperks.bypass true
```

### 7. Test the Setup
1. Start your server
2. Join as a player
3. Run `/perks` to open the GUI
4. Try purchasing a perk to test functionality

## Common Permission Nodes

### Essentials
- `essentials.grindstone` - Access to /grindstone
- `essentials.workbench` - Access to /workbench
- `essentials.anvil` - Access to /anvil
- `essentials.enderchest` - Access to /enderchest
- `essentials.cartographytable` - Access to /cartographytable
- `essentials.stonecutter` - Access to /stonecutter
- `essentials.sethome.multiple.tier1` - 8 homes total
- `essentials.sethome.multiple.tier2` - 13 homes total
- `essentials.sethome.multiple.tier3` - 20 homes total

### Jobs
- `jobs.max.4` - 4 job slots
- `jobs.max.5` - 5 job slots
- `jobs.max.6` - 6 job slots

### AxVaults
- `axvaults.vault.2` - Access to vault 2
- `axvaults.vault.3` - Access to vault 3
- `axvaults.vault.4` - Access to vault 4
- `axvaults.vault.5` - Access to vault 5

## Troubleshooting

### Common Issues

1. **"Plugin not found" error**
   - Ensure all required dependencies are installed
   - Check server console for specific missing plugins

2. **Perks not working after purchase**
   - Verify LuckPerms is installed and working
   - Check that permission nodes match your plugin configurations
   - Enable debug mode to see detailed logs

3. **Economy integration not working**
   - Ensure Vault is installed
   - Verify your economy plugin is compatible with Vault
   - Check `/plugins` to ensure all plugins are enabled

4. **GUI not displaying correctly**
   - Update to the latest version of Paper or Spigot
   - Check for resource pack conflicts
   - Verify MiniMessage support is available

### Debug Mode
Enable debug mode in config.yml to get detailed logging:

```yaml
debug: true
```

### Console Commands for Testing
```bash
# Check plugin status
/plugins

# Test economy
/balance <player>
/eco give <player> 1000

# Test permissions
/lp user <player> permission check bubbleperks.use
/lp user <player> permission set essentials.grindstone true

# Reload BubblePerks
/perks reload
```

## Support

If you need additional help:
1. Check the console for error messages
2. Enable debug mode for detailed logs
3. Verify all dependencies are correctly installed
4. Test with a minimal setup first, then add features gradually
