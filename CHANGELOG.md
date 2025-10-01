# BubblePerks Changelog

## Version 2.0.0 (2025-10-01)

### 🎉 Major Features
- **CoinsEngine Integration**: Direct integration with CoinsEngine v2.5.0 via reflection-based API
  - No compile-time dependency required
  - Automatic detection and fallback to Vault if unavailable
  - Supports custom currency IDs (configurable in config.yml)
  - Smart delegation system between CoinsEngine and Vault

### ✨ Enhancements
- **Improved GUI Design**: Complete overhaul of perks panel
  - Decorative borders and separators
  - Category headers (Utility Commands, Home Upgrades, Vault Upgrades, Job Upgrades)
  - Better visual organization with glass pane dividers
  - Enhanced player info display with balance formatting
  - Glow effect on purchased perks

### 🐛 Bug Fixes
- Fixed slot mapping mismatch between GUI rendering and click handlers
- Fixed requirements check that was blocking all purchases (removed incorrect XP level validation)
- Fixed message placeholder replacement ({prefix} now properly replaced)
- Fixed hex color codes not rendering in GUI (replaced with named colors)
- Fixed NullPointerException when EconomyManager not initialized
- Fixed purchase system to properly deduct from CoinsEngine balance

### 🔧 Technical Improvements
- Reflection-based CoinsEngine API calls for better compatibility
- Always initialize EconomyManager with smart delegation pattern
- Cleaner codebase with production-ready logging
- Removed debug logging spam for better performance
- Better error handling and graceful fallbacks

### 📝 Configuration
- New `economy.use-coinsengine` setting (default: true)
- New `economy.coinsengine-currency` setting (default: 'coins')
- Updated descriptions and comments for clarity

---

## Version 1.0.0 (Initial Release)

### Features
- Basic perks system with GUI
- Vault economy integration
- LuckPerms permission management
- PlaceholderAPI support
- Multiple perk categories: Utilities, Homes, Vaults, Jobs
- Configurable perk requirements and pricing
