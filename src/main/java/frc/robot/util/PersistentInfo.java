package frc.robot.util;

public abstract class PersistentInfo {
    private Double mValue;
        
    public PersistentInfo(Double defaultValue)
    {
    }

    public PersistentInfo()
    {
        this(Double.NaN);
    }

    public void readSetting(){};
    
    public abstract void update();

    public void setValue(Double value)
    {
        this.mValue = value;
    }

    public void addToValue(Double value)
    {
        this.mValue += value;
    }

    public Double getValue()
    {   
        return this.mValue;
    }

}