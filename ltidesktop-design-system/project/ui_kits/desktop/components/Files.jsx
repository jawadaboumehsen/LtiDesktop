// Files.jsx — browse dump files (.hpp .txt .json)
const Files = ({ files, onDelete, onSync }) => {
  const [selected, setSelected] = useState(files[0]?.id ?? null);
  const [filter, setFilter] = useState("all");
  const [query, setQuery] = useState("");

  const visible = files.filter(f =>
    (filter === "all" || f.ext === filter) &&
    (query === "" || f.name.toLowerCase().includes(query.toLowerCase()))
  );

  const current = files.find(f => f.id === selected) || visible[0] || null;

  return (
    <div style={{ display: "flex", height: "100%", minHeight: 0 }}>
      {/* List pane */}
      <div style={{
        width: 340, flexShrink: 0, background: "#0C0C0C", borderRight: "1px solid #1A1A1A",
        display: "flex", flexDirection: "column", minHeight: 0
      }}>
        <div style={{ padding: 16, borderBottom: "1px solid #1A1A1A", display: "flex", flexDirection: "column", gap: 12 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 8,
            height: 32, padding: "0 10px",
            background: "#0F0F0F", border: "1px solid #1A1A1A", borderRadius: 2 }}>
            <Icon name="Search" size={14} color="#71717A" strokeWidth={1.8} />
            <input value={query} onChange={e => setQuery(e.target.value)} placeholder="search files…"
              style={{ flex: 1, background: "transparent", border: "none", outline: "none",
                color: "#fff", fontSize: 12, fontFamily: "var(--font-mono)", caretColor: "#00E5FF" }} />
          </div>
          <div style={{ display: "flex", gap: 4, padding: 2, background: "#0F0F0F", borderRadius: 2 }}>
            {["all", "hpp", "txt", "json"].map(k => (
              <button key={k} onClick={() => setFilter(k)} style={{
                flex: 1, padding: "5px 0", borderRadius: 2, border: "none", cursor: "pointer",
                background: filter === k ? "rgba(0,229,255,.10)" : "transparent",
                color: filter === k ? "#00E5FF" : "#A1A1AA",
                fontSize: 10, fontWeight: 600, textTransform: "uppercase", letterSpacing: ".05em",
                fontFamily: "inherit"
              }}>{k === "all" ? "All" : `.${k}`}</button>
            ))}
          </div>
        </div>

        <div style={{ flex: 1, overflowY: "auto", minHeight: 0 }}>
          {visible.length === 0 ? (
            <EmptyFiles onSync={onSync} />
          ) : visible.map(f => {
            const active = current?.id === f.id;
            return (
              <FileRow key={f.id} file={f} active={active} onClick={() => setSelected(f.id)} />
            );
          })}
        </div>

        <div style={{ padding: 12, borderTop: "1px solid #1A1A1A",
          display: "flex", justifyContent: "space-between", alignItems: "center",
          fontSize: 10, fontFamily: "var(--font-mono)", color: "#71717A", letterSpacing: ".05em" }}>
          <span>{visible.length} of {files.length} files</span>
          <button onClick={onSync} style={{
            background: "transparent", border: "none", color: "#00E5FF", fontSize: 10,
            fontWeight: 600, letterSpacing: ".05em", cursor: "pointer", fontFamily: "inherit",
            display: "flex", alignItems: "center", gap: 4
          }}>
            <Icon name="Refresh" size={12} color="#00E5FF" strokeWidth={2} />
            SYNC
          </button>
        </div>
      </div>

      {/* Viewer pane */}
      <div style={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, minWidth: 0 }}>
        {current ? <FileViewer file={current} onDelete={() => { onDelete(current.id); setSelected(null); }} /> : <EmptyViewer />}
      </div>
    </div>
  );
};

const FileRow = ({ file, active, onClick }) => {
  const [hover, setHover] = useState(false);
  const meta = extMeta(file.ext);
  return (
    <div onClick={onClick}
      onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{
        display: "grid", gridTemplateColumns: "32px 1fr auto", alignItems: "center", gap: 10,
        padding: "10px 16px",
        background: active ? "#161616" : (hover ? "rgba(22,22,22,.5)" : "transparent"),
        borderLeft: active ? "2px solid #00E5FF" : "2px solid transparent",
        cursor: "pointer", transition: "background .15s"
      }}>
      <div style={{ width: 28, height: 28, borderRadius: 4,
        background: `${meta.color}1A`, display: "flex", alignItems: "center", justifyContent: "center" }}>
        <Icon name={meta.icon} size={14} color={meta.color} strokeWidth={1.8} />
      </div>
      <div style={{ minWidth: 0 }}>
        <div style={{ fontSize: 12, fontWeight: 600, color: "#fff",
          fontFamily: "var(--font-mono)", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>
          {file.name}
        </div>
        <div style={{ fontSize: 10, color: "#71717A", fontFamily: "var(--font-mono)", marginTop: 2 }}>
          {file.size} · {file.time}
        </div>
      </div>
      <span style={{ fontSize: 9, color: meta.color, fontFamily: "var(--font-mono)", fontWeight: 700, letterSpacing: ".05em" }}>
        .{file.ext.toUpperCase()}
      </span>
    </div>
  );
};

const FileViewer = ({ file, onDelete }) => {
  const [copied, setCopied] = useState(false);
  const copy = () => {
    navigator.clipboard?.writeText(file.content || "");
    setCopied(true);
    setTimeout(() => setCopied(false), 1400);
  };
  const meta = extMeta(file.ext);
  const lines = (file.content || "").split("\n");

  return (
    <>
      <div style={{
        padding: "12px 20px", borderBottom: "1px solid #1A1A1A",
        display: "flex", justifyContent: "space-between", alignItems: "center", flexShrink: 0,
        background: "#0F0F0F"
      }}>
        <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
          <Icon name={meta.icon} size={18} color={meta.color} strokeWidth={1.8} />
          <div>
            <div style={{ fontSize: 13, fontWeight: 600, color: "#fff", fontFamily: "var(--font-mono)" }}>
              {file.name}
            </div>
            <div style={{ fontSize: 10, color: "#71717A", fontFamily: "var(--font-mono)", marginTop: 2,
              letterSpacing: ".05em" }}>
              {file.path} · {file.size} · {file.time}
            </div>
          </div>
        </div>
        <div style={{ display: "flex", gap: 8 }}>
          <Button primary={false} icon="Copy" onClick={copy}>{copied ? "COPIED" : "COPY"}</Button>
          <Button primary={false} icon="Download">DOWNLOAD</Button>
          <Button primary={false} icon="Trash" onClick={onDelete}
            style={{ color: "#FF3366", borderColor: "rgba(255,51,102,.4)" }}>DELETE</Button>
        </div>
      </div>

      <div style={{ flex: 1, overflow: "auto", background: "#0A0A0A", minHeight: 0 }}>
        <pre style={{ margin: 0, padding: "16px 0", display: "grid",
          gridTemplateColumns: "56px 1fr",
          fontFamily: "var(--font-mono)", fontSize: 12, lineHeight: 1.65 }}>
          {lines.map((line, i) => (
            <React.Fragment key={i}>
              <span style={{ color: "#3F3F46", textAlign: "right", paddingRight: 16,
                userSelect: "none", borderRight: "1px solid #161616" }}>{i + 1}</span>
              <span style={{ paddingLeft: 16, paddingRight: 16, whiteSpace: "pre", color: "#E4E4E7" }}>
                {colorize(line, file.ext)}
              </span>
            </React.Fragment>
          ))}
        </pre>
      </div>
    </>
  );
};

const EmptyFiles = ({ onSync }) => (
  <div style={{ padding: 32, textAlign: "center", color: "#71717A" }}>
    <Icon name="Folder" size={32} color="#3F3F46" strokeWidth={1.5} />
    <div style={{ fontSize: 12, color: "#A1A1AA", marginTop: 12, marginBottom: 4 }}>No files match</div>
    <div style={{ fontSize: 11, color: "#71717A", marginBottom: 16 }}>Try changing the filter or sync from host</div>
    <Button onClick={onSync} icon="Refresh">SYNC FROM HOST</Button>
  </div>
);

const EmptyViewer = () => (
  <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center",
    flexDirection: "column", gap: 8, color: "#71717A", background: "#0A0A0A" }}>
    <Icon name="Eye" size={28} color="#3F3F46" strokeWidth={1.5} />
    <div style={{ fontSize: 12, color: "#A1A1AA", marginTop: 8 }}>Select a file to preview</div>
  </div>
);

// — utilities ————————————————————————————————————
const extMeta = (ext) => ({
  hpp:  { icon: "FileCode", color: "#00E5FF" },
  txt:  { icon: "FileText", color: "#A1A1AA" },
  json: { icon: "FileJson", color: "#FFD600" },
}[ext] || { icon: "File", color: "#A1A1AA" });

// extremely simple per-ext token coloring
const colorize = (line, ext) => {
  if (ext === "json") {
    const parts = [];
    let rest = line;
    const re = /("(?:\\.|[^"\\])*")(\s*:)?|(\b(?:true|false|null)\b)|(-?\d+(?:\.\d+)?)/g;
    let m, idx = 0;
    while ((m = re.exec(line))) {
      if (m.index > idx) parts.push(line.slice(idx, m.index));
      if (m[1] && m[2]) {
        parts.push(<span style={{ color: "#00E5FF" }}>{m[1]}</span>);
        parts.push(<span style={{ color: "#71717A" }}>{m[2]}</span>);
      } else if (m[1]) {
        parts.push(<span style={{ color: "#00FFAA" }}>{m[1]}</span>);
      } else if (m[3]) {
        parts.push(<span style={{ color: "#FF3366" }}>{m[3]}</span>);
      } else if (m[4]) {
        parts.push(<span style={{ color: "#FFD600" }}>{m[4]}</span>);
      }
      idx = re.lastIndex;
    }
    if (idx < line.length) parts.push(line.slice(idx));
    return parts.length ? parts.map((p, i) => <React.Fragment key={i}>{p}</React.Fragment>) : line || " ";
  }
  if (ext === "hpp") {
    if (line.trim().startsWith("//")) return <span style={{ color: "#52525B", fontStyle: "italic" }}>{line}</span>;
    if (line.trim().startsWith("#"))  return <span style={{ color: "#FFD600" }}>{line}</span>;
    const kw = /\b(class|struct|public|private|protected|namespace|template|typename|const|static|void|int|float|double|char|bool|return|if|else|for|while|new|delete|nullptr|using|virtual|override)\b/g;
    const parts = [];
    let idx = 0, m;
    while ((m = kw.exec(line))) {
      if (m.index > idx) parts.push(line.slice(idx, m.index));
      parts.push(<span style={{ color: "#00E5FF", fontWeight: 600 }}>{m[0]}</span>);
      idx = kw.lastIndex;
    }
    if (idx < line.length) parts.push(line.slice(idx));
    return parts.length ? parts.map((p, i) => <React.Fragment key={i}>{p}</React.Fragment>) : line || " ";
  }
  // txt: tag-style coloring like terminal
  const m = line.match(/^\[(INFO|WARN|ERROR|OK|SYS|BOOT|AUTH)\]\s*(.*)$/);
  if (m) {
    const tone = { ERROR: "#FF3366", WARN: "#FFD600", OK: "#00FFAA", INFO: "#00E5FF", SYS: "#A1A1AA", BOOT: "#A1A1AA", AUTH: "#00FFAA" }[m[1]];
    return <><span style={{ color: tone, fontWeight: 600 }}>[{m[1]}]</span> {m[2]}</>;
  }
  return line || " ";
};

window.Files = Files;
