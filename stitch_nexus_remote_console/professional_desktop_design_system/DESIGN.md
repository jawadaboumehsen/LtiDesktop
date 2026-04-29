---
name: Professional Desktop Design System
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#434655'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0053db'
  primary: '#004ac6'
  on-primary: '#ffffff'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#b4c5ff'
  secondary: '#565e74'
  on-secondary: '#ffffff'
  secondary-container: '#dae2fd'
  on-secondary-container: '#5c647a'
  tertiary: '#46566c'
  on-tertiary: '#ffffff'
  tertiary-container: '#5e6e85'
  on-tertiary-container: '#e9f0ff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#dae2fd'
  secondary-fixed-dim: '#bec6e0'
  on-secondary-fixed: '#131b2e'
  on-secondary-fixed-variant: '#3f465c'
  tertiary-fixed: '#d3e4fe'
  tertiary-fixed-dim: '#b7c8e1'
  on-tertiary-fixed: '#0b1c30'
  on-tertiary-fixed-variant: '#38485d'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  title-sm:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-base:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-uppercase:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
  code-terminal:
    fontFamily: monospace
    fontSize: 13px
    fontWeight: '450'
    lineHeight: 20px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  unit: 8px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  xxl: 48px
  sidebar_width: 260px
  container_gutter: 24px
---

## Brand & Style

The design system is engineered for high-stakes professional environments where clarity, reliability, and precision are paramount. The aesthetic follows a **Modern Corporate** direction, blending minimalist efficiency with the structural density required for data-rich desktop applications.

The visual language balances two distinct zones: a stable, authoritative "Command" zone (dark sidebars and navigation) and a focused, high-legibility "Workspace" zone (light content areas). This duality ensures the user feels grounded in the application’s structure while remaining productive within the content. The emotional response is one of calm control, high trust, and technical sophistication.

## Colors

The color palette is architected to define spatial hierarchy through contrast. 

*   **Primary Action:** A vibrant "Electric Blue" (#2563EB) is reserved strictly for primary calls to action, active states, and focus indicators, ensuring interactive elements are never ambiguous.
*   **The Command Zone:** Deep Navy (#0F172A) and Slate (#1E293B) are used for sidebars and global navigation. This creates a "frame" that recedes visually, pushing the content forward.
*   **The Workspace:** Crisp White (#FFFFFF) and subtle "Off-White" grays (#F8FAFC) provide a clean canvas for data and text, reducing eye strain during long sessions.
*   **Feedback:** Success, Warning, and Error states utilize standard semantic hues but are tempered with slightly higher saturation to remain visible against both light and dark backgrounds.

## Typography

This design system utilizes **Inter** for all UI elements due to its exceptional legibility and neutral, functional character. The scale is optimized for desktop density, favoring 14px as the standard body size to allow for more information on screen without sacrificing readability.

For technical contexts and the CLI component, a high-quality system Monospace stack is utilized. This ensures that character alignment is perfect for code and data logs. Letter spacing is slightly tighter on headings to create a more "locked-in" professional feel, while labels use all-caps with increased tracking for clear categorization.

## Layout & Spacing

The layout is built on a strict **8px grid system**. All component dimensions, padding, and margins are multiples of 8, ensuring a predictable and rhythmic visual flow.

The desktop application uses a **Fixed-Fluid hybrid model**:
1.  **Sidebar:** A fixed width of 260px for primary navigation.
2.  **Content Area:** A fluid container with a maximum width for readability (e.g., 1200px) centered in the viewport, or fully fluid for data-heavy dashboard views.
3.  **Margins:** Generous 24px or 32px margins wrap the main content area to provide "breathing room," preventing the UI from feeling cramped despite high information density.

## Elevation & Depth

Hierarchy is established through **Tonal Layering** and **Ambient Shadows**. 

*   **Level 0 (Background):** The deepest layer, usually the Slate/Navy sidebar or the light gray application background.
*   **Level 1 (Surface):** The primary workspace. Uses a 1px border (#E2E8F0) to define boundaries instead of heavy shadows.
*   **Level 2 (Popovers/Menus):** Elevated elements use highly diffused, low-opacity shadows (Color: Slate-900, Opacity: 8%, Blur: 12px) to suggest they are floating above the workspace.
*   **The CLI:** The terminal is treated as an "inset" element, using a subtle inner shadow to look recessed into the interface, emphasizing its status as a distinct functional tool.

## Shapes

The design system employs a **Soft (4px)** corner radius. This subtle rounding maintains a professional, "engineered" look while removing the aggressive sharpness of 0px corners. 

*   **Standard Components:** Buttons, inputs, and cards use the base 4px radius.
*   **Large Containers:** Modal windows or main content panels may use 8px (rounded-lg) to soften the overall application frame.
*   **CLI:** The terminal window retains sharp or near-sharp corners (2px) to reinforce its technical, utilitarian nature.

## Components

### Buttons
Primary buttons use the Vibrant Blue background with white text. Ghost buttons use Slate-600 text and only show a subtle background on hover. All buttons have a height of 32px (compact) or 40px (standard) to align with the 8px grid.

### Input Fields
Inputs feature a 1px border in Slate-200. On focus, the border transitions to the Primary Blue with a 2px outer "halo" (ring) at 20% opacity. Labels sit above the field in the `label-uppercase` style.

### Side Navigation
The sidebar uses the Deep Navy background. Navigation items are Slate-300 by default, transitioning to White with a 2px Primary Blue vertical "active indicator" on the left edge when selected.

### CLI / Terminal
A specialized component with a background of #020617 (near black). It uses the `code-terminal` typography level in a light gray or green. It features no scrollbars; instead, it uses a subtle "fade" at the top to indicate overflow.

### Cards
Cards are white with a 1px Slate-200 border. They do not use shadows unless they become interactive or "draggable," at which point they transition to Level 2 elevation.