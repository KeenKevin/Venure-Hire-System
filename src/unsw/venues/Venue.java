package unsw.venues;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Details about a venue (name, rooms)
 * @author Kevin Chu
 */
public class Venue {
    /**
     * Name of venue (String)
     */
    private String name;
    /**
     * List of rooms a venue has (ArrayList<Room>)
     */
    private ArrayList<Room> rooms;

    /**
     * Constructs Venue object with name
     * @param name Name of venue (String)
     */
    public Venue(String name) {
        this.name = name;
        rooms = new ArrayList<Room>();
    }

    /**
     * Obtains name of venue (String)
     * @return Name of venue (String)
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a new room to Venue
     * @param name Name of room (String)
     * @param size Size of room (String)
     */
    public void addRoom(String name, String size) {
        // Creating new room
        Room newRoom = new Room(name, size);

        // Adding new room
        rooms.add(newRoom);
    }

    /**
     * Request a potential booking
     * @param start Start date of new booking (LocalDate)
     * @param end End date of new booking (LocalDate)
     * @param sizes Amount of room sizes requested
     * @return ArrayList<Room> if it is a possible booking, null if not possible
     */
    public ArrayList<Room> request(LocalDate start, LocalDate end, HashMap<String, Integer> sizes) {
        // Creating ArrayList of rooms
        ArrayList<Room> result = new ArrayList<Room>();

        // For all rooms
        for (Room r : rooms) {
            // Checking if the rooms for a size is already filled
            if (sizes.get(r.getSize()) > 0) {
                // More rooms of the size need to be booked, attempt to book if possible
                Room room = r.request(start, end);

                // Check if the room can be booked for the time frame
                if (room != null) {
                    // Found room to book
                    result.add(room);
                    sizes.put(r.getSize(), sizes.get(r.getSize()) - 1);
                }
            }

            // Check if the venue can satisfy the request
            if (requestSatisfied(sizes)) {
                return result;
            }
        }

        // Venue cannot satisfy the request
        return null;
    }

    /**
     * Request a potential change of reservation
     * @param id Identification of reservation (String)
     * @param start Start date of reservation (LocalDate)
     * @param end End date of reservation (LocalDate)
     * @param sizes Amount of rooms required for each size (HashMap<String, Integer>)
     * @return New rooms if request can be filled, otherwise null
     */
    public ArrayList<Room> change(String id, LocalDate start, LocalDate end, HashMap<String, Integer> sizes) {
        // Creating result to store rooms which can be booked
        ArrayList<Room> result = new ArrayList<Room>();

        // For all rooms
        for (Room r : rooms) {
            // Checking if the rooms for a size is already filled
            if (sizes.get(r.getSize()) > 0) {
                // More rooms of the size need to be booked, attempt to book if possible
                Room room = r.change(id, start, end);

                // Check if the room can be booked for the time frame
                if (room != null) {
                    // Found room to book
                    result.add(room);
                    sizes.put(r.getSize(), sizes.get(r.getSize()) - 1);
                }
            }

            // Check if the venue can satisfy the request
            if (requestSatisfied(sizes)) {
                return result;
            }
        }

        // Venue cannot satisfy request
        return null;
    }

    /**
     * Obtain's a list of the venue's room details (name and reservations)
     * @return Returns details of all room details at a venue (ArrayList of HashMap(String, Object))
     */
    public ArrayList<HashMap<String, Object>> getRoomDetails() {
        // Creating Result
        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

        // For all rooms
        for (Room r : rooms) {
            // Add room's JSON
            result.add(r.getDetails());
        }

        return result;
    }

    /**
     * Checks if a request is satisfied based on the leftover room requests
     * @param sizes
     * @return Whether or not the requests given are all satisfied by remaining orders
     */
    private Boolean requestSatisfied(HashMap<String, Integer> sizes) {
        // Check if the venue can satisfy the request
        for (Integer i : sizes.values()) {
            if (i != 0) {
                // Venue has not yet  fully satisified request
                return false;
            }
        }
        
        // Request has been satisfied
        return true;
    }

}
