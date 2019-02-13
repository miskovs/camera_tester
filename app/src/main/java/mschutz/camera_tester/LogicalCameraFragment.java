package mschutz.camera_tester;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LogicalCameraFragment extends Fragment {

    private static CameraCharacteristics.Key[] usefulCharacteristics = {
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL,
            CameraCharacteristics.LENS_FACING,
            CameraCharacteristics.LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE,
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES
    };

    private static HashMap<CameraCharacteristics.Key, String> keyToStringMap = new HashMap(){{
        put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, "INFO_SUPPORTED_HARDWARE_LEVEL");
        put(CameraCharacteristics.LENS_FACING, "LENS_FACING");
        put(CameraCharacteristics.LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE, "LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE");
        put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, "REQUEST_AVAILABLE_CAPABILITIES");
    }};

    private static HashMap<CameraCharacteristics.Key, HashMap<Integer, String>> keyToVerboseValue = new HashMap(){{
       put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, new HashMap(){{
           put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED, "INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED");
           put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL, "INFO_SUPPORTED_HARDWARE_LEVEL_FULL");
           put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY, "INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY");
           put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3, "INFO_SUPPORTED_HARDWARE_LEVEL_3");
           put(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL, "INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL");
       }});
        put(CameraCharacteristics.LENS_FACING, new HashMap(){{
            put(CameraCharacteristics.LENS_FACING_FRONT, "LENS_FACING_FRONT");
            put(CameraCharacteristics.LENS_FACING_BACK, "LENS_FACING_BACK");
            put(CameraCharacteristics.LENS_FACING_EXTERNAL, "LENS_FACING_EXTERNAL");
        }});
        put(CameraCharacteristics.LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE, new HashMap(){{
            put(CameraCharacteristics.LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE_APPROXIMATE, "APPROXIMATE");
            put(CameraCharacteristics.LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE_CALIBRATED, "CALIBRATED");
        }});
        put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES, new HashMap(){{
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE, "BACKWARD_COMPATIBLE");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR, "MANUAL_SENSOR");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING, "MANUAL_POST_PROCESSING");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW, "RAW");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING, "PRIVATE_REPROCESSING");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_READ_SENSOR_SETTINGS, "READ_SENSOR_SETTINGS");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BURST_CAPTURE, "BURST_CAPTURE");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_YUV_REPROCESSING, "YUV_REPROCESSING");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT, "DEPTH_OUTPUT");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_CONSTRAINED_HIGH_SPEED_VIDEO, "CONSTRAINED_HIGH_SPEED_VIDEO");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MOTION_TRACKING, "MOTION_TRACKING");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA, "LOGICAL_MULTI_CAMERA");
            put(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MONOCHROME, "MONOCHROME");
        }});
    }};

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.single_page, container, false);
        Bundle args = getArguments();
        String cameraId = Integer.toString(args.getInt("cameraId"));
        ((TextView) rootView.findViewById(R.id.pageText)).setText("Logical camera id " + cameraId);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics cameraCharacteristics;
        try{
            cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException e){
            return rootView;
        }
        LinearLayout scrollViewLayout = rootView.findViewById(R.id.scrollViewLayout);
        for (CameraCharacteristics.Key k : usefulCharacteristics){
            Object result = cameraCharacteristics.get(k);
            TextView newView = new TextView(this.getContext());
            if (result instanceof String){
                newView.setText("Key: " + keyToStringMap.get(k) + "\nValue:" + result);
            }
            else if (result instanceof Integer){
                newView.setText("Key: " + keyToStringMap.get(k) + "\nValue:" + keyToVerboseValue.get(k).get(result));
            }
            else if (result == null){
                newView.setText("Key: " + keyToStringMap.get(k) + "\nValue:" + "null, unsupported?");
            }
            else if (result instanceof int[]){
                String fullResult = "";
                for (int i : (int[]) result) {
                    fullResult += keyToVerboseValue.get(k).get(i);
                    fullResult += "\n\t";
                }
                newView.setText("Key: " + keyToStringMap.get(k) + "\nValues:" + fullResult);
            }

            scrollViewLayout.addView(newView);
        }

        List availablePhysicalCameraRequestKeys = cameraCharacteristics.getAvailablePhysicalCameraRequestKeys();
        if (availablePhysicalCameraRequestKeys == null){
            TextView newView = new TextView(this.getContext());
            newView.setText("availablePhysicalCameraRequestKeys is null");
            scrollViewLayout.addView(newView);
        }

        Set<String> physicalCameraIdsSet = cameraCharacteristics.getPhysicalCameraIds();
        if (physicalCameraIdsSet.isEmpty()){
            TextView newView = new TextView(this.getContext());
            newView.setText("physicalCameraIds set is empty");
            scrollViewLayout.addView(newView);
        }

        return rootView;
    }
}