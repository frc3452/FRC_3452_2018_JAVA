package frc.robot.util;

/**
 * Stores and continually update settings void update() is abstract, define this
 * to update mValue either through this.mValue, setValue(), or addToValue()
 */
public abstract class PersistentInfo {
    private Double mValue = Double.NaN;

    private Double mPreviousAddedValue = 0.0;

    public PersistentInfo(Double defaultValue) {
        this.mValue = defaultValue;
    }

    public PersistentInfo() {
        this(Double.NaN);
    }

    public void readSetting() {
    };

    public abstract void update();

    public void setValue(Double value) {
        this.mValue = value;
    }

    public void addToValue(Double value) {
        this.mValue += value;
    }

    public void addDifference(Double newValue, boolean absoluteValue) {
        this.mValue += (absoluteValue ? Math.abs(newValue - mPreviousAddedValue) : newValue - mPreviousAddedValue);
        this.mPreviousAddedValue = newValue;
    }

    public void addDifference(Double newValue) {
        addDifference(newValue, false);
    }

    public Double getValue() {
        return this.mValue;
    }

}