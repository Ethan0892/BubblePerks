# BubblePerks Purchase System - Troubleshooting Guide

## Issue: "Failed to purchase perk!" Error

This guide will help you diagnose and fix purchase issues in BubblePerks.

---

## 🔍 Quick Diagnostic Steps

### Step 1: Enable Debug Mode
First, enable debug mode in `config.yml`:

```yaml
# Debug mode
debug: true
```

Then reload the plugin with `/perks reload` or restart the server.

### Step 2: Test a Purchase
1. Open the perks GUI with `/perks`
2. Try to click on a perk
3. Check the server console for debug messages

### Step 3: Check the Console Logs
Look for messages like:
```
=== Purchase Attempt ===
Player: YourName
Perk: grindstone
Cost: 100.0
Balance for YourName: X
Withdraw X from YourName: SUCCESS/FAILED
```

---

## 🛠️ Common Issues and Fixes

### Issue 1: Economy Not Working

**Symptoms:**
- Console shows: "Economy not available when checking balance"
- Balance shows as 0 even though you have money

**Solutions:**

1. **Check Vault Installation**
   ```
   /plugins
   ```
   Make sure you see `Vault` in green.

2. **Check Economy Plugin**
   Make sure you have an economy plugin installed:
   - CoinsEngine
   - EssentialsX Economy
   - CMI Economy
   - PlayerPoints
   - etc.

3. **Verify Vault Hook**
   Look for this message in console on startup:
   ```
   [BubblePerks] Hooked into economy: [EconomyName]
   ```
   
   If you don't see this, Vault isn't finding your economy plugin.

4. **Test Economy Commands**
   ```
   /balance
   /eco give [yourname] 1000
   ```

---

### Issue 2: LuckPerms Not Executing Commands

**Symptoms:**
- Money is withdrawn but permission not granted
- Console shows: "Command result: false"

**Solutions:**

1. **Check LuckPerms Installation**
   ```
   /lp info
   ```

2. **Test Permission Commands Manually**
   ```
   /lp user [yourname] permission set essentials.grindstone true
   ```

3. **Verify Command Format in perks.yml**
   Check each perk has proper commands:
   ```yaml
   grindstone:
     commands:
       - 'lp user {player} permission set essentials.grindstone true'
   ```

4. **Check Console Permissions**
   The console must have permission to run LuckPerms commands.

---

### Issue 3: Perk Already Owned (But You Don't Have It)

**Symptoms:**
- Says you already own a perk
- But you can't use the command

**Solutions:**

1. **Check Your Permissions**
   ```
   /lp user [yourname] permission check essentials.grindstone
   ```

2. **Clear the Permission if Needed**
   ```
   /lp user [yourname] permission unset essentials.grindstone
   ```

3. **Try Purchasing Again**

---

### Issue 4: Clicking Does Nothing

**Symptoms:**
- No error message
- No purchase happens
- Nothing in console

**Solutions:**

1. **Check Slot Mapping**
   The new GUI uses hardcoded slots:
   - Slots 11-16: Utility commands
   - Slots 21-23: Home upgrades
   - Slots 30-32: Vault upgrades
   - Slots 39-41: Job upgrades

2. **Check Perk IDs Match**
   In `perks.yml`, make sure your perk IDs match:
   ```yaml
   perks:
     grindstone:      # Must be "grindstone"
     workbench:       # Must be "workbench"
     anvil:           # Must be "anvil"
     enderchest:      # Must be "enderchest"
     cartographytable: # Must be "cartographytable"
     stonecutter:     # Must be "stonecutter"
     homes_tier1:     # Must be "homes_tier1"
     homes_tier2:     # Must be "homes_tier2"
     homes_tier3:     # Must be "homes_tier3"
     vault_tier1:     # Must be "vault_tier1"
     vault_tier2:     # Must be "vault_tier2"
     vault_tier3:     # Must be "vault_tier3"
     job_tier1:       # Must be "job_tier1"
     job_tier2:       # Must be "job_tier2"
     job_tier3:       # Must be "job_tier3"
   ```

3. **Reload the Plugin**
   ```
   /perks reload
   ```

---

### Issue 5: Requirements Not Met

**Symptoms:**
- Console shows: "Requirements not met for perk: X"
- Tier upgrades are locked

**Solutions:**

1. **Check Tier Requirements**
   For tier upgrades, you must have the previous tier:
   ```yaml
   homes_tier2:
     requirements:
       permission: 'essentials.sethome.multiple.tier1'  # Must have tier 1
   ```

2. **Grant Previous Tier First**
   ```
   /lp user [yourname] permission set essentials.sethome.multiple.tier1 true
   ```

---

## 📊 Debugging Checklist

Use this checklist to systematically debug issues:

- [ ] Debug mode is enabled in config.yml
- [ ] Plugin reloaded after config changes
- [ ] Vault plugin is installed and loaded (green)
- [ ] Economy plugin is installed and loaded
- [ ] LuckPerms (or permission plugin) is installed
- [ ] Can see balance with /balance command
- [ ] Balance is greater than perk cost
- [ ] Perk IDs in perks.yml match expected names
- [ ] Commands in perks.yml are properly formatted
- [ ] Console shows "Hooked into economy" on startup
- [ ] No errors in console on plugin load
- [ ] Clicking a perk shows debug messages in console

---

## 🔧 Manual Testing Commands

### Test Economy:
```
/balance
/eco give [yourname] 1000
/balance
```

### Test Permission:
```
/lp user [yourname] permission check essentials.grindstone
/lp user [yourname] permission set essentials.grindstone true
/lp user [yourname] permission check essentials.grindstone
/grindstone
```

### Test Purchase Flow:
```
# 1. Give yourself money
/eco give [yourname] 1000

# 2. Check balance
/balance

# 3. Open GUI
/perks

# 4. Click a perk you don't have

# 5. Check console for debug output

# 6. Verify permission was granted
/lp user [yourname] permission info

# 7. Test the command
/grindstone (or whatever perk you bought)
```

---

## 📝 Common Console Messages

### SUCCESS Messages:
```
[BubblePerks] Hooked into economy: CoinsEngine
[BubblePerks] Loaded 15 perks
[INFO] Balance for PlayerName: 1000.0
[INFO] Withdraw 100.0 from PlayerName: SUCCESS
[INFO] Executing command: lp user PlayerName permission set essentials.grindstone true
[INFO] Command result: true
[INFO] Purchase successful!
```

### ERROR Messages:
```
[WARNING] Economy not available when checking balance
[WARNING] No economy provider found!
[WARNING] Vault not found!
[WARNING] Attempted to purchase null perk
[WARNING] Failed to withdraw funds
[INFO] Insufficient funds: X < Y
[INFO] Requirements not met for perk: X
```

---

## 🚀 Quick Fix Script

If nothing works, try this reset:

```bash
# 1. Stop the server
stop

# 2. Remove the plugin
# Delete BubblePerks folder from plugins/

# 3. Install fresh
# Copy new BubblePerks.jar to plugins/

# 4. Start server
# Let it generate fresh configs

# 5. Configure
# Edit config.yml, perks.yml, messages.yml

# 6. Give yourself test money
/eco give [yourname] 10000

# 7. Test purchase
/perks
```

---

## 💡 Advanced Debugging

### Check Actual Item Clicked:
Look for this in console when debug is on:
```
[INFO] No perk found for slot: X material: MATERIAL_NAME
```

This tells you:
- Which slot you clicked (X)
- What material was in that slot

### Verify Perk Configuration:
```
/perks reload
```
Console should show:
```
[INFO] Loaded X perks
```

If it shows 0, your perks.yml has syntax errors.

### Test Perk Detection:
Enable debug mode and click each slot. You should see either:
- A purchase attempt message
- "No perk found for slot: X"

---

## 📞 Still Having Issues?

If you've tried everything above:

1. **Capture Full Console Output**
   - Enable debug mode
   - Restart server
   - Try to purchase
   - Copy ALL console messages

2. **Check These Files**
   - config.yml (especially debug: true)
   - perks.yml (check YAML syntax)
   - messages.yml

3. **Verify Plugin Versions**
   - Minecraft version
   - Paper/Spigot version
   - Vault version
   - Economy plugin version
   - LuckPerms version

4. **Test With Minimal Setup**
   - Test server with ONLY:
     - BubblePerks
     - Vault
     - CoinsEngine (or your economy)
     - LuckPerms

---

## ✅ Expected Working Flow

When everything works correctly:

1. Player clicks perk
2. Console shows: "=== Purchase Attempt ==="
3. Console shows: "Balance for Player: X"
4. Console shows: "Withdraw X from Player: SUCCESS"
5. Console shows: "Executing command: lp user..."
6. Console shows: "Command result: true"
7. Console shows: "Purchase successful!"
8. Player sees: "✔ Successfully purchased [perk]!"
9. GUI refreshes showing green glowing item
10. Player can now use the perk command

---

**Last Updated**: October 1, 2025
**Version**: 1.0.0
