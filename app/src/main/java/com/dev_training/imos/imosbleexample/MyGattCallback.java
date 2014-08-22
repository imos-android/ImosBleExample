package com.dev_training.imos.imosbleexample;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by i-MOS on 2014/08/22.
 */
public class MyGattCallback extends BluetoothGattCallback{
    String TI_BASE_UUID = "-0451-4000-b000-000000000000";
    UUID accServiceUuid = UUID.fromString("f000aa10"+TI_BASE_UUID);
    UUID accCharacteristicUuid = UUID.fromString("f000aa11"+TI_BASE_UUID);
    UUID accConfigCharacteristicUuid = UUID.fromString("f000aa12"+TI_BASE_UUID);
    UUID accPeriodCharacteristicUuid = UUID.fromString("f000aa13"+TI_BASE_UUID);

    private Activity mActivity;

    public MyGattCallback(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (status == BluetoothGatt.GATT_SUCCESS){
            if (newState == BluetoothProfile.STATE_CONNECTED){
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,
                                "接続に成功しました。", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            gatt.discoverServices();
        } else {

        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        //

    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity,
                        "通知成功",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (characteristic.getUuid().equals(accCharacteristicUuid)){
                    TextView accStateTextView = (TextView) mActivity.findViewById(R.id.accTextView);
                    byte accValue[] = characteristic.getValue();
                    accStateTextView.setText("Acc State: "+accValue[0]+","+accValue[1]+","+accValue[2]);
                } else {
                    TextView buttonStateTextView = (TextView) mActivity.findViewById(R.id.buttonStateTextView);
                    buttonStateTextView.setText("Simple Key State: "+characteristic.getValue()[0]);
                }

            }
        });
    }
}
