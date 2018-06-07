package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team3452.robot.util.Constants;

import java.util.ArrayList;

public class Intake extends Subsystem implements GZSubsystem {

    public Spark Intake_L, Intake_R;

    private ArrayList<SpeedController> controllers = new ArrayList<SpeedController>();

    /**
     * hardware initialization
     *
     * @author max
     * @since
     */
    public Intake() {
        Intake_L = new Spark(Constants.kIntake.INTAKE_L);
        Intake_R = new Spark(Constants.kIntake.INTAKE_R);

        controllers.add(Intake_L);
        controllers.add(Intake_R);

        Intake_L.setInverted(Constants.kIntake.INTAKE_L_INVERT);
        Intake_R.setInverted(Constants.kIntake.INTAKE_R_INVERT);

        Intake_L.setSubsystem("Intake");
        Intake_R.setSubsystem("Intake");

        Intake_L.setName("Intake L");
        Intake_R.setName("Intake R");
    }

    /**
     * @param speed
     * @author max
     * @since
     */
    public void manual(double speed) {
        Intake_L.set(speed);
        Intake_R.set(speed);
    }

    @Override
    public void initDefaultCommand() {
    }

    @Override
    public void setDisable(boolean toSetDisable) {
        for (SpeedController controller : controllers)
            if (toSetDisable)
                controller.disable();
            else
                controller.set(0);

    }

}
