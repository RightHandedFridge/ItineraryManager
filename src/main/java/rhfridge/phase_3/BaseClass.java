package obf.phase_3;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author obf
 */
public class BaseClass {
    //Contains methods that are required for all classes (e.g input validators)
    protected String ref;
    protected boolean valid;
    
    public String getRef(){
        return this.ref;
    }
    
    /**
     * Validates whether or not the Reference Code provided matches the format
     * "AAA-00"
     * 
     *
     * @param inpRef The reference code to check for validity
     * @return The string inputted in the parameters
     * @throws ValidationException if the reference code is invalid
     */
    protected String ValidateRef(String inpRef) throws ValidationException {
        String refCodeFormat = "^[A-Z]{3}-\\d{2}$";
        
        if(inpRef.matches(refCodeFormat)){
            return inpRef;
        }
        else{
            throw new ValidationException("Invalid Ref Code Format: " + inpRef + ". Reference Codes need to be in the format AAA-00");
        }
    }
    
    /**
     * Validates whether or not a given String is in the format "DD/MM/YYYY"
     * 
     * @param inpValue The string to check for formatting
     * @return The string inputted in the parameters
     * @throws ValidationException if the string isn't formatted in "DD/MM/YYYY"
     */
    public String ValidateDate(String inpValue) throws ValidationException {
        try{
            DateTimeFormatter inpFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate inpDate = LocalDate.parse(inpValue, inpFormat);
            DateTimeFormatter outFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return inpDate.format(outFormat);
        }
        catch(Exception e){
            throw new ValidationException("Invalid date format: " + inpValue + ". Please use 'DD/MM/YYYY'");
        }
    }
    
    
    /**
     * Validates whether or not the inputted String is in the format "HH:mm"
     * 
     * @param inpValue The string to check for formatting
     * @return The string given in the parameters
     * @throws ValidationException If the string isn't formatted to "HH:mm"
     */
    public String ValidateTime(String inpValue) throws ValidationException{
        try{
            DateTimeFormatter inpFormat = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime inpTime = LocalTime.parse(inpValue, inpFormat);
            DateTimeFormatter outFormat = DateTimeFormatter.ofPattern("HH:mm");
            return inpTime.format(outFormat);
        }
        catch(Exception e){
            throw new ValidationException("Invalid time format: " + inpValue + ". Please use HH:mm");
        }
    }
    
    /**
     * Converts a given String to an integer and validates it to ensure it's
     * not less than 0.
     * 
     * @param inpInt The string to be converted
     * @return The string given in the parameters as an integer
     * @throws ValidationException if the provided string cannot be converted to an integer or is less than zero
     */
    
    public int ValidateInteger(String inpInt) throws ValidationException{
        try{
            int finalInt = Integer.parseInt(inpInt);
            
            if (finalInt < 0){
                throw new ValidationException("Integer evaluated to less than zero: " + inpInt);
            }
            
            return finalInt;
        }
        catch (NumberFormatException e){
            throw new ValidationException("Invalid Input for Duration: " + inpInt);
        }
    }
    
    /**
     * Converts a given String to a float and ensures it's not less than zero
     * 
     * @param inpFloat The string to be converted
     * @return The string given in the parameters as a float
     * @throws ValidationException if the provided string cannot be converted to an float or is less than zero
     */
    public float ValidateFloat(String inpFloat) throws ValidationException{
        try {
            float finalFloat = Float.parseFloat(inpFloat);

            if (finalFloat < 0) {
                throw new ValidationException("Float evaluated to less than zero: " + inpFloat);
            }
            return finalFloat;
            } 
        catch (NumberFormatException e) {
            throw new ValidationException("Invalid input for float value: " + inpFloat);
        }
    }
    
    /**
     * Converts given string to a bool
     * 
     * @param inpBool The string to be converted
     * @return The string provided as a Boolean
     * @throws ValidationException if the string is not "true" or "false" or if it cannot be converted to a boolean
     */
    public boolean validateBool(String inpBool) throws ValidationException{
        try{
            inpBool = inpBool.toLowerCase();
            if(!inpBool.equals("true") && !inpBool.equals("false")){
                throw new ValidationException("Invalid input for boolean value: " + inpBool);
            }
            else{
                boolean finalBool = Boolean.parseBoolean(inpBool);
                return finalBool;
            }
        }
        catch (ValidationException e){
            throw new ValidationException(e.getMessage());
        }
    }
}
