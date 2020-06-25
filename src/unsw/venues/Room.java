package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kevin Chu
 */
public class Room {
    /**
     * Room's name
     */
    private String name;
    private String size;
    private ArrayList<Reservation> reservations;

    public Room(String name, String size) {
        this.name = name;
        this.size = size;
        reservations = new ArrayList<Reservation>();
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Room request(LocalDate start, LocalDate end) {
        for (Reservation r : reservations) {
            if (r.inReservation(start, end)) {
                return null;
            }
        }

        return this;
    }

    public Room change(String id, LocalDate start, LocalDate end) {
        for (Reservation r : reservations) {
            if (r.getId().equals(id) == false) {
                if (r.inReservation(start, end)) {
                    return null;
                }
            }
        }

        return this;
    }

    public void confirmBooking(Reservation reservation) {
        reservations.add(reservation);
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();

        result.put("room", name);

        reservations.sort(Comparator.comparing(r -> r.getStart()));
        JSONArray resultReservations = new JSONArray();
        for (Reservation r : reservations) {
            resultReservations.put(r.toJSON());
        }
        result.put("reservations", resultReservations);

        return result;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

}
