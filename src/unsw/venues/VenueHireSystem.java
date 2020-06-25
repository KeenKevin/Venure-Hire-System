package unsw.venues;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class VenueHireSystem {
    private ArrayList<Venue> venues;
    private ArrayList<Reservation> reservations;

    public VenueHireSystem() {
        venues = new ArrayList<Venue>();
        reservations = new ArrayList<Reservation>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {
            case "room": {
                String venue = json.getString("venue");
                String room = json.getString("room");
                String size = json.getString("size");
                addRoom(venue, room, size);
                break;
            }

            case "request": {
                String id = json.getString("id");
                LocalDate start = LocalDate.parse(json.getString("start"));
                LocalDate end = LocalDate.parse(json.getString("end"));
                int small = json.getInt("small");
                int medium = json.getInt("medium");
                int large = json.getInt("large");

                JSONObject result = request(id, start, end, small, medium, large);

                System.out.println(result.toString());
                break;
            }

            case "change": {
                String id = json.getString("id");
                LocalDate start = LocalDate.parse(json.getString("start"));
                LocalDate end = LocalDate.parse(json.getString("end"));
                int small = json.getInt("small");
                int medium = json.getInt("medium");
                int large = json.getInt("large");

                JSONObject result = change(id, start, end, small, medium, large);
                
                System.out.println(result.toString());
                break;
            }

            case "cancel": {
                String id = json.getString("id");
                cancel(id);

                break;
            }

            case "list": {
                String venue = json.getString("venue");

                JSONArray result = list(venue);

                System.out.println(result.toString());
                break;
            }
        }
    }

    private void addRoom(String venueName, String room, String size) {
        Venue venue = getVenue(venueName);
        if (venue == null) {
            venue = addVenue(venueName);
        }

        venue.addRoom(room, size);
    }

    public JSONObject request(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();

        for (Venue v : venues) {
            ArrayList<Room> rooms = v.request(start, end, small, medium, large);

            if (rooms != null) {
                Reservation newReservation = new Reservation(id, start, end, rooms);
                reservations.add(newReservation);

                JSONArray roomNames = new JSONArray();
                for (Room r : rooms) {
                    roomNames.put(r.getName());
                    r.confirmBooking(newReservation);
                }
                result.put("rooms", roomNames);

                result.put("venue", v.getName());
                result.put("status", "success");

                return result;
            }
        }

        result.put("status", "rejected");
        return result;
    }

    public JSONObject change(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();

        for (Venue v : venues) {
            ArrayList<Room> rooms = v.change(id, start, end, small, medium, large);

            if (rooms != null) {
                Reservation previousReservation = getReservation(id);
                previousReservation.change(start, end, rooms);
                result.put("status", "success");

                JSONArray roomNames = new JSONArray();
                for (Room r : rooms) {
                    roomNames.put(r.getName());
                }
                result.put("rooms", roomNames);
                result.put("venue", v.getName());

                return result;
            }
        }

        result.put("status", "rejected");
        return result;
    }

    public void cancel(String id) {
        Reservation reservation = getReservation(id);
        reservation.removeRooms();
        reservations.remove(reservation);
    }

    public JSONArray list(String id) {
        Venue venue = getVenue(id);
        JSONArray result = venue.getRoomJSON();

        return result;
    }

    public Venue getVenue(String venueName) {
        for (Venue v : venues) {
            if (v.getName().equals(venueName)) {
                return v;
            }
        }
        return null;
    }

    public Venue addVenue(String venue) {
        Venue result = new Venue(venue);
        venues.add(result);
        return result;
    }

    public Reservation getReservation(String id) {
        for (Reservation r : reservations) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
