import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

// Inventory (Serializable)
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory State:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : reservations) {
            r.display();
        }
    }
}

// Wrapper for full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    InventoryService inventory;
    BookingHistory history;

    public SystemState(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state SAVED successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state LOADED successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }

        // fallback safe state
        return new SystemState(new InventoryService(), new BookingHistory());
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        // Step 1: Load previous state (if exists)
        SystemState state = PersistenceService.load();

        InventoryService inventory = state.inventory;
        BookingHistory history = state.history;

        // Step 2: If first run, initialize data
        if (inventory.getInventory().isEmpty()) {
            inventory.addRoom("Single", 2);
            inventory.addRoom("Suite", 1);

            history.addReservation(new Reservation("RES-101", "Alice", "Single"));
            history.addReservation(new Reservation("RES-102", "Bob", "Suite"));
        }

        // Step 3: Display current state
        inventory.display();
        history.display();

        // Step 4: Simulate new booking
        System.out.println("\nAdding new booking...");
        history.addReservation(new Reservation("RES-103", "Charlie", "Single"));

        // Step 5: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, history));

        System.out.println("\nSystem shutting down... Restart to verify persistence.");
    }
}