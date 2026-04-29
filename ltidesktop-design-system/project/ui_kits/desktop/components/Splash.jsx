// Splash.jsx
const Splash = () => (
  <div style={{
    width: "100%", height: "100%", display: "flex", alignItems: "center", justifyContent: "center",
    flexDirection: "column", gap: 24, background: "#0A0A0A"
  }}>
    <div style={{
      width: 96, height: 96, borderRadius: 8, background: "rgba(0,229,255,.10)",
      display: "flex", alignItems: "center", justifyContent: "center",
      boxShadow: "0 0 80px rgba(0,229,255,.20)"
    }}>
      <img src="../../assets/logo-mark.svg" width="56" height="56" alt="" />
    </div>
    <div style={{ textAlign: "center" }}>
      <div style={{ fontSize: 24, fontWeight: 700, letterSpacing: ".02em" }}>LtiPatcher</div>
      <div style={{ fontSize: 12, color: "#A1A1AA", marginTop: 4, fontFamily: "var(--font-mono)" }}>v0.1.0 · INITIALIZING</div>
    </div>
    <div style={{ width: 200, height: 2, background: "#1A1A1A", overflow: "hidden", borderRadius: 1 }}>
      <div style={{ width: "40%", height: "100%", background: "#00E5FF",
        animation: "splashBar 1.4s ease-in-out infinite" }} />
    </div>
    <style>{`@keyframes splashBar { 0%{transform:translateX(-100%)} 100%{transform:translateX(350%)} }`}</style>
  </div>
);
window.Splash = Splash;
