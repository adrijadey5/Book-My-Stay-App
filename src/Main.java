import java.util.*;

// Reservation (Booking Request)
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

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service (State Holder)
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Booking Service (Core Allocation Logic)
class BookingService {

    private InventoryService inventory;

    // Tracks allocated room IDs globally (no duplicates)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Tracks roomType -> allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processQueue(BookingRequestQueue queue) {

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String type = req.getRoomType();

            System.out.println("\nProcessing request for " + req.getGuestName());

            // Step 1: Check availability
            if (inventory.getAvailability(type) <= 0) {
                System.out.println("No rooms available for type: " + type);
                continue;
            }

            // Step 2: Generate unique room ID
            String roomId = generateRoomId(type);

            // Step 3: Ensure uniqueness (defensive check)
            while (allocatedRoomIds.contains(roomId)) {
                roomId = generateRoomId(type);
            }

            // Step 4: Assign room
            allocatedRoomIds.add(roomId);

            roomAllocations.putIfAbsent(type, new HashSet<>());
            roomAllocations.get(type).add(roomId);

            // Step 5: Update inventory immediately
            inventory.decrement(type);

            // Step 6: Confirm booking
            System.out.println("Booking CONFIRMED for " + req.getGuestName());
            System.out.println("Room Type: " + type + " | Room ID: " + roomId);
        }
    }

    // Generate unique room ID
    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }
}

// MAIN CLASS (as required)
public class Main {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Suite", 1);

        // Step 2: Setup Booking Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite")); // should fail

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Queue (FIFO + Safe Allocation)
        bookingService.processQueue(queue);
    }
}