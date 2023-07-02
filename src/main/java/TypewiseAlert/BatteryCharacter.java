package TypewiseAlert;

import TypewiseAlert.types.CoolingType;

public class BatteryCharacter {
    public CoolingType mCoolingType;
    public String mBrand;

    BatteryCharacter(final CoolingType coolingType, final String brand) {
        mCoolingType = coolingType;
        mBrand = brand;
    }

    public CoolingType getCoolingType() {
        return mCoolingType;
    }

    public String getBrand() {
        return mBrand;
    }
}
