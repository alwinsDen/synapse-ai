package com.alwinsden.dino

interface Platform {
    val name: String
    val operatingVersion : String
}

expect fun getPlatform(): Platform