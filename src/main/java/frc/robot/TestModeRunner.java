package frc.robot;

import java.util.ArrayList;

import frc.robot.util.GZSubsystem;

public class TestModeRunner {
    private static TestModeRunner mInstance = null;

    private static TestModeRunner getInstance() {
        if (mInstance == null)
            mInstance = new TestModeRunner();

        return mInstance;
    }

    private ArrayList<OptionList> optionsList = new ArrayList<OptionList>();

    private int inMenu = -1;
    private int posInMenu = 0;

    private TestModeRunner() {

        // MOTOR TESTING
        ArrayList<Option> motorTestingOptions = new ArrayList<>();
        for (GZSubsystem s : Robot.allSubsystems.getSubsystems()) {
            if (s.hasMotors()) {
                motorTestingOptions.add(new Option("Motor Test " + s.getClass().getSimpleName()) {
                    public void run() {
                        s.addMotorTestingGroups();
                    }
                });
            }
        }

        optionsList.add(new OptionList("Motor testing", motorTestingOptions) {
            public void run() {
                for (Option o : this.getOptions())
                    o.run();
            }
        });

    }

    public void update() {
        if (!GZOI.getInstance().isTest())
            return;


        
    }

    public abstract static class OptionList extends Option {
        private ArrayList<Option> mOptions;

        public OptionList(String name, ArrayList<Option> options) {
            this(name, options, false);
        }

        public OptionList(String name, ArrayList<Option> options, boolean selectedByDefault) {
            super(name, selectedByDefault);
            this.mOptions = options;
        }

        public boolean areAnyOptionsSelected()
        {
            boolean retval = false;

            for (Option o : mOptions)
                retval |= o.isSelected();

            return retval;
        }

        public ArrayList<Option> getOptions() {
            return mOptions;
        }

        public abstract void run();

    }

    public abstract static class Option {
        private boolean mHovering = false;
        private boolean mSelected;

        private String mName;

        public Option(String name) {
            this(name, false);
        }

        public Option(String name, boolean selectedByDefault) {
            this.mName = name;
            this.mSelected = selectedByDefault;
        }

        public void hover(boolean isHovering) {
            this.mHovering = isHovering;
        }

        public void print()
        {
            String print = "";
            
            String check;
            if (isSelected())
            check = "X";
            else if (isBeingHovered())
            check = "<>";
            else 
            check = " ";
            
            print += "[ " + check + " ] " + mName;

            System.out.println(print);
        }

        public void selected(boolean selected) {
            this.mSelected = selected;
        }

        public boolean isBeingHovered() {
            return mHovering;
        }

        public boolean isSelected() {
            return mSelected;
        }

        public abstract void run();
    }
}