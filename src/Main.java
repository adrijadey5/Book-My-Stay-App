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

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized ensures only one thread modifies queue at a time
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Thread-safe Inventory
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    // Critical section
    public synchronized boolean allocateRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available <= 0) {
            return false;
        }

        // simulate delay (to expose race condition if not synchronized)
        try { Thread.sleep(50); } catch (Exception e) {}

        inventory.put(type, available - 1);
        return true;
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Concurrent Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {

            Reservation r;

            // synchronized retrieval
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            processBooking(r);
        }
    }

    private void processBooking(Reservation r) {
        boolean success = inventory.allocateRoom(r.getRoomType());

        if (success) {
            String roomId = generateRoomId(r.getRoomType());
            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS: " + r.getGuestName() +
                    " got " + roomId);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: No room for " + r.getGuestName());
        }
    }

    private String generateRoomId(String type) {
        return type.substring(0, 2).toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 5);
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        // Step 1: Shared Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2); // only 2 rooms

        // Step 2: Shared Queue
        BookingQueue queue = new BookingQueue();

        // Step 3: Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single"));

        // Step 4: Multiple Threads (Concurrent Users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t3.setName("Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Step 5: Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (Exception e) {}

        // Final inventory
        System.out.println("\nFinal Available Rooms: " +
                inventory.getAvailability("Single"));
    }
}