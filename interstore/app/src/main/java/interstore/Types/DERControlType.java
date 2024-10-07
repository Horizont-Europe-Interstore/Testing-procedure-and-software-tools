package interstore.Types;

/*
    Control modes supported by the DER. Bit positions SHALL be defined as follows:
    0 - Charge mode
    1 - Discharge mode
    2 - opModConnect (Connect / Disconnect - implies galvanic isolation)
    3 - opModEnergize (Energize / De-Energize)
    4 - opModFixedPFAbsorbW (Fixed Power Factor Setpoint when absorbing active power)
    5 - opModFixedPFInjectW (Fixed Power Factor Setpoint when injecting active power)
    6 - opModFixedVar (Reactive Power Setpoint)
    7 - opModFixedW (Charge / Discharge Setpoint)
    8 - opModFreqDroop (Frequency-Watt Parameterized Mode)
    9 - opModFreqWatt (Frequency-Watt Curve Mode)
    10 - opModHFRTMayTrip (High Frequency Ride Through, May Trip Mode)
    11 - opModHFRTMustTrip (High Frequency Ride Through, Must Trip Mode)
    12 - opModHVRTMayTrip (High Voltage Ride Through, May Trip Mode)
    13 - opModHVRTMomentaryCessation (High Voltage Ride Through, Momentary Cessation Mode)
    14 - opModHVRTMustTrip (High Voltage Ride Through, Must Trip Mode)
    15 - opModLFRTMayTrip (Low Frequency Ride Through)
*/

public class DERControlType extends HexBinary32{
    public DERControlType(String hexValue) {
        setHexValue32value(hexValue);  // Use the inherited method to set the value
    }

    @Override
    public void setHexValue32value(String hexValue) {
        // Convert string to integer and validate if it's between 0 and 15
        if (!isValidDERControlType(hexValue)) {
            throw new IllegalArgumentException("Invalid DERControlType value: " + hexValue + ". Value must be between 0 and 15.");
        }
        // Use the parent class method for setting the hex value
        super.setHexValue32value(hexValue);
    }


    // Validate if the provided value is within the allowed range (0–15)
    private boolean isValidDERControlType(String hexValue) {
        try {
            int intValue = Integer.parseInt(hexValue, 16);
            return intValue >= 0 && intValue <= 15;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}
