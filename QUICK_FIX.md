# Quick Fix Guide - BubblePerks Purchase Issues

## ⚡ Instant Checklist

### 1. Enable Debug Mode
`config.yml`:
```yaml
debug: true
```
Reload: `/perks reload`

### 2. Required Perk IDs in `perks.yml`
```yaml
perks:
  # Slots 11-16 (Utilities)
  grindstone:
  workbench:
  anvil:
  enderchest:
  cartographytable:
  stonecutter:
  
  # Slots 21-23 (Homes)
  homes_tier1:
  homes_tier2:
  homes_tier3:
  
  # Slots 30-32 (Vaults)
  vault_tier1:
  vault_tier2:
  vault_tier3:
  
  # Slots 39-41 (Jobs)
  job_tier1:
  job_tier2:
  job_tier3:
```

### 3. Test Commands
```bash
# Give money
/eco give [name] 10000

# Check balance
/balance

# Open GUI
/perks

# Check permission
/lp user [name] permission check essentials.grindstone

# Manual grant (for testing)
/lp user [name] permission set essentials.grindstone true
```

### 4. Required Plugins
- ✅ Vault
- ✅ Economy Plugin (CoinsEngine, Essentials, etc.)
- ✅ LuckPerms (or permission manager)
- ✅ BubblePerks

### 5. Console Check on Startup
Look for:
```
[BubblePerks] Hooked into economy: [YourEconomyPlugin]
[BubblePerks] Loaded 15 perks
```

### 6. Console Check When Clicking
Look for:
```
=== Purchase Attempt ===
Player: [Name]
Perk: [PerkID]
Cost: [Amount]
...
Purchase successful!
```

## 🔴 If It Still Doesn't Work

1. Stop server
2. Delete `BubblePerks` folder from plugins
3. Copy fresh jar
4. Start server
5. Edit configs with correct perk IDs
6. Reload plugin
7. Test

## 📞 Debug Commands
```bash
# Full server restart
stop

# After restart, check these:
/plugins
/lp info
/vault-info (if available)
/balance
/eco give [name] 10000

# Enable debug
# Edit config.yml: debug: true
/perks reload

# Test purchase
/perks
# Click a perk
# Check console output
```

## ✅ Success Looks Like
- Console shows "Purchase successful!"
- Player sees "✔ Successfully purchased [perk]!"
- Hear level-up sound
- GUI refreshes, item glows green
- Can use perk command (e.g., `/grindstone`)

---
For detailed troubleshooting: See `TROUBLESHOOTING.md`
