import java.time.LocalDateTime;
public class TestDate {
    public static void main(String[] args) {
        try {
            System.out.println(LocalDateTime.parse("2026-07-08T12:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
