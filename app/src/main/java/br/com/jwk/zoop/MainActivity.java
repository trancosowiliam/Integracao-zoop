package br.com.jwk.zoop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST_CODE = 1001;
    private Button btnAction;

    private final String[] ALL_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAction = findViewById(R.id.mainBtnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConfigPinPadActivity.class));
            }
        });

        if (permissionChecked(ALL_PERMISSIONS)) {
            init();
        } else {
            requestPermissions();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (permissionChecked(permissions)) {
                    init();
                }

                break;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(ALL_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
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

    private void init() {
        btnAction.setEnabled(true);
    }
}
