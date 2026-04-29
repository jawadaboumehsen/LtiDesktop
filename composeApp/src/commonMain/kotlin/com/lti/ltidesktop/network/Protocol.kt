package com.lti.ltidesktop.network

import kotlinx.serialization.Serializable

@Serializable
data class CommandRequest(val id: String, val cmd: String)

@Serializable
data class CommandResponse(val id: String, val lines: List<String>, val done: Boolean = true)
