package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

/**
 * This program provides driver station control of  Mecanum Drive Prototype.
 * <p>
 * This robot uses four VEX Mecanum wheels, each direct driven by Neverest 20 motors.
 * It is designed as a linear op mode, and uses RUN_WITH_ENCODER motor operation.
 * <p>
 * The gamepad1 right joystick is used for translation movement, while the left joystick x-axis controls rotation.
 */

//@TeleOp(name = "Mecanum Proto Manual", group = "Linear Opmode")
@Autonomous(name = "Autonomous Code", group = "Linear Opmode")
// @Disabled
public class AutonomousSquare extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor backRightMotor = null;

    // declare motor speed variables
    double FR;
    double FL;
    double BR;
    double BL;

    int ms = 1000;

    // operational constants
    double motorMax = 0.6; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        frontLeftMotor = hardwareMap.dcMotor.get("frontleft_motor");
        frontRightMotor = hardwareMap.dcMotor.get("frontright_motor");
        backLeftMotor = hardwareMap.dcMotor.get("rearleft_motor");
        backRightMotor = hardwareMap.dcMotor.get("rearright_motor");

        // Set the drive motor direction:
        // "Reverse" the motor that runs backwards when connected directly to the battery

        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set the drive motor run modes:
        // "RUN_USING_ENCODER" causes the motor to try to run at the specified fraction of full velocity
        // Note: We were not able to make this run mode work until we switched Channel A and B encoder wiring into
        // the motor controllers. (Neverest Channel A connects to MR Channel B input, and vice versa.)
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Reset speed variables
        FL = 0;
        FR = 0;
        BL = 0;
        BR = 0;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        //Square movement
        while (opModeIsActive() && runtime.time(TimeUnit.SECONDS) < 10) {
            moveFront(1);
            sleep(ms);
            moveRight(1);
            sleep(ms);
            moveBack(1);
            sleep(ms);
            moveLeft(1);
            sleep(ms);
            stopRobot();
            idle();
        }

//        while (opModeIsActive() && runtime.time(TimeUnit.SECONDS) < 30) {
//            moveFront(1);
//            sleep(ms);
//            moveRight(1);
//            sleep(ms);
//            moveBack(1);
//            sleep(ms);
//            moveLeft(1);
//            sleep(ms);
//            moveBack(1);
//            sleep(ms);
//            moveRight(1);
//            sleep(ms);
//            moveFront(1);
//            sleep(ms);
//            moveLeft(1);
//            sleep(ms);
//            stopRobot();
//            idle();
//        }

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            idle();
        }
    }

    private void stopRobot() {
        FL = BL = FR = BR = 0;
        assignPowerToMotors();
        sendTelemetryData("stopRobot");
    }

    private void moveFront(int power) {
        FL = power;
        BL = power;
        FR = power;
        BR = power;
        assignPowerToMotors();
        sendTelemetryData("moveFront");
    }

    private void moveBack(int power) {
        FL = -power;
        BL = -power;

        FR = -power;
        BR = -power;
        assignPowerToMotors();
        sendTelemetryData("moveBack");
    }

    private void moveRight(int power) {
        FL = +power;
        BL = -power;

        FR = -power;
        BR = +power;
        assignPowerToMotors();
        sendTelemetryData("moveRight");
    }

    private void moveLeft(int power) {
        FL = -power;
        BL = +power;

        FR = +power;
        BR = -power;
        assignPowerToMotors();
        sendTelemetryData("moveLeft");
    }

    //Pivot Movements
    private void moveFrontLeft(int power) {
        FL = BR = 0;
        BL = FR = power;
        assignPowerToMotors();
        sendTelemetryData("moveFrontLeft");
    }

    private void moveBackLeft(int power) {
        FL = BR = 0;
        BL = FR = -power;
        assignPowerToMotors();
        sendTelemetryData("moveBackLeft");
    }

    private void moveBackRight(int power) {
        FL = BR = -power;
        BL = FR = 0;
        assignPowerToMotors();
        sendTelemetryData("moveBackRight");
    }

    private void moveFrontRight(int power) {
        FL = BR = power;
        BL = FR = 0;
        assignPowerToMotors();
        sendTelemetryData("moveFrontRight");
    }

    private void rotateClockWise(int power) {
        FL = BL = power;
        FR = BR = -power;
        assignPowerToMotors();
        sendTelemetryData("rotateClockWise");
    }

    private void sendTelemetryData(String param) {
        // Send some useful parameters to the driver station
        telemetry.addData("Movement", param);
        telemetry.addData("FL | FR", "%.3f | %.3f", FL, FR);
        telemetry.addData("BL | BR", "%.3f | %.3f", BL, BR);
        telemetry.update();
    }

    private void assignPowerToMotors() {
        // Clip motor power values to +-motorMax
        FL = Math.max(-motorMax, Math.min(FL, motorMax));
        FR = Math.max(-motorMax, Math.min(FR, motorMax));
        BL = Math.max(-motorMax, Math.min(BL, motorMax));
        BR = Math.max(-motorMax, Math.min(BR, motorMax));

        // Send values to the motors
        frontLeftMotor.setPower(FL);
        frontRightMotor.setPower(FR);
        backLeftMotor.setPower(BL);
        backRightMotor.setPower(BR);
    }
}