package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This program provides driver station control of  Mecanum Drive Prototype.
 * <p>
 * This robot uses four VEX Mecanum wheels, each direct driven by Neverest 20 motors.
 * It is designed as a linear op mode, and uses RUN_WITH_ENCODER motor operation.
 * <p>
 * The gamepad1 right joystick is used for translation movement, while the left joystick x-axis controls rotation.
 */

@TeleOp(name = "Mecanum Proto Manual", group = "Linear Opmode")
// @Autonomous(...) is the other common choice
// @Disabled
public class MecanumManualDrive extends LinearOpMode {

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
    // declare joystick position variables
    double X1;
    double Y1;
    double X2;
    // operational constants
    double joyScale = 0.5;
    double motorMax = 0.6; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode

    @Override
    public void runOpMode() throws InterruptedException{
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
        // These polarities are for the Neverest 20 motors

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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            // Reset speed variables
            FL = 0;
            FR = 0;
            BL = 0;
            BR = 0;

            // Get joystick values
            Y1 = -gamepad1.right_stick_y;// * joyScale; // invert so up is positive
            X1 = gamepad1.right_stick_x;// * joyScale;
            X2 = gamepad1.left_stick_x;// * joyScale;

//            // Forward/back movement
//            FL += Y1; //-1
//            BL += Y1;//-1
//
//            FR += Y1;  //-1
//            BR += Y1;//-1
//            // Side to side movement
//            FL += X1;
//            BL -= X1;
//
//            FR -= X1;
//            BR += X1;

//            // Rotation movement or Clock wise & Anti-Clock wise
//            FL += X2;
//            BL += X2;
//
//            FR -= X2;
//            BR -= X2;
//
//            //Combined code of all the above movements
            FL = Y1 + X1 + X2;
            BL = Y1 - X1 + X2;

            FR = Y1 - X1 - X2;
            BR = Y1 + X1 - X2;

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

            // Send some useful parameters to the driver station
            telemetry.addData("FL | FR", "%.3f | %.3f", FL, FR);
            telemetry.addData("BL | BR", "%.3f | %.3f", BL, BR);
            idle();
        }

        if (isStopRequested()) {
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
    }
}