package com.hanamilink.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.util.Log;

import com.hanamiLink.ble.BLEDeviceManager;
import com.hanamiLink.ble.BleBaseAdapter;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.utils.BLEUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BleManagerAdapter extends BleBaseAdapter {

    private static final String TAG = BleManagerAdapter.class.getSimpleName();

    @Override
    public void managerIsBluetoothEnable(boolean var1) {

    }

    @Override
    public int managerDeviceConnectCount() {
        return 0;
    }

    @Override
    public void managerStartScan() {

    }

    @Override
    public void managerStopScan() {

    }

    @Override
    public void managerChangeNameForDisplay(BleDevice var1) {

    }

    @Override
    public UUID managerWithServiceUUID() {
        return null;
    }

    @Override
    public String managerWithServiceUUIDPrefixString() {
        return null;
    }

    @Override
    public UUID[] managerWithDeviceUUIDArray() {
        return new UUID[0];
    }

    @Override
    public List<ScanFilter> managerWithDeviceScanFilters() {
        return null;
    }

    @Override
    public String managerWithDeviceNameHasPrefix() {
        return null;
    }

    @Override
    public boolean managerWithScanFilter(BluetoothDevice var1, byte[] var2) {
        return false;
    }

    @Override
    public void managerDiscoverDeviceChange() {

    }

    @Override
    public void managerDidAddNewDevice(BleDevice var1) {

    }

    @Override
    public void managerDidConnect(BleDevice var1) {

    }

    @Override
    public void managerDidDisconnect(BleDevice var1) {

    }

    @Override
    public void managerReadyRemoveDevice(String var1) {

    }

    @Override
    public void managerDidRemoveDevice() {

    }

    @Override
    public String deviceUUID4CharacteristicNotify() {
        return null;
    }

    @Override
    public String deviceUUID4CharacteristicWrite() {
        return null;
    }

    @Override
    public void managerDidReadyWriteAndNotify(BleDevice var1) {

    }

    @Override
    public void managerDidWarnCountOut() {

    }


    public void deviceReceiveDatawithDevice(BleDevice device, byte[] data) {
        device.getNameString();

        Log.e(TAG, "deviceReceiveData-withDevice:" + BLEUtils.bytesToHexString(data));

        if (data == null || data.length < 3) {
            return;
        }
        device.setLastTimeReceiveData(System.currentTimeMillis());

        String devName = device.getStringParam(key_name, "设备");
        int header = data[0] & 0xFF;

    }
}
