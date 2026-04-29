// Primitives — Button, Field, Chip, Switch, Slider
const { useState } = React;

const Button = ({ children, onClick, icon, primary = true, fullWidth = false, style = {} }) => {
  const [hover, setHover] = useState(false);
  const [press, setPress] = useState(false);
  const bg = primary
    ? (hover ? "rgba(0,229,255,.20)" : "rgba(0,229,255,.10)")
    : (hover ? "#1E1E1E" : "#161616");
  const color = primary ? "#00E5FF" : "#fff";
  const border = primary ? "rgba(0,229,255,.5)" : "#262626";
  return (
    <button
      onClick={onClick}
      onMouseEnter={() => setHover(true)} onMouseLeave={() => { setHover(false); setPress(false); }}
      onMouseDown={() => setPress(true)} onMouseUp={() => setPress(false)}
      style={{
        display: "inline-flex", alignItems: "center", gap: 6, height: 28, padding: "0 12px",
        background: bg, border: `1px solid ${border}`, borderRadius: 2,
        color, fontSize: 12, fontWeight: 500, cursor: "pointer",
        transform: press ? "scale(0.96)" : "scale(1)",
        transition: "transform .12s, background .15s, border-color .15s",
        width: fullWidth ? "100%" : "auto",
        justifyContent: "center", fontFamily: "var(--font-sans)",
        ...style
      }}>
      {icon && <Icon name={icon} size={14} color={color} strokeWidth={2} />}
      {children}
    </button>
  );
};

const Chip = ({ tone = "success", children }) => {
  const map = {
    success: { c: "#00FFAA", bg: "rgba(0,255,170,.10)", b: "rgba(0,255,170,.20)" },
    warn:    { c: "#FFD600", bg: "rgba(255,214,0,.10)", b: "rgba(255,214,0,.20)" },
    error:   { c: "#FF3366", bg: "rgba(255,51,102,.10)", b: "rgba(255,51,102,.20)" },
    info:    { c: "#00E5FF", bg: "rgba(0,229,255,.10)", b: "rgba(0,229,255,.20)" },
  }[tone];
  return (
    <span style={{
      display: "inline-flex", alignItems: "center", gap: 6, height: 24, padding: "0 8px",
      background: map.bg, border: `1px solid ${map.b}`, borderRadius: 4,
      color: map.c, fontSize: 11, fontWeight: 600, textTransform: "uppercase", letterSpacing: ".045em"
    }}>
      <span style={{ width: 6, height: 6, background: map.c }} />
      {children}
    </span>
  );
};

const Field = ({ label, value, onChange, placeholder, icon, mono = true, autoFocus = false }) => {
  const [focus, setFocus] = useState(false);
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
      <label style={{ fontSize: 11, fontWeight: 600, letterSpacing: ".045em", textTransform: "uppercase", color: "#A1A1AA" }}>{label}</label>
      <div style={{
        display: "flex", alignItems: "center", gap: 8, height: 36, padding: "0 12px",
        background: "transparent", border: `1px solid ${focus ? "#00E5FF" : "#262626"}`, borderRadius: 2
      }}>
        {icon && <Icon name={icon} size={18} color="#A1A1AA" strokeWidth={1.6} />}
        <input
          autoFocus={autoFocus}
          value={value} onChange={e => onChange(e.target.value)} placeholder={placeholder}
          onFocus={() => setFocus(true)} onBlur={() => setFocus(false)}
          style={{
            flex: 1, background: "transparent", border: "none", outline: "none",
            color: "#fff", fontSize: 13, lineHeight: "18px",
            fontFamily: mono ? "var(--font-mono)" : "var(--font-sans)",
            caretColor: "#00E5FF",
          }} />
      </div>
    </div>
  );
};

const Switch = ({ checked, onChange }) => (
  <button onClick={() => onChange(!checked)} style={{
    position: "relative", width: 36, height: 20, borderRadius: 10,
    background: checked ? "#00E5FF" : "#262626", border: "none",
    cursor: "pointer", transition: ".2s", padding: 0
  }}>
    <span style={{
      position: "absolute", top: 2, left: checked ? 18 : 2, width: 16, height: 16, borderRadius: 8,
      background: checked ? "#fff" : "#A1A1AA", transition: ".2s"
    }} />
  </button>
);

const Slider = ({ value, min, max, onChange }) => {
  const pct = ((value - min) / (max - min)) * 100;
  return (
    <div style={{ position: "relative", width: 150, height: 14, display: "flex", alignItems: "center" }}>
      <div style={{ position: "absolute", inset: "5px 0", background: "#262626", borderRadius: 2 }} />
      <div style={{ position: "absolute", top: 5, left: 0, width: `${pct}%`, height: 4, background: "#00E5FF", borderRadius: 2 }} />
      <div style={{ position: "absolute", top: "50%", left: `${pct}%`, transform: "translate(-50%,-50%)", width: 14, height: 14, borderRadius: 7, background: "#00E5FF", boxShadow: "0 0 0 4px rgba(0,229,255,.15)" }} />
      <input type="range" min={min} max={max} value={value} onChange={e => onChange(+e.target.value)}
        style={{ position: "absolute", inset: 0, width: "100%", opacity: 0, cursor: "pointer" }} />
    </div>
  );
};

Object.assign(window, { Button, Chip, Field, Switch, Slider });
