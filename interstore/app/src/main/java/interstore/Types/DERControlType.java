package interstore.Types;

import java.util.HashMap;
import java.util.Map;

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
    16 = opModLFRTMustTrip (Low Frequency Ride-Through, Must Trip mode)
    17 = opModLVRTMayTrip (Low Voltage Ride-Through, May Trip mode)
    18 = opModLVRTMomentaryCessation (Low Voltage Ride-Through, Momentary Cessation mode)
    19 = opModLVRTMustTrip (Low Voltage Ride-Through, Must Trip mode)
    20 = opModMaxLimW (maximum active power)
    21 = opModTargetVar (target reactive power)
    22 = opModTargetW (target active power)
    23 = opModVoltVar (Volt-Var mode)
    24 = opModVoltWatt (Volt-Watt mode)
    25 = opModWattPF (Watt-Powerfactor mode)
    26 = opModWattVar (Watt-Var mode)

*/

public class DERControlType extends HexBinary32{
     
    private static  final Map<Integer, String> derControltypes = new HashMap<>(); 
    static {
       derControltypes.put(0, "Charge mode");
       derControltypes.put(1, "Discharge mode");
       derControltypes.put(2, "opModConnect (Connect / Disconnect - implies galvanic isolation)");
       derControltypes.put(3, "opModEnergize (Energize / De-Energize)");
       derControltypes.put(4, "opModFixedPFAbsorbW (Fixed Power Factor Setpoint when absorbing active power)");
       derControltypes.put(5, "opModFixedPFInjectW (Fixed Power Factor Setpoint when injecting active power)");
       derControltypes.put(6, "opModFixedVar (Reactive Power Setpoint)");
       derControltypes.put(7, "opModFixedW (Charge / Discharge Setpoint)");
       derControltypes.put(8, "opModFreqDroop (Frequency-Watt Parameterized Mode)");
       derControltypes.put(9, "opModFreqWatt (Frequency-Watt Curve Mode)");
       derControltypes.put(10, "opModHFRTMayTrip (High Frequency Ride Through, May Trip Mode)");
       derControltypes.put(11, "opModHFRTMustTrip (High Frequency Ride Through, Must Trip Mode)");
       derControltypes.put(12, "opModHVRTMayTrip (High Voltage Ride Through, May Trip Mode)");
       derControltypes.put(13, "opModHVRTMomentaryCessation (High Voltage Ride Through, Momentary Cessation Mode)");
       derControltypes.put(14, "opModHVRTMustTrip (High Voltage Ride Through, Must Trip Mode)");
       derControltypes.put(15, "opModLFRTMayTrip (Low Frequency Ride Through, May Trip Mode)");
       derControltypes.put(16, "opModLFRTMustTrip (Low Frequency Ride-Through, Must Trip mode)");
       derControltypes.put(17, "opModLVRTMayTrip (Low Voltage Ride-Through, May Trip mode)");
       derControltypes.put(18, "opModLVRTMomentaryCessation (Low Voltage Ride-Through, Momentary Cessation mode)");
       derControltypes.put(19, "opModLVRTMustTrip (Low Voltage Ride-Through, Must Trip mode)");
       derControltypes.put(20, "opModMaxLimW (maximum active power)");
       derControltypes.put(21, "opModTargetVar (target reactive power)");
       derControltypes.put(22, "opModTargetW (target active power)");
       derControltypes.put(23, "opModVoltVar (Volt-Var mode)");
       derControltypes.put(24, "opModVoltWatt (Volt-Watt mode)");
       derControltypes.put(25, "opModWattPF (Watt-Powerfactor mode)");
       derControltypes.put(26, "opModWattVar (Watt-Var mode)");
    }

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
    
    public String getDescription(int derTypeNumber) {
        return "DER Type " + derTypeNumber + ": " +derControltypes.getOrDefault(derTypeNumber, "Unknown mode");
    }

    public String getControlModesDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();
        int value = Integer.parseInt(getHexValue32value(), 16);

        for (int i = 0; i <= 26; i++) {
            if ((value & (1 << i)) != 0) {
                descriptionBuilder.append(getDescription(i)).append("\n");
            }
        }
        return descriptionBuilder.toString();
    }


}
