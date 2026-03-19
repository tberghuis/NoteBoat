package xyz.tberghuis.noteboat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform