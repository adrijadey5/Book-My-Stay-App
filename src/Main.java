import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Booking History (Stores all confirmed bookings)
class BookingHistory {

    // List preserves insertion order (chronological)
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Booking Report Service (Read-only reporting)
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n=== Booking History ===");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n=== Booking Summary Report ===");

        List<Reservation> list = history.getAllReservations();

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : list) {
            String type = r.getRoomType();
            roomTypeCount.put(type, roomTypeCount.getOrDefault(type, 0) + 1);
        }

        for (String type : roomTypeCount.keySet()) {
            System.out.println("Room Type: " + type + " | Bookings: " + roomTypeCount.get(type));
        }

        System.out.println("Total Bookings: " + list.size());
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        // Step 1: Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES-101", "Alice", "Single");
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite");
        Reservation r3 = new Reservation("RES-103", "Charlie", "Single");

        // Step 3: Add to history (chronological order)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 4: Reporting Service
        BookingReportService reportService = new BookingReportService(history);

        // Step 5: Admin views booking history
        reportService.displayAllBookings();

        // Step 6: Admin generates summary report
        reportService.generateSummaryReport();
    }
}