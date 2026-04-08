import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation (Input from user)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {
        int current = inventory.getOrDefault(type, 0);

        // Guard against invalid state
        if (current <= 0) {
            throw new InvalidBookingException("Cannot decrement. No rooms available for type: " + type);
        }

        inventory.put(type, current - 1);
    }
}

// Validator (Fail-Fast)
class InvalidBookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + reservation.getRoomType());
        }
    }
}

// Booking Service with Validation
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            // Step 1: Validate input (FAIL FAST)
            InvalidBookingValidator.validate(reservation, inventory);

            // Step 2: Allocate (safe now)
            inventory.decrement(reservation.getRoomType());

            // Step 3: Confirm booking
            String roomId = generateRoomId(reservation.getRoomType());

            System.out.println("Booking SUCCESS for " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType() +
                    " | Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 5);
    }
}

// MAIN CLASS (Case Sensitive)
public class Main {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Suite", 0);

        // Step 2: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 3: Test Cases (Valid + Invalid)

        // Valid booking
        bookingService.confirmBooking(new Reservation("Alice", "Single"));

        // Invalid: No availability
        bookingService.confirmBooking(new Reservation("Bob", "Suite"));

        // Invalid: Wrong room type
        bookingService.confirmBooking(new Reservation("Charlie", "Deluxe"));

        // Invalid: Empty name
        bookingService.confirmBooking(new Reservation("", "Single"));

        // Invalid: Trying again after inventory exhausted
        bookingService.confirmBooking(new Reservation("David", "Single"));
    }
}