---
name: ltipatcher-design
description: Use this skill to generate well-branded interfaces and assets for LtiPatcher (a Compose Multiplatform desktop console for remote system administration), either for production or throwaway prototypes/mocks/etc. Contains essential design guidelines, colors, type, fonts, assets, and UI kit components for prototyping.
user-invocable: true
---

Read the README.md file within this skill, and explore the other available files.

If creating visual artifacts (slides, mocks, throwaway prototypes, etc), copy assets out and create static HTML files for the user to view. If working on production code, you can copy assets and read the rules here to become an expert in designing with this brand.

If the user invokes this skill without any other guidance, ask them what they want to build or design, ask some questions, and act as an expert designer who outputs HTML artifacts _or_ production code, depending on the need.

## Quick orientation

- `colors_and_type.css` — root tokens (cyan #00E5FF accent on a black-stack `#0A0A0A`/`#0F0F0F`/`#161616` surface ramp, Inter sans + JetBrains Mono).
- `assets/` — logos and brand marks. Copy into your file's local `assets/` folder; do not hot-link.
- `ui_kits/desktop/` — drop-in JSX components (Sidebar, TopBar, Button, Field, Chip, Switch, Slider, Dashboard, Console, Settings). `index.html` is a working click-thru; lift components from `components/`.
- `preview/` — single-purpose specimen cards. Use these as visual references, not as components.

## Design rules at a glance

- **Surface stack only.** No blue gradients. Black tones (`#0A0A0A`, `#0F0F0F`, `#161616`) separated by 1px hairline borders (`#1A1A1A` / `#262626`). Cards = filled rect + border; no rounded-corner-with-left-accent tropes.
- **Cyan is the only accent that exists.** `#00E5FF` for primary actions, focus rings, active nav indicators, mono prompts, data series. Use mint `#00FFAA` only for "ok" state, yellow `#FFD600` for "warn", red `#FF3366` for "error".
- **Tight radii.** 2dp for inputs/buttons, 4dp for cards. No pill buttons. No 12dp+ corners.
- **Uppercase labels.** Section headings, status chips, button copy: ALL CAPS, +0.05em tracking, 600 weight.
- **Mono for data.** Any number, IP, hash, command, log line, ID — JetBrains Mono. Sans is for UI chrome only.
- **No emoji. No drawn-from-scratch SVGs.** Use the existing icon set in `ui_kits/desktop/components/Icon.jsx` (Lucide-style 1.8 stroke), or substitute a Lucide icon and flag it.
- **Animation is functional.** ~150-200ms ease-standard on hover/focus; splash/connect uses a horizontal cyan progress bar. Avoid bounces or playful springs.
