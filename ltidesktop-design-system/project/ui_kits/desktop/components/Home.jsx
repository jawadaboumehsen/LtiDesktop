// Home.jsx — action hub with quick-access tiles
const Home = ({ session, onAction }) => {
  const [busy, setBusy] = useState(null);   // key of in-progress action
  const [done, setDone] = useState({});     // last result per key

  const trigger = (key, ms = 1400) => {
    if (busy) return;
    setBusy(key);
    setTimeout(() => {
      setBusy(null);
      setDone(prev => ({ ...prev, [key]: Date.now() }));
      onAction?.(key);
    }, ms);
  };

  const primaryActions = [
    { key: "dump",       icon: "Download",  title: "Get Dump Files",   desc: "Pull crash + memory dumps from the host",  ms: 1800 },
    { key: "reconnect",  icon: "Cable",     title: "Reconnect Session", desc: "Re-establish the SSH tunnel",             ms: 1200 },
    { key: "memdump",    icon: "Memory",    title: "Capture Memory Snapshot", desc: "Dump RAM regions to .hpp",         ms: 2000 },
    { key: "logs",       icon: "FileText",  title: "Sync Logs",         desc: "Pull /var/log/* to local store",          ms: 1500 },
    { key: "diag",       icon: "BugReport", title: "Run Diagnostics",   desc: "Smoke-test the patcher pipeline",         ms: 1700 },
    { key: "reboot",     icon: "PowerOff",  title: "Force Reboot Host", desc: "SIGHUP + cold restart",                   ms: 1300, danger: true },
  ];

  const recent = [
    { key: "dump",      title: "Dump pulled",        meta: "system.hpp · 2.4 MB", time: "4m ago",  tone: "success" },
    { key: "logs",      title: "Logs synced",        meta: "12 files · 8.1 MB",   time: "12m ago", tone: "info" },
    { key: "reconnect", title: "Reconnected",        meta: `${session?.host || "host"}:${session?.port || 22}`, time: "23m ago", tone: "success" },
    { key: "diag",      title: "Diagnostics passed", meta: "all checks ok",       time: "1h ago",  tone: "success" },
  ];
  const toneColors = { success: "#00FFAA", info: "#00E5FF", warn: "#FFD600", error: "#FF3366" };

  return (
    <div style={{ padding: 24, height: "100%", overflowY: "auto", display: "flex", flexDirection: "column", gap: 24 }}>
      {/* Banner */}
      <div style={{
        padding: 20, background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 4,
        display: "flex", justifyContent: "space-between", alignItems: "center"
      }}>
        <div>
          <div style={{ fontSize: 11, fontWeight: 600, letterSpacing: ".05em", color: "#A1A1AA", textTransform: "uppercase" }}>
            Connected Host
          </div>
          <div style={{ fontSize: 20, fontWeight: 700, color: "#fff", marginTop: 4, fontFamily: "var(--font-mono)" }}>
            {session?.user || "root"}@{session?.host || "192.168.1.42"}
            <span style={{ color: "#71717A" }}>:{session?.port || "22"}</span>
          </div>
        </div>
        <Chip tone="success">Session Active</Chip>
      </div>

      {/* Action grid */}
      <div>
        <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: ".05em", color: "#A1A1AA",
          textTransform: "uppercase", marginBottom: 12 }}>Quick Actions</div>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 12 }}>
          {primaryActions.map(a => (
            <ActionTile key={a.key} action={a}
              busy={busy === a.key} done={done[a.key]}
              disabled={busy && busy !== a.key}
              onClick={() => trigger(a.key, a.ms)} />
          ))}
        </div>
      </div>

      {/* Recent actions log */}
      <div style={{ background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 4 }}>
        <div style={{ padding: "14px 20px", borderBottom: "1px solid #1A1A1A",
          display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <span style={{ fontSize: 13, fontWeight: 700, color: "#fff" }}>Recent Actions</span>
          <span style={{ fontSize: 11, color: "#71717A", fontFamily: "var(--font-mono)" }}>last 24h</span>
        </div>
        <div style={{ padding: "4px 20px 12px" }}>
          {recent.map((r, i) => (
            <div key={i} style={{
              display: "grid", gridTemplateColumns: "32px 1fr auto", alignItems: "center", gap: 12,
              padding: "12px 0", borderBottom: i < recent.length - 1 ? "1px solid #1A1A1A" : "none"
            }}>
              <div style={{ width: 28, height: 28, borderRadius: 4,
                background: `${toneColors[r.tone]}1A`,
                display: "flex", alignItems: "center", justifyContent: "center" }}>
                <Icon name={primaryActions.find(p => p.key === r.key)?.icon || "Info"}
                  size={14} color={toneColors[r.tone]} strokeWidth={1.8} />
              </div>
              <div>
                <div style={{ fontSize: 12, fontWeight: 600, color: "#fff", letterSpacing: ".02em" }}>
                  {r.title.toUpperCase()}
                </div>
                <div style={{ fontSize: 11, fontFamily: "var(--font-mono)", color: "#A1A1AA", marginTop: 2 }}>{r.meta}</div>
              </div>
              <span style={{ fontSize: 10, fontFamily: "var(--font-mono)", color: "#71717A" }}>{r.time}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const ActionTile = ({ action, busy, done, disabled, onClick }) => {
  const [hover, setHover] = useState(false);
  const justDone = done && Date.now() - done < 2400;
  const accent = action.danger ? "#FF3366" : "#00E5FF";
  const accentBg = action.danger ? "rgba(255,51,102,.10)" : "rgba(0,229,255,.10)";

  return (
    <button
      onClick={onClick} disabled={disabled || busy}
      onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{
        textAlign: "left", padding: 16, gap: 12, cursor: disabled ? "not-allowed" : "pointer",
        background: hover && !disabled ? "#161616" : "#0F0F0F",
        border: `1px solid ${hover && !disabled ? "#262626" : "#1A1A1A"}`, borderRadius: 4,
        display: "flex", flexDirection: "column", opacity: disabled ? 0.4 : 1,
        transition: "background .15s, border-color .15s, opacity .15s",
        fontFamily: "inherit", color: "#fff"
      }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div style={{
          width: 32, height: 32, borderRadius: 4, background: accentBg,
          display: "flex", alignItems: "center", justifyContent: "center"
        }}>
          {busy
            ? <span style={{ width: 14, height: 14,
                border: `2px solid ${accent}33`, borderTopColor: accent,
                borderRadius: "50%", animation: "spin .8s linear infinite" }} />
            : <Icon name={action.icon} size={16} color={accent} strokeWidth={1.8} />}
        </div>
        {justDone && <Chip tone="success">DONE</Chip>}
      </div>
      <div>
        <div style={{ fontSize: 13, fontWeight: 700, color: "#fff", letterSpacing: ".01em" }}>{action.title}</div>
        <div style={{ fontSize: 11, color: "#A1A1AA", marginTop: 4, lineHeight: 1.5 }}>{action.desc}</div>
      </div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginTop: 4 }}>
        <span style={{ fontSize: 10, fontFamily: "var(--font-mono)", color: "#71717A", letterSpacing: ".05em" }}>
          {busy ? "RUNNING…" : action.danger ? "DESTRUCTIVE" : "READY"}
        </span>
        <Icon name="ArrowRight" size={14} color={accent} strokeWidth={2} />
      </div>
    </button>
  );
};

window.Home = Home;
