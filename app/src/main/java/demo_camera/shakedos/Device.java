package demo_camera.shakedos;

import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

/**
 * Created by Shaked on 6/29/15.
 */
public class Device {

    private static int orientation;

    public static void load() {
        //check if emulator is running
        if (Build.BRAND.toLowerCase().contains("generic")) {
            Device.orientation = 0;
            Log.e("BRAND==>", "BRAND generic");

        } else {
            Device.orientation = 90;
            Log.e("BRAND==>", "BRAND generic");
        }
    }

    public static int getOrientation() {
        return Device.orientation;
    }

}