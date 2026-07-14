package obf.phase_3;

/**
 * Exception class to be used to standardise all the errors that
 * could be outputted by the Base Class's methods
 * 
 * 
 * @author obf
 */
public class ValidationException extends Exception{
    public ValidationException(String message){
        super(message);
    }
}
