import java.util.HashMap;
public class BookMyStayApp {

    public static void main(String[] args) {
        runUC3();
    }

    // === UC3: Centralized Room Inventory Management ===
    private static void runUC3() {
        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display inventory state
        System.out.println("=== Hotel Booking System v1.0 ===\n");

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Single Room") + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Double Room") + "\n");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite Room") + "\n");

        // Example of controlled update
        inventory.updateAvailability("Single Room", 4);
        System.out.println("\nAfter booking one Single Room:");
        System.out.println("Single Room availability: " + inventory.getAvailability("Single Room"));
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

// === Centralized Inventory Manager ===
class RoomInventory {
    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    // Register a room type with initial availability
    public void addRoomType(String roomType, int count) {
        availability.put(roomType, count);
    }

    // Retrieve current availability
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    // Controlled update of availability
    public void updateAvailability(String roomType, int newCount) {
        if (availability.containsKey(roomType)) {
            availability.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Display full inventory state
    public void displayInventory() {
        System.out.println("=== Current Inventory ===");
        for (String roomType : availability.keySet()) {
            System.out.println(roomType + " -> Available: " + availability.get(roomType));
        }
    }
}