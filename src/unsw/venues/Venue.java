package unsw.venues;

import java.util.ArrayList;

import org.json.JSONArray;

import java.time.LocalDate;

public class Venue {
    private String name;
    private ArrayList<Room> rooms;

    public Venue(String name) {
        this.name = name;
        rooms = new ArrayList<Room>();
    }

    public String getName() {
        return name;
    }

    public void addRoom(String room, String size) {
        Room newRoom = new Room(room, size);
        rooms.add(newRoom);
    }

    public ArrayList<Room> request(LocalDate start, LocalDate end, int small, int medium, int large) {
        ArrayList<Room> result = new ArrayList<Room>();
        for (Room r : rooms) {
            if (small > 0 && r.getSize().equals("small")) {
                Room room = r.request(start, end);
                if (room != null) {
                    result.add(room);
                    small -= 1;
                }
            }
            if (medium > 0 && r.getSize().equals("medium")) {
                Room room = r.request(start, end);
                if (room != null) {
                    result.add(room);
                    medium -= 1;
                }
            }
            if (large > 0 && r.getSize().equals("large")) {
                Room room = r.request(start, end);
                if (room != null) {
                    result.add(room);
                    large -= 1;
                }
            }
        }

        if (small == 0 && medium == 0 && large == 0) {
            return result;
        }
        return null;
    }

    public ArrayList<Room> change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        ArrayList<Room> result = new ArrayList<Room>();

        for (Room r : rooms) {
            if (small > 0 && r.getSize().equals("small")) {
                Room room = r.change(id, start, end);
                if (room != null) {
                    result.add(room);
                    small -= 1;
                }
            }
            if (medium > 0 && r.getSize().equals("medium")) {
                Room room = r.change(id, start, end);
                if (room != null) {
                    result.add(room);
                    medium -= 1;
                }
            }
            if (large > 0 && r.getSize().equals("large")) {
                Room room = r.change(id, start, end);
                if (room != null) {
                    result.add(room);
                    large -= 1;
                }
            }
        }

        if (small == 0 && medium == 0 && large == 0) {
            return result;
        }
        return null;
    }

    public JSONArray getRoomJSON() {
        JSONArray result = new JSONArray();
        for (Room r : rooms) {
            result.put(r.toJSON());
        }

        return result;
    }

}
