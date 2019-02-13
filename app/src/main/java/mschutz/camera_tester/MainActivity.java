package mschutz.camera_tester;

import android.app.ActionBar;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;

public class MainActivity extends FragmentActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdsList = {};
        try{
             cameraIdsList = manager.getCameraIdList();
        } catch (CameraAccessException e){
            return;
        }

        mSectionsPagerAdapter =
                new SectionsPagerAdapter(
                        getSupportFragmentManager(),
                        cameraIdsList.length);
        mViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }
}
