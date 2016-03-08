package demo_camera.shakedos;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;
import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

/**
 * Created by Shaked on 3/27/15.
 */
public class CameraManager implements Camera.ErrorCallback {
    private Camera camera = null;
    private int cameraHardwareErrorCount = 0;
    private int cameraId = 0;
    private Camera.Parameters cameraParameters;
    private Activity activity;

    public CameraManager(Activity activity){
        this.activity=activity;
    }
    public void open() {
        if (null == this.camera) {

            this.camera = Camera.open(cameraId);
        }
        if (null != this.camera) {
            this.camera.stopPreview();
            this.camera.setParameters(this.getParameters());
            this.camera.startPreview();
            this.camera.setErrorCallback(this);
        }
    }

    public void close() {
        try {
            if (null != camera) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
             }
        } catch (Exception e) {}
    }

    public Camera getCamera() {
        return camera;
    }

    public Camera.Parameters getCameraParameters() {
        return cameraParameters;
    }

    public void setCameraParameters(Camera.Parameters cameraParameters) {
        this.cameraParameters = cameraParameters;
    }

    private Camera.Parameters getParameters() {
        cameraParameters = this.camera.getParameters();
        cameraParameters.setRotation(Device.getOrientation());
       // cameraParameters.set("orientation", "portrait");
       /* cameraParameters.set("s3d-prv-frame-layout", "none");
        cameraParameters.set("s3d-cap-frame-layout", "none");
        cameraParameters.set("iso", "auto");*/
       // cameraParameters.set("contrast", 100);
      /*  cameraParameters.set("brightness", 50);
        cameraParameters.set("saturation", 100);
        cameraParameters.set("sharpness", 100);
        cameraParameters.setAntibanding("auto");*/
        cameraParameters.setPictureFormat(ImageFormat.JPEG);

        cameraParameters.setJpegQuality(100);
        //DisplayMetrics displayMetrics=MainActivity.getDisplayMetrics(activity);
       /* List<Camera.Size> sizes=cameraParameters.getSupportedPictureSizes();
        for( Camera.Size size:sizes){
            Log.e("getPictureSize", "{" + size.width + "," + size.height + "}");
        }*/
       // Camera.Size size=cameraParameters.getPictureSize();
        //Log.e("getPictureSize", "{" + size.width + "," + size.height + "}");
       // cameraParameters.setPictureSize(displayMetrics.heightPixels, displayMetrics.widthPixels);
       // cameraParameters.setPreviewSize(displayMetrics.heightPixels, displayMetrics.widthPixels);
        // size=cameraParameters.getPictureSize();
        //Log.e("getPictureSize", "{" + size.width + "," + size.height + "}");
        cameraParameters.setPictureFormat(ImageFormat.JPEG);
        List<String> supportedFocusModes = cameraParameters.getSupportedFocusModes();
        if (supportedFocusModes.contains(FOCUS_MODE_CONTINUOUS_PICTURE)) {
            cameraParameters.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (supportedFocusModes.contains(FOCUS_MODE_AUTO)) {
            cameraParameters.setFocusMode(FOCUS_MODE_AUTO);
        }
        cameraParameters.setFlashMode(FLASH_MODE_OFF);
        return cameraParameters;
    }

    public void onError(int error, Camera camera) {
        if (error == Camera.CAMERA_ERROR_SERVER_DIED) {
            if (1 == cameraHardwareErrorCount) {
                //Camera doesn't work
                return ;
            }

            cameraHardwareErrorCount++;
            try {
                this.close();
                this.open();
            } catch (RuntimeException e) {
                //Camera doesn't work
                return ;
            }
        }
    }
}
