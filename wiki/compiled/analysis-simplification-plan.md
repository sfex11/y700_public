---
type: analysis
status: active
updated: 2026-04-16
---

# Wiki Simplification Plan

## Summary
- 21 actions in recognition-map.md, all referencing 1280x800 coordinates
- 0 of 64 needed templates are mobile-ready (89% missing, 11% PC-only)
- LLM vision works as fallback (12-47s) but is slow
- Recommend: simplify to 10 core actions, convert coords to 720x450, use LLM-first strategy

---

## 1. Current State

### Recognition Map (21 actions)
| # | Action | Category | Template Deps | Templates Available | Usable? |
|---|--------|----------|---------------|---------------------|---------|
| 1 | start-app | login | 3 | 0/3 | SYSTEM only |
| 2 | select-server | login | 4 | 0/4 | OCR/LLM only |
| 3 | handle-login-popup | login | 5 | 0/5 | OCR/LLM only |
| 4 | daily-checkin | login | 5 | 0/5 | OCR/LLM only |
| 5 | dock | in-port | 4 | 0/4 | OCR/LLM only |
| 6 | supply | in-port | 3 | 0/3 | OCR/LLM only |
| 7 | depart | in-port | 5 | 0/5 | OCR/LLM only |
| 8 | auto-sail | sailing | 7 | 0/7 | OCR/LLM only |
| 9 | detect-port | sailing | 4 | 0/4 | LLM only |
| 10 | handle-durability | sailing | 7 | 0/7 | OCR/color only |
| 11 | enter-market | trade | 4 | 0/4 | OCR/LLM only |
| 12 | check-prices | trade | 3 | 0/3 | OCR/LLM only |
| 13 | buy-goods | trade | 5 | 0/5 | OCR only |
| 14 | sell-goods | trade | 6 | 0/6 | OCR only |
| 15 | reconnect | recovery | 4 | 0/4 | OCR/SYSTEM only |
| 16 | restart-app | recovery | 2 | 0/2 | SYSTEM only |
| 17 | back-to-home | recovery | 3 | 0/3 | SYSTEM/LLM only |
| 18 | dismiss-generic | popup | 3 | 0/3 | OCR/LLM only |
| 19 | handle-error | popup | 4 | 0/4 | OCR/LLM only |
| 20 | handle-update | popup | 2 | 0/2 | OCR/SYSTEM only |
| 21 | handle-maintenance | popup | 2 | 0/2 | OCR/SYSTEM only |

**None have working template matching.** Every action falls through to OCR or LLM vision.

---

## 2. Recommended Tier Structure

### TIER 1 - KEEP ACTIVE (10 actions) - Core game loop
These form the minimum viable game loop: login → sail → dock → trade → repeat.

| Action | Why Keep | Primary Method | Speed |
|--------|----------|---------------|-------|
| start-app | Must start game | system (adb commands) | instant |
| handle-login-popup | Blocks progress | llm (detect popup) + ocr (find buttons) | 12-20s |
| supply | Before every departure | llm (screen analysis) | 15-25s |
| depart | Leave port | llm (find depart button) | 12-20s |
| auto-sail | Core gameplay loop | llm (set destination) | 15-30s |
| detect-port | Know when to dock | llm (see port icon) | 12-20s |
| dock | Enter port | llm (confirm dock) | 12-20s |
| enter-market | Trade core | llm (navigate to market) | 15-25s |
| sell-goods | Profit | llm + ocr (read prices) | 15-30s |
| buy-goods | Restock | llm + ocr (read prices) | 15-30s |

### TIER 2 - KEEP BUT SIMPLIFY (5 actions) - Safety/recovery
| Action | Why Keep | Simplified Approach |
|--------|----------|-------------------|
| reconnect | Network issues | system checks + llm |
| restart-app | Crash recovery | system commands only |
| back-to-home | Lost navigation | system (back key) + llm verify |
| dismiss-generic | Any popup | llm (detect + find close) |
| handle-durability | Ship damage | llm (detect low HP) → dock+repair |

### TIER 3 - ARCHIVE (6 actions) - Can defer
| Action | Why Archive | Condition to Reactivate |
|--------|------------|------------------------|
| select-server | Usually auto-connects | If server selection needed |
| daily-checkin | Low priority, complex | After core loop is stable |
| check-prices | Rolled into buy/sell via LLM | If need separate price scanning |
| handle-error | Covered by dismiss-generic | If specific error patterns emerge |
| handle-update | System-level, rare | If game updates frequently |
| handle-maintenance | System-level, rare | If maintenance happens often |

---

## 3. Coordinate Conversion: 1280x800 → 720x450

### Scale factors
- x_scale = 720 / 1280 = 0.5625
- y_scale = 450 / 800 = 0.5625

### Key coordinate conversions (rounded to integers)

#### Full-screen regions [0, 0, 1280, 800] → [0, 0, 720, 450]

#### Commonly referenced regions:

| Original (1280x800) | Converted (720x450) | Used In |
|---------------------|---------------------|---------|
| [0, 0, 1280, 100] (HUD top bar) | [0, 0, 720, 56] | sailing, detect-port |
| [0, 700, 1280, 100] (bottom menu) | [0, 394, 720, 56] | home detection |
| [300, 200, 680, 400] (splash center) | [169, 113, 383, 225] | start-app |
| [340, 650, 600, 50] (loading bar) | [191, 366, 338, 28] | start-app loading |
| [200, 50, 880, 100] (server header) | [113, 28, 495, 56] | select-server |
| [200, 150, 880, 550] (server list) | [113, 84, 495, 309] | select-server |
| [350, 450, 580, 100] (dock confirm) | [197, 253, 326, 56] | dock |
| [350, 500, 580, 100] (popup center) | [197, 281, 326, 56] | reconnect, popups |
| [350, 600, 580, 100] (popup bottom) | [197, 338, 326, 56] | login, popups |
| [350, 650, 580, 80] (depart btn) | [197, 366, 326, 45] | depart |
| [400, 650, 480, 80] (auto-sail) | [225, 366, 270, 45] | auto-sail |
| [400, 600, 480, 80] (dock btn) | [225, 338, 270, 45] | detect-port |
| [500, 600, 280, 80] (depart btn) | [281, 338, 158, 45] | depart |
| [450, 600, 380, 80] (confirm btn) | [253, 338, 214, 45] | various |
| [450, 500, 380, 80] (confirm btn) | [253, 281, 214, 45] | various |
| [100, 100, 400, 80] (supply tab) | [56, 56, 225, 45] | supply |
| [800, 600, 300, 80] (auto supply) | [450, 338, 169, 45] | supply |
| [900, 600, 200, 150] (depart icon) | [506, 338, 113, 84] | depart |
| [1100, 700, 150, 80] (auto sail icon) | [619, 394, 84, 45] | auto-sail verify |
| [1050, 10, 200, 40] (gold display) | [591, 6, 113, 23] | trade |
| [900, 10, 200, 40] (cargo display) | [506, 6, 113, 23] | trade |
| [50, 10, 200, 40] (coordinates) | [28, 6, 113, 23] | sailing |
| [100, 50, 300, 80] (buy tab) | [56, 28, 169, 45] | trade |
| [400, 50, 300, 80] (sell tab) | [225, 28, 169, 45] | trade |
| [200, 200, 880, 400] (popup area) | [113, 113, 495, 225] | popups |
| [850, 50, 100, 100] (close X) | [478, 28, 56, 56] | popups |

### Conversion formula for any coordinate:
```
new_x = round(old_x * 0.5625)
new_y = round(old_y * 0.5625)
new_w = round(old_w * 0.5625)
new_h = round(old_h * 0.5625)
```

---

## 4. Simplified Recognition Strategy (LLM-First)

Since template matching doesn't work (0/64 templates), invert the priority:

### Old priority: template > ocr > color > pixel > system > llm
### New priority: system > llm > ocr > color > template

**Rationale:**
- `system` checks (pidof, dumpsys) are instant and reliable → always first
- `llm` vision works (12-47s) and is flexible → use as primary for visual recognition
- `ocr` works for text but is fragile on game fonts → use as LLM alternative/confirmation
- `template` requires images we don't have → disable until templates are collected
- `color/pixel` requires calibration → defer

### Per-action simplified method assignments:

**TIER 1 actions:**
| Action | precondition | execution | verification |
|--------|-------------|-----------|-------------|
| start-app | system | system (adb) | system + llm |
| handle-login-popup | llm | llm + system(tap) | llm |
| supply | llm | llm + system(tap) | llm |
| depart | llm | llm + system(tap) | llm |
| auto-sail | llm | llm + system(tap) | llm |
| detect-port | llm | llm | llm |
| dock | llm | llm + system(tap) | llm |
| enter-market | llm | llm + system(tap) | llm |
| sell-goods | llm + ocr | llm + system(tap) | ocr + llm |
| buy-goods | llm + ocr | llm + system(tap) | ocr + llm |

**TIER 2 actions:**
| Action | precondition | execution | verification |
|--------|-------------|-----------|-------------|
| reconnect | system + ocr | system(tap) + llm | system + llm |
| restart-app | system | system (adb) | system |
| back-to-home | system + llm | system (back key) | llm |
| dismiss-generic | llm | llm + system(tap) | llm |
| handle-durability | llm | llm + system(tap) | llm |

---

## 5. What to Do Next

### Immediate (can do now):
1. Create `recognition-map-v2.md` with:
   - Only 15 actions (Tier 1 + Tier 2)
   - All coordinates converted to 720x450
   - `template` method removed or marked as aspirational
   - `llm` promoted to primary visual method
   - Region coordinates updated with the table above

2. Archive 6 Tier 3 actions to `wiki/actions/_archived/`

3. Update runtime to:
   - Use `llm` as default visual method
   - Apply 720x450 coordinates for tap targets
   - Use system checks as fast pre-filter

### Short-term (next session):
4. Collect 5-10 critical template images from actual device screenshots
   - Priority: home_main_menu, popup_confirm_btn, sailing_hud_frame
5. Calibrate OCR for game-specific fonts
6. Add color-based detection for durability gauge

### Medium-term:
7. Collect all 52 templates via overlay/Telegram workflow
8. Enable template matching for speed-critical paths
9. Calibrate pixel/color thresholds

---

## 6. File Changes Summary

| File | Action | Description |
|------|--------|-------------|
| compiled/recognition-map-v2.md | CREATE | Simplified 15-action map, 720x450 coords, LLM-first |
| compiled/recognition-map.md | KEEP | Archive original as reference |
| actions/_archived/*.md | MOVE | 6 Tier 3 action MDs moved here |
| compiled/template-inventory.md | KEEP | Reference for future template collection |
