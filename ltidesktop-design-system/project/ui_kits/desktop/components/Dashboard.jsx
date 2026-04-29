// Dashboard.jsx
const Dashboard = ({ session }) => {
  return (
    <div style={{ padding: 24, display: "flex", flexDirection: "column", gap: 24, height: "100%", overflowY: "auto" }}>
      {/* Bento status row */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 16 }}>
        <StatusCard icon="Power" label="POWER" value="ON" tone="success" />
        <StatusCard icon="Lock" label="HDD STATUS" value="UNLOCKED" tone="success" />
        <StatusCard icon="WifiTethering" label="NETWORK" value="ACTIVE" tone="success" />
        <StatusCard icon="Timer" label="UPTIME" value="04:21:14" tone="info" mono />
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1.4fr 1fr", gap: 16, flex: 1, minHeight: 0 }}>
        <Card title="System Activity" subtitle={`${session.host}:${session.port}`}>
          <ActivityChart />
        </Card>
        <Card title="Recent Activity" right={<button style={{ background: "transparent", border: "none", color: "#00E5FF", fontSize: 11, fontWeight: 600, letterSpacing: ".05em", cursor: "pointer" }}>VIEW ALL</button>}>
          <LogList />
        </Card>
      </div>
    </div>
  );
};

const StatusCard = ({ icon, label, value, tone, mono }) => {
  const colors = {
    success: "#00FFAA", info: "#00E5FF", warn: "#FFD600", error: "#FF3366"
  }[tone];
  return (
    <div style={{
      background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 4, padding: 16,
      display: "flex", flexDirection: "column", gap: 12
    }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <span style={{ fontSize: 11, fontWeight: 600, color: "#A1A1AA", letterSpacing: ".05em" }}>{label}</span>
        <Icon name={icon} size={16} color={colors} strokeWidth={1.6} />
      </div>
      <div style={{
        fontSize: 22, fontWeight: 700, color: colors,
        fontFamily: mono ? "var(--font-mono)" : "var(--font-sans)",
        letterSpacing: mono ? ".02em" : 0
      }}>{value}</div>
    </div>
  );
};

const Card = ({ title, subtitle, right, children }) => (
  <div style={{ background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 4, display: "flex", flexDirection: "column", minHeight: 0 }}>
    <div style={{ padding: "16px 20px", borderBottom: "1px solid #1A1A1A", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
      <div>
        <div style={{ fontSize: 13, fontWeight: 700, color: "#fff" }}>{title}</div>
        {subtitle && <div style={{ fontSize: 11, fontFamily: "var(--font-mono)", color: "#71717A", marginTop: 2 }}>{subtitle}</div>}
      </div>
      {right}
    </div>
    <div style={{ flex: 1, padding: 20, minHeight: 0 }}>{children}</div>
  </div>
);

const ActivityChart = () => {
  const points = [40, 35, 50, 42, 60, 55, 72, 65, 78, 70, 85, 80, 92, 88, 76, 82, 70, 78, 88, 95, 82, 75, 68, 80];
  const max = 100;
  const w = 520, h = 220, pad = 24;
  const dx = (w - pad * 2) / (points.length - 1);
  const path = points.map((p, i) => `${i === 0 ? "M" : "L"} ${pad + i * dx} ${h - pad - (p / max) * (h - pad * 2)}`).join(" ");
  const area = path + ` L ${w - pad} ${h - pad} L ${pad} ${h - pad} Z`;
  return (
    <svg viewBox={`0 0 ${w} ${h}`} style={{ width: "100%", height: "100%" }}>
      <defs>
        <linearGradient id="chartFill" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#00E5FF" stopOpacity="0.35" />
          <stop offset="100%" stopColor="#00E5FF" stopOpacity="0" />
        </linearGradient>
      </defs>
      {[0,1,2,3].map(i => (
        <line key={i} x1={pad} x2={w - pad} y1={pad + i * (h - pad * 2) / 3} y2={pad + i * (h - pad * 2) / 3} stroke="#1A1A1A" strokeWidth="1" />
      ))}
      <path d={area} fill="url(#chartFill)" />
      <path d={path} fill="none" stroke="#00E5FF" strokeWidth="1.5" />
      {points.map((p, i) => i % 4 === 0 && (
        <circle key={i} cx={pad + i * dx} cy={h - pad - (p / max) * (h - pad * 2)} r="2.5" fill="#00E5FF" />
      ))}
    </svg>
  );
};

const LogList = () => {
  const logs = [
    { icon: "CloudSync", title: "PATCH COMPLETED", meta: "system_v2.1.4", time: "2m ago", tone: "success" },
    { icon: "Login", title: "USER LOGIN", meta: "admin@local", time: "8m ago", tone: "info" },
    { icon: "Warning", title: "PERMISSION DENIED", meta: "/etc/secure", time: "14m ago", tone: "warn" },
    { icon: "Build", title: "BUILD SUCCESSFUL", meta: "build #4291", time: "23m ago", tone: "success" },
    { icon: "Cloud", title: "BACKUP UPLOADED", meta: "remote://us-east", time: "41m ago", tone: "info" },
  ];
  const colors = { success: "#00FFAA", warn: "#FFD600", error: "#FF3366", info: "#00E5FF" };
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 0 }}>
      {logs.map((l, i) => (
        <div key={i} style={{
          display: "grid", gridTemplateColumns: "32px 1fr auto", gap: 12, alignItems: "center",
          padding: "10px 0", borderBottom: i < logs.length - 1 ? "1px solid #1A1A1A" : "none"
        }}>
          <div style={{ width: 28, height: 28, borderRadius: 4, background: `${colors[l.tone]}1A`,
            display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Icon name={l.icon} size={14} color={colors[l.tone]} strokeWidth={1.8} />
          </div>
          <div style={{ minWidth: 0 }}>
            <div style={{ fontSize: 12, fontWeight: 600, color: "#fff", letterSpacing: ".02em" }}>{l.title}</div>
            <div style={{ fontSize: 11, fontFamily: "var(--font-mono)", color: "#A1A1AA", marginTop: 2 }}>{l.meta}</div>
          </div>
          <span style={{ fontSize: 10, fontFamily: "var(--font-mono)", color: "#71717A" }}>{l.time}</span>
        </div>
      ))}
    </div>
  );
};

window.Dashboard = Dashboard;
