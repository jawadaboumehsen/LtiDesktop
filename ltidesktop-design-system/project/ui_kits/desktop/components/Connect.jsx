// Connect.jsx
const Connect = ({ onConnect }) => {
  const [host, setHost] = useState("192.168.1.42");
  const [port, setPort] = useState("22");
  const [user, setUser] = useState("root");
  const [busy, setBusy] = useState(false);

  const submit = () => {
    if (!host || !port || busy) return;
    setBusy(true);
    setTimeout(() => onConnect({ host, port, user }), 1200);
  };

  return (
    <div style={{
      width: "100%", height: "100%", background: "#0A0A0A",
      display: "flex", alignItems: "center", justifyContent: "center", padding: 32
    }}>
      <div style={{ width: 400, display: "flex", flexDirection: "column", gap: 24 }}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 12, marginBottom: 16 }}>
          <img src="../../assets/logo-mark.svg" width="40" height="40" alt="" />
          <div style={{ fontSize: 20, fontWeight: 700 }}>Connect to Console</div>
          <div style={{ fontSize: 12, color: "#A1A1AA" }}>Enter your remote host details</div>
        </div>

        <Field label="Host" value={host} onChange={setHost} placeholder="hostname or IP" icon="Dns" autoFocus />
        <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", gap: 12 }}>
          <Field label="Port" value={port} onChange={setPort} placeholder="22" icon={null} />
          <Field label="User" value={user} onChange={setUser} placeholder="root" icon={null} />
        </div>

        <Button onClick={submit} icon={busy ? null : "ArrowRight"} fullWidth style={{ height: 36 }}>
          {busy ? (
            <span style={{ display: "inline-flex", alignItems: "center", gap: 8 }}>
              <span style={{
                width: 12, height: 12, border: "2px solid rgba(0,229,255,.3)", borderTopColor: "#00E5FF",
                borderRadius: "50%", animation: "spin 0.8s linear infinite"
              }} />
              CONNECTING…
            </span>
          ) : "CONNECT"}
        </Button>

        <div style={{
          padding: 12, background: "rgba(255,214,0,.06)", border: "1px solid rgba(255,214,0,.20)",
          borderRadius: 2, display: "flex", gap: 10, alignItems: "flex-start"
        }}>
          <Icon name="Info" size={16} color="#FFD600" />
          <div style={{ fontSize: 11, color: "#A1A1AA", lineHeight: 1.6 }}>
            Demo mode — any host/port will simulate a successful connection.
          </div>
        </div>
      </div>
      <style>{`@keyframes spin { to { transform: rotate(360deg) } }`}</style>
    </div>
  );
};
window.Connect = Connect;
