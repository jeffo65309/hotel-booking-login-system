package roomBookingSystem;

import java.io.*;
import java.util.*;
import security.UserManager;

public class RoomBookingSystem {
    static Scanner console = new Scanner(System.in); 
    static ArrayList<Room> rooms = new ArrayList<>();

    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        Console consoleInput = System.console(); 
        
        int choice = -1; // Default
        while (choice != 1 && choice != 2) {
            try {
                System.out.println("Do you want to (1) Register or (2) Login?");
                choice = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
        

            if (choice == 1) {
                boolean registrationSuccess = false;
                while (!registrationSuccess) {
                    System.out.print("Enter username: ");
                    String username = console.nextLine();
                    
                    String password;
                    if (consoleInput != null) {
                        char[] passwordArray = consoleInput.readPassword("Enter password: ");
                        password = new String(passwordArray);
                    } else {
                        System.out.print("Enter password: ");
                        password = console.nextLine();
                    }

                    userManager.registerUser(username, password);
                    
                    if (!registrationSuccess) {
                        System.out.println("Do you want to try registering again? (Y/N)");
                        String retry = console.nextLine();
                        if (retry.equalsIgnoreCase("N")) {
                            System.out.println("Returning to the main menu...");
                            break;
                        }
                    }
                }
            } else if (choice == 2) {
                if (!userManager.authenticateUser()) {
                    System.out.println("Access Denied. Exiting the program.");
                    return;
                }
                System.out.println("Login successful! Proceeding to booking system...");
                break;  
            } else {                
                System.out.println("Invalid choice. Please select 1 for Register or 2 for Login.");
            }
        }

        LoadRoomsData("rooms.txt");
        
        String option = "";
        do {
            displayMenu();
            option = console.next().toUpperCase();

            switch (option) {
                case "1":
                    displayRooms();
                    break;
                case "2":
                    bookRoom();
                    break;
                case "3":
                    searchRoom();
                    break;
                case "4":
                    cancelBooking();
                    break;
                case "Q":
                    saveRoomsData("rooms.txt");
                    System.out.println("Data saved, Goodbye!");
                    console.close();
                    break;
                default:
                    System.out.println("Not a valid option, try again.");
            }
        } while (!option.equals("Q"));
    }

    public static void displayMenu() {
        System.out.println("\n-- Room Booking System --");
        System.out.println("1. Show all rooms");
        System.out.println("2. Book a room");
        System.out.println("3. Search room by requirements");
        System.out.println("4. Cancel a booking");
        System.out.println("Q. Quit");
        System.out.print("---Enter your choice: ");
    }

    public static void LoadRoomsData(String fileName) {
        try {
            Scanner fileReader = new Scanner(new File(fileName));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.isEmpty()) continue;

                String[] roomInfo = line.split(" ");
                int roomNum = Integer.parseInt(roomInfo[0]);
                String roomType = roomInfo[1];
                double roomPrice = Double.parseDouble(roomInfo[2]);
                boolean hasBalcony = roomInfo[3].equals("true");
                boolean hasLounge = roomInfo[4].equals("true");
                String email = roomInfo[5];

                Room room = new Room(roomNum, roomType, roomPrice, hasBalcony, hasLounge, email);
                rooms.add(room);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, the file is not found! Make sure rooms.txt exists. ");
        } catch (NumberFormatException e) {
            System.out.println("Error in file layout check your room prices and numbers.");
        }
    }

    public static void saveRoomsData(String fileName) {
        try {
            PrintWriter writer = new PrintWriter(new File(fileName));
            for (Room room : rooms) {                
                writer.println(room.getRoomNum() + " " + room.getRoomType() + " " + room.getRoomPrice() + " "
                        + room.hasBalcony() + " " + room.hasLounge() + " " + room.getEmail());
            }
            writer.close();
            System.out.println("Changes saved to the file!");
        } catch (IOException e) {
            System.out.println("Something went wrong when saving to the file!");
        }
    }


	//Display all rooms and their info
	public static void displayRooms() {
		for (Room room : rooms) {
			System.out.println(room); 
		}
	}

	//Book a room
	public static void bookRoom() {

		System.out.print(" Do you want to see the full list of rooms? (Y/N)");
		if (console.next().equalsIgnoreCase("Y")) {
			for (Room room : rooms) {
				System.out.println(room);
			}
		}

		int roomNum = 1;
		System.out.print("Enter your email to book a room: ");
		String email = console.next();

		while (true) {
			System.out.print("Enter the room number you want to book: ");
			if (console.hasNextInt()) {
				roomNum = console.nextInt();
				break;
			} else {
				System.out.println("Not Valid, enter a correct room number ");
				console.next();

			}

			boolean found = false;
			for (Room room : rooms) {
				if (room.getRoomNum() == roomNum) {
					found = true;
					if (room.isFree()) {
						room.setEmail(email); //Replaces free with the email
						System.out.println("Room " + roomNum + " booked for " + email + ".");
					} else {
						System.out.println("Sorry, room " + roomNum + " is already booked by " + room.getEmail() + ".");
					}
					break;
				}
			}
			if (!found) {
				System.out.println("Room not found. Double check the number.");
			}
		}
	}

	//Cancel a booking
	public static void cancelBooking() {
		int roomNum = 1;
		while (true) {
			System.out.print("Enter the room number you want to cancel: ");
			if (console.hasNextInt()) {
				roomNum = console.nextInt();
				break;
			} else {
				System.out.println("Not Valid, enter a correct room number ");
				console.next();

			}
		}

		for (Room room : rooms) {
			if (room.getRoomNum() == roomNum) {
				if (!room.isFree()) {
					System.out.println("Booking for room " + roomNum + " has been canceled.");
					room.setEmail("free"); //Sets the room back to free
				} else {
					System.out.println("Room " + roomNum + " is already free.");
				}
				return;
			}
		}
		System.out.println("Room not found. Please try again.");
	}

	//Search Questions for room based on the customers requirements.
	public static void searchRoom() {
		try {
			System.out.println("Enter customers preferences ");
			System.out.println("Lounge? (Y/N): ");
			boolean requiresLounge = console.next().equalsIgnoreCase("Y");
			System.out.println("Balcony? (Y/N): ");
			boolean requiresBalcony = console.next().equalsIgnoreCase("Y");
			System.out.println("Room Type (Single, Double, Suite or any): ");
			String roomTypePref = console.next();
			System.out.println("Max price: ");
			double maxPrice = console.nextDouble();

			boolean roomFound = false;

			//Checks and prints out of available rooms that can be booked
			for (Room room : rooms) {
				if ((room.getRoomType().equalsIgnoreCase(roomTypePref)) || (roomTypePref.equalsIgnoreCase("any"))
						&& (!requiresBalcony || room.hasBalcony()) && (!requiresLounge || room.hasLounge())
						&& room.getRoomPrice() <= maxPrice && room.isFree()) {
					System.out.println(room);

					roomFound = true;
				}
			}
			System.out.println("These are the rooms availible that fit your search ");
			bookRoom();
			if (!roomFound) {
				System.out.println("No matching rooms found.");
			}
			//to handle any invalid inputs
		} catch (Exception e) {
			System.out.println("No matching rooms found.");
			console.nextLine();
		}

	}

}
