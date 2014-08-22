package com.dev_training.imos.imosbleexample;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by i-MOS on 2014/08/22.
 */
public class MyGattCallback extends BluetoothGattCallback{

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
                TextView buttonStateTextView = (TextView) mActivity.findViewById(R.id.buttonStateTextView);
                buttonStateTextView.setText("Simple Key State: "+characteristic.getValue()[0]);
            }
        });
    }
}
