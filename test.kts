import java.time.LocalDateTime
try {
    val dt = LocalDateTime.parse("2026-07-01T12:00")
    println("Success: $dt")
} catch (e: Exception) {
    println("Error: ${e.message}")
}
