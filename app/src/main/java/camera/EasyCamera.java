package camera;
import activities.DocumentScannerActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class EasyCamera {
    public static final int REQUEST_CAPTURE = 10;
    public static final String EXTRA_OUTPUT_URI = "Output Uri: ";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "ImageWidth";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "Image Height:";
    public static final String EXTRA_MARGIN_BY_WIDTH = "Margin By Width: ";
    public static final String EXTRA_MARGIN_BY_HEIGHT = "Margin By Height: ";


    private Intent mCameraIntent;
    private Bundle mCameraBundle;

    public static EasyCamera create(@NonNull Uri destination) {
        return new EasyCamera(destination);
    }

    private EasyCamera( @NonNull Uri destination) {
        mCameraIntent = new Intent();
        mCameraBundle = new Bundle();
        mCameraBundle.putParcelable(EXTRA_OUTPUT_URI, destination);
    }

    /**
     * Set the margin of the framing frame around
     * @param leftRight The width is based on the value of both sides, in px
     * @param topBottom The height is subject to the value of the distance, in px
     * @return
     */
    public EasyCamera withMarginCameraEdge(int leftRight,int topBottom){
        mCameraBundle.putInt(EXTRA_MARGIN_BY_WIDTH,leftRight);
        mCameraBundle.putInt(EXTRA_MARGIN_BY_HEIGHT,topBottom);
        return this;
    }

    /**
     * @param activity
     * start up DocumentScannerActivity
     */
    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_CAPTURE);
    }

    /**
     * @param activity
     * @param requestCode requestCode
     *
     */
    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
        mCameraIntent.setClass(context, DocumentScannerActivity.class);
        mCameraIntent.putExtras(mCameraBundle);
        return mCameraIntent;
    }

    /**
     * Get the file Uri
     * @param intent
     * @return
     */
    public static Uri getOutput(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_OUTPUT_URI);
    }

    /**
     * Get the width of the picture taken
     * @param intent
     * @return
     */
    public static int getImageWidth(@NonNull Intent intent){
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_WIDTH,0);
    }

    /**
     * Get the high picture of the picture
     * @param intent
     * @return
     */
    public static int getImageHeight(@NonNull Intent intent){
        return intent.getIntExtra(
                EXTRA_OUTPUT_IMAGE_HEIGHT,0);
    }


}
