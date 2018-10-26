package br.com.jwk.zoop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zoop.zoopandroidsdk.TerminalListManager;
import com.zoop.zoopandroidsdk.terminal.DeviceSelectionListener;

import org.json.JSONObject;

import java.util.Vector;


public class ConfigPinPadActivity extends AppCompatActivity implements DeviceSelectionListener {

    TerminalListManager terminalListManager;

    private final int PERMISSIONS_REQUEST_CODE = 1001;
    private final String[] ALL_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_pin_pad);

        if (permissionChecked(ALL_PERMISSIONS)) {
            init();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ALL_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        terminalListManager.finishTerminalDiscovery();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (permissionChecked(permissions)) {
                    init();
                }
        }
    }

    private void init() {
        terminalListManager = new TerminalListManager(this, getApplicationContext());
        terminalListManager.startTerminalsDiscovery();
    }

    @Override
    public void showDeviceListForUserSelection(Vector<JSONObject> vectorZoopTerminals) {
        try {
            if (vectorZoopTerminals.size() > 0) {
                terminalListManager.requestZoopDeviceSelection(vectorZoopTerminals.get(0));
            }
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "showDeviceListForUserSelection exception");
        }
    }

    @Override
    public void updateDeviceListForUserSelecion(JSONObject joNewlyFoundZoopDevice, Vector<JSONObject> vectorZoopTerminals, int iNewlyFoundDeviceIndex) {
        try {
            terminalListManager.requestZoopDeviceSelection(joNewlyFoundZoopDevice);
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "updateDeviceListForUserSelecion exception");
        }
    }

    @Override
    public void bluetoothIsNotEnabledNotification() {
        terminalListManager.enableDeviceBluetoothAdapter();
    }

    @Override
    public void deviceSelectedResult(JSONObject joZoopSelectedDevice, Vector<JSONObject> vectorAllAvailableZoopTerminals, int iSelectedDeviceIndex) {
        try {
            String namePinpad = joZoopSelectedDevice.getString("name");
            Log.i("ZOOP_LOG", "namePinpad: " + namePinpad);
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "deviceSelectedResult exception");
        }
    }

    private boolean permissionChecked(String[] permissions) {
        for (String permission : permissions) {
            if (!permissionChecked(ContextCompat.checkSelfPermission(this, permission))) {
                return false;
            }
        }

        return true;
    }

    private boolean permissionChecked(int permission) {
        return permission == PackageManager.PERMISSION_GRANTED;
    }
}