package frc.robot;

import java.util.ArrayList;
import java.util.Arrays;

import frc.robot.Constants.kFiles;
import frc.robot.util.GZSubsystem;
import frc.robot.util.GZUtil;
import frc.robot.util.MotorChecker;
import frc.robot.util.GZJoystick.Buttons;

public class TestModeRunner {
    private static TestModeRunner mInstance = null;

    private static TestModeRunner getInstance() {
        if (mInstance == null)
            mInstance = new TestModeRunner();

        return mInstance;
    }

    private ArrayList<OptionList> optionsList = new ArrayList<OptionList>();

    private int inMenu = -1;
    private int prevInMenu = inMenu - 1;
    private int posInMenu = 0;
    private int prevPosInMenu = posInMenu - 1;
    private boolean selectPressed = false;

    private TestModeRunner() {

        // ADD MOTOR TESTING INTERNAL MENU
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

        // ADD MENU FOR MOTOR TESTING TO BIG MENU
        optionsList.add(new OptionList("Motor testing", motorTestingOptions) {
            public void run() {
                for (Option o : this.getOptions())
                    o.run();

                MotorChecker.AmperageChecker.getInstance().checkMotors();
            }
        });

        ArrayList<Option> pdpTestingOptions = new ArrayList<>();
        for (GZSubsystem s : Robot.allSubsystems.getSubsystems()) {
            if (s.hasMotors()) {
                pdpTestingOptions.add(new Option("PDP Test " + s.getClass().getSimpleName()) {
                    public void run() {
                        s.addPDPTestingMotors();
                    }
                });
            }
        }

        optionsList.add(new OptionList("PDP Testing", pdpTestingOptions) {
            public void run() {
                for (Option o : this.getOptions())
                    o.run();

                MotorChecker.PDPChannelChecker.getInstance().run(kFiles.PDPChannelCheckerWaitTime);
            }
        });

    }

    public void update() {
        if (!GZOI.getInstance().isTest())
            return;

        selectPressed = false;

        // Check position
        if (inMenu == -1) {
            if (posInMenu > optionsList.size() - 1)
                posInMenu = optionsList.size() - 1;
            else if (posInMenu < 0)
                posInMenu = 0;
        } else {
            int size = optionsList.get(inMenu).getOptions().size() - 1;
            if (posInMenu > size)
                posInMenu = size;
            else if (posInMenu < 0)
                posInMenu = 0;
        }

        // Moving between different menus
        if (GZOI.driverJoy.isDLeftPressed())
            inMenu = -1;
        else if (inMenu == -1 && GZOI.driverJoy.isDRightPressed())
            inMenu = posInMenu;

        // Highlighting
        // Main menu
        if (inMenu == -1) {
            for (OptionList o : optionsList)
                o.hover(false);
            optionsList.get(posInMenu).hover(true);
        } else { // In another menu
            for (Option o : optionsList.get(inMenu).getOptions())
                o.hover(false);
            optionsList.get(inMenu).hover(true);
        }

        // Dont allow more than one main menu item to be selected
        {
            int optionListsSelected = 0;
            for (OptionList o : optionsList)
                if (o.isSelected())
                    optionListsSelected++;
            if (optionListsSelected > 1)
                for (OptionList o : optionsList)
                    o.deselectAllOptions();
        }

        // Select
        if (inMenu != -1 && GZOI.driverJoy.isAPressed()) {
            optionsList.get(inMenu).getOptions().get(posInMenu).toggleSelected();
            selectPressed = true;
        }

        if (prevInMenu != inMenu || prevPosInMenu != posInMenu || selectPressed) {
            String message;
            message = "Use DPad to navigate";
            if (inMenu != -1)
                message += ", A to select, Y to start";
            print(message);
        }

        prevInMenu = inMenu;
        prevPosInMenu = posInMenu;
    }

    private void print(String message) {
        if (inMenu == -1) {
            System.out.println("~~~" + "Testing Menu" + "~~~");
            for (OptionList o : optionsList) {
                o.print();
            }
        } else {
            System.out.println("~~~" + optionsList.get(inMenu).getName() + "~~~");
            for (Option o : optionsList.get(inMenu).getOptions()) {
                o.print();
            }
        }
        System.out.println("~~~ " + message + " ~~~");
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

        @Override
        public boolean isSelected() {
            boolean retval = false;

            for (Option o : mOptions)
                retval |= o.isSelected();

            return retval;
        }

        public ArrayList<Option> getOptions() {
            return mOptions;
        }

        public void deselectAllOptions() {
            for (Option o : mOptions)
                o.selected(false);
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

        public void print() {
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

        public String getName() {
            return this.mName;
        }

        public void selected(boolean selected) {
            this.mSelected = selected;
        }

        public void toggleSelected() {
            selected(!isSelected());
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