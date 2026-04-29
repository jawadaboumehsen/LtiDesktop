# Graph Report - LtiDesktop  (2026-04-28)

## Corpus Check
- 32 files · ~29,672 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 112 nodes · 80 edges · 14 communities detected
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 21|Community 21]]

## God Nodes (most connected - your core abstractions)
1. `AppViewModel` - 11 edges
2. `SettingsRepository` - 7 edges
3. `TerminalRepository` - 7 edges
4. `LtiApiService` - 5 edges
5. `ThemeManager` - 2 edges
6. `LineType` - 1 edges
7. `OutputLine` - 1 edges
8. `ConnectionState` - 1 edges
9. `CommandRequest` - 1 edges
10. `CommandResponse` - 1 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Communities

### Community 0 - "Community 0"
Cohesion: 0.17
Nodes (11): AppEvent, ClearOutput, Connect, Disconnect, HistoryDown, HistoryUp, NavigateTo, SendCommand (+3 more)

### Community 1 - "Community 1"
Cohesion: 0.17
Nodes (1): AppViewModel

### Community 2 - "Community 2"
Cohesion: 0.25
Nodes (1): SettingsRepository

### Community 3 - "Community 3"
Cohesion: 0.25
Nodes (1): TerminalRepository

### Community 4 - "Community 4"
Cohesion: 0.33
Nodes (1): LtiApiService

### Community 6 - "Community 6"
Cohesion: 0.4
Nodes (4): AppEffect, ScrollToBottom, ShowToast, UpdateInput

### Community 7 - "Community 7"
Cohesion: 0.4
Nodes (1): LogData

### Community 9 - "Community 9"
Cohesion: 0.67
Nodes (2): LineType, OutputLine

### Community 10 - "Community 10"
Cohesion: 0.67
Nodes (2): CommandRequest, CommandResponse

### Community 11 - "Community 11"
Cohesion: 0.67
Nodes (2): AppState, Screen

### Community 15 - "Community 15"
Cohesion: 0.67
Nodes (1): ThemeManager

### Community 16 - "Community 16"
Cohesion: 0.67
Nodes (1): JVMPlatform

### Community 18 - "Community 18"
Cohesion: 1.0
Nodes (1): ConnectionState

### Community 21 - "Community 21"
Cohesion: 1.0
Nodes (1): LtiColors

## Knowledge Gaps
- **25 isolated node(s):** `LineType`, `OutputLine`, `ConnectionState`, `CommandRequest`, `CommandResponse` (+20 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **Thin community `Community 1`** (12 nodes): `AppViewModel.kt`, `AppViewModel`, `.clearOutput()`, `.connect()`, `.disconnect()`, `.historyDown()`, `.historyUp()`, `.onCleared()`, `.onEvent()`, `.sendCommand()`, `.updateHost()`, `.updatePort()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 2`** (8 nodes): `SettingsRepository.kt`, `SettingsRepository`, `.getHost()`, `.getPort()`, `.isDarkTheme()`, `.setDarkTheme()`, `.setHost()`, `.setPort()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 3`** (8 nodes): `TerminalRepository.kt`, `TerminalRepository`, `.addLine()`, `.classifyLine()`, `.clear()`, `.executeCommand()`, `.getHistoryDown()`, `.getHistoryUp()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 4`** (6 nodes): `LtiApiService.kt`, `LtiApiService`, `.connect()`, `.disconnect()`, `.execute()`, `.startReceiveLoop()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 7`** (5 nodes): `ActivityLogItem()`, `HomeScreen()`, `LogData`, `StatusCard()`, `HomeScreen.kt`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 9`** (3 nodes): `Models.kt`, `LineType`, `OutputLine`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 10`** (3 nodes): `Protocol.kt`, `CommandRequest`, `CommandResponse`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 11`** (3 nodes): `AppState.kt`, `AppState`, `Screen`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 15`** (3 nodes): `ThemeManager.kt`, `ThemeManager`, `.toggleTheme()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 16`** (3 nodes): `Platform.kt`, `getPlatform()`, `JVMPlatform`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 18`** (2 nodes): `ConnectionState.kt`, `ConnectionState`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 21`** (2 nodes): `Colors.kt`, `LtiColors`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What connects `LineType`, `OutputLine`, `ConnectionState` to the rest of the system?**
  _25 weakly-connected nodes found - possible documentation gaps or missing edges._