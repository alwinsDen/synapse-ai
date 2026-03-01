package com.alwinsden.dino

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val operatingVersion: String =  Build.VERSION.SDK_INT.toString()
}

actual fun getPlatform(): Platform = AndroidPlatform()