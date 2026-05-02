package com.kade.pay

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform