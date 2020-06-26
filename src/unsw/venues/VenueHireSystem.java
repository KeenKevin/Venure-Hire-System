package unsw.venues;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hiring System for multiple venues and multiple rooms
 * @author Kevin Chu
 */
public class VenueHireSystem {
    /**
     * Venues of the hiring system
     */
    private ArrayList<Venue> venues;
    /**
     * Reservations placed on the hiring system
     */
    private ArrayList<Reservation> reservations;

    /**
     * Constructs the hiring system
     */
    public VenueHireSystem() {
        venues = new ArrayList<Venue>();
        reservations = new ArrayList<Reservation>();
    }

    /**
     * Endpoint used to modify venue system
     * @param json Command to be executed (JSONObject)
     */
    private void processCommand(JSONObject json) {
        // For command given by user
        switch (json.getString("command")) {
            // If request is to create a new room
            case "room": {
                // Obtaining input
                String venue = json.getString("venue");
                String room = json.getString("room");
                String size = json.getString("size");

                // Executing command
                addRoom(venue, room, size);
                break;
            }
            // If request is to book a room
            case "request": {
                // Obtaining input
                String id = json.getString("id");
                LocalDate start = LocalDate.parse(json.getString("start"));
                LocalDate end = LocalDate.parse(json.getString("end"));
                int small = json.getInt("small");
                int medium = json.getInt("medium");
                int large = json.getInt("large");

                // Standardise Sizes
                HashMap<String, Integer> sizes = standardiseSizeAmount(small, medium, large);

                // Executing Command
                JSONObject result = request(id, start, end, sizes);

                // Returning result
                System.out.println(result.toString());
                break;
            }
            // If request is to change a reservation
            case "change": {
                // Obtaining input
                String id = json.getString("id");
                LocalDate start = LocalDate.parse(json.getString("start"));
                LocalDate end = LocalDate.parse(json.getString("end"));
                int small = json.getInt("small");
                int medium = json.getInt("medium");
                int large = json.getInt("large");

                // Standardise Sizes
                HashMap<String, Integer> sizes = standardiseSizeAmount(small, medium, large);

                // Executing command
                JSONObject result = change(id, start, end, sizes);

                // Returning result
                System.out.println(result.toString());
                break;
            }
            // If the request is to cancel a reservation
            case "cancel": {
                // Obtaining input
                String id = json.getString("id");

                // Executing command
                cancel(id);

                break;
            }
            // If the request is to list details about a venue's room and reservations
            case "list": {
                // Obtaining input
                String venue = json.getString("venue");

                // Executing command
                JSONArray result = list(venue);

                // Returning result
                System.out.println(result.toString());
                break;
            }
        }
    }

    /**
     * Adds a new room to the system
     * @param venueName New or current name of venue (String)
     * @param room New room's name (String)
     * @param size New room's size (String - small, medium, large)
     */
    private void addRoom(String venueName, String room, String size) {
        // Getting venue if already exists
        Venue venue = getVenue(venueName);

        // Creating new venue if it does not exist
        if (venue == null) {
            venue = addVenue(venueName);
        }

        // Adding room to venue
        venue.addRoom(room, size);
    }

    /**
     * Initiate a booking to the venue
     * @param id Identification of new reservation (String)
     * @param start Start date of reservation (LocalDate)
     * @param end End date of reservation (LocalDate)
     * @param sizes Amount of room sizes requested
     * @return Request status and if successful, venue and room details (JSONObject)
     */
    private JSONObject request(String id, LocalDate start, LocalDate end, HashMap<String, Integer> sizes) {
        // Creating JSONObject
        JSONObject result = new JSONObject();

        // For all venues
        for (Venue v : venues) {
            // Check if a possible booking can be made
            ArrayList<Room> rooms = v.request(start, end, sizes);

            // If a booking can be made
            if (rooms != null) {
                // Creating reservation
                Reservation newReservation = new Reservation(id, start, end, rooms);
                reservations.add(newReservation);

                // Adding venue and status to JSONObject
                result.put("venue", v.getName());
                result.put("status", "success");

                // Obtaining room information for JSONObject and confirming booking
                JSONArray roomNames = new JSONArray();
                for (Room r : rooms) {
                    roomNames.put(r.getName());
                    r.confirmBooking(newReservation);
                }
                result.put("rooms", roomNames);

                return result;
            }
        }

        // Request could not be granted
        result.put("status", "rejected");
        return result;
    }

    /**
     * Change a booking to the venue
     * @param id Identification of new reservation (String)
     * @param start Start date of reservation (LocalDate)
     * @param end End date of reservation (LocalDate)
     * @param sizes Requested sizes and amount of sizes
     * @return Request status and if successful, venue and room details (JSONObject)
     */
    private JSONObject change(String id, LocalDate start, LocalDate end, HashMap<String, Integer> sizes) {
        // Creating JSONObject
        JSONObject result = new JSONObject();

        // For all venues
        for (Venue v : venues) {
            // Check if the change request can be fulfilled
            ArrayList<Room> rooms = v.change(id, start, end, sizes);

            // If a booking can be made
            if (rooms != null) {
                // Creating reservation
                Reservation previousReservation = getReservation(id);
                previousReservation.change(start, end, rooms);

                // Obtaining rooms, venue and status for return JSONObject
                JSONArray roomNames = new JSONArray();
                for (Room r : rooms) {
                    roomNames.put(r.getName());
                }
                result.put("rooms", roomNames);
                result.put("venue", v.getName());
                result.put("status", "success");

                return result;
            }
        }

        // Request could not be fulfiled
        result.put("status", "rejected");
        return result;
    }

    /**
     * Cancel a reservation
     * @param id Identification of reservation
     */
    private void cancel(String id) {
        // Obtaining reservation
        Reservation reservation = getReservation(id);

        // Reservation removing instances of reservation in rooms
        reservation.removeRooms();

        // Removing reservation from self
        reservations.remove(reservation);
    }

    /**
     * List rooms and reservations of a venue
     * @param venueName Name of venue for listing (String)
     * @return List of all rooms in a venue and the room's respective reservations (JSONArray)
     */
    private JSONArray list(String venueName) {
        // Obtaining venue
        Venue venue = getVenue(venueName);

        // Obtaining venue's JSONArray
        JSONArray result = new JSONArray(venue.getRoomDetails());

        return result;
    }

    /**
     * Obtaining venue object from venues in the hiring system
     * @param venueName Name of the venue (String)
     * @return Venue object with name venueName
     */
    public Venue getVenue(String venueName) {
        // For all venues
        for (Venue v : venues) {
            // Checking if the venue has venueName
            if (v.getName().equals(venueName)) {
                // Found venue
                return v;
            }
        }
        
        // Could not find venue with venueName
        return null;
    }

    /**
     * Adding a new venue to the hiring system
     * @param venue Venue name (String)
     * @return Newly created Venue with name venue (Venue)
     */
    public Venue addVenue(String venue) {
        // Creating new venue
        Venue result = new Venue(venue);

        // Adding new venue to system
        venues.add(result);

        return result;
    }

    /**
     * Obtain a reservation given its identification
     * @param id Identification of reservation
     * @return Reservation with identification id (Reservation)
     */
    public Reservation getReservation(String id) {
        // For all reservations
        for (Reservation r : reservations) {
            // Checking if identification matches
            if (r.getId().equals(id)) {
                // Found reservation
                return r;
            }
        }

        // Reservation could not be found
        return null;
    }

    /**
     * Converts sizes to a more standard method to iterate through
     * @param small Amount of small rooms (int)
     * @param medium Amount of medium rooms (int)
     * @param large Amount of large rooms (int)
     * @return Amount of small, medium and large rooms referenced by room size (HashMap<String, Integer>)
     */
    private HashMap<String, Integer> standardiseSizeAmount(int small, int medium, int large) {
        // Creating hash map to store size
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        result.put("small", small);
        result.put("medium", medium);
        result.put("large", large);
    }

    /**
     * Constantly reads from STDIN for JSON commands to the hiring system
     * @param args Initial JSON command
     */
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
