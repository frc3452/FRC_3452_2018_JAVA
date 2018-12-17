package frc.robot;

public abstract class ExampleAbstractClass {
    private double mNumberWeWantToDoSomethingTo;

    public ExampleAbstractClass(double startingNumber)
    {
        setNumberTo(startingNumber);
    }

    public abstract void doSomethingWithNumber();

    public void setNumberTo(double newNumber)
    {
        this.mNumberWeWantToDoSomethingTo = newNumber;
    }

    public double getNumber()
    {
        return mNumberWeWantToDoSomethingTo;
    }

}