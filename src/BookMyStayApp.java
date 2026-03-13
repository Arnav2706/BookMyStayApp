
public class BookMyStayApp {

    public static void main(String[] args) {
        runUC2();
    }

    // === UC2: Basic Room Types & Static Availability ===
    private static void runUC2() {
        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability representation
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display room details and availability
        System.out.println("=== Hotel Booking System v1.0 ===\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");
    }
}

// === Abstract Room class ===
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per Night: $" + price);
    }
}

// === Concrete Room classes ===
class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1, 50.0); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 90.0); }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 3, 150.0); }
}