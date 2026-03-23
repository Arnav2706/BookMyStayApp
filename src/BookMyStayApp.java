import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BookMyStayApp {

    public static void main(String[] args) {
        runUC6();
    }

    // === UC6: Reservation Confirmation & Room Allocation ===
    private static void runUC6() {
        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        // Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Suite Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Double Room"));
        requestQueue.addRequest(new Reservation("Diana", "Single Room")); // may fail if no availability

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        System.out.println("=== Hotel Booking System v1.0 ===\n");
        System.out.println("Processing Booking Requests:\n");

        // Process requests in FIFO order
        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Display final inventory state
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

// === Reservation Class ===
class Reservation {
    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRequestedRoomType() {
        return requestedRoomType;
    }
}

// === Booking Request Queue (FIFO) ===
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

// === Booking Service (Allocation & Confirmation) ===
class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    public void processReservation(Reservation reservation) {
        String roomType = reservation.getRequestedRoomType();
        int available = inventory.getAvailability(roomType);

        if (available > 0) {
            // Generate unique room ID
            String roomId = UUID.randomUUID().toString();

            // Ensure uniqueness with Set
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            // Update inventory immediately
            inventory.updateAvailability(roomType, available - 1);

            // Confirm reservation
            System.out.println("Reservation Confirmed: " + reservation.getGuestName() +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomId);
        } else {
            System.out.println("Reservation Failed: " + reservation.getGuestName() +
                    " | Room Type: " + roomType +
                    " | Reason: No availability");
        }
    }
}
// === Add-On Service Class ===
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println("Service: " + serviceName + " | Cost: $" + cost);
    }
}

// === Add-On Service Manager ===
class AddOnServiceManager {
    private HashMap<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach services to a reservation ID
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).addAll(services);
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            System.out.println("Add-On Services for Reservation " + reservationId + ":");
            for (AddOnService s : services) {
                s.displayService();
            }
            System.out.println("Total Additional Cost: $" + calculateTotalCost(reservationId));
        }
    }
}