package roomBookingSystem;

//Simple Room class to store the room information
public class Room {
    private int roomNum;
    private String roomType;
    private double roomPrice;
    private boolean hasBalcony;
    private boolean hasLounge;
    private String email; //email if booked, otherwise reads as free.

    public Room(int roomNum, String roomType, double roomPrice, boolean hasBalcony, boolean hasLounge, String email) {
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.hasBalcony = hasBalcony;
        this.hasLounge = hasLounge;
        this.email = email;}
    
 //String version of a room
    public String toString() {
     return "Room " +roomNum+ " (" +roomType+ ", £" +roomPrice+ ") " +
             "Balcony: " +(hasBalcony ? "Yes" : "No")+ ", Lounge: " +(hasLounge ? "Yes" : "No") +
             " - " +(isFree() ? "Available" : "Booked by " +email);
 }


        
     

    //Getters and setters 
    public int getRoomNum() {
        return roomNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public boolean hasBalcony() {
        return hasBalcony;
    }

    public boolean hasLounge() {
        return hasLounge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Check if the room is free
    public boolean isFree() {
        return email.equalsIgnoreCase("free");
    }

    
}