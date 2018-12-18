package frc.robot.util;

/**
 * Stores and continually update settings void update() is abstract, define this
 * to update mValue either through this.mValue, setValue(),addToValue(), addDifference()
 */
public abstract class PersistentInfo {

    private Double mDefaultValue;

    private Double mValue = Double.NaN;

    private Double mPreviousAddedValue = 0.0;

    public PersistentInfo(Double defaultValue) {
        this.mDefaultValue = defaultValue;
        this.mValue = defaultValue;
    }

    public void setValueToDefault()
    {
        this.mValue = this.mDefaultValue;
        this.mPreviousAddedValue = this.mDefaultValue;
    }

    /**
     * 
     */
    public PersistentInfo() {
        this(0.0);
    }

    /**
     * 
     */
    public abstract void readSetting();

    public abstract void update();

    /**
     * Set value to
     */
    public void setValue(Double value) {
        this.mValue = value;
    }

    /***
     *  Adds (+=) to value 
     */
    public void addToValue(Double value) {
        this.mValue += value;
    }

    /**
     * Add difference from last value added 
     * @param newValue
     * @param notAbsoluteValue choose to not use absolute value (for whatever reason)
     */
    public void addDifference(Double newValue, boolean notAbsoluteValue) {
        this.mValue += (notAbsoluteValue ? (newValue - mPreviousAddedValue) : Math.abs(newValue - mPreviousAddedValue));
        this.mPreviousAddedValue = newValue;
    }

    /**
     * Add difference from last value added (using absolute value)
     * @param newValue
     */
    public void addDifference(Double newValue) {
        addDifference(newValue, false);
    }

    public Double getValue() {
        return this.mValue;
    }

}
