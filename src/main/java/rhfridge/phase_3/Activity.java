package obf.phase_3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author obf
 */
public class Activity extends BaseClass implements Cloneable {

    protected String acTime;
    protected String name;
    protected String desc;
    protected int duration;
    protected boolean insReq;
    protected float cost;
    protected List<Addon> adnCodes = new ArrayList<>();

    /**
     * Constructor for Activity Class
     *
     * @param ref Reference code in the format AAA-00
     * @param name Name of activity (e.g Dancing)
     * @param desc Description of Activity (e.g Dancing with professional
     * trainers)
     * @param insReq Whether or not insurance is required to have this activity
     * @param duration How long the activity will take (Minutes)
     * @param cost How much the activity costs (Pennies)
     * @throws ValidationException if any inputs are invalid
     */
    public Activity(String ref, String name, String desc, String insReq, String duration, String baseCost) throws ValidationException {
        this.ref = ValidateRef(ref);
        this.name = name;
        this.desc = desc;
        this.duration = ValidateInteger(duration);
        this.insReq = validateBool(insReq);
        this.cost = ValidateFloat(baseCost);
    }

    /**
     * Setter function for time value in activity
     *
     * @param inpTime String of value to set time to (24-hour clock)
     * @throws ValidationException If inputted time is invalid
     * @deprecated
     */
    @Deprecated
    public void setTime(String inpTime) throws ValidationException {
        this.acTime = ValidateTime(inpTime);
    }

    /**
     * Get function for name of activity
     *
     *
     * @return The name of the activity as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Get function for description of activity
     *
     * @return The description of the activity as a string
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Get function for the duration of an activity
     *
     * @return The duration of an activity as an integer
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Get function for whether or not insurance is required
     *
     * @return Boolean value which is true if insurance is required
     */
    public boolean insReq() {
        return insReq;
    }

    /**
     * Get function for the cost of the activity
     *
     * @return Price of the activity as a float
     */
    public float getCost() {
        return cost;
    }

    /**
     * Convert price of activity to string
     * 
     * @return Price of activity as a String
     */
    public float getCostPounds() {
        float cost = getCost();
        float costPounds = cost / 100f;
        return costPounds;
    }

    /**
     * Set function for adding an add-on to an activity
     *
     *
     * @param inpAdn The add-on to add to the activity
     */
    public void addAdn(Addon inpAdn) {
        if (inpAdn != null && !adnCodes.contains(inpAdn)) {
            adnCodes.add(inpAdn);
        }
    }

    /**
     * Get function for retrieving the list that stores activity add-on
     *
     * @return List of type Addon
     */
    public List<Addon> getActAddons() {
        return adnCodes;
    }

    /**
     * Override Function that displays all details of the activity
     *
     * @return String containing all details of the activity
     */
    @Override
    public String toString() {
        return "Activity {"
                + "Reference='" + ref + '\''
                + ", Time='" + acTime + '\''
                + ", Name='" + name + '\''
                + ", Description='" + desc + '\''
                + ", Duration=" + duration + " mins"
                + ", Insurance Required=" + insReq
                + ", Base Cost=" + cost + " pence"
                + ", Add-ons=" + adnCodes
                + '}';
    }

    /**
     * Cloning function that creates a new object of type ActivityDetailed
     *
     *
     * @param time The time which the activity occurs
     * @param itnRef The reference code for the itinerary that
     * @return New object of type ActivityDetailed or null if an error occurs
     */
    public ActivityDetailed clone(String time, String itnRef) {
        try {
            return new ActivityDetailed(this.ref, this.name, this.desc, Boolean.toString(this.insReq), Integer.toString(this.duration), Float.toString(this.cost), time, itnRef);
        } catch (ValidationException e) {
            System.out.println("Error in Cloning Proccess");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
