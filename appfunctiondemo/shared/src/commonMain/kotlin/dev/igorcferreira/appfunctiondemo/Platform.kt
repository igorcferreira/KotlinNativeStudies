package dev.igorcferreira.appfunctiondemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform