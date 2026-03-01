package com.alwinsden.dino

class JVMPlatform: Platform {
    override val name: String = "Java"
    override val operatingVersion: String = System.getProperty("java.version")
}

actual fun getPlatform(): Platform = JVMPlatform()