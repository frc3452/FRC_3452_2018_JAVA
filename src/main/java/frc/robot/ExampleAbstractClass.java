package frc.robot;

public abstract class ExampleAbstractClass {
    //Private variable
    private double mNumberWeWantToDoSomethingTo;

    //Constructor
    public ExampleAbstractClass(double startingNumber)
    {
        this.setNumberTo(startingNumber);
    }

    //Abstract method, when we create this variable we will have to define it there
    public abstract void doSomethingWithNumber();

    //Set number to
    public void setNumberTo(double newNumber)
    {
        this.mNumberWeWantToDoSomethingTo = newNumber;
    }

    //Get number
    public double getNumber() { return this.mNumberWeWantToDoSomethingTo; }

}