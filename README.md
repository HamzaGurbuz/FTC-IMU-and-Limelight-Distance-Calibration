# 🤖 FTC Sensor Test Suite — IMU & Limelight Distance Calibration

## 🧩 Overview
This repository contains two testing modules for the **FIRST Tech Challenge (FTC)** robot system:

1. **`TestBench.java`** — A simple IMU test class for reading orientation data (Yaw, Pitch, Roll) using the **BHI260IMU** sensor.  
2. **`LLSimpleDistance.java`** — A TeleOp that uses the **Limelight 3A** camera to measure target distance based on the target area (ta) and supports saving/loading calibration data.

Both are designed to help with **sensor testing, calibration, and verification** before integrating into your main robot codebase.

---

## 🧭 TestBench.java — IMU Orientation Reader

### Description
`TestBench.java` provides an easy-to-use interface for retrieving **Yaw, Pitch, and Roll** angles from the **BHI260IMU**.  
It properly sets hub orientation parameters and simplifies IMU data access.

### 🚀 Key Features
- Initializes the **BHI260IMU** via `hardwareMap`.  
- Uses `RevHubOrientationOnRobot` for proper axis alignment.  
- Returns `YawPitchRollAngles` directly from the IMU.  
- Retrieves Yaw in **degrees** (modifiable to radians).

### 🧱 Example Usage
```java
@TeleOp(name = "IMU Test", group = "Test")
public class IMUTestOpMode extends OpMode {
    private TestBench testBench;

    @Override
    public void init() {
        testBench = new TestBench();
        testBench.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Yaw (deg)", testBench.getYaw());
        telemetry.update();
    }
}
```

### 🧩 Code Snippet
```java

YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
double yaw = angles.getYaw(AngleUnit.DEGREES);
```

## 🎯 LLSimpleDistance.java — Limelight 3A Distance Calibration

### Description
`LLSimpleDistance.java` estimates the robot’s **distance** from a target using the **Limelight 3A** camera’s target area (`ta`).  
It includes a **persistent calibration** system that saves and reloads the distance constant (`k`) from local storage.

### ⚡ Key Features
- Calculates distance using:

- Press **A** on the gamepad to calibrate at a known distance (e.g. 50 cm).  
- Saves calibration constant `k` to `/sdcard/FIRST/k_value.txt`.  
- Automatically loads saved calibration on init.  
- Averages multiple readings to reduce measurement noise.

### 🧪 Calibration Steps
1. Place the robot **exactly 50 cm** from the target.  
2. Start the TeleOp: `Limelight Distance Test (Persistent Calibration)`.  
3. Wait for a valid target detection.  
4. Press **A** to record calibration — the constant `k` will be saved.  
5. Restart the robot to confirm the calibration persists.

### 📊 Example Telemetry Output

Target Area (ta): 0.0356
Distance (cm): 49.8
Calibration Constant (k): 297.45
✅ Calibration completed!


---

## 📁 Project Structure
TeamCode/
└── src/main/java/org/firstinspires/ftc/teamcode/
├── TestBench.java
├── LLSimpleDistance.java
└── IMUTestOpMode.java (optional)
---


---

## ⚙️ Requirements
- **FTC SDK 9.0+**
- **REV Control Hub / Expansion Hub**
- **BHI260IMU** (internal sensor)
- **Limelight 3A** connected & configured
- Correct device names in configuration:
  - `"imu"` → BHI260IMU  
  - `"limelight"` → Limelight 3A

---

## 🧰 Developer Notes
- Update `RevHubOrientationOnRobot` parameters to match your hub’s mounting.  
- If Yaw or distance reads inverted, check axis configuration.  
- Increase the TA buffer size in `LLSimpleDistance` for smoother readings.  
- Save unique calibration values for each camera setup/environment.

---

## 🧑‍💻 Author
**Necmettin Hamza Gürbüz**  
FTC Robotics Developer & Control System Engineer  

---




