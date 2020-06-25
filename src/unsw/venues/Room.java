package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Details about a room (name, size, reservations)
 * @author Kevin Chu
 */
public class Room {
    /**
     * Room's name (String)
     */
    private String name;
    /**
     * Size of the room (small, medium, large)
     */
    private String size;
    /**
     * Reservations of the room (ArrayList<Reservation>)
     */
    private ArrayList<Reservation> reservations;

    /**
     * Constructs a Room given a name and a size
     * @param name Room's name (String)
     * @param size Room's size (String - small, medium, large)
     */
    public Room(String name, String size) {
        this.name = name;
        this.size = size;
        reservations = new ArrayList<Reservation>();
    }

    /**
     * Obtain the size of the room as a String
     * @return Size of room as a string (small, medium, large)
     */
    public String getSize() {
        return size;
    }

    /**
     * Obtain the name of the room as a String
     * @return Name of the room as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Check if the room is available from start date to end date
     * @param start Starting time of booking (LocalDate)
     * @param end Ending time of booking (LocalDate)
     * @return Room if it can be booked, null if it is already booked
     */
    public Room request(LocalDate start, LocalDate end) {
        // For all reservations
        for (Reservation r : reservations) {
            // Check if there start and end date is not currently booked
            if (r.inReservation(start, end)) {
                // Currently booked, return null
                return null;
            }
        }

        // Not currently booked, return room
        return this;
    }

    /**
     * Check if the room is available from start date to end date ignoring a reservation ID
     * @param id Reservation ID to ignore (String)
     * @param start Start time of new booking (LocalDate)
     * @param end Start time of new booking (LocalDate)
     * @return Room if it can be booked, null if it is already booked
     */
    public Room change(String id, LocalDate start, LocalDate end) {
        // For all reservations
        for (Reservation r : reservations) {
            // Ignore reservation which is being replaced
            if (!r.getId().equals(id)) {
                // Checking if time slot is already booked
                if (r.inReservation(start, end)) {
                    // Time slot is already taken
                    return null;
                }
            }
        }

        // Time slot is not taken
        return this;
    }

    /**
     * Adds reservation to Room's list of reservations
     * @param reservation Reservation object containing details of reservation
     */
    public void confirmBooking(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Obtain details about the Room (name) and its reservations (id, start, end) as a HashMap
     * @return Room details (name and reservations) as a HashMap
     */
    public HashMap<String, Object> getDetails() {
        // Creating JSONObject
        HashMap<String, Object> result = new HashMap<String, Object>();

        // Adding room details
        result.put("room", name);

        // Adding reservations of room sorted by starting date
        reservations.sort(Comparator.comparing(r -> r.getStart()));
        ArrayList<HashMap<String, Object>> resultReservations = new ArrayList<HashMap<String, Object>>();
        for (Reservation r : reservations) {
            resultReservations.add(r.getDetails());
        }
        result.put("reservations", resultReservations);

        return result;
    }

    /**
     * Removes a reseravtion from Room
     * @param reservation Reservation filled with details
     */
    public void cancelBooking(Reservation reservation) {
        reservations.remove(reservation);
    }

}
