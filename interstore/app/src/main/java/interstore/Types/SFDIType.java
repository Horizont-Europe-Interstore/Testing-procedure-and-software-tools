
package interstore.Types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public class SFDIType extends UInt40 {

    public void setSfdiType(long value) {
        super.setUint40(value);
    }

    public long getSfdiType() {
        return super.getUint40();
    }

    @Override
    public String toString() {
        String sfdi = Long.toString(getSfdiType());
        if (isValidSFDI(sfdi)) {
            return sfdi;
        } else {
            throw new IllegalArgumentException("Invalid SFDI: " + sfdi);
        }
    }

    private boolean isValidSFDI(String sfdi) {
        int sum = 0;
        for (char digit : sfdi.toCharArray()) {
            if (Character.isDigit(digit)) {
                sum += Character.getNumericValue(digit);
            }
        }
        return sum % 10 == 0;
    }

    @Converter(autoApply = true)
    public static class SFDITypeConverter implements AttributeConverter<SFDIType, Long> {

        @Override
        public Long convertToDatabaseColumn(SFDIType attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getSfdiType();
        }

        @Override
        public SFDIType convertToEntityAttribute(Long dbData) {
            if (dbData == null) {
                return null;
            }
            SFDIType sfdiType = new SFDIType();
            sfdiType.setSfdiType(dbData);
            return sfdiType;
        }
    }
}




/* 
package interstore.Types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public class SFDIType extends UInt40{
    
    public void setSfdiType(long value) {
        super.setUint40(value);
        
    }

    public long getSfdiType() {
        return super.getUint40();
    } 

    @Override
    public String toString() {
        String sfdi = super.toString();
        if (isValidSFDI(sfdi)) {
            return sfdi;
        } else {
            throw new IllegalArgumentException("Invalid SFDI: " + sfdi);
        }
    }

    private boolean isValidSFDI(String sfdi) {
        // Calculate the sum of the digits, including the checksum digit
        int sum = 0;
        for (char digit : sfdi.toCharArray()) {
            if (Character.isDigit(digit)) {
                sum += Character.getNumericValue(digit);
            }
        }

        // Check if the sum modulo 10 is zero
        return sum % 10 == 0;
    }

     @Converter(autoApply = true)
public class SFDITypeConverter implements AttributeConverter<SFDIType, Long> {

    @Override
    public Long convertToDatabaseColumn(SFDIType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getSfdiType();
    }

    @Override
    public SFDIType convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        SFDIType sfdiType = new SFDIType();
        sfdiType.setSfdiType(dbData);
        return sfdiType;
    }
}




}



*/



/*
 * public SFDIType(long value) {
        super(value);
    }
    
 * 
 */