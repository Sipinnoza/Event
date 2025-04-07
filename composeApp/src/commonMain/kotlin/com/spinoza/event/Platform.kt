package com.spinoza.event

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform