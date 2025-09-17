# BubblePerks

A modern, feature-rich perks system for Minecraft 1.21.8+ servers. This plugin allows players to purchase various upgrades and utility commands using in-game currency.

## Features

- **Beautiful GUI**: Custom-designed perks menu with gradient text and modern styling
- **Multiple Perk Types**: Commands, upgrades, utilities, and cosmetics
- **Economy Integration**: Full Vault API support for economy systems
- **PlaceholderAPI Support**: Custom placeholders for other plugins
- **Permission Integration**: Automatic permission granting via LuckPerms
- **Configurable**: Fully customizable perks, costs, and rewards
- **Database Support**: SQLite and MySQL support for data persistence
- **Multi-tier Upgrades**: Progressive upgrade system for homes, vaults, and jobs

## Perk Categories

### Command Perks
- **Grindstone**: Access to `/grindstone` command (virtual grindstone)
- **Workbench**: Access to `/craft` command (virtual crafting table)
- **Anvil**: Access to `/anvil` command (virtual anvil)
- **Ender Chest**: Access to `/echest` command (virtual ender chest)
- **Cartography Table**: Access to `/cartographytable` command (virtual cartography table)
- **Stonecutter**: Access to `/stonecutter` command (virtual stonecutter)

### Upgrade Perks
- **Homes**: Increase maximum home count (3 tiers: +3, +5, +7)
- **Vaults**: Unlock additional vaults (3 tiers: +1, +1, +2)
- **Jobs**: Increase job slots (3 tiers: +1, +1, +1)

## Commands

- `/perks` - Open the perks GUI
- `/perks help` - Show help information
- `/perks reload` - Reload plugin configuration (admin only)

## Permissions

- `bubbleperks.use` - Basic access to perks system (default: true)
- `bubbleperks.admin` - Administrative access (default: op)
- `bubbleperks.bypass` - Bypass all perk requirements (default: false)

## Placeholders

When PlaceholderAPI is installed, the following placeholders are available:

- `%bubbleperks_has_<perkid>%` - Check if player has purchased a perk
- `%bubbleperks_level_<perkid>%` - Get player's perk level
- `%bubbleperks_total_perks%` - Get total number of perks owned by player

## Dependencies

### Required
- **Vault** - Economy integration
- **LuckPerms** - Permission management

### Optional
- **PlaceholderAPI** - Placeholder support
- **Essentials** - Command perks integration
- **Jobs** - Job slot upgrades
- **AxVaults** - Vault upgrades
- **CoinsEngine** - Alternative economy support

## Installation

1. Download the latest release from the releases page
2. Place the JAR file in your `plugins` folder
3. Install required dependencies (Vault, LuckPerms)
4. Restart your server
5. Configure the plugin files in `plugins/BubblePerks/`
6. Reload with `/perks reload`

## Configuration

### config.yml
Main plugin configuration including database settings, GUI options, and integrations.

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
