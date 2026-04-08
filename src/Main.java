import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Room ID: " + roomId +
                " | Status: " + (isCancelled ? "CANCELLED" : "ACTIVE"));
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Booking History (for validation + tracking)
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void addReservation(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return history.get(id);
    }
}

// Cancellation Service (Core Rollback Logic)
class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate reservation
        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation does not exist.");
            return;
        }

        if (r.isCancelled()) {
            System.out.println("Cancellation FAILED: Already cancelled.");
            return;
        }

        // Step 2: Push room ID to rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 3: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 4: Mark reservation cancelled
        r.cancel();

        // Step 5: Confirm rollback
        System.out.println("Cancellation SUCCESS for Reservation ID: " + reservationId);
        System.out.println("Released Room ID: " + rollbackStack.peek());
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 0); // Already booked
        inventory.addRoom("Suite", 0);

        // Step 2: Booking History (simulate confirmed bookings)
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("RES-101", "Alice", "Single", "SI-12345");
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite", "SU-54321");

        history.addReservation(r1);
        history.addReservation(r2);

        // Step 3: Cancellation Service
        CancellationService cancellationService = new CancellationService(inventory, history);

        // Step 4: Perform cancellations

        // Valid cancellation
        cancellationService.cancelBooking("RES-101");

        // Invalid: already cancelled
        cancellationService.cancelBooking("RES-101");

        // Invalid: non-existent booking
        cancellationService.cancelBooking("RES-999");

        // Check inventory after rollback
        System.out.println("\nUpdated Inventory:");
        System.out.println("Single Rooms Available: " + inventory.getAvailability("Single"));
        System.out.println("Suite Rooms Available: " + inventory.getAvailability("Suite"));
    }
}