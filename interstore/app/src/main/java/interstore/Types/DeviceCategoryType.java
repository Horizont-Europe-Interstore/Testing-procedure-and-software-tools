package interstore.Types;

import interstore.AbstractDevice;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
public class DeviceCategoryType extends HexBinary32 implements AbstractDevice {
    
    public void setDeviceCategory(String hexValue)
    {
       // write super to acces the class HexBinary32
        validateDeviceCategory(hexValue);
        super.setHexValue32value(hexValue);
      

    }
    public String getDeviceCategory()
    {
        return super.getHexValue32value();
    } 
    public boolean validateDeviceCategory(String hexValue) {
        try {
            // Parse as decimal integer (the value is already in decimal format)
            int decimalValue = Integer.parseInt(hexValue);
            
            // Check if it's a valid power of 2 (single bit set) or 0
            // Valid values are 2^0 to 2^24 (bits 0-24)
            if (decimalValue == 0) {
                return true; // 0 is valid (no category)
            }
            
            // Check if it's a power of 2 and within valid range (2^0 to 2^24)
            // 2^24 = 16777216, so max valid single bit value
            if (decimalValue > 0 && decimalValue <= 16777216) {
                // Check if it's a power of 2 (only one bit set)
                boolean isPowerOfTwo = (decimalValue & (decimalValue - 1)) == 0;
                if (isPowerOfTwo) {
                    return true;
                }
                // Also allow combinations of multiple bits
                if (decimalValue <= 33554431) { // 2^25 - 1 (all bits 0-24 can be set)
                    return true;
                }
            }
            
            throw new IllegalArgumentException("Invalid DeviceCategoryType value: " + hexValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid DeviceCategoryType value: " + hexValue);
        }
    }

    @Converter(autoApply = true)
    public static class DeviceCategoryTypeConverter implements AttributeConverter<DeviceCategoryType, String> {
        
        @Override
        public String convertToDatabaseColumn(DeviceCategoryType attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getHexValue32value();
        }

        @Override
        public DeviceCategoryType convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            DeviceCategoryType deviceCategoryType = new DeviceCategoryType();
            deviceCategoryType.setDeviceCategory(dbData);
            return deviceCategoryType;
        }
    }

    @Override
    public void setsfdi(long sfid) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setsfdi'");
    }
    @Override
    public Long getsfdi() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getsfdi'");
    }
    @Override
    public void sethexBinary160(String hexBinary160) {
       
        throw new UnsupportedOperationException("Unimplemented method 'sethexBinary160'");
    }
    @Override
    public String gethexBinary160() {
       
        throw new UnsupportedOperationException("Unimplemented method 'gethexBinary160'");
    }
    @Override
    public String getConfigurationLink() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getConfigurationLink'");
    }
    @Override
    public void setConfigurationLink(String Link) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setConfigurationLink'");
    }
    @Override
    public String getDeviceInformationLink() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getDeviceInformationLink'");
    }
    @Override
    public void setDeviceInformationLink(String Link) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setDeviceInformationLink'");
    }
    @Override
    public String getDeviceStatusLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getDeviceStatusLink'");
    }
    @Override
    public void setDeviceStatusLink(String Link) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setDeviceStatusLink'");
    }
    @Override
    public String getRegistrationLink() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getRegistrationLink'");
    }
    @Override
    public void setRegistrationLink(String Link) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setRegistrationLink'");
    }
    @Override
    public String getPowerStatusLink() {

        throw new UnsupportedOperationException("Unimplemented method 'getPowerStatusLink'");
    }
    @Override
    public void setPowerStatusLink(String Link) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setPowerStatusLink'");
    }
    @Override
    public String getFileStatusLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getFileStatusLink'");
    }
    @Override
    public void setFileStatusLink(String Link) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setFileStatusLink'");
    }
    @Override
    public String getIPInterfaceListLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getIPInterfaceListLink'");
    }
    @Override
    public void setIPInterfaceListLink(String Link) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setIPInterfaceListLink'");
    }
    @Override
    public String getDERListLink() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getDERListLink'");
    }
    @Override
    public void setDERListLink(String Link) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setDERListLink'");
    }
    @Override
    public String getSubscriptionListLink() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getSubscriptionListLink'");
    }
    @Override
    public void setSubscriptionListLink(String Link) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setSubscriptionListLink'");
    }
    @Override
    public String getFunctionSetAssignmentsListLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getFunctionSetAssignmentsListLink'");
    }
    @Override
    public void setFunctionSetAssignmentsListLink(String Link) {
   
        throw new UnsupportedOperationException("Unimplemented method 'setFunctionSetAssignmentsListLink'");
    }
    @Override
    public String getLoadShedAvailabilityListLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getLoadShedAvailabilityListLink'");
    }
    @Override
    public void setLoadShedAvailabilityListLink(String Link) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setLoadShedAvailabilityListLink'");
    }
    @Override
    public String getLogEventListLink() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getLogEventListLink'");
    }
    @Override
    public void setLogEventListLink(String Link) {
        throw new UnsupportedOperationException("Unimplemented method 'setLogEventListLink'");
    }

}


