# BubblePerks GUI Visual Layout

## 6-Row Inventory Layout (54 slots)

```
┌───┬───┬───┬───┬───┬───┬───┬───┬───┐
│ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │  ROW 1: Header & Player Info
│ █ │ █ │ █ │ █ │ 👤│ █ │ █ │ █ │ █ │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 9 │10 │11 │12 │13 │14 │15 │16 │17 │  ROW 2: Utility Commands
│ ▓ │ 🔧│🪨 │⛏ │🔨 │📦 │🗺️ │⚒ │ · │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│18 │19 │20 │21 │22 │23 │24 │25 │26 │  ROW 3: Home Upgrades
│ ▓ │🛏️ │📊│🟢 │🟡 │🟠 │ · │ · │ · │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│27 │28 │29 │30 │31 │32 │33 │34 │35 │  ROW 4: Vault Upgrades
│ ▓ │📦 │📊│🟢 │🟡 │🟠 │ · │ · │ · │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│36 │37 │38 │39 │40 │41 │42 │43 │44 │  ROW 5: Job Upgrades
│ ▓ │⚒ │📊│🟢 │🟡 │🟠 │ · │ · │ · │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│45 │46 │47 │48 │49 │50 │51 │52 │53 │  ROW 6: Footer & Navigation
│ █ │ █ │ █ │ █ │ ❌│ █ │ █ │ █ │ █ │
└───┴───┴───┴───┴───┴───┴───┴───┴───┘

LEGEND:
  █ = Border (Black Stained Glass Pane)
  ▓ = Separator (Cyan Stained Glass Pane)
  👤 = Player Info/Info Button
  🔧 = Category Header
  📊 = Status Display
  🟢 = Tier 1 Upgrade
  🟡 = Tier 2 Upgrade
  🟠 = Tier 3 Upgrade
  ❌ = Close Button
  · = Filler (Light Gray Glass Pane)
```

---

## Detailed Slot Breakdown

### Row 1: Header (Slots 0-8)
```
[█][█][█][█][Player Head][█][█][█][█]
                ↓
         Gradient Title
         Balance Display
         Perks Count
```

### Row 2: Utility Commands (Slots 9-17)
```
[▓][Header][Perk 1][Perk 2][Perk 3][Perk 4][Perk 5][Perk 6][·]
    ↓       ↓       ↓       ↓       ↓       ↓       ↓       ↓
  Separator Category  Grind  Work   Anvil  EChest  Carto  Stone
            ⚙️      stone  bench                   Table  cutter
```

### Row 3: Home Upgrades (Slots 18-26)
```
[▓][Header][Status][Tier 1][Tier 2][Tier 3][·][·][·]
    ↓       ↓       ↓       ↓       ↓       ↓
  Separator  ⌂      Current  +3     +5      +7
            Homes   Count   Homes   Homes   Homes
                    (5→8)   (8→13)  (13→20)
```

### Row 4: Vault Upgrades (Slots 27-35)
```
[▓][Header][Status][Tier 1][Tier 2][Tier 3][·][·][·]
    ↓       ↓       ↓       ↓       ↓       ↓
  Separator  ⚑      Current  +1     +1      +2
            Vaults  Count   Vault   Vault   Vaults
                    (1→2)   (2→3)   (3→5)
```

### Row 5: Job Upgrades (Slots 36-44)
```
[▓][Header][Status][Tier 1][Tier 2][Tier 3][·][·][·]
    ↓       ↓       ↓       ↓       ↓       ↓
  Separator  ⚒      Current  +1     +1      +1
            Jobs    Count   Job     Job     Job
                    (3→4)   (4→5)   (5→6)
```

### Row 6: Footer (Slots 45-53)
```
[█][█][█][█][Close Button][█][█][█][█]
                ↓
            ❌ Close
         Click to exit
```

---

## Color Coding System

### Upgrade States
```
┌─────────────┬──────────────┬─────────────────────┐
│   State     │   Material   │   Visual            │
├─────────────┼──────────────┼─────────────────────┤
│ Purchased   │ Lime Glass   │ 🟢 ✔ Name (Glow)   │
│ Affordable  │ Yellow Glass │ 🟡 ● Name          │
│ Expensive   │ Orange Glass │ 🟠 ● Name          │
│ Locked      │ Red Glass    │ 🔴 🔒 Name         │
└─────────────┴──────────────┴─────────────────────┘
```

### Command Perks
```
┌─────────────┬──────────────┬─────────────────────┐
│   State     │   Visual     │   Lore              │
├─────────────┼──────────────┼─────────────────────┤
│ Owned       │ 🟢 ✔ Name   │ ✔ PURCHASED         │
│             │ (Glow)       │ This perk is active!│
├─────────────┼──────────────┼─────────────────────┤
│ Can Buy     │ 🟡 ● Name   │ Cost: XXX 🫧        │
│             │              │ ▸ Click to purchase!│
├─────────────┼──────────────┼─────────────────────┤
│ Too Poor    │ 🔴 ● Name   │ Cost: XXX 🫧        │
│             │              │ Your balance: YYY   │
│             │              │ ✘ Insufficient funds│
└─────────────┴──────────────┴─────────────────────┘
```

---

## Section Headers

### Utility Commands
```
┌──────────────────────────────┐
│  🔧 ⚙ Utility Commands      │
│  Quick access to useful tools│
│  ━━━━━━━━━━━━━━━━━━━━      │
└──────────────────────────────┘
```

### Home Upgrades
```
┌──────────────────────────────┐
│  🛏️ ⌂ Home Upgrades          │
│  Expand your home limit      │
│  ━━━━━━━━━━━━━━━━━━━━      │
└──────────────────────────────┘
```

### Vault Upgrades
```
┌──────────────────────────────┐
│  📦 ⚑ Vault Upgrades         │
│  Unlock additional storage   │
│  ━━━━━━━━━━━━━━━━━━━━      │
└──────────────────────────────┘
```

### Job Upgrades
```
┌──────────────────────────────┐
│  ⛏️ ⚒ Job Upgrades           │
│  Increase your job capacity  │
│  ━━━━━━━━━━━━━━━━━━━━      │
└──────────────────────────────┘
```

---

## Status Displays

### Home Status Example
```
┌──────────────────────────────┐
│  📋 Current Status           │
│                              │
│  ◈ Available Homes: 5        │
│  ◈ Maximum: 20               │
│                              │
└──────────────────────────────┘
```

### Vault Status Example
```
┌──────────────────────────────┐
│  📦 Current Status           │
│                              │
│  ◈ Available Vaults: 1       │
│  ◈ Maximum: 5                │
│                              │
└──────────────────────────────┘
```

### Job Status Example
```
┌──────────────────────────────┐
│  📖 Current Status           │
│                              │
│  ◈ Available Jobs: 3         │
│  ◈ Maximum: 6                │
│                              │
└──────────────────────────────┘
```

---

## Navigation Elements

### Info Button (Top - Slot 4)
```
┌──────────────────────────────┐
│  📘 ℹ Information            │
│                              │
│  Welcome to the Perks Shop!  │
│                              │
│  ➤ Click perks to purchase   │
│  ➤ Earn bubbles by playing   │
│  ➤ Upgrades are permanent    │
│                              │
│  Perks Owned: 5              │
└──────────────────────────────┘
```

### Close Button (Bottom - Slot 49)
```
┌──────────────────────────────┐
│  ❌ ✖ Close                  │
│                              │
│  Click to close this menu    │
└──────────────────────────────┘
```

---

## Progressive Upgrade Flow

### Example: Home Upgrades
```
Base (5 homes) → Tier 1 → Tier 2 → Tier 3
                 (8)      (13)     (20)
                 ↓        ↓        ↓
                100🫧    150🫧    200🫧
                +3       +5       +7

Purchase Order (locked progression):
1. Must own Tier 1 to buy Tier 2
2. Must own Tier 2 to buy Tier 3
3. Visual lock (🔒) on unavailable tiers
```

---

## Click Interaction Flow

```
User Opens GUI
     ↓
Sees Color-Coded Items
     ↓
Green (✔) → Already Owned
Yellow (●) → Can Purchase → Click → Purchase Success → Item Glows
Orange/Red (●) → Too Expensive → See Balance Comparison
Red (🔒) → Locked → See Requirement Message
```

---

## Material Reference

| Element Type          | Material                      | Color  |
|-----------------------|-------------------------------|--------|
| Border                | BLACK_STAINED_GLASS_PANE     | Black  |
| Separator             | CYAN_STAINED_GLASS_PANE      | Cyan   |
| Filler                | LIGHT_GRAY_STAINED_GLASS_PANE| Gray   |
| Owned Upgrade         | LIME_STAINED_GLASS_PANE      | Green  |
| Available Upgrade     | YELLOW_STAINED_GLASS_PANE    | Yellow |
| Expensive Upgrade     | ORANGE_STAINED_GLASS_PANE    | Orange |
| Locked Upgrade        | RED_STAINED_GLASS_PANE       | Red    |
| Player Info           | PLAYER_HEAD                   | N/A    |
| Close Button          | BARRIER                       | N/A    |
| Info Button           | BOOK                          | N/A    |

---

**This layout provides:**
- ✅ Clear visual hierarchy
- ✅ Intuitive navigation
- ✅ Immediate status feedback
- ✅ Progressive unlock system
- ✅ Professional appearance
- ✅ Easy to scan and understand
