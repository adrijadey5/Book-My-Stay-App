/**

 /**

 * UseCase2RoomInitialization
 * Demonstrates Room abstraction, inheritance, and static availability.
 *
 * @author Adrija
 * @version 2.0
 */

// Abstract class
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;


    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    // Abstract method
    public abstract void displayDetails();


}

// Single Room
class SingleRoom extends Room {


    public SingleRoom() {
        super("Single Room", 1, 2000);
    }

    @Override
    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }


}

// Double Room
class DoubleRoom extends Room {


    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }

    @Override
    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }

}

// Suite Room
class SuiteRoom extends Room {


    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    @Override
    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }


}

// Main class
public class Main {


    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v2.0");

        // Create room objects (Polymorphism)
        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        // Static availability (no data structures)
        boolean singleAvailable = true;
        boolean doubleAvailable = false;
        boolean suiteAvailable = true;

        System.out.println("\nRoom Details & Availability:");

        r1.displayDetails();
        System.out.println("Available: " + singleAvailable);

        r2.displayDetails();
        System.out.println("Available: " + doubleAvailable);

        r3.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }

}
