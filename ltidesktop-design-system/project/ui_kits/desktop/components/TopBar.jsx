// TopBar.jsx
const TopBar = ({ screen }) => {
  const titles = {
    home: "Mission Control",
    dashboard: "System Overview",
    files: "Dump Files",
    cli: "Remote Terminal Session",
    settings: "Console Configuration",
  };
  return (
    <div style={{
      height: 48, padding: "0 16px", display: "flex", alignItems: "center", justifyContent: "space-between",
      background: "#0C0C0C", borderBottom: "1px solid #262626", flexShrink: 0
    }}>
      <span style={{ fontSize: 15, fontWeight: 700, color: "#fff" }}>{titles[screen]}</span>
      <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
        <Chip tone="success">Connected</Chip>
        <span style={{ width: 1, height: 24, background: "rgba(38,38,38,.5)", margin: "0 8px" }} />
        <button style={iconBtn}><Icon name="WifiTethering" size={20} color="#A1A1AA" /></button>
        <button style={iconBtn}>
          <Icon name="Bell" size={20} color="#A1A1AA" />
          <span style={{ position: "absolute", top: 6, right: 6, width: 6, height: 6, background: "#FF3366" }} />
        </button>
        <span style={{ width: 1, height: 24, background: "rgba(38,38,38,.5)", margin: "0 4px" }} />
        <div style={{
          width: 32, height: 32, borderRadius: 16, background: "#161616", border: "1px solid rgba(38,38,38,.5)",
          display: "flex", alignItems: "center", justifyContent: "center",
          color: "#fff", fontSize: 14, fontWeight: 600, cursor: "pointer"
        }}>A</div>
      </div>
    </div>
  );
};

const iconBtn = {
  position: "relative", width: 32, height: 32, borderRadius: 4, background: "transparent",
  border: "none", display: "flex", alignItems: "center", justifyContent: "center", cursor: "pointer"
};

window.TopBar = TopBar;
