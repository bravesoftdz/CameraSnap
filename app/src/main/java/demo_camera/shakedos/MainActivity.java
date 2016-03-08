package demo_camera.shakedos;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Wrapper wrapper;
    private CameraFragment cameraFragment;
    private Picture picture;
    //Set the square picture at the end, in real life this should come from configuration.
    //private final int pictureSquareSize = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Device.load();
        setContentView(R.layout.activity_main);
        wrapper = new Wrapper(this);
        picture = new Picture();
        Bundle args = new Bundle();
        args.putSerializable("wrapper", wrapper);
        cameraFragment = new CameraFragment();
        cameraFragment.setArguments(args);
        cameraFragment.setRetainInstance(true);
        getFragmentManager().beginTransaction().add(cameraFragment, "shakedos").commit();
        this.bindButtons();
    }

    private void bindButtons() {
        final ImageButton snapButton = this.wrapper.getSnapButton();
        final ImageButton approveButton = this.wrapper.getApproveButton();
        final ImageButton btnRetake = this.wrapper.getBtnRetake();
        snapButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        (new TakePictureTask(
                                MainActivity.this,
                                cameraFragment,
                                wrapper,
                                picture
                        )).execute(
                                MainActivity.this.getPackageManager().hasSystemFeature(
                                        PackageManager.FEATURE_CAMERA_AUTOFOCUS
                                )
                        );
                    }
                }
        );
        final String dataDir = "shakedos";
        approveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=picture.getCurrentPicture())
                        (new ApprovePictureTask(
                                MainActivity.this,
                                wrapper.getGridContainer(),
                                wrapper.getFullScreen(),
                                dataDir,
                                picture
                        )).execute();
                    }
                }
        );
        btnRetake.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View V) {
                        (new RetakePictureTask(wrapper, cameraFragment)).execute();
                    }
                }
        );
    }
    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.e("getDisplayMetrics", "{" + width + "," + height + "}");

       return metrics;



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishWithError() {
        Toast.makeText(
                this.getApplicationContext(), "Application ended with an error. I am not saving " +
                        "errors at the moment...",
                Toast.LENGTH_LONG
        ).show();
        this.setResult(2);
        this.finish();
    }
}
