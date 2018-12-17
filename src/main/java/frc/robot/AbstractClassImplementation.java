package frc.robot;

public class AbstractClassImplementation {

    ExampleAbstractClass add = new ExampleAbstractClass(0) {

        // Since doSomethingWithNumber is abstract, when we create a new variable of
        // type "ExampleAbstractClass"
        // we have to define what doSomethingWithNumber does for each variable
        @Override
        public void doSomethingWithNumber() {
            setNumberTo(getNumber() + 1);
        }
    };

    ExampleAbstractClass square = new ExampleAbstractClass(2) {

        @Override
        public void doSomethingWithNumber() {
            setNumberTo(getNumber() * getNumber());
        }
    };

    ExampleAbstractClass squareRoot = new ExampleAbstractClass(36){
    
        @Override
        public void doSomethingWithNumber() {
            setNumberTo( Math.sqrt(getNumber()) );
        }
    };
    

    // Heres our constructor
    public AbstractClassImplementation() {
        //Print, do, print
        System.out.println(add.getNumber()); //HW: What would this print out?
        add.doSomethingWithNumber();
        System.out.println(add.getNumber()); //HW: What would this print out?

        //Print, do, print
        System.out.println(square.getNumber()); //HW: What would this print out?
        square.doSomethingWithNumber();
        System.out.println(square.getNumber()); //HW: What would this print out?

        //Print, do, print
        System.out.println(squareRoot.getNumber()); //HW: What would this print out?
        squareRoot.doSomethingWithNumber();
        System.out.println(squareRoot.getNumber()); //HW: What would this print out?
    }

}