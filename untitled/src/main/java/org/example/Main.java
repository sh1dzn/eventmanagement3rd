import java.sql.*;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connected to the database!");

            // Create an Event
            createEvent(conn, "Tech Conference", "2025-02-10", "Astana");

            // Read Events
            readEvents(conn);

            // Update an Event
            updateEvent(conn, 1, "Updated Conference", "2025-02-15", "Almaty");

            // Delete an Event
            deleteEvent(conn, 1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create Event
    public static void createEvent(Connection conn, String name, String date, String location) {
        String sql = "INSERT INTO Event (name, date, location) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, location);
            pstmt.executeUpdate();
            System.out.println("Event created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read Events
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

    // Update Event
    public static void updateEvent(Connection conn, int eventId, String name, String date, String location) {
        String sql = "UPDATE Event SET name = ?, date = ?, location = ? WHERE event_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, location);
            pstmt.setInt(4, eventId);
            pstmt.executeUpdate();
            System.out.println("Event updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Event
    public static void deleteEvent(Connection conn, int eventId) {
        String sql = "DELETE FROM Event WHERE event_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
            System.out.println("Event deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}