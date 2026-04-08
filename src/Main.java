import java.util.*;

// Reservation (represents booking intent)
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View next request without removing (peek)
    public Reservation viewNextRequest() {
        return queue.peek();
    }

    // Process next request (dequeue)
    public Reservation getNextRequest() {
        return queue.poll();
    }

    // Display all queued requests
    public void displayQueue() {
        System.out.println("\nCurrent Booking Queue (FIFO Order):");
        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Step 1: Initialize Queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 2: Guests submit booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Single");

        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Step 3: Display queue (arrival order preserved)
        requestQueue.displayQueue();

        // Step 4: Peek next request (no removal)
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.viewNextRequest();
        if (next != null) {
            next.display();
        }

        // Step 5: Simulate processing (still no inventory logic here)
        System.out.println("\nProcessing requests in FIFO order:");
        while (true) {
            Reservation req = requestQueue.getNextRequest();
            if (req == null) break;

            System.out.print("Processing -> ");
            req.display();
        }

        // Final queue state
        requestQueue.displayQueue();
    }
}