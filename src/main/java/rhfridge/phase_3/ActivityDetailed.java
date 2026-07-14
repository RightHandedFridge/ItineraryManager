package obf.phase_3;

/**
 *
 * @author obf
 */
public class ActivityDetailed extends Activity{
    
    private String itnRef;
    private String time;
    
    public ActivityDetailed(String ref, String name, String desc, String insReq, String duration, String baseCost, String time, String itnRef) throws ValidationException {
        super(ref, name, desc, insReq, duration, baseCost);
        this.itnRef = itnRef;
        this.time = ValidateTime(time);
    }
    
    /**
     * Get function to return time of Activity for Itinerary
     * 
     * @return Time of activity as a string
     */
    public String getTime(){
        return time;
    }
    
    
    /**
     * Get function to return itinerary reference that this activity for
     * 
     * @return Itinerary reference as a string
     */
    public String getItnRef(){
        return itnRef;
    }
    
    /**
     * Override function that displays all details of activity including 
     * itinerary reference and time
     * 
     * @return String containing all details of activity
     */
    @Override
    public String toString() {
        return "Activity {"
                + "Reference='" + ref + '\''
                + "Itn Reference='" + itnRef + '\''
                + ", Time='" + time + '\''
                + ", Name='" + name + '\''
                + ", Description='" + desc + '\''
                + ", Duration=" + duration + " mins"
                + ", Insurance Required=" + insReq
                + ", Base Cost=" + cost + " pence"
                + ", Add-ons=" + adnCodes
                + '}';
    }
    
}
