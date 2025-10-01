# BubblePerks

> **Version 2.0.0** - A modern, feature-rich perks system for Minecraft 1.21.8+ servers with native CoinsEngine support.

A comprehensive perks system that allows players to purchase various upgrades and utility commands using in-game currency through an elegant GUI.

## ✨ Features

- **Beautiful GUI**: Modern perks menu with gradient text, borders, and category sections
- **CoinsEngine Integration**: Native support for CoinsEngine v2.5.0+ with automatic currency detection
- **Vault Compatibility**: Seamless fallback to Vault economy systems
- **Multiple Perk Types**: Commands, upgrades, utilities, and progressive tiers
- **PlaceholderAPI Support**: Custom placeholders for integration with other plugins
- **Permission Integration**: Automatic permission granting via LuckPerms
- **Fully Configurable**: Customizable perks, costs, requirements, and rewards
- **Database Support**: SQLite and MySQL support for data persistence
- **Multi-tier Upgrades**: Progressive upgrade system for homes, vaults, and jobs

## 🎮 Perk Categories

### Utility Command Perks
- **Grindstone**: Access to `/grindstone` command (virtual grindstone)
- **Workbench**: Access to `/craft` command (virtual crafting table)
- **Anvil**: Access to `/anvil` command (virtual anvil)
- **Ender Chest**: Access to `/echest` command (virtual ender chest)
- **Cartography Table**: Access to `/cartographytable` command (virtual cartography table)
- **Stonecutter**: Access to `/stonecutter` command (virtual stonecutter)

### Progressive Upgrade Perks
- **Homes**: Increase maximum home count (3 tiers: +3, +5, +7 homes)
- **Vaults**: Unlock additional vaults (3 tiers: +1, +1, +2 vaults)
- **Jobs**: Increase job slots (3 tiers: +1, +1, +1 job slots)


## 📋 Commands

- `/perks` - Open the perks GUI
- `/perks help` - Show help information
- `/perks reload` - Reload plugin configuration (admin only)

## 🔐 Permissions

- `bubbleperks.use` - Basic access to perks system (default: true)
- `bubbleperks.admin` - Administrative access (default: op)
- `bubbleperks.bypass` - Bypass all perk requirements (default: false)

## 🏷️ Placeholders

When PlaceholderAPI is installed, the following placeholders are available:

- `%bubbleperks_has_<perkid>%` - Check if player has purchased a perk
- `%bubbleperks_level_<perkid>%` - Get player's perk level
- `%bubbleperks_total_perks%` - Get total number of perks owned by player

## 📦 Dependencies

### Required
- **Vault** - Economy integration (fallback)
- **LuckPerms** - Permission management

### Optional
- **CoinsEngine** - Primary economy system (recommended, v2.5.0+)
- **PlaceholderAPI** - Placeholder support
- **Essentials** - Command perks integration & economy fallback
- **Jobs** - Job slot upgrades
- **AxVaults** - Vault upgrades

## 🚀 Installation

1. Download the latest release (`BubblePerks-2.0.0.jar`)
2. Place the JAR file in your `plugins` folder
3. Install required dependencies:
   - **Vault** (economy bridge)
   - **LuckPerms** (permissions)
   - **CoinsEngine** (recommended for currency) OR **Essentials** (economy fallback)
4. Restart your server
5. Configure the plugin files in `plugins/BubblePerks/`
6. Set your currency ID in `config.yml` under `economy.coinsengine-currency`
7. Reload with `/perks reload`

## ⚙️ Configuration

### config.yml
Main plugin configuration including economy settings, GUI options, and integrations.

**Economy Settings (v2.0.0+)**:
```yaml
economy:
  currency-symbol: '🫧'
  format-thousands: true
  coinsengine-currency: 'coins'  # Your CoinsEngine currency ID
  use-coinsengine: true           # Use CoinsEngine instead of Vault
```


### perks.yml
Define all available perks including costs, permissions, commands, and rewards.

### messages.yml
Customize all plugin messages and text displayed to players.

## API Usage

```java
// Get the plugin instance
BubblePerks plugin = (BubblePerks) Bukkit.getPluginManager().getPlugin("BubblePerks");

// Check if player has a perk
boolean hasPerk = plugin.getPerkManager().hasPlayerPurchased(player, "grindstone");

// Get player's perk level
int level = plugin.getPerkManager().getPlayerPerkLevel(player, "homes_tier1");

// Purchase a perk programmatically
boolean success = plugin.getPerkManager().purchasePerk(player, "workbench");
```

## Building

This project uses Maven for dependency management and building.

```bash
git clone https://github.com/BubbleCraft/BubblePerks.git
cd BubblePerks
mvn clean package
```

The compiled JAR will be in the `target` directory.

## Support

For support, bug reports, or feature requests, please open an issue on the GitHub repository.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
