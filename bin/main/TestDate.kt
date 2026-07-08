import java.time.LocalDateTime

fun main() {
    try {
        println("Trying to parse: 2026-07-08T15:00")
        val date = LocalDateTime.parse("2026-07-08T15:00")
        println("Success: $date")
    } catch(e: Exception) {
        println("Error: ${e.message}")
    }
}
