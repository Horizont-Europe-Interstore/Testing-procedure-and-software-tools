package interstore.Types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
public class HexBinary160 {
    private String hexValue;

    public void setHexbinary160(String hexValue) {
        if (!isValidHexBinary160(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary160 value: " + hexValue);
        }
        this.hexValue = hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }

    public String getHexValue() {
        return this.hexValue;
    }

    private boolean isValidHexBinary160(String hexValue) {
        try {
            new java.math.BigInteger(hexValue, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        return hexValue.length() <= 40;
    }

    @Converter(autoApply = true)
    public static class HexBinary160Converter implements AttributeConverter<HexBinary160, String> {

        @Override
        public String convertToDatabaseColumn(HexBinary160 attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getHexValue();
        }

        @Override
        public HexBinary160 convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            HexBinary160 hexBinary160 = new HexBinary160();
            hexBinary160.setHexbinary160(dbData);
            return hexBinary160;
        }
    }
}







/* 


public class HexBinary160 {
    private  String hexValue;

   
       
        // Return the hex string (with leading "0" if necessary)
    
    public void setHexbinary160(String hexValue) {
        // Ensure that the hex string is valid
        if (!isValidHexBinary160(hexValue)) {
            throw new IllegalArgumentException("Invalid HexBinary160 value: " + hexValue);
        }

        // Store the hex string (with leading "0" if necessary)
        this.hexValue = hexValue.length() % 2 == 0 ? hexValue : "0" + hexValue;
    }    
   
    

    public String getHexValue() {
        return this.hexValue;
    }

    // Check if the given hex string is a valid HexBinary160 value
    private boolean isValidHexBinary160(String hexValue) {
        // Check if it is a valid hex string and has at most 40 characters
        try {
            Integer.parseInt(hexValue,16);
        }
        catch (NumberFormatException e){
            return false;
        }
        return hexValue.length() <= 40;
    }
   
@Converter(autoApply = true)
public class HexBinary160Converter implements AttributeConverter<HexBinary160, String> {

    @Override
    public String convertToDatabaseColumn(HexBinary160 attribute) {
        // Convert HexBinary160 to a String for database storage
        if (attribute == null) {
            return null;
        }
        return attribute.getHexValue();
    }

    @Override
    public HexBinary160 convertToEntityAttribute(String dbData) {
        // Convert a database column value back to HexBinary160
        if (dbData == null) {
            return null;
        }
        HexBinary160 hexBinary160 = new HexBinary160();
        hexBinary160.setHexbinary160(dbData);
        return hexBinary160;
    }
}


}


*/ 



















/*
 *  
 * 
 */