# BubblePerks GUI Improvements

## Overview
The perks panel has been completely redesigned with enhanced visual elements, better organization, and improved user experience. This document outlines all the improvements made.

---

## 🎨 Visual Enhancements

### 1. **Decorative Borders**
- Added black stained glass pane borders around the edges of the GUI
- Creates a framed, professional appearance
- Border slots: 0, 1, 2, 3, 5, 6, 7, 8 (top) and 45, 46, 47, 48, 50, 51, 52, 53 (bottom)

### 2. **Section Separators**
- Cyan stained glass panes with decorative lines separate each section
- Makes it easy to distinguish between different perk categories
- Separator slots: 9, 18, 27, 36 (left side of each content row)

### 3. **Enhanced Color Coding**
The GUI now uses intuitive color coding for all perks and upgrades:
- **🟢 Green (Lime)**: Purchased/Unlocked items
- **🟡 Yellow**: Available to purchase (can afford)
- **🟠 Orange**: Cannot afford yet
- **🔴 Red**: Locked (requires previous tier)
- **⚫ Light Gray**: Filler/background

### 4. **Visual Status Indicators**
Items now display clear status symbols:
- ✔ **Checkmark**: Owned/Purchased
- ● **Dot**: Available but not purchased
- 🔒 **Lock**: Requires prerequisites
- ▸ **Arrow**: Interactive prompt to purchase

---

## 📋 Layout Organization

### **Row 1** (Slots 0-8): Header & Player Info
- **Slot 4**: Player head with detailed balance and perk statistics
- Bordered by black glass panes for a clean header look

### **Row 2** (Slots 9-17): Utility Commands
- **Slot 9**: Section separator
- **Slot 10**: Category header (Command Block) with title
- **Slots 11-16**: Up to 6 utility command perks (grindstone, workbench, anvil, etc.)

### **Row 3** (Slots 18-26): Home Upgrades
- **Slot 18**: Section separator
- **Slot 19**: Category header (Bed) with title
- **Slot 20**: Status display showing current home count
- **Slots 21-23**: Three progressive upgrade tiers

### **Row 4** (Slots 27-35): Vault Upgrades
- **Slot 27**: Section separator
- **Slot 28**: Category header (Ender Chest) with title
- **Slot 29**: Status display showing current vault count
- **Slots 30-32**: Three progressive upgrade tiers

### **Row 5** (Slots 36-44): Job Upgrades
- **Slot 36**: Section separator
- **Slot 37**: Category header (Diamond Pickaxe) with title
- **Slot 38**: Status display showing current job count
- **Slots 39-41**: Three progressive upgrade tiers

### **Row 6** (Slots 45-53): Footer & Navigation
- **Slot 49**: Close button (Barrier) for easy exit
- Bordered by black glass panes for a clean footer

---

## 🎯 Feature Improvements

### 1. **Enhanced Player Info Display**
```
┌─────────────────────────────┐
│  [Player Head]              │
│  Player's Perks             │
│  ━━━━━━━━━━━━━━━━━━━━━━   │
│  ◈ Balance: 1000 🫧         │
│  ◈ Perks: 5/15              │
│  ━━━━━━━━━━━━━━━━━━━━━━   │
└─────────────────────────────┘
```

### 2. **Category Headers**
Each section now has a clear header with:
- Relevant icon (Command Block, Bed, Ender Chest, Diamond Pickaxe)
- Colored title with unique emoji
- Descriptive subtitle
- Visual separator line

### 3. **Status Displays**
New status items show:
- Current count (homes/vaults/jobs)
- Maximum possible count
- Visual representation with appropriate icons

### 4. **Progressive Upgrades**
Upgrade items now display:
- **Tier progression**: Visual indicator of which tier this is
- **Total count**: Shows what total you'll have after purchase
- **Prerequisites**: Clear indication if previous tier is required
- **Locked state**: Red glass pane with lock icon if prerequisites not met
- **Enchantment glow**: Purchased items have a glowing effect

### 5. **Enhanced Perk Items**
Command perks now show:
- **Status prefix**: Color-coded symbol (✔, ●) before the name
- **Detailed lore**: Original perk description with visual separator
- **Purchase info**: 
  - If owned: "✔ PURCHASED" with confirmation text
  - If affordable: Cost with green arrow prompt
  - If too expensive: Cost, current balance, and "✘ Insufficient funds"
- **Enchantment glow**: Purchased perks glow

### 6. **Navigation Elements**
- **Close button**: Prominent barrier item at bottom center
- **Info button**: Book with helpful tips and current statistics

---

## 💬 Message Enhancements

### Improved Messages Include:
- Status indicators (✔, ✘, ⚠, 🔒)
- Gradient text support for titles
- Consistent formatting with separator lines
- Detailed tooltips with multiple information levels
- Color-coded feedback (green for success, red for errors, yellow for warnings)

---

## 🎮 User Experience Improvements

### Better Visual Feedback
1. **Color transitions**: Items change color based on affordability and status
2. **Icon consistency**: Same symbols used throughout for consistency
3. **Clear hierarchy**: Important information is highlighted
4. **Scanability**: Users can quickly identify what they can afford

### Improved Organization
1. **Logical grouping**: Related items are visually grouped
2. **Clear sections**: Easy to navigate between different perk types
3. **Status at a glance**: Current counts visible without clicking
4. **Progressive disclosure**: Information revealed in logical order

### Enhanced Interactivity
1. **Click prompts**: Clear "Click to purchase" messages
2. **Requirement feedback**: Locked items explain what's needed
3. **Balance comparison**: Shows your balance vs. cost directly
4. **Purchase confirmation**: Visual glow effect on owned items

---

## 🔧 Technical Improvements

### Code Structure
- **Modular design**: Separate methods for each section
- **Reusable components**: Helper methods for common item types
- **Consistent patterns**: Same approach for all upgrade tiers
- **Better maintainability**: Clear method names and documentation

### Configuration
- **Well-documented**: Config file has clear layout reference
- **Flexible slots**: Easy to adjust layout by changing slot numbers
- **Visual reference**: Comments show which row/section each slot belongs to

---

## 📊 Summary of Changes

### Files Modified
1. **PerksGUI.java**: Complete redesign with 400+ lines of improvements
2. **config.yml**: Reorganized with better documentation
3. **messages.yml**: Enhanced with more detailed messages

### Key Metrics
- **Visual elements**: 16 border slots + 4 separator slots
- **Categories**: 4 main sections with headers
- **Status displays**: 3 new status items for home/vault/job counts
- **Enhanced feedback**: 10+ new message types
- **Color states**: 5 different visual states for items

---

## 🚀 Future Enhancement Ideas

### Potential Additions
1. **Multi-page support**: For more perks in the future
2. **Search/filter**: If the number of perks grows significantly
3. **Favorites**: Mark frequently used perks
4. **Preview mode**: Show what you'll get before purchasing
5. **Animation**: Smooth transitions when items change state
6. **Sound effects**: Audio feedback for purchases and interactions
7. **Particle effects**: Visual celebrations for purchases

---

## 📝 Notes for Server Admins

### Testing Checklist
- [ ] Open the GUI and verify all borders appear correctly
- [ ] Check that each section has proper headers and separators
- [ ] Verify color coding for affordable/unaffordable items
- [ ] Test purchasing a perk and verify the glow effect
- [ ] Check that locked upgrades show properly
- [ ] Verify status displays show correct counts
- [ ] Test with different balance amounts
- [ ] Verify close button works

### Customization Options
- Adjust slot positions in config.yml
- Modify colors in messages.yml
- Change border materials (BLACK_STAINED_GLASS_PANE)
- Customize separator materials (CYAN_STAINED_GLASS_PANE)
- Adjust category icons in the code

---

**Version**: 1.0.0 Enhanced
**Last Updated**: October 1, 2025
**Compatibility**: Minecraft 1.20.1+ (Paper/Spigot)
