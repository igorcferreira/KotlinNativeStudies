package dev.igorcferreira.sharepointembedded_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform