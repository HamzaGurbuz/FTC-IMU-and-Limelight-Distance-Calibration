package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * TestBench — Brogan M. Pratt tarzında
 * IMU ve sensör testleri için merkezi bir sınıf
 */
public class TestBench {

    private BHI260IMU imu;

    // TestBench'i başlat (IMU'yu initialize et)
    public void init(HardwareMap hardwareMap) {
        imu = hardwareMap.get(BHI260IMU.class, "imu"); // imu adı robotun config'ine göre değişebilir

        // IMU parametreleri (Rev Hub orientation)
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(parameters);
    }

    // IMU'dan yaw, pitch, roll açılarını döndür
    public YawPitchRollAngles getOriantation() {
        // IMU'dan yön (Yaw, Pitch, Roll) açılarını alın.
        // Yaw, genellikle z-ekseni etrafındaki dönüşü (başlık) temsil eder.
        return imu.getRobotYawPitchRollAngles();
    }

    public double getYaw() {
        // Yaw açısını derece cinsinden alın (veya ihtiyaca göre RADIAN seçin)
        return getOriantation().getYaw(AngleUnit.DEGREES);
    }

}
