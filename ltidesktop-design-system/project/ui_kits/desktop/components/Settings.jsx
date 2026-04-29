// Settings.jsx
const Settings = () => {
  const [tab, setTab] = useState("network");
  const tabs = [
    { key: "network", label: "Network", icon: "WifiTethering" },
    { key: "appearance", label: "Appearance", icon: "Palette" },
    { key: "system", label: "System", icon: "Computer" },
    { key: "about", label: "About", icon: "Info" },
  ];

  return (
    <div style={{ display: "flex", height: "100%", minHeight: 0 }}>
      <div style={{
        width: 220, flexShrink: 0, background: "#0C0C0C", borderRight: "1px solid #1A1A1A",
        padding: "20px 12px", display: "flex", flexDirection: "column", gap: 2
      }}>
        {tabs.map(t => {
          const active = tab === t.key;
          return (
            <div key={t.key} onClick={() => setTab(t.key)} style={{
              padding: "10px 12px", borderRadius: 4, cursor: "pointer",
              display: "flex", alignItems: "center", gap: 10,
              background: active ? "#161616" : "transparent",
              color: active ? "#fff" : "#A1A1AA",
              borderLeft: active ? "2px solid #00E5FF" : "2px solid transparent",
              fontSize: 13
            }}>
              <Icon name={t.icon} size={16} color={active ? "#00E5FF" : "#A1A1AA"} />
              {t.label}
            </div>
          );
        })}
      </div>
      <div style={{ flex: 1, padding: 32, overflowY: "auto" }}>
        {tab === "network" && <NetworkPane />}
        {tab === "appearance" && <AppearancePane />}
        {tab === "system" && <SystemPane />}
        {tab === "about" && <AboutPane />}
      </div>
    </div>
  );
};

const Section = ({ title, desc, children }) => (
  <div style={{ marginBottom: 32 }}>
    <div style={{ marginBottom: 16 }}>
      <div style={{ fontSize: 13, fontWeight: 700, letterSpacing: ".05em", textTransform: "uppercase", color: "#fff" }}>{title}</div>
      {desc && <div style={{ fontSize: 11, color: "#A1A1AA", marginTop: 4 }}>{desc}</div>}
    </div>
    <div style={{ display: "flex", flexDirection: "column", gap: 1, background: "#1A1A1A" }}>
      {children}
    </div>
  </div>
);

const Row = ({ label, hint, control }) => (
  <div style={{
    display: "flex", justifyContent: "space-between", alignItems: "center",
    padding: "14px 20px", background: "#0F0F0F"
  }}>
    <div>
      <div style={{ fontSize: 13, color: "#fff" }}>{label}</div>
      {hint && <div style={{ fontSize: 11, color: "#71717A", marginTop: 2 }}>{hint}</div>}
    </div>
    {control}
  </div>
);

const NetworkPane = () => {
  const [auto, setAuto] = useState(true);
  const [keep, setKeep] = useState(true);
  const [timeout_, setTimeout_] = useState(30);
  return (
    <div>
      <Section title="Connection" desc="How LtiPatcher reaches your remote console">
        <Row label="Auto-reconnect" hint="Reconnect if the link drops" control={<Switch checked={auto} onChange={setAuto} />} />
        <Row label="Keep-alive packets" hint="Send periodic keepalive" control={<Switch checked={keep} onChange={setKeep} />} />
        <Row label="Timeout" hint={`${timeout_}s before reporting unreachable`} control={<Slider value={timeout_} min={5} max={60} onChange={setTimeout_} />} />
      </Section>
    </div>
  );
};

const AppearancePane = () => {
  const [theme, setTheme] = useState("dark");
  const [mono, setMono] = useState(true);
  return (
    <Section title="Appearance">
      <Row label="Theme" hint="Console color scheme" control={
        <div style={{ display: "flex", gap: 4, padding: 2, background: "#161616", borderRadius: 4 }}>
          {["dark","midnight"].map(k => (
            <button key={k} onClick={() => setTheme(k)} style={{
              padding: "6px 14px", borderRadius: 2, border: "none", cursor: "pointer",
              background: theme === k ? "rgba(0,229,255,.10)" : "transparent",
              color: theme === k ? "#00E5FF" : "#A1A1AA", fontSize: 11, fontWeight: 600,
              textTransform: "uppercase", letterSpacing: ".05em", fontFamily: "inherit"
            }}>{k}</button>
          ))}
        </div>
      } />
      <Row label="Monospace UI" hint="Use mono font for tabular data" control={<Switch checked={mono} onChange={setMono} />} />
    </Section>
  );
};

const SystemPane = () => (
  <Section title="System">
    <Row label="Restart Console" hint="Disconnect and re-establish session" control={<Button primary={false} icon="Refresh">RESTART</Button>} />
    <Row label="Force Reboot" hint="Send SIGHUP to remote host" control={<Button primary={false} icon="PowerOff" style={{ color: "#FF3366", borderColor: "rgba(255,51,102,.4)" }}>REBOOT</Button>} />
  </Section>
);

const AboutPane = () => (
  <Section title="About LtiPatcher">
    <Row label="Version" control={<span style={{ fontFamily: "var(--font-mono)", fontSize: 12, color: "#A1A1AA" }}>v0.1.0 · build 4291</span>} />
    <Row label="Runtime" control={<span style={{ fontFamily: "var(--font-mono)", fontSize: 12, color: "#A1A1AA" }}>Compose Multiplatform · JVM 21</span>} />
    <Row label="License" control={<span style={{ fontFamily: "var(--font-mono)", fontSize: 12, color: "#A1A1AA" }}>MIT</span>} />
  </Section>
);

window.Settings = Settings;
