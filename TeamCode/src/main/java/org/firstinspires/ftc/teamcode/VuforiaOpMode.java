package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Vuforia OpMode", group="LinearOpMode")
public class VuforiaOpMode extends LinearOpMode{

    //Check out ConceptVuforiaNavigation.java in the samples folder for more info
    //Initialize vars for later
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables targets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
    private VuforiaTrackableDefaultListener listener;

    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    private float mmPerInch        = 25.4f;
    private float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
    private float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static final String VUFORIA_KEY = "AQWOTWn/////AAAAGYYjaW82j0AljSy59dLGoDU8VKmAt4XIbjgYUTVD9WDipRlj+YytZAlBbwjNUROHazUayVYQLeLvuAvkhX0mZnD4te8NBUH7/EsKCABr+GI5OZI6//qzJxpWIOenOlOSODCXwfWjIHuTfpRhz+mZxjhRMcXd7y0b6GCezGzjmivjtBm70P8WIQ48Za+Ii6OT/ybmi7WM7SIAE1MfWClI3lHOh4Y3pEb1WRb/gTxEVDq2yeBei78ivf1mP10kti7Q+XJ45t/xfIF2BYXyWovVqxZvMzuSJns4b9CMoR/E/YLaxuPvfgFRntck6XOhzAaExSbzvCLFIjxfoaoKg6agnNI0EM29SwVPuC9hq6+OjGgv";


    @Override
    public void runOpMode() throws InterruptedException {
        setupVuforia();

        lastKnownLocation = createMatrix( 0, 0, 0, 0, 0, 0 );

        targets.activate();

        waitForStart();
        while (opModeIsActive()) {
            // /Set last known location of robot
            OpenGLMatrix latestLocation = createMatrix( 0, 0, 0, 0, 0, 0 );
            //Set last known location if the robot does not see the vision targets
            if (latestLocation != null)
                lastKnownLocation = latestLocation;

            //Loop through targets and track each target.

            for (VuforiaTrackable target : targets) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) target.getListener()).getPose();
                if ( pose != null ) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(target.getName() + "-Translation", translation);
                    //double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                    //Displays the number of degrees needed to turn to face teh bEaCoN!!
                    //telemetry.addData(target.getName() + "-Degrees", degreesToTurn);
                }
                if ( pose == null) pose = lastKnownLocation;
            }

            /*for (VuforiaTrackable target : targets) {

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) target.getListener()).getPose();
                if ( pose != null ) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(target.getName() + "-Translation", translation);
                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                    //Displays the number of degrees needed to turn to face teh bEaCoN!!
                    telemetry.addData(target.getName() + "-Degrees", degreesToTurn);
                }
                if ( pose == null ) pose = lastKnownLocation;
            }*/

            //Print tracking data to terminal
            telemetry.addData("Tracking " + targets.get(0).getName(), listener.isVisible() + "\n");
            telemetry.addData("Last Known Location ", formatMatrix(lastKnownLocation) );

            telemetry.update();
            idle();
        }
    }

    private void setupVuforia() {
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        //Set the license key
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //This line should be obvious
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //Set the vision target
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        targets.get(0).setName("Wheels");
        targets.get(1).setName("Tools");
        targets.get(0).setName("Legos");
        targets.get(0).setName("Gears");
        //Set init location of targets
        targets.get(0).setLocation(createMatrix(0, 0, 0, 0, 0, 0));
        targets.get(1).setLocation(createMatrix(0, 0, 0, 0, 0, 0));

        //Set init location of phone on brobot
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        listener = (VuforiaTrackableDefaultListener) targets.get(0).getListener();
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

}
