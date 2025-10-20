package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@TeleOp(name = "Limelight Distance Test (Persistent Calibration)", group = "Test")
public class LLSimpleDistance extends OpMode {

    private Limelight3A limelight;
    private double k = -1;
    private double distance = 0.0;
    private double[] taBuffer = new double[5];
    private int index = 0;

    // Calibration file path (stored in Control Hub internal memory)
    private static final String K_FILE_PATH = "/sdcard/FIRST/k_value.txt";

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

        // Load saved calibration value if available
        k = loadK();

        if (k > 0) {
            telemetry.addData("Loaded calibration constant (k):", k);
        } else {
            telemetry.addLine("No calibration found. Place the robot at a known distance and press A.");
        }
        telemetry.update();
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            double ta = result.getTa();

            // Noise reduction using moving average
            taBuffer[index % taBuffer.length] = ta;
            index++;
            double avgTa = 0;
            for (double t : taBuffer) avgTa += t;
            avgTa /= taBuffer.length;

            distance = getDistance(avgTa);

            // 🔹 Calibration key (example: when the robot is 50 cm away)
            if (gamepad1.a) { // Press A to calibrate
                double knownDistance = 50.0; // known distance in cm
                calibrateAndSave(knownDistance, avgTa);
                telemetry.addLine("✅ Calibration completed!");
            }

            telemetry.addData("Target Area (ta)", "%.4f", avgTa);
            telemetry.addData("Distance (cm)", "%.1f", distance);
            telemetry.addData("Calibration Constant (k)", "%.2f", k);
        } else {
            telemetry.addLine("No AprilTag detected!");
        }

        telemetry.update();
    }

    /** Distance calculation formula */
    private double getDistance(double ta) {
        if (ta <= 0) return 0;
        if (k <= 0) k = 350.0; // fallback default
        return k / Math.sqrt(ta);
    }

    /** Perform calibration and save k to file */
    private void calibrateAndSave(double knownDistanceCm, double ta) {
        if (ta > 0) {
            k = knownDistanceCm * Math.sqrt(ta);
            saveK(k);
            telemetry.addData("Calibration completed! k =", k);
        } else {
            telemetry.addLine("Calibration failed: invalid ta value.");
        }
    }

    /** Save k value to file */
    private void saveK(double value) {
        try {
            File file = new File(K_FILE_PATH);
            file.getParentFile().mkdirs(); // create folder if missing
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(value));
            writer.close();
        } catch (IOException e) {
            telemetry.addLine("Failed to save k value: " + e.getMessage());
        }
    }

    /** Load k value from file */
    private double loadK() {
        try {
            File file = new File(K_FILE_PATH);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextDouble()) {
                    double value = scanner.nextDouble();
                    scanner.close();
                    return value;
                }
                scanner.close();
            }
        } catch (Exception e) {
            telemetry.addLine("Failed to load k value: " + e.getMessage());
        }
        return -1;
    }
}

