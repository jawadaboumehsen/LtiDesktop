// Console.jsx
const { useEffect, useRef } = React;

const initialLines = [
  { tag: "INFO", color: "#00E5FF", text: "Connection established · 192.168.1.42:22" },
  { tag: "AUTH", color: "#00FFAA", text: "RSA key accepted · session #4f7a21" },
  { tag: "SYS",  color: "#A1A1AA", text: "Last login: Wed Apr 23 14:02:11 from 10.0.0.1" },
  { tag: "BOOT", color: "#A1A1AA", text: "Linux node-01 5.15.0-101 #amd64 SMP" },
];

const Console = () => {
  const [lines, setLines] = useState(initialLines);
  const [cmd, setCmd] = useState("");
  const [history, setHistory] = useState([]);
  const [hIdx, setHIdx] = useState(-1);
  const scrollRef = useRef(null);

  useEffect(() => {
    if (scrollRef.current) scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
  }, [lines]);

  const fakeRun = (c) => {
    if (c === "clear") { setLines([]); return []; }
    if (c.startsWith("ls")) return [
      { tag: "OUT", color: "#71717A", text: "boot   etc    home   root   tmp    var" },
      { tag: "OUT", color: "#71717A", text: "dev    lib    proc   sys    usr    bin" },
    ];
    if (c.startsWith("uptime")) return [
      { tag: "OUT", color: "#71717A", text: " 14:23:08 up 4:21,  3 users,  load average: 0.42, 0.38, 0.30" },
    ];
    if (c.startsWith("whoami")) return [{ tag: "OUT", color: "#71717A", text: "root" }];
    if (c.startsWith("status")) return [
      { tag: "OK",  color: "#00FFAA", text: "● power=on hdd=unlocked net=active" },
    ];
    if (c.startsWith("patch")) return [
      { tag: "RUN", color: "#FFD600", text: "Applying patch system_v2.1.5..." },
      { tag: "OK",  color: "#00FFAA", text: "Patch applied · 14 files modified" },
    ];
    if (c.trim() === "") return [];
    return [{ tag: "ERR", color: "#FF3366", text: `${c.split(" ")[0]}: command not found` }];
  };

  const submit = () => {
    const c = cmd.trim();
    setLines(prev => {
      const echo = { tag: "$", color: "#00E5FF", text: c, isEcho: true };
      return c === "clear" ? [] : [...prev, echo, ...fakeRun(c)];
    });
    if (c) setHistory(prev => [...prev, c]);
    setCmd(""); setHIdx(-1);
  };

  const onKey = (e) => {
    if (e.key === "Enter") submit();
    else if (e.key === "ArrowUp") { e.preventDefault(); if (history.length) {
      const i = hIdx < 0 ? history.length - 1 : Math.max(0, hIdx - 1);
      setHIdx(i); setCmd(history[i]);
    }}
    else if (e.key === "ArrowDown") { e.preventDefault();
      if (hIdx >= 0) { const i = hIdx + 1;
        if (i >= history.length) { setHIdx(-1); setCmd(""); } else { setHIdx(i); setCmd(history[i]); }
      }
    }
    else if (e.key === "l" && e.ctrlKey) { e.preventDefault(); setLines([]); }
  };

  return (
    <div style={{ height: "100%", padding: 24, display: "flex", flexDirection: "column", gap: 16, minHeight: 0 }}>
      <div style={{ background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 4,
        flex: 1, display: "flex", flexDirection: "column", minHeight: 0 }}>
        <div style={{
          padding: "10px 16px", borderBottom: "1px solid #1A1A1A",
          display: "flex", justifyContent: "space-between", alignItems: "center",
          fontFamily: "var(--font-mono)", fontSize: 11, color: "#71717A", letterSpacing: ".05em"
        }}>
          <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
            <span style={{ width: 8, height: 8, borderRadius: 4, background: "#00FFAA" }} />
            <span>root@192.168.1.42:~ — bash</span>
          </div>
          <span>↑/↓ history · ⌃L clear</span>
        </div>
        <div ref={scrollRef} style={{
          flex: 1, padding: 16, overflowY: "auto",
          fontFamily: "var(--font-mono)", fontSize: 13, lineHeight: 1.55, color: "#E4E4E7"
        }}>
          {lines.map((l, i) => (
            <div key={i} style={{ display: "flex", gap: 12 }}>
              <span style={{ color: l.color, fontWeight: 600, minWidth: 56, textAlign: "right",
                opacity: l.isEcho ? 1 : 0.85
              }}>{l.tag}</span>
              <span style={{ color: l.isEcho ? "#fff" : (l.color === "#71717A" ? "#A1A1AA" : "#E4E4E7"), whiteSpace: "pre-wrap" }}>{l.text}</span>
            </div>
          ))}
        </div>
        <div style={{
          padding: "12px 16px", borderTop: "1px solid #1A1A1A",
          display: "flex", alignItems: "center", gap: 12,
          fontFamily: "var(--font-mono)", fontSize: 13
        }}>
          <span style={{ color: "#00E5FF", fontWeight: 600 }}>root@node-01 ~ $</span>
          <input
            autoFocus value={cmd} onChange={e => setCmd(e.target.value)} onKeyDown={onKey}
            placeholder="type a command (try: ls, status, patch, clear)"
            style={{
              flex: 1, background: "transparent", border: "none", outline: "none",
              color: "#fff", fontFamily: "var(--font-mono)", fontSize: 13,
              caretColor: "#00E5FF"
            }} />
        </div>
      </div>
    </div>
  );
};

window.Console = Console;
