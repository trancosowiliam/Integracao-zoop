package br.com.jwk.zoop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zoop.zoopandroidsdk.TerminalListManager;
import com.zoop.zoopandroidsdk.ZoopAPI;
import com.zoop.zoopandroidsdk.ZoopTerminalPayment;
import com.zoop.zoopandroidsdk.terminal.ApplicationDisplayListener;
import com.zoop.zoopandroidsdk.terminal.DeviceSelectionListener;
import com.zoop.zoopandroidsdk.terminal.ExtraCardInformationListener;
import com.zoop.zoopandroidsdk.terminal.TerminalMessageType;
import com.zoop.zoopandroidsdk.terminal.TerminalPaymentListener;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Vector;


public class ConfigPinPadActivity extends AppCompatActivity
        implements DeviceSelectionListener, TerminalPaymentListener, ApplicationDisplayListener, ExtraCardInformationListener {

    private TerminalListManager terminalListManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_pin_pad);

        try {
            ZoopAPI.initialize(getApplication(), BuildConfig.MARKETPLACE_ID, BuildConfig.SELLER_ID, BuildConfig.PUBLISHABLE_KEY);
            terminalListManager = new TerminalListManager(this, getApplicationContext());
            terminalListManager.startTerminalsDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.cppBtnZoopInit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTerminalListManager();
            }
        });
    }

    @Override
    protected void onDestroy() {
        terminalListManager.finishTerminalDiscovery();
        super.onDestroy();
    }

    @Override
    public void showDeviceListForUserSelection(Vector<JSONObject> vectorZoopTerminals) {
        try {
            Log.i("ZOOP_LOG", "showDeviceListForUserSelection: " + vectorZoopTerminals.toString());
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
            Log.i("ZOOP_LOG", "updateDeviceListForUserSelecion: " + joNewlyFoundZoopDevice.toString());
            terminalListManager.requestZoopDeviceSelection(joNewlyFoundZoopDevice);
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "updateDeviceListForUserSelecion exception");
        }
    }

    @Override
    public void bluetoothIsNotEnabledNotification() {
        Log.i("ZOOP_LOG", "bluetoothIsNotEnabledNotification");
        terminalListManager.enableDeviceBluetoothAdapter();
    }

    @Override
    public void deviceSelectedResult(JSONObject joZoopSelectedDevice, Vector<JSONObject> vectorAllAvailableZoopTerminals, int iSelectedDeviceIndex) {
        try {
            Log.i("ZOOP_LOG", "deviceSelectedResult: " + joZoopSelectedDevice.toString());
            String namePinpad = joZoopSelectedDevice.getString("name");
            Log.i("ZOOP_LOG", "namePinpad: " + namePinpad);
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "deviceSelectedResult exception");
        }
    }

    // ----- TerminalPaymentListener ----- //
    @Override
    public void paymentFailed(JSONObject jsonObject) {
        Log.i("ZOOP_LOG", "paymentFailed: " + jsonObject.toString());
    }

    @Override
    public void paymentSuccessful(JSONObject jsonObject) {
        Log.i("ZOOP_LOG", "paymentSuccessful: " + jsonObject.toString());
    }

    @Override
    public void paymentAborted() {
        Log.i("ZOOP_LOG", "paymentAborted");
    }

    @Override
    public void cardholderSignatureRequested() {
        Log.i("ZOOP_LOG", "cardholderSignatureRequested");
    }

    @Override
    public void currentChargeCanBeAbortedByUser(boolean b) {
        Log.i("ZOOP_LOG", "currentChargeCanBeAbortedByUser: " + b);
    }

    @Override
    public void signatureResult(int i) {
        Log.i("ZOOP_LOG", "signatureResult");
    }

    // ----- ApplicationDisplayListener ----- //
    @Override
    public void showMessage(String s, TerminalMessageType terminalMessageType) {
        Log.i("ZOOP_LOG", "showMessage: " + s);
    }

    @Override
    public void showMessage(String s, TerminalMessageType terminalMessageType, String s1) {
        Log.i("ZOOP_LOG", "showMessages1: " + s);
    }

    // ----- ApplicationDisplayListener ----- //
    @Override
    public void cardLast4DigitsRequested() {
        Log.i("ZOOP_LOG", "cardLast4DigitsRequested");
    }

    @Override
    public void cardExpirationDateRequested() {
        Log.i("ZOOP_LOG", "cardExpirationDateRequested");
    }

    @Override
    public void cardCVCRequested() {
        Log.i("ZOOP_LOG", "cardCVCRequested");
    }

    private void startTerminalListManager() {
        try {
            Log.i("ZOOP_LOG", "startTerminalListManager");

            ZoopTerminalPayment zoopTerminalPayment = new ZoopTerminalPayment();
            zoopTerminalPayment.setTerminalPaymentListener(this);
            zoopTerminalPayment.setApplicationDisplayListener(this);
            zoopTerminalPayment.setExtraCardInformationListener(this);
            zoopTerminalPayment.charge(new BigDecimal(2.3),
                    ZoopTerminalPayment.CHARGE_TYPE_CREDIT, 1, BuildConfig.MARKETPLACE_ID, BuildConfig.SELLER_ID, BuildConfig.PUBLISHABLE_KEY);
        } catch (Exception e) {
            Log.i("ZOOP_LOG", "startTerminalListManager exception");
        }
    }
}