# LtiDesktop UI Kit

A click-thru HTML/JSX recreation of the LtiDesktop product. Open `index.html` to see the full flow:

1. **Splash** (auto-advances after ~1.5s)
2. **Connect** (enter any host/port → press Connect → simulated 1.2s connect)
3. **Dashboard** (default once connected)
4. **Remote Console** (interactive terminal — type commands, ↑/↓ history, ⌃L clears)
5. **System Settings** (left rail tabs)

The sidebar collapse, top-bar status pill, command stream coloring, splash + crossfade transitions are all implemented to match the Compose source.

## Files
- `index.html` — entry, mounts `<App />`
- `tokens.css` — re-exports root `colors_and_type.css`
- `components/`
  - `Icon.jsx` — Lucide icon registry mapped from Material Symbols names used in source
  - `Button.jsx`, `Field.jsx`, `Switch.jsx`, `Slider.jsx`, `Chip.jsx`
  - `Sidebar.jsx`, `TopBar.jsx`
  - `Splash.jsx`, `Connect.jsx`
  - `Dashboard.jsx` (with `StatusCard`, `AnalyticsChart`, `LogList` inline)
  - `Console.jsx` (terminal output + command input)
  - `Settings.jsx`
  - `App.jsx` — root state machine
