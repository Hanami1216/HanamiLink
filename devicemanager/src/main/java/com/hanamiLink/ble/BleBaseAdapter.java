package com.hanamiLink.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;

import java.util.List;
import java.util.UUID;

public abstract class BleBaseAdapter {
    public static final String key_index = "key_index";
    public static final String key_idString = "idString";
    public static final String key_name = "name";

    public BleBaseAdapter() {
    }

    public abstract void managerIsBluetoothEnable(boolean var1);

    public abstract int managerDeviceConnectCount();

    public abstract void managerStartScan();

    public abstract void managerStopScan();

    public abstract void managerChangeNameForDisplay(BleDevice var1);

    public abstract UUID managerWithServiceUUID();

    public abstract String managerWithServiceUUIDPrefixString();

    public abstract UUID[] managerWithDeviceUUIDArray();

    public abstract List<ScanFilter> managerWithDeviceScanFilters();

    public abstract String managerWithDeviceNameHasPrefix();

    public abstract boolean managerWithScanFilter(BluetoothDevice var1, byte[] var2);

    public abstract void managerDiscoverDeviceChange();

    public abstract void managerDidAddNewDevice(BleDevice var1);

    public abstract void managerDidConnect(BleDevice var1);

    public abstract void managerDidDisconnect(BleDevice var1);

    public abstract void managerReadyRemoveDevice(String var1);

    public abstract void managerDidRemoveDevice();

    public abstract String deviceUUID4CharacteristicNotify();

    public abstract String deviceUUID4CharacteristicWrite();

    public abstract void managerDidReadyWriteAndNotify(BleDevice var1);

    public abstract void managerDidWarnCountOut();

    public abstract void deviceReceiveDatawithDevice(BleDevice var1, byte[] var2);
}
