package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.DumpFile
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography

@Composable
fun FilesScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }

    val filteredFiles = state.files.filter {
        (selectedFilter == "all" || it.ext.lowercase() == selectedFilter) &&
        (searchQuery.isEmpty() || it.name.lowercase().contains(searchQuery.lowercase()))
    }

    val currentFile = state.files.find { it.id == state.selectedFileId } ?: filteredFiles.firstOrNull()

    Row(modifier = Modifier.fillMaxSize()) {
        // List Pane (340dp)
        Column(
            modifier = Modifier
                .width(340.dp)
                .fillMaxHeight()
                .background(Color(0xFF0C0C0C))
                .border(width = 0.dp, color = Color.Transparent)
                .drawBehind {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            // Header: Search & Filter
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawLine(
                            color = Color(0xFF1A1A1A),
                            start = androidx.compose.ui.geometry.Offset(0f, size.height + 16.dp.toPx()),
                            end = androidx.compose.ui.geometry.Offset(size.width, size.height + 16.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Search
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(Color(0xFF0F0F0F), RoundedCornerShape(2.dp))
                        .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(2.dp))
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Search, null, tint = Color(0xFF71717A), modifier = Modifier.size(14.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TerminalTypography.copy(color = Color.White, fontSize = 12.sp),
                            cursorBrush = SolidColor(LtiTheme.colors.primary),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("search files…", color = Color(0xFF71717A), style = TerminalTypography.copy(fontSize = 12.sp))
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                // Filter Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F0F0F), RoundedCornerShape(2.dp))
                        .padding(2.dp)
                ) {
                    listOf("all", "hpp", "txt", "json").forEach { filter ->
                        val active = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (active) LtiTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { selectedFilter = filter },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (filter == "all") "All" else ".$filter",
                                style = LtiTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = if (active) LtiTheme.colors.primary else Color(0xFFA1A1AA)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // File List
            LazyColumn(modifier = Modifier.weight(1f)) {
                if (filteredFiles.isEmpty()) {
                    item {
                        EmptyFiles(onSync = { onEvent(AppEvent.SyncFiles) })
                    }
                } else {
                    items(filteredFiles) { file ->
                        FileRowItem(
                            file = file,
                            active = currentFile?.id == file.id,
                            onClick = { onEvent(AppEvent.SelectFile(file.id)) }
                        )
                    }
                }
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .drawBehind {
                        drawLine(
                            color = Color(0xFF1A1A1A),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredFiles.size} of ${state.files.size} files",
                    style = TerminalTypography.copy(fontSize = 10.sp, color = Color(0xFF71717A), letterSpacing = 0.5.sp)
                )
                TextButton(
                    onClick = { onEvent(AppEvent.SyncFiles) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Refresh, null, tint = LtiTheme.colors.primary, modifier = Modifier.size(12.dp))
                        Text(
                            "SYNC",
                            style = LtiTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = LtiTheme.colors.primary
                        )
                    }
                }
            }
        }

        // Viewer Pane
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF0A0A0A))) {
            if (currentFile != null) {
                FileViewer(
                    file = currentFile,
                    onDelete = { onEvent(AppEvent.DeleteFile(it)) }
                )
            } else {
                EmptyViewer()
            }
        }
    }
}

@Composable
private fun FileRowItem(
    file: DumpFile,
    active: Boolean,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val meta = getExtMeta(file.ext)

    val accentColor = LtiTheme.colors.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (active) {
                    drawLine(
                        color = accentColor,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
            .background(if (active) Color(0xFF161616) else if (isHovered) Color(0xFF161616).copy(alpha = 0.5f) else Color.Transparent)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            androidx.compose.ui.input.pointer.PointerEventType.Enter -> isHovered = true
                            androidx.compose.ui.input.pointer.PointerEventType.Exit -> isHovered = false
                        }
                    }
                }
            }
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Icon Chip
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(meta.color.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(meta.icon, null, tint = meta.color, modifier = Modifier.size(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = TerminalTypography.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${file.size} • ${file.time}",
                style = TerminalTypography.copy(fontSize = 10.sp),
                color = Color(0xFF71717A)
            )
        }

        Text(
            text = ".${file.ext.uppercase()}",
            style = TerminalTypography.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
            color = meta.color
        )
    }
}

@Composable
private fun FileViewer(
    file: DumpFile,
    onDelete: (String) -> Unit
) {
    val meta = getExtMeta(file.ext)
    val lines = remember(file.content) { file.content.split("\n") }
    var copied by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F0F0F))
                .drawBehind {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(meta.icon, null, tint = meta.color, modifier = Modifier.size(18.dp))
                Column {
                    Text(
                        text = file.name,
                        style = TerminalTypography.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "${file.path} • ${file.size} • ${file.time}",
                        style = TerminalTypography.copy(fontSize = 10.sp),
                        color = Color(0xFF71717A)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DesktopButton(
                    text = if (copied) "COPIED" else "COPY",
                    onClick = { copied = true }, // Simulated
                    icon = Icons.Default.ContentCopy,
                    isPrimary = false
                )
                DesktopButton(
                    text = "DOWNLOAD",
                    onClick = { },
                    icon = Icons.Default.FileDownload,
                    isPrimary = false
                )
                DesktopButton(
                    text = "DELETE",
                    onClick = { onDelete(file.id) },
                    icon = Icons.Default.Delete,
                    isPrimary = false,
                    modifier = Modifier.border(1.dp, LtiTheme.colors.error.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                )
            }
        }

        // Editor
        Box(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                lines.forEachIndexed { i, line ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp) // Adjusted line height to match 1.65 ratio approx
                    ) {
                        Text(
                            text = "${i + 1}",
                            modifier = Modifier
                                .width(56.dp)
                                .drawBehind {
                                    drawLine(
                                        color = Color(0xFF161616),
                                        start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                                .padding(end = 16.dp),
                            style = TerminalTypography.copy(fontSize = 12.sp, color = Color(0xFF3F3F46)),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                        Text(
                            text = colorize(line, file.ext),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            style = TerminalTypography.copy(fontSize = 12.sp, color = Color(0xFFE4E4E7)),
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFiles(onSync: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(Icons.Default.Folder, null, tint = Color(0xFF3F3F46), modifier = Modifier.size(32.dp))
        Text("No files match", color = Color(0xFFA1A1AA), style = LtiTheme.typography.bodyMedium)
        Text("Try changing the filter or sync from host", color = Color(0xFF71717A), style = LtiTheme.typography.bodySmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        DesktopButton(text = "SYNC FROM HOST", onClick = onSync, icon = Icons.Default.Refresh)
    }
}

@Composable
private fun EmptyViewer() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Visibility, null, tint = Color(0xFF3F3F46), modifier = Modifier.size(28.dp))
        Text("Select a file to preview", color = Color(0xFFA1A1AA), style = LtiTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
    }
}

private data class FileMeta(val icon: ImageVector, val color: Color)

private fun getExtMeta(ext: String) = when (ext.lowercase()) {
    "hpp" -> FileMeta(Icons.Default.Code, Color(0xFF00E5FF))
    "txt" -> FileMeta(Icons.Default.Description, Color(0xFFA1A1AA))
    "json" -> FileMeta(Icons.Default.SettingsApplications, Color(0xFFFFD600))
    else -> FileMeta(Icons.AutoMirrored.Filled.InsertDriveFile, Color(0xFFA1A1AA))
}

private fun colorize(line: String, ext: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        when (ext.lowercase()) {
            "json" -> {
                // Simplified JSON highlighting
                val re = Regex("""("(?:\\.|[^"\\])*")(\s*:)?|(\b(?:true|false|null)\b)|(-?\d+(?:\.\d+)?)""")
                var lastIndex = 0
                re.findAll(line).forEach { match ->
                    append(line.substring(lastIndex, match.range.first))
                    val group1 = match.groups[1]
                    val group2 = match.groups[2]
                    val group3 = match.groups[3]
                    val group4 = match.groups[4]

                    if (group1 != null && group2 != null) {
                        withStyle(SpanStyle(color = Color(0xFF00E5FF))) { append(group1.value) }
                        withStyle(SpanStyle(color = Color(0xFF71717A))) { append(group2.value) }
                    } else if (group1 != null) {
                        withStyle(SpanStyle(color = Color(0xFF00FFAA))) { append(group1.value) }
                    } else if (group3 != null) {
                        withStyle(SpanStyle(color = Color(0xFFFF3366))) { append(group3.value) }
                    } else if (group4 != null) {
                        withStyle(SpanStyle(color = Color(0xFFFFD600))) { append(group4.value) }
                    }
                    lastIndex = match.range.last + 1
                }
                append(line.substring(lastIndex))
            }
            "hpp" -> {
                if (line.trim().startsWith("//")) {
                    withStyle(SpanStyle(color = Color(0xFF52525B), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) { append(line) }
                } else if (line.trim().startsWith("#")) {
                    withStyle(SpanStyle(color = Color(0xFFFFD600))) { append(line) }
                } else {
                    val kw = Regex("""\b(class|struct|public|private|protected|namespace|template|typename|const|static|void|int|float|double|char|bool|return|if|else|for|while|new|delete|nullptr|using|virtual|override)\b""")
                    var lastIndex = 0
                    kw.findAll(line).forEach { match ->
                        append(line.substring(lastIndex, match.range.first))
                        withStyle(SpanStyle(color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)) { append(match.value) }
                        lastIndex = match.range.last + 1
                    }
                    append(line.substring(lastIndex))
                }
            }
            "txt", "log" -> {
                val m = Regex("""^\[(INFO|WARN|ERROR|OK|SYS|BOOT|AUTH)\]\s*(.*)$""").find(line)
                if (m != null) {
                    val tone = m.groupValues[1]
                    val content = m.groupValues[2]
                    val color = when (tone) {
                        "ERROR" -> Color(0xFFFF3366)
                        "WARN" -> Color(0xFFFFD600)
                        "OK" -> Color(0xFF00FFAA)
                        "INFO" -> Color(0xFF00E5FF)
                        else -> Color(0xFFA1A1AA)
                    }
                    withStyle(SpanStyle(color = color, fontWeight = FontWeight.Bold)) { append("[$tone]") }
                    append(" ")
                    append(content)
                } else {
                    append(line)
                }
            }
            else -> append(line)
        }
        if (length == 0) append(" ") // Prevent zero-width lines
    }
}
