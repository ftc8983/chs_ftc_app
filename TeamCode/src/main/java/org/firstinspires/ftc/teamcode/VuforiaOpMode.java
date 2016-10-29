package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

@Autonomous(name="Autonomous Mode", group="LinearOpMode")
public class VuforiaOpMode extends LinearOpMode{

    //Check out ConceptVuforiaNavigation.java in the samples folder for more info
    //Initialize vars for later
    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaTrackable target;
    VuforiaTrackableDefaultListener listener;

    OpenGLMatrix lastKnownLocation;
    OpenGLMatrix phoneLocation;

    float mmPerInch        = 25.4f;
    float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
    float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static final String VUFORIA_KEY = "AQWOTWn/////AAAAGYYjaW82j0AljSy59dLGoDU8VKmAt4XIbjgYUTVD9WDipRlj+YytZAlBbwjNUROHazUayVYQLeLvuAvkhX0mZnD4te8NBUH7/EsKCABr+GI5OZI6//qzJxpWIOenOlOSODCXwfWjIHuTfpRhz+mZxjhRMcXd7y0b6GCezGzjmivjtBm70P8WIQ48Za+Ii6OT/ybmi7WM7SIAE1MfWClI3lHOh4Y3pEb1WRb/gTxEVDq2yeBei78ivf1mP10kti7Q+XJ45t/xfIF2BYXyWovVqxZvMzuSJns4b9CMoR/E/YLaxuPvfgFRntck6XOhzAaExSbzvCLFIjxfoaoKg6agnNI0EM29SwVPuC9hq6+OjGgv";


    @Override
    public void runOpMode() throws InterruptedException {
        setupVuforia();

        lastKnownLocation = createMatrix( 0, 0, 0, 0, 0, 0 );

        visionTargets.activate();

        waitForStart();
        while (opModeIsActive()) {
            /
            // /Set last known location of robot
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            //Set last known location if the robot does not see the vision targets
            if (latestLocation != null)
                lastKnownLocation = latestLocation;



            //Print tracking data to terminal
            telemetry.addData("Tracking " + target.getName(), listener.isVisible() + "\n");
            telemetry.addData("Last Known Location ", formatMatrix(lastKnownLocation) );

            telemetry.update();
        }
    }

    public void setupVuforia() {
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

    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        //Makes matrix of robot location
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w)
                );
    }
    String formatMatrix(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }

}
