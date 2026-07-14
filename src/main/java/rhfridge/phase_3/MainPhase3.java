package obf.phase_3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author obf
 */
public class MainPhase3 {

    public static void main(String[] args) {
        FileManager files = new FileManager();
        Scanner scanner = new Scanner(System.in);

        // Load all necessary data
        files.loadValidAddons();
        files.loadValidActs();
        files.loadItnActs();
        files.loadItineraries();

        // Main loop
        while (true) {
            if (displayMenu(files, scanner)) {
                continue;
            } else {
                break;
            }
        }

        // Save all data before exiting
        files.writeAddons();
        files.writeActivites();
        files.writeItineraries();

        // Close the scanner resource
        scanner.close();
        System.out.println("Program exited successfully.");
    }

    private static boolean displayMenu(FileManager files, Scanner scanner) {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create Itinerary");
        System.out.println("2. Create Activity");
        System.out.println("3. Create Add-on");
        System.out.println("4. List Itinerary");
        System.out.println("5. How do I use this?");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                files.createItinerary();
                return true;
            case "2":
                files.createActivity();
                return true;
            case "3":
                files.createAddons();
                return true;
            case "4":
                System.out.println("Please input an Itinerary:");
                String itn = scanner.nextLine();
                if (files.findItn(itn) != null) {
                    files.findItn(itn).listItn();
                } else {
                    System.out.println("Itinerary not found!");
                }
                return true;
            case "5":
                howTo();
                return true;
            case "6":
                System.out.println("Exiting...");
                return false; // Signal to exit the main loop
            default:
                System.out.println("Invalid choice, please try again.");
                return true;
        }
    }

    /**
     * Displays information on how the program works to aid in usage for the
     * user
     */
    private static void howTo() {
        System.out.println("---How to---");
        System.out.println("Welcome to the Itinerary Manager!");
        System.out.println("On first run you will encounter the main menu.");
        System.out.println("The options do the following tasks:");
        System.out.println("");
        System.out.println("1: Create Itinerary");
        System.out.println("This will intiate the Creating Itinerary sequence");
        System.out.println("This will likely fail if you haven't created any add-ons or activites");
        System.out.println("Though some do ship with this program by default.");
        System.out.println("These are stored in the itineraries.txt file in the storage folder");
        System.out.println("Additionally any activites you add will be stored in the itnacts.txt file");
        System.out.println("");
        System.out.println("2: Create Activity");
        System.out.println("This will intiate the Creating Activity sequence");
        System.out.println("This will likely fail if you haven't created any add-ons yet");
        System.out.println("Creating activites in this mode will allow you to add them to the Itineraries you create");
        System.out.println("These are stored in the activites.txt file in the storage folder");
        System.out.println("");
        System.out.println("3: Create Add-on");
        System.out.println("This will intiate the Creating Add-on sequence");
        System.out.println("Creating add-ons in this mode will allow you to add them to activites and itineraries");
        System.out.println("These are stored in the addons.txt file in the storage folder");
        System.out.println("");
        System.out.println("4: List Itinerary");
        System.out.println("This will allow you enter an itinerary reference and have it listed to you.");
        System.out.println("This works even if the itiinerary hasn't been saved yet");
        System.out.println("This can be useful to check your itinerary was created correctly");
        System.out.println("");
        System.out.println("5: Summons this How To prompt");
        System.out.println("");
        System.out.println("6: Exits the Program");
        System.out.println("This menu will loop until you enter Option 6");

    }
}
