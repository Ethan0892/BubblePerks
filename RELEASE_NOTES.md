# 🎉 BubblePerks v2.0.0 - Production Release

## What's New

### CoinsEngine Native Integration 🫧
BubblePerks now supports **CoinsEngine v2.5.0+** directly! The plugin automatically detects CoinsEngine and uses it for economy transactions. If CoinsEngine isn't available, it seamlessly falls back to Vault/Essentials.

### Key Features
- ✅ **Reflection-based API** - No compile-time dependency needed
- ✅ **Auto-detection** - Works with any CoinsEngine currency
- ✅ **Smart fallback** - Vault integration still available
- ✅ **Zero config** - Just drop in and it works
- ✅ **Production ready** - Debug logging removed, optimized performance

## Installation

1. **Download**: `BubblePerks-2.0.0.jar` from `target/` folder
2. **Upload**: Place in your server's `plugins/` folder
3. **Restart**: Restart your server (not reload)
4. **Configure**: Edit `plugins/BubblePerks/config.yml` if needed
5. **Test**: Run `/perks` and purchase a perk

## Configuration

### Default Settings (works out of the box):
```yaml
economy:
  use-coinsengine: true          # Enable CoinsEngine
  coinsengine-currency: 'coins'  # Default currency ID
```

### Custom Currency Setup:
If your CoinsEngine uses a different currency ID:
1. Check your currency ID: `/coins balance` or look in `plugins/CoinsEngine/currencies/`
2. Update `config.yml`:
   ```yaml
   economy:
     coinsengine-currency: 'your_currency_id'
   ```
3. Reload: `/perks reload`

## Expected Startup Logs

✅ **Success** (CoinsEngine detected):
```
[BubblePerks] Hooked into CoinsEngine with currency: coins
[BubblePerks] Using CoinsEngine for economy!
```

⚠️ **Fallback** (Vault used instead):
```
[BubblePerks] Using Vault for economy
[BubblePerks] Hooked into economy: EssentialsX Economy
```

## Testing Checklist

- [ ] `/perks` opens GUI without errors
- [ ] Balance shows correctly in GUI
- [ ] Clicking a perk attempts purchase
- [ ] Money deducts from CoinsEngine (`/coins balance`)
- [ ] Permission granted via LuckPerms
- [ ] GUI refreshes showing purchased perk with glow
- [ ] Insufficient funds message works
- [ ] Already owned perks can't be repurchased

## Technical Details

### Architecture
```
Player clicks perk in GUI
    ↓
InventoryListener detects click
    ↓
PerkManager.purchasePerk()
    ↓
EconomyManager (facade)
    ↓
┌───────────────────────┐
│ CoinsEngine available?│
└───────────────────────┘
    ↓ YES              ↓ NO
CoinsEngineManager    Vault
(reflection API)      (standard)
```

### Files Modified from v1.0.0
- `pom.xml` - Version bump to 2.0.0
- `plugin.yml` - Updated description
- `BubblePerks.java` - Added CoinsEngine detection
- `EconomyManager.java` - Added delegation logic
- `CoinsEngineManager.java` - **NEW** - Reflection-based integration
- `InventoryListener.java` - Removed debug logging
- `PerkManager.java` - Removed debug logging
- `config.yml` - Added CoinsEngine settings
- `README.md` - Updated documentation
- `CHANGELOG.md` - **NEW** - Version history

## Support

If you encounter issues:

1. **Check logs** for startup messages
2. **Verify dependencies**: Vault, LuckPerms, CoinsEngine
3. **Test currency ID**: Make sure it matches your CoinsEngine config
4. **Check balance**: Use both `/balance` (Vault) and `/coins balance` (CoinsEngine)
5. **Review permissions**: Ensure players have `bubbleperks.use`

## Changelog Summary

### Added
- CoinsEngine v2.5.0+ integration via reflection
- Smart economy detection and fallback system
- Production-ready logging
- CHANGELOG.md

### Fixed
- Slot mapping bug between GUI and click handlers
- Requirements check blocking all purchases
- Message placeholder not being replaced
- Hex colors not rendering in GUI
- NullPointerException on GUI open

### Changed
- Removed all debug logging
- Cleaner error messages
- Updated README with v2 info
- Version bumped to 2.0.0

---

**Build**: `BubblePerks-2.0.0.jar` (2025-10-01)
**Compatibility**: Minecraft 1.21.8+ (Paper/Purpur)
**Java**: 21
