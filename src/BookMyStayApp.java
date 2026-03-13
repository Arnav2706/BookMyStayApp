import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * BookMyStayApp.java
 *
 * Demonstrates Use Cases 1–6 for a Hotel Booking System.
 * Each use case builds incrementally, showcasing key Java concepts:
 * - UC1: Application Entry & Welcome Message
 * - UC2: Basic Room Types & Static Availability
 * - UC3: Centralized Room Inventory Management
 * - UC4: Room Search & Availability Check
 * - UC5: Booking Request Queue (FIFO)
 * - UC6: Reservation Confirmation & Room Allocation
 *
 * @author YourName
 * @version 1.0
 */
public class BookMyStayApp {

    public static void main(String[] args) {
        // Uncomment the use case you want to run
        // runUC1();
        // runUC2();
        // runUC3();
        // runUC4();
        // runUC5();
        runUC6();
    }

    // === UC1: Application Entry & Welcome Message ===
    // Purpose: Establish a predictable starting point for the application.
    // - Demonstrates how execution begins in Java via the main() method.
    // - Prints a welcome message, application name, and version.
    private static void runUC1() {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application: BookMyStayApp");
        System.out.println("Version: 1.0");
    }

    // === UC2: Basic Room Types & Static Availability ===
    // Purpose: Introduce object modeling with abstraction and inheritance.
    // - Defines abstract Room class with common attributes.
    // - Concrete classes (SingleRoom, DoubleRoom, SuiteRoom) extend Room.
    // - Availability stored in simple variables (int counts).
    private static void runUC2() {
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("=== Hotel Booking System v1.0 ===\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");
    }

    // === UC3: Centralized Room Inventory Management ===
    // Purpose: Replace scattered availability variables with a centralized structure.
    // - Introduces RoomInventory class encapsulating availability logic.
    // - Uses HashMap<String, Integer> for room type → availability mapping.
    // - Provides O(1) lookup and updates.
    private static void runUC3() {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("=== Hotel Booking System v1.0 ===\n");

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Single Room") + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Double Room") + "\n");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite Room") + "\n");
    }

    // === UC4: Room Search & Availability Check ===
    // Purpose: Enable guests to view available rooms without modifying state.
    // - SearchService provides read-only access to inventory.
    // - Filters out rooms with zero availability.
    // - Separation of concerns: search logic is isolated from booking logic.
    private static void runUC4() {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0); // deliberately unavailable
        inventory.addRoomType("Suite Room", 2);

        SearchService searchService = new SearchService(inventory);

        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        System.out.println("=== Hotel Booking System v1.0 ===\n");
        System.out.println("Available Rooms for Guests:\n");

        searchService.displayAvailableRooms(rooms);
    }

    // === UC5: Booking Request Queue (FIFO) ===
    // Purpose: Handle multiple booking requests fairly.
    // - Introduces Reservation class representing guest intent.
    // - BookingRequestQueue stores requests in FIFO order.
    // - No inventory mutation occurs at this stage.
    private static void runUC5() {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        BookingRequestQueue requestQueue = new BookingRequestQueue();
        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Suite Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Double Room"));

        System.out.println("=== Hotel Booking System v1.0 ===\n");
        System.out.println("Booking Requests (waiting to be processed):\n");
        requestQueue.displayRequests();
    }

    // === UC6: Reservation Confirmation & Room Allocation ===
    // Purpose: Confirm booking requests by assigning rooms safely.
    // - BookingService processes requests in FIFO order.
    // - Generates unique room IDs using UUID.
    // - Uses HashMap<String, Set<String>> to track allocated IDs.
    // - Updates inventory immediately after allocation.
    // - Prevents double-booking by enforcing uniqueness in Set.
    private static void runUC6() {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingRequestQueue requestQueue = new BookingRequestQueue();
        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Suite Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Double Room"));
        requestQueue.addRequest(new Reservation("Diana", "Single Room")); // may fail if no availability

        BookingService bookingService = new BookingService(inventory);

        System.out.println("=== Hotel Booking System v1.0 ===\n");
        System.out.println("Processing Booking Requests:\n");

        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.getNextRequest();
            bookingService.processReservation(r);
        }

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();
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

    public String getRoomType() {
        return roomType;
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

    public void addRoomType(String roomType, int count) {
        availability.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newCount) {
        if (availability.containsKey(roomType)) {
            availability.put(roomType, newCount);
        }
    }

    public void displayInventory() {
        for (String roomType : availability.keySet()) {
            System.out.println(roomType + " -> Available: " + availability.get(roomType));
        }
    }
}

// === Search Service (Read-Only Access) ===
class SearchService {
    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void displayAvailableRooms(List<Room> rooms) {
        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

//