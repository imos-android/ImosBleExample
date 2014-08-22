package com.dev_training.imos.imosbleexample;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import static  com.dev_training.imos.imosbleexample.UUIDs.*;



public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        private static final String TAG = "PlaceholderFragment";
        private static final String MY_DEVICE_ADDRESS ="BC:6A:29:AB:5C:C7" ;
        private View mRootView;
        private BluetoothAdapter mBluetoothAdapter;
        private MyGattCallback mMyGattCallback;
        private BluetoothGatt mGatt;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_my, container, false);
            mRootView.findViewById(R.id.button).setOnClickListener(this);
            mRootView.findViewById(R.id.enableNotifyButton).setOnClickListener(this);
            return mRootView;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                    mBluetoothAdapter = bluetoothManager.getAdapter();
                    if (mBluetoothAdapter == null
                            || !mBluetoothAdapter.isEnabled()){
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(intent);
                        return;
                    }
                    mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                        @Override
                        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                            // Scan
                            if (bluetoothDevice.getAddress().equals(MY_DEVICE_ADDRESS)){
                                //
                                mMyGattCallback = new MyGattCallback(getActivity());
                                mGatt = bluetoothDevice.connectGatt(
                                        getActivity(),
                                        false,
                                        mMyGattCallback
                                );
                            }
                        }
                    });
                    break;
                case R.id.enableNotifyButton:
                    BluetoothGattService simpleKeyService
                            = mGatt.getService(UUID_TI_SENSOR_TAG_SIMPLE_KEY_SERVICE);
                    BluetoothGattCharacteristic simpleKeyServiceCharacteristic
                            = simpleKeyService.getCharacteristic(UUID_TI_SENSOR_TAG_SIMPLE_KEY);

                    mGatt.setCharacteristicNotification(simpleKeyServiceCharacteristic,
                            true);
                    BluetoothGattDescriptor descriptor
                            = simpleKeyServiceCharacteristic.getDescriptor(
                            UUID_CLIENT_CHARACTERISTIC_CONFIGURATION);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mGatt.writeDescriptor(descriptor);

                    break;
            }
        }
    }
}
