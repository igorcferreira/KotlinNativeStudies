package dev.igorcferreira.cloudkitfeatureflag

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}