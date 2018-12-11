package frc.robot.util;

/**
 * Stores and continually update settings
 * void update() is abstract, define this to update mValue either through this.mValue, setValue(), or addToValue()
 */
public abstract class PersistentInfo {
    private Double mValue = Double.NaN;

    public PersistentInfo(Double defaultValue) {
        this.mValue = mValue;
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

    public Double getValue() {
        return this.mValue;
    }

}