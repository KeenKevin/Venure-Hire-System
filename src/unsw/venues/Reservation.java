package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Details about a reservation (id, start, end, rooms)
 * @author Kevin Chu
 */
public class Reservation {
    /**
     * Reservation's ID (String)
     */
    private String id;
    /**
     * Start date of reservation (LocalDate)
     */
    private LocalDate start;
    /**
     * End date of reservation (LocalDate)
     */
    private LocalDate end;
    /**
     * List of reserved rooms (ArrayList<Room>)
     */
    private ArrayList<Room> rooms;

    /**
     * Constructs a Reservation given an id, start date, end date and rooms
     * @param id Identification of reservation (String)
     * @param start Start date of reservation (LocalDate)
     * @param end End date of reservation (LocalDate)
     * @param rooms Rooms booked by reservation (ArrayList<Room>)
     */
    public Reservation(String id, LocalDate start, LocalDate end, ArrayList<Room> rooms) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.rooms = rooms;
    }

    /**
     * Check if a start and end date conflicts with reservation's date
     * @param startReservation Start date of potential reseservation (LocalDate)
     * @param endReservation End date of potential reservation (LocalDate)
     * @return Whether the start or end date is inbetween reservation's date (Boolean)
     */
    public Boolean inReservation(LocalDate startReservation, LocalDate endReservation) {
        Boolean encompass = startReservation.isBefore(start) && endReservation.isAfter(end);
        Boolean startInside = startReservation.isAfter(start) && startReservation.isBefore(end);
        Boolean endInside = endReservation.isAfter(start) && endReservation.isBefore(end);
        Boolean sameStartDate = startReservation.equals(start) || startReservation.equals(end);
        Boolean sameEndDate = endReservation.equals(start) || endReservation.equals(end);

        return encompass || startInside || endInside || sameStartDate || sameEndDate;
    }

    /**
     * Obtain the identification number of a reservation (String)
     * @return ID of reservation (String)
     */
    public String getId() {
        return id;
    }

    /**
     * Obtain the starting date of a reservation (LocalDate)
     * @return Start date of reservation (LocalDate)
     */
    public LocalDate getStart() {
        return start;
    }

    /**
     * Obtain details of a reservation
     * @return Obtain details of a reservation
     */
    public HashMap<String, Object> getDetails() {
        // Creating HashMap
        HashMap<String, Object> result = new HashMap<String, Object>();

        // Adding details
        result.put("id", id);
        result.put("start", start.toString());
        result.put("end", end.toString());

        return result;
    }

    /**
     * Change reservation details to new bookings
     * @param start New start date of reservation (LocalDate)
     * @param end New end date of reservation (LocalDate)
     * @param rooms Set of new rooms (ArrayList<Rooms>)
     */
    public void change(LocalDate start, LocalDate end, ArrayList<Room> rooms) {
        // Deleting rooms and its association in the Rooms object
        removeRooms();
        // Setting new values
        this.rooms = rooms;
        this.start = start;
        this.end = end;

        // Setting association in Rooms object
        for (Room r : this.rooms) {
            r.confirmBooking(this);
        }
    }

    /**
     * Remove room links to this reservation
     */
    public void removeRooms() {
        // Deleting reservation from rooms
        for (Room r : rooms) {
            r.cancelBooking(this);
        }

        // Removing reservation's rooms
        rooms = new ArrayList<Room>();
    }

}
