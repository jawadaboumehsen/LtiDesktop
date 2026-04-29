# LtiDesktop Design System

A self-contained design system for **LtiDesktop** — a premium cross-platform desktop application built with Compose Multiplatform (Kotlin). LtiDesktop is a system dashboard, console interface, and administrative utility for "Lti Solutions" featuring the signature high-contrast **"Antigravity"** dark aesthetic.

## Sources

- **Codebase (imported):** [`jawadaboumehsen/LtiDesktop`](https://github.com/jawadaboumehsen/LtiDesktop) — Compose Multiplatform / Kotlin 2.0+ / Material 3 / Koin / Coroutines & StateFlow / MVI architecture. Source files were imported into `composeApp/` and read directly.
- **No Figma was provided.** Visual fidelity is sourced 1:1 from the Kotlin source (theme tokens, component composables, and bundled vector assets).
- **No slide deck was provided.** No `slides/` directory is included.

## Products

LtiDesktop is a single product surface — a **desktop application** with three primary screens reachable through a collapsible left rail:

1. **Dashboard / Home** — bento-grid system overview (status cards, performance chart, activity logs)
2. **Remote Console / CLI** — interactive terminal output panel + command input
3. **System Settings / Preferences** — tabbed left-rail settings (General, Terminal, Connection, Appearance, About)

A **Connect Screen** (modal-style dialog) gates the application before connection, and a 2.5s **Splash Screen** plays at launch.

There is **one UI kit** in this design system: `ui_kits/desktop/` — a high-fidelity HTML/JSX recreation of the Compose desktop UI.

---

## Index

```
LtiDesktop Design System/
├── README.md                   ← you are here
├── SKILL.md                    Agent Skill manifest (cross-compatible with Claude Code skills)
├── colors_and_type.css         CSS vars: surfaces, text, brand, semantic, type, spacing, radii, motion
│
├── assets/                     Logos and brand vectors (extracted from Compose drawables)
│   ├── logo-mark.svg           "LTI" monogram with circuit via-node + orange pulse dot
│   ├── logo-wordmark.svg       "LTIPATCHER" full wordmark (electric blue + grey)
│   ├── logo-splash.svg         Splash icon — chip with PCB pin traces
│   ├── logo-notification.svg   Tray notification glyph
│   └── logo-photo.png          Bitmap brand photo (39 KB)
│
├── preview/                    Design System preview cards (each is a small HTML page)
│   ├── colors-*.html           Surfaces, text, brand, semantic
│   ├── type-*.html             Display, body, label, terminal
│   ├── spacing-*.html          Spacing scale, radii, shadows
│   ├── components-*.html       Buttons, inputs, status cards, nav items, tabs, log rows, badges
│   └── brand-*.html            Logos
│
├── ui_kits/desktop/            HTML/JSX recreation of the LtiDesktop product
│   ├── README.md
│   ├── index.html              Click-thru prototype (Connect → Dashboard → Console → Settings)
│   ├── components/*.jsx        Sidebar, TopBar, StatusCard, AnalyticsChart, LogList,
│   │                           Terminal, CommandInput, ConnectCard, SettingsRail, ...
│   └── tokens.css              imports colors_and_type.css
│
└── composeApp/                 Imported Kotlin source — kept for reference; do not edit
    └── src/commonMain/kotlin/com/lti/ltidesktop/
        ├── ui/theme/{Colors,Theme,Type}.kt   ← single source of truth for tokens
        └── ui/components/*.kt                ← reference for every UI behavior
```

---

## CONTENT FUNDAMENTALS

LtiDesktop is **engineer-facing**, **terse**, and writes like a serious systems product. Copy is sysadmin-flavored: it favors precise nouns over flowery verbs, never speaks in first person, addresses the user as the implicit subject of an imperative ("Establish a secure connection…"), and never uses emoji or exclamation marks anywhere.

**Voice & tone**
- Calm, declarative, technical. No marketing fluff.
- Short sentences. Often a fragment is enough ("Stable.", "Ready to connect.", "Last 24h.").
- Never first person. Never "we". Occasional implicit "you" through imperative ("Toggle between…", "Manage network settings…"). No second-person pronouns.

**Casing**
- **Title Case** for screen names, section titles, button labels: *System Dashboard, Remote Console, System Settings, Apply Changes, Establish Connection, Reset to Default, Back to Home, Refresh Data*.
- **UPPERCASE with letter-spacing 0.5** for micro-labels above fields and group headers: *SERVER IP ADDRESS, PORT NUMBER, APPLICATION, NETWORK, THEME, WINDOW, BEHAVIOR*.
- **Sentence case** for descriptions and helper text: *"Automatically attempt to connect to the last known host when the app starts."*
- **lowercase mono** in terminal contexts: `root@corp-sys-prod-01:~`, `$`.

**Tag-style identifiers** in terminal output: `[CMD]`, `[ERROR]`, `[SYS]`, `[WARN]`, `[NORMAL]` — bracketed, all-caps, color-coded by severity.

**Numerals** are formatted with thousands commas (`1,240`) and explicit short units (`4h 12m`, `94%`, `4.2GB`, `2 mins ago`). Never spelled out.

**No emoji. No unicode decoration. No exclamation marks.** Iconography carries any visual weight.

**Specific copy examples (verbatim from the source):**
- "System Dashboard" / "Overview of network health and activity."
- "System Console" / "Establish a secure connection to the host server to continue."
- "Connection Required" / "You must establish a secure link to the server before using the remote console."
- "Real-time network throughput and latency."
- "High memory usage detected on worker node." / "Node: Beta-03 • Usage: 94%"
- "© 2026 Lti Solutions. All rights reserved. / A professional cross-platform terminal interface."
- Error states are direct: *"Connection Required"*, never "Oops!".

**Bullet/separator style:** the middle dot `•` separates inline metadata pairs (`IP: 192.168.1.104 • Node: Alpha-01`). Never use commas for these.

---

## VISUAL FOUNDATIONS

LtiDesktop's visual language is **flat, dark, sharp, and high-contrast**, with neon cyan as the only chromatic note in an otherwise monochrome surface stack. The aesthetic is closer to a Bloomberg terminal or a modern devops console than to a consumer app — every choice favors information density and crispness over warmth.

**Backgrounds.** Pure `#000000` root, never a gradient. Surface containers tonally step up in tiny 6-point increments: `#0C0C0C` → `#161616` → `#1E1E1E`. There are **no full-bleed images, no hand-drawn illustrations, no repeating textures, no patterns**. The only "imagery" is the rendered network performance chart (a stroke + faint vertical-gradient fill confined to a card). The wordmark and the logo mark are vector-drawn pixel-grid letterforms.

**Color use.** Cyan `#00E5FF` is reserved for *interactive intent* — the active nav indicator, focused field borders, primary button strokes, chart strokes, link-style affordances ("View All"). Semantic colors `#00FFAA` (success), `#FFD600` (warning), `#FF3366` (error) are used sparingly, almost always at 10% alpha as a chip background paired with the full-strength color for icon + text. White is reserved for top-level text and "Active" headline values.

**Type.** A single sans family (`FontFamily.SansSerif` in source — substituted with **Inter** in this system) covers all UI; a single mono family (`FontFamily.Monospace` — substituted with **JetBrains Mono**) covers terminal lines and monospace contexts. Sizes are small: 20/16/14/13/12/11. Weights are 400 / 500 / 600 / 700. Letter-spacing is slightly negative on display+headline (-0.4 / -0.16 sp) and positive on small-cap labels (+0.5 sp).

**Spacing.** Strict 4-pt grid: 4 / 8 / 12 / 16 / 20 / 24 / 32 / 48 / 64. Card padding is 16 or 20. Section gaps are 16. Top-bar height is 48dp. Sidebar is 80dp collapsed / 200–600dp expanded (drag-resizable).

**Borders.** 1dp hairlines at `#262626` are the workhorse — every card, every divider, every input gets one. Borders soften to 50% alpha in nested contexts. There is **no border-radius left-accent trick** ("colored left border, rounded right corners") — instead the active-nav indicator is a tiny separate 3×20dp pill drawn at `x = -16`.

**Corner radii.** A defined `Shapes` set: `extraSmall = 0`, `small = 2`, `medium = 4`, `large = 4`. **The dominant radius is 4dp.** Sharper than typical Material — closer to a CAD/console aesthetic. Nothing is pill-shaped except status dots (which are full circles).

**Shadows.** Drop shadows are almost entirely absent. The **only** elevated element is the Connect modal: `Modifier.shadow(elevation = 24.dp, spotColor = Color.Black.copy(alpha = 0.5f))`. Everything else relies on **tonal surfaces + 1dp borders** to create hierarchy. Cards do not lift on hover — they swap from `surfaceContainerLowest` to `surfaceContainer` and tighten their border alpha.

**No protection gradients.** No capsule pills around content over imagery. Glass / translucency is used only via the global `Modifier.graphicsLayer(alpha = state.settings.opacity)` window-opacity setting (0.5–1.0 range, exposed as a slider in Appearance). Backdrop blurs are not used.

**Hover states** (from source):
- Status cards: `surfaceContainerLowest` → `surfaceContainer`, border alpha 0.5 → 1.0.
- Nav items (idle): transparent → `surfaceVariant @ 0.5α`.
- Buttons: bg goes from `primary @ 0.10α` → `primary @ 0.20α`.
- Sidebar drag-handle: transparent → `primary @ 0.50α`.
- Cursor switches to `Hand` on every interactive surface.

**Press states.** Buttons scale to **0.96** via `animateFloatAsState` triggered by `MutableInteractionSource.collectIsPressedAsState`. There is no color flash on press — only the scale.

**Focus state.** Outlined text fields swap unfocused border `#262626` for `primary #00E5FF`; cursor is also primary.

**Animation.** Coroutines + Compose `animate*AsState` everywhere. Specific motions:
- Splash: 1000ms tween fade-in + a `spring(dampingRatio = MediumBouncy, stiffness = Low)` scale 0.8 → 1.2.
- Screen transitions: 300ms `fadeIn togetherWith fadeOut` via `AnimatedContent`.
- Connect → Connected handoff: 500ms `Crossfade`.
- Sidebar width: `animateDpAsState` (default tween).
- Scrollbar thickness: 4dp → 8dp on hover, 300ms.
- Pulse dot beside "Active" status: solid `success` color, 8dp square — **no animated pulse in code**, the visual pulse is suggested but static. (Easy candidate for a future enhancement.)

**Easing.** Default Compose `tween` (linear-out-slow-in) for fades; spring for splash; `Crossfade` default. Bounces are reserved for the splash only.

**Layout rules.**
- Top bar is always 48dp tall, fixed.
- Sidebar: 80dp collapsed, drag-resizable from 200dp to 600dp. Header logo / icon doubles as the toggle.
- Settings screen: 260dp left rail, 64×48 padding on the content area.
- Bento grid on Dashboard: `2fr | 1fr` two-column with 16dp gap; the right "Activity Logs" column spans full height.

**Use of transparency.** Selectively, never decoratively. Examples: borders soften to 0.5α inside nested contexts; semantic color chips are 10% alpha bg + 100% alpha icon; the global window-opacity slider attenuates the entire app 0.5–1.0; secondary text drops to 0.5α to render placeholders.

**Imagery vibe.** Cool. Monochrome with a single neon highlight. No grain, no warm tones, no people, no marketing photography. The brand "imagery" is its terminal output and chart strokes.

**Cards** = `surfaceContainerLowest` background + 1dp border `#262626` + 4dp radius + `padding 16–24`. No shadow, no inner glow, no rounded-on-one-side gimmicks. On hover, raise tonally one step + tighten border to full alpha.

**Top-bar status badge** is the canonical "chip" form: 6dp dot (full color) + 8/4 padding + 4dp radius + colored 0.1α bg + colored 0.2α border + uppercase label-small text in the chip color. This pattern is used for every status indicator in the system.

---

## ICONOGRAPHY

LtiDesktop uses **Material Symbols (Compose `androidx.compose.material.icons`)** as its sole icon system. There is **no custom icon font, no SVG sprite, and no PNG-based icon set**. Icons are drawn live as Compose `ImageVector`s, always at 20dp (top-bar / sidebar / status), 18dp (settings rows), 14dp (button leading icon), 12dp (log-row glyph), or 32dp (hero icon in Connect / Console gate).

**Icons in active use** (from imports across the source):
- Navigation: `GridView` (Dashboard), `Terminal` (Console), `Settings` (System Settings), `MenuOpen` / `Menu` (collapse).
- Actions: `Refresh`, `ArrowBack`, `ArrowForward`, `KeyboardReturn` (Execute), `Logout`.
- Top bar: `WifiTethering`, `Notifications`, `Lock`.
- Connect: `Dns`, `Computer`, `Power`, `PowerOff`.
- Activity logs: `Login`, `CloudSync`, `Warning`, `Build`, `Info`.
- Status cards: `Dns`, `Terminal`, `Timer`.
- Settings tabs: `Settings`, `Terminal`, `Cloud`, `Palette`, `Info`.

**Tinting rules.** Idle icons use `--fg-secondary` (`#A1A1AA`). Active/selected nav icons swap to `--info` / `--primary` (`#00E5FF`). Status-card icons use the **semantic** color + 10% alpha bg square (40dp). Log-row icons are 12dp with the same colored 10% alpha 24dp bg square.

**Icon-only buttons** (top-bar bell / wifi) use a 32dp clickable hover target, no visible bg in idle state.

**Substitution in the design system.** Because the Compose source only references icon *names*, not bitmap sources, this design system substitutes them with the equivalent **[Lucide](https://lucide.dev/) icons** loaded from CDN. The mapping is documented in `ui_kits/desktop/components/Icon.jsx`. Lucide's stroke-style 24px icons match Material Symbols' "Outlined" weight closely. **FLAGGED:** if the user prefers a 1:1 visual match, swap to Material Symbols via Google Fonts (`https://fonts.googleapis.com/icon?family=Material+Icons` or the variable Material Symbols web font).

**Brand vectors** (in `assets/`) are sharp, pixel-grid, rectilinear letterforms with PCB-trace decorations. The wordmark "LTI" portion is electric blue `#4776E6`; "PATCHER" is mid-grey `#BDBDBD`; an orange via-dot `#F2994A` accents the splash mark. **Note:** these brand vector colors are *distinct from* the runtime UI palette (cyan/black) — the logo lives in its own color world and is shown against pure black. Treat the orange and electric-blue as logo-only accents, not part of the UI token system.

**No emoji. No unicode decorative chars** beyond the bullet `•` separator.

---

## Substitution flags (please review)

1. **Fonts** — Compose source references `FontFamily.SansSerif` and `FontFamily.Monospace` (system defaults). This system substitutes **Inter** (sans) + **JetBrains Mono** (mono). If you want a specific brand font, please drop the font files into `fonts/` and update `colors_and_type.css`.
2. **Icon set** — Material Symbols (referenced by name in Kotlin) substituted with **Lucide** via CDN. See `ui_kits/desktop/components/Icon.jsx` for mappings.
3. **Light theme** — Light theme tokens are present in source but the app ships dark-default. Sidebar + terminal stay dark in light mode by intent (`Colors.kt`).
4. **Pulse animation** — The "Active" status indicator next to a value is described as a pulse but renders as a static dot in source. Open question whether to add a real pulse.
