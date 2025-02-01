import java.sql.*;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/event";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connected to the database!");

            // Создание события и получение сгенерированного ID
            int eventId = createEvent(conn, "Tech Conference", "2025-02-10", "Astana");

            // Чтение событий
            readEvents(conn);

            // Обновление события по корректному eventId
            updateEvent(conn, eventId, "Updated Conference", "2025-02-15", "Almaty");

            // Чтение для проверки изменений
            readEvents(conn);

            // Удаление события по корректному eventId
            deleteEvent(conn, eventId);

            // Чтение для проверки удаления
            readEvents(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static int createEvent(Connection conn, String name, String date, String location) {
        String sql = "INSERT INTO Event (name, date, location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, location);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    System.out.println("Event created successfully with ID: " + generatedId);
                    return generatedId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void readEvents(Connection conn) {
        String sql = "SELECT * FROM Event";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Event ID: " + rs.getInt("event_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Date: " + rs.getDate("date"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEvent(Connection conn, int eventId, String name, String date, String location) {
        String sql = "UPDATE Event SET name = ?, date = ?, location = ? WHERE event_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, location);
            pstmt.setInt(4, eventId);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event updated successfully!");
            } else {
                System.out.println("No event found with id " + eventId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEvent(Connection conn, int eventId) {
        String sql = "DELETE FROM Event WHERE event_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Event deleted successfully!");
            } else {
                System.out.println("No event found with id " + eventId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}