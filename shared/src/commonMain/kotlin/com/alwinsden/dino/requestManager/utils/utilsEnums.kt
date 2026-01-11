package com.alwinsden.dino.requestManager.utils

enum class ErrorTypeEnums(val type: Int) {
    CUSTOM(0),
    UNCONTROLLED_EXCEPTION(1),
    UNCONTROLLED_STATE(2),
    UNCONTROLLED_THROWABLE(3)
}