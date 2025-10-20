package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * TestBench — Inspired by Brogan M. Pratt style
 * A central class for IMU and sensor testing.
 */
public class TestBench {

    private BHI260IMU imu;

    /**
     * Initializes the TestBench (sets up the IMU).
     * @param hardwareMap FTC hardware map
     */
    public void init(HardwareMap hardwareMap) {
        imu = hardwareMap.get(BHI260IMU.class, "imu"); // IMU name may vary based on robot configuration

        // IMU parameters (Rev Hub orientation setup)
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );

        imu.initialize(parameters);
    }

    /**
     * Returns the full orientation (Yaw, Pitch, Roll) from the IMU.
     * Yaw usually represents rotation around the Z-axis (heading).
     */
    public YawPitchRollAngles getOrientation() {
        return imu.getRobotYawPitchRollAngles();
    }

    /**
     * Returns the current Yaw angle in degrees.
     * (You can switch to radians if needed.)
     */
    public double getYaw() {
        return getOrientation().getYaw(AngleUnit.DEGREES);
    }
}

