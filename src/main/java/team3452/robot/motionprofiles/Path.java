package team3452.robot.motionprofiles;

/**
 * Contains everything a motion profile needs
 *
 * @author Max
 * @since 5/29/18
 */
public interface Path {
    double[][] mpL();

    double[][] mpR();

    Integer mpDur();
}
