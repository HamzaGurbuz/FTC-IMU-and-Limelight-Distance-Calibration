package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@TeleOp(name = "Limelight Mesafe Ölçümü (Kalıcı Kalibrasyon)", group = "Test")
public class LLSimpleDistance extends OpMode {

    private Limelight3A limelight;
    private double k = -1;
    private double distance = 0.0;
    private double[] taBuffer = new double[5];
    private int index = 0;

    // Kalibrasyon dosya yolu (Control Hub'ta dahili depolama)
    private static final String K_FILE_PATH = "/sdcard/FIRST/k_value.txt";

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

        // Önce kayıtlı k değeri varsa oku
        k = loadK();

        if (k > 0) {
            telemetry.addData("Kayıtlı k değeri yüklendi:", k);
        } else {
            telemetry.addLine("Kalibrasyon yok. Robotu bilinen mesafeye koyup A'ya bas.");
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

            // Gürültüyü azaltmak için ortalama
            taBuffer[index % taBuffer.length] = ta;
            index++;
            double avgTa = 0;
            for (double t : taBuffer) avgTa += t;
            avgTa /= taBuffer.length;

            distance = getDistance(avgTa);

            // 🔹 Kalibrasyon tuşu (örnek: 50 cm'deyken)

            telemetry.addData("Target Area (ta)", "%.4f", avgTa);
            telemetry.addData("Mesafe (cm)", "%.1f", distance);
            telemetry.addData("k Sabiti", "%.2f", k);
        } else {
            telemetry.addLine("AprilTag algılanamadı!");
        }

        telemetry.update();
    }

    /** Mesafe hesaplama formülü */
    private double getDistance(double ta) {
        if (ta <= 0) return 0;
        if (k <= 0) k = 350.0; // yedek varsayılan
        return k / Math.sqrt(ta);
    }

    /** Kalibrasyon yap ve k'yı dosyaya kaydet */
    private void calibrateAndSave(double knownDistanceCm, double ta) {
        if (ta > 0) {
            k = knownDistanceCm * Math.sqrt(ta);
            saveK(k);
            telemetry.addData("Kalibrasyon tamamlandı! k =", k);
        } else {
            telemetry.addLine("Kalibrasyon başarısız: ta geçersiz.");
        }
    }

    /** k değerini dosyaya kaydet */
    private void saveK(double value) {
        try {
            File file = new File(K_FILE_PATH);
            file.getParentFile().mkdirs(); // klasör yoksa oluştur
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(value));
            writer.close();
        } catch (IOException e) {
            telemetry.addLine("k değeri kaydedilemedi: " + e.getMessage());
        }
    }

    /** k değerini dosyadan yükle */
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
            telemetry.addLine("k değeri yüklenemedi: " + e.getMessage());
        }
        return -1;
    }
}
