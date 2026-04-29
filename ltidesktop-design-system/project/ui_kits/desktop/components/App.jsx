// App.jsx — root state machine
const initialFiles = [
  { id: 1, name: "system_dump_4291.hpp", ext: "hpp", size: "2.4 MB", time: "4m ago",
    path: "/var/dumps/system_dump_4291.hpp",
    content: `// System memory dump — node-01
// Generated 2026-04-29T14:23:08Z · build 4291
#include <cstdint>
#include <vector>
#include <string>

namespace lti::dump {

struct MemoryRegion {
  uint64_t   base;
  uint64_t   size;
  uint32_t   flags;
  std::string label;
};

class SystemSnapshot {
public:
  SystemSnapshot() = default;
  virtual ~SystemSnapshot() = default;

  // Capture every mapped region in the live process.
  bool capture(const std::string& host);

  const std::vector<MemoryRegion>& regions() const { return regions_; }

private:
  std::vector<MemoryRegion> regions_;
  uint32_t                  checksum_ = 0;
};

} // namespace lti::dump
` },
  { id: 2, name: "kernel_panic.txt", ext: "txt", size: "18 KB", time: "12m ago",
    path: "/var/log/kernel_panic.txt",
    content: `[INFO] kernel: Linux node-01 5.15.0-101 #amd64 SMP
[BOOT] kernel: Booting paravirtualized kernel on KVM
[INFO] kernel: BIOS-provided physical RAM map
[INFO] kernel: ACPI: PM-Timer IO Port: 0x608
[WARN] kernel: x86/cpu: SGX disabled by BIOS
[INFO] systemd: Started Network Manager Wait Online
[OK]   systemd: Reached target Network is Online
[ERROR] worker-03: cannot allocate memory (errno=12)
[ERROR] worker-03: page allocation failure: order:4
[WARN] kernel: Out of memory: Kill process 4291 (patcher)
[INFO] systemd: patcher.service: Main process exited
[OK]   systemd: patcher.service: restart scheduled (1/5)
[AUTH] sshd[2841]: Accepted publickey for root from 10.0.0.1
` },
  { id: 3, name: "patch_manifest.json", ext: "json", size: "3 KB", time: "23m ago",
    path: "/etc/patcher/manifest.json",
    content: `{
  "version": "2.1.5",
  "build": 4291,
  "applied_at": "2026-04-29T14:02:11Z",
  "host": "node-01",
  "patches": [
    { "id": "CVE-2026-1042", "severity": "high",   "applied": true },
    { "id": "CVE-2026-1118", "severity": "medium", "applied": true },
    { "id": "CVE-2026-1204", "severity": "low",    "applied": false }
  ],
  "checksums": {
    "sha256": "9f4c2e0a7b1d6e3c8a5f4b2d1e0c9a8b7d6e5f4c3b2a1908",
    "verified": true
  },
  "next_run": null
}` },
  { id: 4, name: "trace_4291.txt", ext: "txt", size: "92 KB", time: "1h ago",
    path: "/var/log/trace_4291.txt",
    content: `[INFO] trace start · pid=4291
[SYS]  cpu=0.42 mem=512MB net=stable
[INFO] connecting to upstream registry
[OK]   handshake complete · TLS 1.3
[INFO] downloading manifest (3 KB)
[OK]   manifest verified · sha256 ok
[WARN] retry on shard 7 (1/3)
[OK]   shard 7 received
[INFO] applying 14 hunks
[OK]   patch applied · 14 files modified
[OK]   trace end · duration=2.1s
` },
];

const App = () => {
  const [phase, setPhase] = useState("splash");
  const [screen, setScreen] = useState("home");
  const [session, setSession] = useState(null);
  const [navExpanded, setNavExpanded] = useState(true);
  const [files, setFiles] = useState(initialFiles);

  useEffect(() => {
    if (phase === "splash") {
      const t = setTimeout(() => setPhase("connect"), 1500);
      return () => clearTimeout(t);
    }
  }, [phase]);

  const onConnect = (s) => { setSession(s); setPhase("main"); };

  // Home actions wire into Files: pulling dumps adds new entries.
  const onAction = (key) => {
    if (key === "dump" || key === "memdump") {
      const stamp = Math.floor(Math.random() * 9000 + 1000);
      const newFile = {
        id: Date.now(),
        name: key === "memdump" ? `mem_snapshot_${stamp}.hpp` : `system_dump_${stamp}.hpp`,
        ext: "hpp",
        size: `${(Math.random() * 4 + 1).toFixed(1)} MB`,
        time: "just now",
        path: `/var/dumps/${key === "memdump" ? "mem_snapshot_" : "system_dump_"}${stamp}.hpp`,
        content: `// Captured ${new Date().toISOString()}\n// Trigger: ${key}\n#include <cstdint>\n\nnamespace lti::dump {\n  // ${Math.floor(Math.random()*200 + 50)} regions captured\n  // see SystemSnapshot::regions() for full list\n}\n`,
      };
      setFiles(prev => [newFile, ...prev]);
    } else if (key === "logs") {
      const stamp = Math.floor(Math.random() * 9000 + 1000);
      setFiles(prev => [{
        id: Date.now(),
        name: `sync_${stamp}.txt`, ext: "txt",
        size: `${Math.floor(Math.random()*80 + 10)} KB`, time: "just now",
        path: `/var/log/sync_${stamp}.txt`,
        content: `[INFO] sync started\n[OK]   12 files transferred\n[OK]   sync complete · ${stamp}\n`,
      }, ...prev]);
    }
  };

  if (phase === "splash") return <Splash />;
  if (phase === "connect") return <Connect onConnect={onConnect} />;

  return (
    <div style={{ display: "flex", height: "100%", background: "#0A0A0A" }}>
      <Sidebar
        screen={screen} onNav={setScreen}
        expanded={navExpanded} onToggle={() => setNavExpanded(v => !v)}
        width={navExpanded ? 240 : 64}
      />
      <div style={{ flex: 1, display: "flex", flexDirection: "column", minWidth: 0 }}>
        <TopBar screen={screen} />
        <div style={{ flex: 1, minHeight: 0 }}>
          {screen === "home" && <Home session={session} onAction={onAction} />}
          {screen === "dashboard" && <Dashboard session={session} />}
          {screen === "files" && <Files files={files}
            onDelete={(id) => setFiles(p => p.filter(f => f.id !== id))}
            onSync={() => onAction("logs")} />}
          {screen === "cli" && <Console />}
          {screen === "settings" && <Settings />}
        </div>
      </div>
    </div>
  );
};

window.App = App;
