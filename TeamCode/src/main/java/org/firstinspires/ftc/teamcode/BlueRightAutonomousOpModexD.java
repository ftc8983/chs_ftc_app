/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;

//Light/Color Sensors import statements
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

//Vuforia import statements
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;



@Autonomous(name="Blue Right Autonomous", group="Linear Opmode")
public class BlueRightAutonomousOpModexD extends LinearOpMode {

    /* Declare OpMode members. */

    ColorSensor colorSensorDown;  // Hardware Device Object

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private ColorSensor colorSensor = null;
    private DcMotor zipMotor = null;
    private DcMotorController motorCtrl = null;
    private ServoController servoCtrl = null;
    private LegacyModule legacyMod = null;

    //Declare getter and setter for pause boolean singular
    boolean singular = true;

    void setSingular(boolean sig) {
        boolean singular = sig;
    }

    public boolean getSingular() {
        return singular;
    }


/*
    //*** Vuforia Initialization Stuff Below
    //Check out ConceptVuforiaNavigation.java in the samples folder for more info
    //Initialize vars for later
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable target;
    private VuforiaTrackableDefaultListener listener;

    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    private float mmPerInch        = 25.4f;
    private float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
    private float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static final String VUFORIA_KEY = "AQWOTWn/////AAAAGYYjaW82j0AljSy59dLGoDU8VKmAt4XIbjgYUTVD9WDipRlj+YytZAlBbwjNUROHazUayVYQLeLvuAvkhX0mZnD4te8NBUH7/EsKCABr+GI5OZI6//qzJxpWIOenOlOSODCXwfWjIHuTfpRhz+mZxjhRMcXd7y0b6GCezGzjmivjtBm70P8WIQ48Za+Ii6OT/ybmi7WM7SIAE1MfWClI3lHOh4Y3pEb1WRb/gTxEVDq2yeBei78ivf1mP10kti7Q+XJ45t/xfIF2BYXyWovVqxZvMzuSJns4b9CMoR/E/YLaxuPvfgFRntck6XOhzAaExSbzvCLFIjxfoaoKg6agnNI0EM29SwVPuC9hq6+OjGgv";
    //*** End of Vuforia Stuff
*/


    @Override
    public void runOpMode() throws InterruptedException, NullPointerException  {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        motorCtrl = hardwareMap.dcMotorController.get("motorcycle");
        leftMotor = hardwareMap.dcMotor.get("left motor");
        rightMotor = hardwareMap.dcMotor.get("right motor");
        servoCtrl = hardwareMap.servoController.get("servo");
        legacyMod = hardwareMap.legacyModule.get("legacy module");
        zipMotor = hardwareMap.dcMotor.get("zip motor");
        colorSensor = hardwareMap.colorSensor.get("color sensor 1");
        colorSensorDown = hardwareMap.colorSensor.get("color sensor down");
        //Unrelated But Useful Notes Below
        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        //** Color Sensor ruOpMode Stuff
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        colorSensor.enableLed(bLedOn);
        //** End of Color Sensor Stuff



/*
        //*** Vuforia runOpMode Stuff Below
        setupVuforia();

        lastKnownLocation = createMatrix( 0, 0, 0, 0, 0, 0 );

        visionTargets.activate();
    //*** End of Vuforia runOpMode Stuff
*/


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        runtime.reset();


        /*********************WHILE OPMODE IS ACTIVE**********************/
        // run until the end of the match (driver presses STOP)


        // Step 1:  Drive forward for 2 seconds 60cm
        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(-.2);
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 2:  Spin right for 1 second 90
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() <1.53)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(.2);
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 3:  Drive forward for 2 seconds 60cm
        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(-.2);
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 4:  Spin left for 1 second 90
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.53)) {
            leftMotor.setPower(-.2);
            rightMotor.setPower(-.2);
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 5:  Drive forward for 2 seconds 60cm
        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(-.2);
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 6:  Spin right for 1 second 90
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() <1.53)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(.2);
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }
        // Step 7:  Drive forward for 2 seconds 60cm
        while (opModeIsActive() && (runtime.seconds() < 2.2)) {
            leftMotor.setPower(.2);
            rightMotor.setPower(-.2);
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
            idle();
        }

        while (opModeIsActive()) {
            //pause for ten seconds on start
            double operated = runtime.seconds();
            if (operated < 10.0) {
                leftMotor.setPower(0);
                //rightMotor must be negative
                rightMotor.setPower(0);
            } else {
                leftMotor.setPower(0.99);
                //rightMotor must be negative
                rightMotor.setPower(0.99);
            }

         //right turn

// ** Color Sensor whileActive Stuff

            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());


/**
            // convert the RGB values to HSV values.
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
            //** End of Color Sensor Stuff
 **/


/*
         //*** Vuforia whileActive Stuff
            // /Set last known location of robot
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            //Set last known location if the robot does not see the vision targets
            if (latestLocation != null)
                lastKnownLocation = latestLocation;



            //Print tracking data to terminal
            telemetry.addData("Tracking " + target.getName(), listener.isVisible() + "\n");
            telemetry.addData("Last Known Location ", formatMatrix(lastKnownLocation) );
         //*** End of Vuforia whileActive Stuff
*/

        rightMotor.setPower(rightMotor.getPower()*-1);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }




/*
    //Vuforia Setup Method (duh)
    private void setupVuforia() {
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        //Set the license key
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //This line should be obvious
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //Set the vision target
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");

        target = visionTargets.get(0);
        target.setName("Wheels Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 0, 0));
        //Set init location of phone on brobot
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    private OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        //Makes matrix of robot location
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w)
                );
    }
    private String formatMatrix(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
*/

}
