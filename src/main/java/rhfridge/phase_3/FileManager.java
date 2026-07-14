package obf.phase_3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.logging.*;

/**
 * Class that is used to create, write and load objects
 *
 * @author obf
 */
public class FileManager {

    private static final Logger logger = Logger.getLogger("FileManager");

    private static final String ACTIVITES = "storage/activities.txt";
    private static final String ADDONS = "storage/addons.txt";
    private static final String ITINERARIES = "storage/itineraries.txt";
    private static final String ITNACTS = "storage/itnacts.txt";

    private boolean actsLoaded = false;
    private boolean adnsLoaded = false;
    private boolean itnsLoaded = false;
    private boolean itnactLoaded = false;

    private boolean actsWritten = true;
    private boolean adnsWritten = true;
    private boolean itnsWritten = true;
    private boolean itnactWritten = true;

    private Map<String, Activity> validActivities = new HashMap<>();
    private Map<String, Addon> validAddons = new HashMap<>();
    private Map<String, Itinerary> validItineraries = new HashMap<>();
    private Map<String, ActivityDetailed> validItnActs = new HashMap<>();

    /**
     * Constructor for FileManager class Simply outputs a line to console in
     * case users get confused where the files are stored
     *
     */
    public FileManager() {
        System.out.println("Looking for files in: " + System.getProperty("user.dir") + "/storage");
    }

    /**
     * Method to check if a given file exists or not
     *
     * @param filePath The file to check for existence
     * @return true if the file exists or was successfully created
     */
    private boolean checkAndCreateFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    logger.info(filePath + " created.");
                } else {
                    logger.info(filePath + " already exists.");
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while creating " + filePath);
            e.getMessage();
            return false;
        }
    }

    //Loading files
    /**
     * Procedure to load add-on(s) from file
     *
     * Goes line by line in the file, splits each element by tab then creates a
     * new add-on object if the parts are valid
     *
     */
    public void loadValidAddons() {
        adnsLoaded = false;
        if (!checkAndCreateFile(ADDONS)) { //Check if add-on file exists
            System.out.println("Couldn't find Add-on file!");
        }
        List<String> lines = getLinesFromFile(ADDONS);
        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts.length <= 3) { //Line must be 3 parts long after splitting
                try {
                    Addon addonFromFile = new Addon(parts[0], parts[1], parts[2]);
                    //Each Addon is outputted to the console for debugging
                    logger.info("Loaded Addon:");
                    logger.info(addonFromFile.toString());
                    validAddons.put(parts[0], addonFromFile);
                } catch (ValidationException e) {
                    //If any errors occur, inform the user via the console
                    logger.warning("Error with line! \n"
                            + "Ref: " + parts[0] + "\n"
                            + "Name: " + parts[1] + "\n"
                            + "Cost: " + parts[2] + "\n"
                            + e.getMessage());
                }
            } else {
                //If any errors occur, inform the user via the console
                logger.warning("Error with line! \n"
                        + "Ref: " + parts[0] + "\n"
                        + "Name: " + parts[1] + "\n"
                        + "Cost: " + parts[2] + "\n");
            }
        }
        adnsLoaded = true;
    }

    /**
     * Procedure to load all activities from file
     *
     * Goes line by line in the file, splits each element by tab then creates a
     * new activity object if the parts are valid
     *
     */
    public void loadValidActs() {
        actsLoaded = false;
        if (!checkAndCreateFile(ACTIVITES)) {
            System.out.println("Couldn't find Itinerary file!");
        }
        List<String> lines = getLinesFromFile(ACTIVITES);
        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts.length <= 6) {
                try {
                    Activity actFromFile = new Activity(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    logger.info("Loaded Activity:");
                    logger.info(actFromFile.toString());
                    validActivities.put(parts[0], actFromFile);
                } catch (ValidationException e) {
                    logger.warning("Error with line! \n"
                            + "Ref: " + parts[0] + "\n"
                            + "Name: " + parts[1] + "\n"
                            + "Description: " + parts[2] + "\n"
                            + "Duration (raw): " + parts[3] + "\n"
                            + "Instructor Required (raw): " + parts[4] + "\n"
                            + "Cost (raw): " + parts[5] + "\n"
                            + "Validation Error: " + e.getMessage());

                }
            } else {
                logger.warning("Error with line! \n"
                        + "Ref: " + parts[0] + "\n"
                        + "Name: " + parts[1] + "\n"
                        + "Description: " + parts[2] + "\n"
                        + "Duration (raw): " + parts[3] + "\n"
                        + "Instructor Required (raw): " + parts[4] + "\n"
                        + "Cost (raw): " + parts[5] + "\n");
            }
        }
        actsLoaded = true;
    }

    /**
     * Procedure to load all itineraries from file
     *
     * Goes line by line in the file, splits each element by tab then creates a
     * new itinerary object if the parts are valid.
     *
     *
     */
    public void loadItineraries() {
        itnsLoaded = false;
        if (!checkAndCreateFile(ITINERARIES)) {
            System.out.println("Couldn't find Itinerary file!");
        }
        List<String> lines = getLinesFromFile(ITINERARIES);

        for (String line : lines) {
            try {
                String[] parts = splitLine(line);

                if (parts.length <= 7) {
                    String itnRef = parts[0];
                    String date = parts[1];
                    String attName = parts[2];
                    String attTotal = parts[3];

                    // Create the itinerary object
                    Itinerary itinerary = new Itinerary(itnRef.split(":")[0], date, attName, attTotal);

                    if (itnRef.contains(":")) { // Itinerary has addons
                        //Then we will split the first value in the file
                        String[] itnAddons = itnRef.split(":")[1].split(",");
                        //And set our itnRef to the first value
                        itnRef = itnRef.split(":")[0];
                        //Then we will look through our hashmap of loaded add-ons to confirm that the add-ons are valid
                        for (String addon : itnAddons) {
                            System.out.println("Addon Ref: " + addon);
                            if (validAddons.containsKey(addon)) {
                                Addon itnAddon = validAddons.get(addon).clone();
                                itinerary.addAdn(itnAddon);
                                logger.info("Loaded Itinerary Addon: " + itnAddon.toString());
                            } else {
                                logger.warning("Invalid addon reference: " + addon);
                            }
                        }
                        logger.info("Loaded Itinerary w/ Addons: ");
                        itinerary.listItn();
                    } else { // Itinerary has no addons
                        logger.info("Loaded Itinerary w/o Addons: ");
                        itinerary.listItn();
                    }

                    // Add itinerary to the valid itineraries map
                    validItineraries.put(itnRef, itinerary);

                    // Handle activities
                    int activityCount = Integer.parseInt(parts[5]);
                    if (activityCount > 0) {
                        String[] activities = parts[6].split("/");
                        for (String activity : activities) {
                            if (activity.contains(":")) { // Activity has addons
                                String[] actParts = activity.split(":");
                                String actRef = actParts[0];
                                String[] actAddons = actParts[1].split(",");
                                if (findItnAct(actRef, itnRef) != null) {
                                    ActivityDetailed actFromFile = findItnAct(actRef, itnRef);
                                    for (String addon : actAddons) {
                                        if (validAddons.containsKey(addon)) {
                                            actFromFile.addAdn(validAddons.get(addon).clone());
                                        } else {
                                            logger.warning("Invalid activity addon reference: " + addon);
                                        }
                                    }
                                    itinerary.addAct(actFromFile);
                                    logger.info("Loaded Activity w/ Addons: " + actFromFile.toString());
                                } else {
                                    logger.warning("Invalid activity reference: " + actRef);
                                }
                            } else { // Activity has no addons
                                if (validActivities.containsKey(activity)) {
                                    ActivityDetailed actFromFile = findItnAct(activity, itnRef);
                                    itinerary.addAct(actFromFile);
                                    logger.info("Loaded Activity: " + actFromFile);
                                } else {
                                    logger.warning("Invalid activity reference: " + activity);
                                }
                            }
                        }
                    }
                    itinerary.listItn();
                } else {
                    logger.warning("Error with line format! Too many parts. Ref: " + parts[0]);
                }

            } catch (ValidationException | NumberFormatException e) {
                logger.warning("Error processing line: " + line + "\n" + e.getMessage());
            }
        }
        itnsLoaded = false;
    }

    /**
     * Procedure to load all activities attached to itineraries from file
     *
     * Goes line by line in the file, splits each element by tab then creates a
     * new ActivityDetailed object if the parts are valid.
     */
    public void loadItnActs() {
        itnactLoaded = false;
        List<String> lines = getLinesFromFile(ITNACTS);
        for (String line : lines) {
            String[] parts = splitLine(line);
            if (parts.length <= 8) {
                try {
                    ActivityDetailed actFromFile = new ActivityDetailed(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
                    logger.info("Loaded Detailed Activity:");
                    logger.info(actFromFile.toString());
                    validItnActs.put(parts[0], actFromFile);
                } catch (ValidationException e) {
                    logger.warning("Error with line! \n"
                            + "Ref: " + parts[0] + "\n"
                            + "Name: " + parts[1] + "\n"
                            + "Description: " + parts[2] + "\n"
                            + "Duration (raw): " + parts[3] + "\n"
                            + "Instructor Required (raw): " + parts[4] + "\n"
                            + "Cost (raw): " + parts[5] + "\n"
                            + "Validation Error: " + e.getMessage());

                }
            } else {
                logger.warning("Error with line! \n"
                        + "Ref: " + parts[0] + "\n"
                        + "Name: " + parts[1] + "\n"
                        + "Description: " + parts[2] + "\n"
                        + "Duration (raw): " + parts[3] + "\n"
                        + "Instructor Required (raw): " + parts[4] + "\n"
                        + "Cost (raw): " + parts[5] + "\n");
            }
        }
        itnactLoaded = true;
    }

    /**
     * Function to find activities that are linked to itineraries
     *
     * @param actRef The activity reference to look for
     * @param itnRef The itinerary reference it is attached to
     * @return ActivityDetailed object if it is found, otherwise null
     */
    public ActivityDetailed findItnAct(String actRef, String itnRef) {
        for (Map.Entry<String, ActivityDetailed> entry : validItnActs.entrySet()) {
            ActivityDetailed activity = entry.getValue();
            // Check if the activity's actRef and itnRef match the provided values
            if (activity.getRef().equals(actRef) && activity.getItnRef().equals(itnRef)) {
                logger.info("Found matching Itinerary Activity: " + activity.toString());
                return activity; // Return the matched ActivityDetailed
            }
        }

        // If no matching activity is found, return null or throw an exception as needed
        logger.warning("No matching activity found for actRef: " + actRef + " and itnRef: " + itnRef);
        return null;
    }

    /**
     * Retrieves all lines from a given file
     *
     * @param fileName The file which to get the lines from
     * @return List of type String which contains all the lines
     */
    public List<String> getLinesFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Splits a string by tab characters Ignores any tabs within quotations
     * marks
     *
     * @param line The line which needs to be split by tabs
     * @return A string array containing the line split by tabs
     */
    private String[] splitLine(String line) {
        List<String> lineInParts = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentPart = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            // Toggle the insideQuotes flag when encountering double quotes
            if (currentChar == '"') {
                insideQuotes = !insideQuotes;
            } else if (currentChar == '\t' && !insideQuotes) {
                lineInParts.add(currentPart.toString().trim());
                currentPart.setLength(0);
            } else {
                currentPart.append(currentChar);
            }
        }

        if (currentPart.length() > 0) {
            lineInParts.add(currentPart.toString().trim());
        }

        return lineInParts.toArray(new String[0]);
    }

    //Writing Files
    /**
     * Procedure to write add-on(s) stored in the hash-map to file
     *
     */
    public void writeAddons() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ADDONS)))) {
            for (Addon addon : validAddons.values()) {
                String line = String.join("\t", addon.getRef(), addon.getName(), String.valueOf(addon.getCost()));
                bw.write(line);
                bw.newLine();
            }
            logger.info("Addons written to file successfully.");
        } catch (IOException e) {
            logger.warning("Failed to write addons to file: " + e.getMessage());
        }
    }

    /**
     * Procedure to write activities stored in the hash-map to file
     *
     */
    public void writeActivites() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ACTIVITES)))) {
            for (Activity activity : validActivities.values()) {
                String line = String.join("\t",
                        activity.getRef(),
                        activity.getName(),
                        activity.getDesc(),
                        String.valueOf(activity.insReq()),
                        String.valueOf(activity.getDuration()),
                        String.valueOf(activity.getCost()));
                bw.write(line);
                bw.newLine();
            }
            logger.info("Activities written to file successfully.");
            actsWritten = true;
        } catch (IOException e) {
            logger.warning("Failed to write activities to file: " + e.getMessage());
        }

    }

    /**
     * Procedure to write itineraries stored in the hash-map to file Also calls
     * writeItnActs() to write activities attached to the itinerary
     *
     */
    public void writeItineraries() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ITINERARIES))) {
            for (Itinerary itinerary : validItineraries.values()) {
                StringBuilder line = new StringBuilder();

                // Add itinerary reference
                line.append(itinerary.getRef());

                // Add itinerary add-ons (if any)
                if (!itinerary.getAddons().isEmpty()) {
                    line.append(":");
                    List<String> addonRefs = new ArrayList<>();
                    for (Addon addon : itinerary.getAddons()) {
                        addonRefs.add(addon.getRef());
                    }
                    line.append(String.join(",", addonRefs));
                }

                // Add other itinerary details
                line.append("\t").append(itinerary.getDate());
                line.append("\t").append(itinerary.getAName());
                line.append("\t").append(itinerary.getTotAtts());
                line.append("\t").append(itinerary.getTotalCost());

                // Add activities
                System.out.println("Number of activities: " + itinerary.getActs().size());
                if (itinerary.getActs().size() > 0) {
                    line.append("\t").append(itinerary.getActs().size());
                    line.append("\t");
                    List<String> activityDetails = new ArrayList<>();
                    for (ActivityDetailed activity : itinerary.getActs()) {
                        StringBuilder activityDetail = new StringBuilder();
                        activityDetail.append(activity.getRef());
                        // Add activity add-ons (if any)
                        if (activity.getActAddons().size() > 0) {
                            activityDetail.append(":");
                            List<String> activityAddonRefs = new ArrayList<>();
                            for (Addon addon : activity.getActAddons()) {
                                activityAddonRefs.add(addon.getRef());
                            }
                            activityDetail.append(String.join(",", activityAddonRefs));
                        }
                        activityDetails.add(activityDetail.toString());
                        writeItnActs(itinerary);
                    }
                    line.append(String.join("/", activityDetails));
                }
                logger.info("Itineraries written to file successfully.");

                // Write the line to the file
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Error saving itineraries to file: " + e.getMessage());
        }
    }

    /**
     * Procedure to write all activities that are attached to an itinerary to a
     * file
     *
     * @param itinerary The itinerary which to retrieve and write the activities
     * from
     */
    private void writeItnActs(Itinerary itinerary) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ITNACTS)))) {
            for (ActivityDetailed activity : itinerary.getActs()) {
                String line = String.join("\t",
                        activity.getRef(),
                        activity.getName(),
                        activity.getDesc(),
                        String.valueOf(activity.insReq()),
                        String.valueOf(activity.getDuration()),
                        String.valueOf(activity.getCost()),
                        String.valueOf(activity.getTime()),
                        String.valueOf(activity.getItnRef()));
                bw.write(line);
                bw.newLine();
            }
            logger.info("Activities written to file successfully.");
            actsWritten = true;
        } catch (IOException e) {
            logger.warning("Failed to write activities to file: " + e.getMessage());
        }

    }

    //Creating objects
    /**
     * Procedure to create add-on(s) from user inputs Once created it then saves
     * it in the validAddons hash-map
     */
    public void createAddons() {
        Scanner usrInp = new Scanner(System.in);
        try {
            System.out.println("---Create Add-on---");
            System.out.println("Reference codes for Add-ons must be 3 capital letters long!");
            System.out.println("(e.g INS, TRN, LNC)");
            System.out.println("Reference Code: ");
            String ref = usrInp.nextLine();

            System.out.println("Name of add-on can be anything");
            System.out.println("(e.g Lunch, Dinner, Pictures)");
            System.out.println("Name: ");
            String name = usrInp.nextLine();

            System.out.println("Cost must be a whole number in pence");
            System.out.println("(e.g 100, 120, 135)");
            System.out.println("Cost: ");
            String cost = usrInp.nextLine();

            System.out.println("New Add-on will be created:");
            System.out.println("Ref: " + ref);
            System.out.println("Name: " + name);
            System.out.println("Cost: " + cost);

            Addon createdAddon = new Addon(ref, name, cost);

            validAddons.put(ref, createdAddon);
        } catch (ValidationException e) {
            System.out.println("Error in addon creation proccess!");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Procedure to create activities from user inputs Once created it then
     * saves it in the validActivities hash-map
     */
    public void createActivity() {
        Scanner usrInp = new Scanner(System.in);
        try {
            System.out.println("---Create Activity---");
            System.out.println("Reference codes for Activity must be in the format AAA-00");
            System.out.println("(e.g SWM-01, LEP-02, ARM-03)");
            System.out.println("Reference Code: ");
            String ref = usrInp.nextLine();

            System.out.println("Name of activity can be anything");
            System.out.println("(e.g Swimming, Leaping, Visiting the Shops)");
            System.out.println("Name: ");
            String name = usrInp.nextLine();

            System.out.println("Please write a short description of the Activity");
            System.out.println("(e.g \" Spend a lovely time swimming with the seals!\")");
            System.out.println("Description: ");
            String desc = usrInp.nextLine();

            System.out.println("Does this activity require insurance?");
            System.out.println("(e.g true/false)");
            System.out.println("Insurance: ");
            String insReq = usrInp.nextLine();

            System.out.println("How long will this activity take in minutes?");
            System.out.println("(e.g 100, 200, 345)");
            System.out.println("Duration: ");
            String duration = usrInp.nextLine();

            System.out.println("Cost must be a whole number in pence");
            System.out.println("(e.g 100, 120, 135");
            System.out.println("Cost: ");
            String cost = usrInp.nextLine();

            System.out.println("New Activity will be created:");
            System.out.println("Ref: " + ref);
            System.out.println("Name: " + name);
            System.out.println("Description: " + desc);
            System.out.println("Insurance: " + insReq);
            System.out.println("Duration: " + duration);
            System.out.println("Cost: " + cost);

            Activity createdAct = new Activity(ref, name, desc, insReq, duration, cost);

            validActivities.put(ref, createdAct);
        } catch (ValidationException e) {
            System.out.println("Error in activity creation proccess!");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Procedure to create itinerary from user inputs 
     * Once created it then saves it in the validItineraries hash-map
     */
    public void createItinerary() {
        Scanner usrInp = new Scanner(System.in);
        try {
            System.out.println("---Create Itinerary---");
            System.out.println("Reference Code for itinerary must be in the format A0000A");
            System.out.println("The last character may only be A-D");
            String ref = usrInp.nextLine();

            System.out.println("When will this Itinerary start?");
            System.out.println("(e.g 10/02/2004, DD/MM/YYYY)");
            String date = usrInp.nextLine();

            System.out.println("Who is the Main Attendee?");
            System.out.println("(e.g A Singh)");
            System.out.println("Please input their intial, then a space followed by their surname");
            System.out.println("Non-Alphabetical characters will not be accepted");
            String attName = usrInp.nextLine();

            System.out.println("How many people will be attending total");
            System.out.println("Including the Main Attendee!");
            System.out.println("(e.g 1, 2, 100)");
            String totalAtts = usrInp.nextLine();

            Itinerary createdItinerary = new Itinerary(ref, date, attName, totalAtts);

            System.out.println("---Itinerary Addons---");
            System.out.println("Please enter the reference code for an add-on");
            System.out.println("you want to add to the itinerary");
            System.out.println("(e.g TRN)");
            System.out.println("Type Add-on Ref or \"exit\" to stop");

            while (true) {
                System.out.println("Add-on Ref: ");
                String addRef = usrInp.nextLine();

                if (addRef.toLowerCase().equals("exit")) {
                    break;
                }

                if (validAddons.get(addRef) != null) {
                    createdItinerary.addAdn(validAddons.get(addRef).clone());
                    System.out.println("Addded addon: " + addRef);
                } else {
                    System.out.println("Couldn't find add-on with reference " + addRef);
                }
                System.out.println("Type Add-on Ref or \"exit\" to stop");
            }

            System.out.println("---Itinerary Activity---");
            System.out.println("Please enter the reference code for an activity");
            System.out.println("you want to add to the itinerary");
            System.out.println("(e.g SWM-01)");
            System.out.println("Type Activity Ref or \"exit\" to stop");

            while (true) {
                System.out.println("Activity Ref: ");
                String actRef = usrInp.nextLine();

                if (actRef.toLowerCase().equals("exit")) {
                    break;
                }

                if (validActivities.get(actRef) != null) {
                    System.out.println("What time will this Activity start?");
                    System.out.println("(e.g 18:00, 21:00)");
                    System.out.println("Please use 24 hour clock");
                    System.out.println("Time: ");
                    String time = usrInp.nextLine();
                    ActivityDetailed createdActivity = validActivities.get(actRef).clone(time, ref);
                    createdItinerary.addAct(createdActivity);
                    System.out.println("Addded activity: " + actRef);

                    System.out.println("---Activity Addons---");
                    System.out.println("Please enter the reference code for an add-on");
                    System.out.println("you want to add to the activity");
                    System.out.println("(e.g INS)");
                    System.out.println("Type Add-on Ref or \"exit\" to stop");
                    while (true) {
                        System.out.println("Add-on Ref: ");
                        String addRef = usrInp.nextLine();

                        if (addRef.toLowerCase().equals("exit")) {
                            break;
                        }

                        if (validAddons.get(addRef) != null) {
                            createdActivity.addAdn(validAddons.get(addRef).clone());
                            System.out.println("Addded addon: " + addRef);
                        } else {
                            System.out.println("Couldn't find add-on with reference " + addRef);
                        }
                        System.out.println("Type Add-on Ref or \"exit\" to stop");
                    }
                } else {
                    System.out.println("Couldn't find activity with reference " + actRef);
                }
                System.out.println("Type Activity-on Ref or \"exit\" to stop");
            }
            System.out.println("Completed Itinerary: " + ref);

            validItineraries.put(ref, createdItinerary);

            createdItinerary.listItn();
        } catch (ValidationException e) {
            System.out.println("Error in itinerary creation proccess!");
            System.out.println(e.getMessage());
        }
    }

    //Finding Objects
    
    /**
     * Function to find itinerary objects by reference code
     * 
     * @param ref The reference code of the itinerary to be searched for
     * @return Itinerary object if it is found otherwise null
     */
    public Itinerary findItn(String ref) {
        if (validItineraries.containsKey(ref)) {
            return validItineraries.get(ref);
        } else {
            return null;
        }
    }
}
