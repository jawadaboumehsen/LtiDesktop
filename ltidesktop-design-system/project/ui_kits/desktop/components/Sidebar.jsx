// Sidebar.jsx
const Sidebar = ({ screen, onNav, expanded, onToggle, width }) => {
  const items = [
    { key: "home", label: "Home", icon: "Home" },
    { key: "dashboard", label: "Dashboard", icon: "GridView" },
    { key: "files", label: "Files", icon: "Folder" },
    { key: "cli", label: "Remote Console", icon: "Terminal" },
    { key: "settings", label: "System Settings", icon: "Settings" },
  ];
  return (
    <div style={{
      width, flexShrink: 0, background: "#0C0C0C", borderRight: "1px solid #1A1A1A",
      display: "flex", flexDirection: "column", transition: "width .25s var(--ease-standard)"
    }}>
      <div style={{
        display: "flex", alignItems: "center",
        padding: expanded ? "24px 16px" : "24px 0",
        justifyContent: expanded ? "flex-start" : "center", cursor: "pointer"
      }} onClick={onToggle}>
        {expanded ? (
          <img src="../../assets/logo-wordmark.svg" height="28" alt="LtiPatcher" />
        ) : (
          <div style={{
            width: 40, height: 40, borderRadius: 4, background: "rgba(0,229,255,.10)",
            display: "flex", alignItems: "center", justifyContent: "center"
          }}>
            <img src="../../assets/logo-mark.svg" width="24" height="24" alt="" />
          </div>
        )}
      </div>
      <div style={{ height: 1, background: "rgba(26,26,26,.8)", margin: expanded ? "0 20px" : "0 12px" }} />
      <div style={{ marginTop: 24, display: "flex", flexDirection: "column", gap: 4 }}>
        {items.map(it => {
          const active = screen === it.key;
          return (
            <NavItem key={it.key} item={it} active={active} expanded={expanded} onClick={() => onNav(it.key)} />
          );
        })}
      </div>
      <div style={{ marginTop: "auto", height: 64, display: "flex", alignItems: "center",
        gap: 12, padding: expanded ? "0 24px" : 0,
        justifyContent: expanded ? "flex-start" : "center", cursor: "pointer", color: "#A1A1AA" }}>
        <Icon name="Logout" size={20} color="#A1A1AA" />
        {expanded && <span style={{ fontSize: 12 }}>Sign Out</span>}
      </div>
    </div>
  );
};

const NavItem = ({ item, active, expanded, onClick }) => {
  const [hover, setHover] = useState(false);
  const bg = active ? "#121212" : (hover ? "rgba(22,22,22,.5)" : "transparent");
  return (
    <div onClick={onClick}
      onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{
        position: "relative", margin: "0 8px", padding: expanded ? "8px 12px" : "8px 0",
        borderRadius: 4, background: bg, cursor: "pointer",
        display: "flex", alignItems: "center",
        gap: expanded ? 12 : 0, justifyContent: expanded ? "flex-start" : "center",
        color: active ? "#fff" : "#A1A1AA", transition: "background .15s"
      }}>
      {active && expanded && (
        <span style={{ position: "absolute", left: -8, top: "50%", transform: "translateY(-50%)",
          width: 3, height: 20, background: "#00E5FF", borderRadius: 2 }} />
      )}
      <Icon name={item.icon} size={20} color={active ? "#00E5FF" : (hover ? "#fff" : "#A1A1AA")} />
      {expanded && <span style={{ fontSize: 13, fontWeight: active ? 500 : 400 }}>{item.label}</span>}
    </div>
  );
};

window.Sidebar = Sidebar;
