import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {
            int available = inventory.getAvailability(type);

            // Defensive check
            if (available > 0 && roomCatalog.containsKey(type)) {
                Room room = roomCatalog.get(type);
                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // Should be filtered out
        inventory.addRoom("Suite", 2);

        // Step 2: Setup Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room(
                "Single",
                1500,
                Arrays.asList("WiFi", "TV", "AC")
        ));

        roomCatalog.put("Double", new Room(
                "Double",
                2500,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")
        ));

        roomCatalog.put("Suite", new Room(
                "Suite",
                5000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar", "Jacuzzi")
        ));

        // Step 3: Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest initiates search
        searchService.searchAvailableRooms();
    }
}