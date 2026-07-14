package obf.phase_3;
/**
 *
 * @author obf
 */
public class Addon extends BaseClass implements Cloneable {

    private float cost; //pence
    private String name;

    /**
     * Constructor for Add-on class
     *
     *
     * @param ref Reference code in the format "AAA"
     * @param cost Price of add-on (pence)
     * @param name Name of add-on
     */
    public Addon(String ref, String name, String cost) throws ValidationException {
        this.ref = ValidateRef(ref);
        this.name = name;
        this.cost = ValidateFloat(cost);
    }

    /**
     * Total cost of add-on
     *
     *
     * @return Price of add-on as a float
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
     * Function to get the name of the add-on
     *
     * @return Name of the add-on as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Outputs all details of the add-on
     *
     * @return String containing all activity details
     */
    @Override
    public String toString() {
        return "Addon {"
                + "Reference='" + ref + '\''
                + ", Name='" + name + '\''
                + ", Cost=" + cost + " pence"
                + '}';
    }

    /**
     * Clones add-on object
     *
     * @return New add-on object with the same data as the original
     */
    public Addon clone() {
        try {
            return new Addon(this.ref, this.name, Float.toString(this.cost));
        } catch (ValidationException e) {
            System.out.println("Error in Cloning Proccess");
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Validates a single reference code for an add-on
     *
     *
     * @param inpRef The reference code to validate
     * @return The inputted reference code if it was valid
     * @throws ValidationException if reference parameter is invalid
     */
    @Override
    protected String ValidateRef(String inpRef) throws ValidationException {
        String refCodeFormat = "^[A-Za-z]{3}$";
        if (inpRef.matches(refCodeFormat)) {
            return inpRef;
        } else {
            throw new ValidationException("Invalid Ref Code! " + inpRef);
        }
    }
}
