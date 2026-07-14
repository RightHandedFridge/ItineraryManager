package obf.phase_3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author obf
 */
public class Itinerary extends BaseClass {

    protected String itDate;
    private String attName;
    private int totalAtts;
    private List<ActivityDetailed> actCodes = new ArrayList<>();
    private List<Addon> adnCodes = new ArrayList<>();
    private float discount = 0f;

    /**
     * Constructor for Itinerary class
     *
     * @param ref Reference code in the format A0000A
     * @param itDate Date in the format dd/mm/yyyy
     * @param attName Main Attendee Name in the format A Aaaa...
     * @param totalAtts Total number of attendees (e.g 1,2,100)
     */
    public Itinerary(String ref, String itDate, String attName, String totalAtts) throws ValidationException {
        this.ref = ValidateRef(ref);
        this.itDate = ValidateDate(itDate); //Date of Occuernce
        this.attName = attName; //Main Atendee Name
        this.totalAtts = ValidateInteger(totalAtts); //Total Attendees
    }

    /**
     * Validates Main Attendee name is in the correct format. The format being
     * one capital letter, followed by a space, followed by another capital
     * letter then any amount of lowercase letters
     *
     * @param inpName The name to be validated
     * @return The name if it is valid
     * @throws IllegalArgumentException if name is invalid
     */
    private String ValidateAttName(String inpName) {
        String refCodeFormat = "^[A-Z] [A-Z][A-Za-z]*$";
        if (inpName.matches(refCodeFormat) && (inpName.length() - 1) <= 20) {
            return inpName;
        } else {
            throw new IllegalArgumentException("Invalid Atendee Name! " + inpName);
        }
    }

    /**
     * Setter function to add activities to an itinerary
     *
     *
     * @param inpAct The Activity to add
     */
    public void addAct(ActivityDetailed inpAct) {
        actCodes.add(inpAct);
    }

    /**
     * Setter function to add an add-on to an itinerary
     *
     *
     * @param inpAdn The add-on to add
     */
    public void addAdn(Addon inpAdn) {
        adnCodes.add(inpAdn);
    }

    /**
     * Get function to retrieve add-on list for the itinerary
     *
     *
     * @return List of type Addon
     */
    public List<Addon> getAddons() {
        return adnCodes;
    }

    /**
     * Get function to retrieve activities list for the itinerary
     *
     * @return List of type Activity Detailed
     */
    public List<ActivityDetailed> getActs() {
        return actCodes;
    }

    /**
     * Get function for itinerary date
     *
     * @return Date as a string
     */
    public String getDate() {
        return itDate;
    }

    /**
     * Get function for the Main Attendee's name
     *
     * @return Name as a string
     */
    public String getAName() {
        return attName;
    }

    /**
     * Get function for the total amount of attendees
     *
     * @return
     */
    public int getTotAtts() {
        return totalAtts;
    }

    /**
     * Set function for the date of the itinerary
     *
     * @param inpValue The date to set the itinerary to
     * @deprecated
     */
    @Deprecated
    public void setDate(String inpValue
    ) {
        try {
            this.itDate = ValidateDate(inpValue);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Set function for the reference code of the itinerary
     *
     * @param inpValue The reference code to give the itinerary
     * @deprecated
     */
    @Deprecated
    public void setRef(String inpValue) {
        try {
            this.ref = ValidateRef(ref);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Function to calculate the total cost of the itinerary including discounts
     *
     * @return Total cost as a float
     */
    public float getTotalCost() {
        float totalCost = 0;
        for (Activity actCost : actCodes) {
            totalCost += actCost.getCost();
        }

        // Add cost for add-ons at the itinerary level if needed
        for (Addon adnCost : adnCodes) {
            totalCost += adnCost.getCost();
        }

        float totalActs = actCodes.size();

        if (totalActs >= 1 && totalActs <= 2) {
            if (totalAtts < 10) {
                discount = 0f;
            } else if (totalAtts >= 10 && totalAtts <= 20) {
                discount = 0.05f;
            } else if (totalAtts > 20) {
                discount = 0.08f;
            }
        }

        if (totalActs >= 3 && totalActs <= 5) {
            if (totalAtts < 10) {
                discount = 0.05f;
            } else if (totalAtts >= 10 && totalAtts <= 20) {
                discount = 0.08f;
            } else if (totalAtts > 20) {
                discount = 0.12f;
            }
        }

        if (totalActs <= 6) {
            if (totalAtts < 10) {
                discount = 0.10f;
            } else if (totalAtts >= 10 && totalAtts <= 20) {
                discount = 0.12f;
            } else if (totalAtts > 20) {
                discount = 0.14f;
            }
        }

        if (discount != 0) {
            return totalCost - (totalCost * discount);
        } else {
            return totalCost;
        }
    }

    /**
     * Convert price of activity to string
     *
     * @return Price of activity as a String
     */
    public float getTCPounds() {
        float tc = getTotalCost();
        float tcPounds = tc / 100f;
        return tcPounds;
    }

    /**
     * Convert the current amount of activities stored into a string if it is
     * less than or equal to 5
     * 
     * @return String containing the amount of activities
     */
    private String getActsAsString() {
        switch (actCodes.size()) {
            case 1:
                return "One";
            case 2:
                return "Two";
            case 3:
                return "Three";
            case 4:
                return "Four";
            case 5:
                return "Five";
            default:
                return String.valueOf(actCodes.size());
        }
    }

    /**
     * Lists all itinerary details
     * Includes prices of add-ons and activities 
     * Includes sub-totals of each item and a grand total too
     * In compliance of Phase 3 of the requirements
     */
    public void listItn() {
        System.out.println("----Itinerary Summary----");
        System.out.println("    - Reference: " + ref);
        System.out.println("    - Client: " + attName);
        System.out.println("    - Date: " + itDate);
        System.out.println("    - Activites: " + getActsAsString() + "  Attendees: " + totalAtts);
        System.out.println("    - Cost: £" + getTCPounds());
        System.out.println("---Cost Breakdown---");
        if (adnCodes.size() > 0) {
            System.out.println("Itinerary Addons");
            float totalAdnCost = 0;
            for (Addon adnOut : adnCodes) {
                if (adnOut != null) {
                    float adnCost = adnOut.getCostPounds() * totalAtts;
                    adnCost = Math.round(adnCost * 100f) / 100f;
                    totalAdnCost = totalAdnCost + adnCost;
                    System.out.println("- " + adnOut.getName() + " @ £" + adnOut.getCostPounds() + " x " + totalAtts + " = " + adnCost);
                }
            }
            totalAdnCost = Math.round(totalAdnCost * 100) / 100;
            System.out.println("    - Sub-Total: £" + totalAdnCost);
        }

        if (actCodes.size() > 0) {
            int actCount = 1;
            System.out.println("Activites");
            for (ActivityDetailed actOut : actCodes) {
                float totalActCost = 0;
                float actCost = actOut.getCostPounds() * totalAtts;
                actCost = Math.round(actCost * 100f) / 100f;
                totalActCost = totalActCost + actCost;
                System.out.println(actCount + ". " + actOut.getName() + " @ £" + actOut.getCostPounds() + " x " + totalAtts + " = £" + actCost);
                if (actOut.getActAddons().size() > 0) {
                    for (Addon actAdn : actOut.getActAddons()) {
                        float actAdnCost = actAdn.getCostPounds() * totalAtts;
                        actAdnCost = Math.round(actAdnCost * 100f) / 100f;
                        totalActCost = totalActCost + actAdnCost;
                        System.out.println(" Add-on: " + actAdn.getName() + " @ £" + actAdn.getCostPounds() + " x " + totalAtts + " = £" + actAdnCost);
                    }
                }
                System.out.println("    - Sub-Total: £" + totalActCost);
            }
            System.out.println(Math.round(discount * 100f) + "% discount");
            System.out.println("Total Cost: £" + getTCPounds());
        }
    }

    @Override
    protected String ValidateRef(String inpRef) throws ValidationException {
        String refCodeFormat = "^[A-Z]\\d{4}[ABCD]$";
        if (inpRef.matches(refCodeFormat)) {
            return inpRef;
        } else {
            throw new ValidationException("Invalid Ref Code Format!");
        }
    }
}
