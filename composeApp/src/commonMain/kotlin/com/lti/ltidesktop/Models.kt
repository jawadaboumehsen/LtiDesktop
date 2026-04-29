package com.lti.ltidesktop

enum class LineType { NORMAL, CMD, ERROR, SYS, WARN }

data class OutputLine(val text: String, val type: LineType = LineType.NORMAL)
