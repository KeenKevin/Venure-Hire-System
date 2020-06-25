package unsw.venues;

import java.util.ArrayList;
import java.time.LocalDate;

import org.json.JSONArray;

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
     * @param Name Name of room (String)
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
     * @param small Amount of small rooms (int)
     * @param medium Amount of medium rooms (int)
     * @param large Amount of large rooms (int)
     * @return ArrayList<Room> if it is a possible booking, null if not possible
     */
    public ArrayList<Room> request(LocalDate start, LocalDate end, int small, int medium, int large) {
        // Creating ArrayList of rooms
        ArrayList<Room> result = new ArrayList<Room>();

        // For all rooms
        for (Room r : rooms) {
            // Find out the size of the room
            switch(r.getSize()) {
                // If the size is small
                case "small": {
                    // Check if we still need small rooms
                    if (small > 0) {
                        // Attempt to book room if possible
                        Room room = r.request(start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            small -= 1;
                        }
                    }
                    break;
                }
                // If the size is medium
                case "medium": {
                    // Check if we still need medium rooms
                    if (medium > 0) {
                        // Attempt to book room if possible
                        Room room = r.request(start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            medium -= 1;
                        }
                    }
                    break;
                }
                // If the size is large
                case "large": {
                    // Check if we still need large rooms
                    if (large > 0) {
                        // Attempt to book room if possible
                        Room room = r.request(start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            large -= 1;
                        }
                    }
                    break;
                }
            }

            // Check if the venue can satisfy the request
            if (small == 0 && medium == 0 && large == 0) {
                return result;
            }
        }

        // Venue cannot satisfy the request
        return null;
    }

    public ArrayList<Room> change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        // Creating result to store rooms which can be booked
        ArrayList<Room> result = new ArrayList<Room>();

        // For all rooms
        for (Room r : rooms) {
            // Find out the size of the room
            switch(r.getSize()) {
                // If the size is small
                case "small": {
                    // Check if we still need small rooms
                    if (small > 0) {
                        // Attempt to book room if possible
                        Room room = r.change(id, start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            small -= 1;
                        }
                    }
                    break;
                }
                // If the size is medium
                case "medium": {
                    // Check if we still need medium rooms
                    if (medium > 0) {
                        // Attempt to book room if possible
                        Room room = r.change(id, start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            medium -= 1;
                        }
                    }
                    break;
                }
                // If the size is large
                case "large": {
                    // Check if we still need large rooms
                    if (large > 0) {
                        // Attempt to book room if possible
                        Room room = r.change(id, start, end);

                        // Check if the room can be booked for the time frame
                        if (room != null) {
                            // Found room to book
                            result.add(room);
                            large -= 1;
                        }
                    }
                    break;
                }
            }

            // Check if the venue can satisfy the request
            if (small == 0 && medium == 0 && large == 0) {
                return result;
            }
        }

        // Venue cannot satisfy request
        return null;
    }

    /**
     * Obtain's a JSONArray of the venue's rooms (name and reservations)
     */
    public JSONArray getRoomJSON() {
        // Creating JSONArray
        JSONArray result = new JSONArray();

        // For all rooms
        for (Room r : rooms) {
            // Add room's JSON
            result.put(r.toJSON());
        }

        return result;
    }

}
