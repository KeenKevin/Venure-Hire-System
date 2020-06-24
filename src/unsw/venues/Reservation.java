package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONObject;

public class Reservation {
    private String id;
    private LocalDate start;
    private LocalDate end;
    private ArrayList<Room> rooms;

    public Reservation(String id, LocalDate start, LocalDate end, ArrayList<Room> rooms) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.rooms = rooms;
    }

    public Boolean inReservation(LocalDate startReservation, LocalDate endReservation) {
        Boolean encompass = startReservation.isBefore(start) && endReservation.isAfter(end);
        Boolean startInside = startReservation.isAfter(start) && startReservation.isBefore(end);
        Boolean endInside = endReservation.isAfter(start) && endReservation.isBefore(end);
        Boolean sameDate = startReservation.equals(start) || endReservation.equals(end);

        return encompass || startInside || endInside || sameDate;
    }

    public String getId() {
        return id;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("start", start.toString());
        result.put("end", end.toString());

        return result;
    }

    public void change(LocalDate start, LocalDate end, ArrayList<Room> rooms) {
        removeRooms();
        this.rooms = rooms;
        this.start = start;
        this.end = end;

        for (Room r : this.rooms) {
            r.addReservation(this);
        }

    }

    public void removeRooms() {
        for (Room r : rooms) {
            r.removeReservation(this);
        }

        rooms = new ArrayList<Room>();
    }

}
