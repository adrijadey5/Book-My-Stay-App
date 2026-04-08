import java.util.HashMap;

abstract class Room {
    protected String type;
    protected int beds;
    protected double price;


    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public abstract void displayDetails();


}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }


    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }


}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }


    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }


}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }


    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }


}

class RoomInventory {
    private HashMap<String, Integer> inventory;


    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public void displayInventory() {
        for (String key : inventory.keySet()) {
            System.out.println(key + " Available: " + inventory.get(key));
        }
    }


}

public class Main {
    public static void main(String[] args) {


        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v3.0");

        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        r1.displayDetails();
        r2.displayDetails();
        r3.displayDetails();

        System.out.println();

        inventory.displayInventory();

        int current = inventory.getAvailability("Single Room");
        inventory.updateAvailability("Single Room", current - 1);

        System.out.println();

        inventory.displayInventory();
    }


}
