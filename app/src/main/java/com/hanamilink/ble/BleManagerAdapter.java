package com.hanamilink.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;

import com.hanamiLink.ble.BLEDeviceManager;
import com.hanamiLink.ble.BleBaseAdapter;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.utils.BLEUtils;
import com.hanamiLink.utils.BleEventType;
import com.hanamiLink.utils.BleEventUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BleManagerAdapter extends BleBaseAdapter {

    private static final String TAG = BleManagerAdapter.class.getSimpleName();

    @Override
    public void managerIsBluetoothEnable(boolean isEnable) {
        if (isEnable) {
            BleEventUtils.postEmptyMsg(BleEventType.BLE_ISENABLE_YES.ordinal());
        } else {
            BleEventUtils.postEmptyMsg(BleEventType.BLE_ISENABLE_NO.ordinal());
        }
    }

    @Override
    public int managerDeviceConnectCount() {
        return 1;
    }

    @Override
    public void managerStartScan() {
        BleEventUtils.postEmptyMsg(BleEventType.BLE_SCAN_START.toNumber());
    }

    @Override
    public void managerStopScan() {
        BleEventUtils.postEmptyMsg(BleEventType.BLE_SCAN_END.toNumber());
    }

    @Override
    public void managerChangeNameForDisplay(BleDevice bleDevice) {
        if (bleDevice != null) {
            String defaultName = bleDevice.getNameString();
            String name = bleDevice.getStringParam(key_name, "");

            if (name == null || name.trim().equals("")) {
                bleDevice.putStringParam(key_name, defaultName);
            }
        }
    }

    @Override
    public UUID managerWithServiceUUID() {
        return UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb");
    }

    @Override
    public String managerWithServiceUUIDPrefixString() {
        return "0000ff12";
    }

    @Override
    public UUID[] managerWithDeviceUUIDArray() {
        return new UUID[0];
    }

    @Override
    public List<ScanFilter> managerWithDeviceScanFilters() {
        UUID[] uuids = managerWithDeviceUUIDArray();
        if (uuids != null) {
            return Arrays.stream(uuids)
                    .map(uuid -> new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(uuid.toString())).build())
                    .collect(Collectors.toList());
        }
        return null;
    }

    //
    @Override
    public String managerWithDeviceNameHasPrefix() {
        return "";
    }

    @Override
    public boolean managerWithScanFilter(BluetoothDevice bluetoothDevice, byte[] scanRecord) {
        //@SuppressLint("MissingPermission")
        //String deviceName = bluetoothDevice.getName();
        //String filterNamePrefix = managerWithDeviceNameHasPrefix();
        //
        //if (filterNamePrefix == null) {
        //    filterNamePrefix = "";
        //}
        //
        //if (deviceName != null && deviceName.startsWith(filterNamePrefix)) {
        //
        //    String idString = bluetoothDevice.getAddress();
        //    // 根据id获取Device
        //    BLEDeviceManager.getInstance().getDeviceById(idString);
        //    return true;
        //}
        return true;
    }

    @Override
    public void managerDiscoverDeviceChange() {
        BleEventUtils.postEmptyMsg(BleEventType.BLE_UPDATE_DEVICE_LIST.toNumber());
    }

    @Override
    public void managerDidAddNewDevice(BleDevice bleDevice) {
        List<BleDevice> list = BLEDeviceManager.getInstance().getAllUsedDevices();

        long maxIndex = 0;

        for (BleDevice d : list) {
            if (maxIndex < d.getIndex()) {
                maxIndex = d.getIndex();
            }
        }
        bleDevice.setIndex(maxIndex + 1);

        BLEDeviceManager.getInstance().saveAllDeviceConnected();
    }

    @Override
    public void managerDidConnect(BleDevice bleDevice) {
        bleDevice.setLastTimeReceiveData(System.currentTimeMillis());

        BLEDeviceManager.getInstance().saveAllDeviceConnected();


        BleEventUtils.postEmptyMsg(BleEventType.BLE_UPDATE_DEVICE_LIST.toNumber());

    }

    @Override
    public void managerDidDisconnect(BleDevice bleDevice) {

        bleDevice.getParamTempMap().clear();

        BleEventUtils.postMsgWithObject(BleEventType.BLE_UPDATE_DEVICE_INFO.toNumber(), bleDevice.getIdString());
        BleEventUtils.postEmptyMsg(BleEventType.BLE_UPDATE_DEVICE_LIST.toNumber());

        BleEventUtils.postMsgWithObject(BleEventType.BLE_DEVICE_DISCONNECTED.toNumber(), bleDevice.getNameString());

    }

    @Override
    public void managerReadyRemoveDevice(String idString) {

    }

    @Override
    public void managerDidRemoveDevice() {
        BleEventUtils.postEmptyMsg(BleEventType.BLE_UPDATE_DEVICE_LIST.toNumber());
    }

    @Override
    public String deviceUUID4CharacteristicNotify() {
        return "0000ff14";
    }

    @Override
    public String deviceUUID4CharacteristicWrite() {
        return "0000ff14";
    }

    @Override
    public void managerDidReadyWriteAndNotify(BleDevice bleDevice) {
        BleEventUtils.postMsgWithObject(BleEventType.BLE_DEVICE_CONNECTED.toNumber(),bleDevice.getNameString());
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            BLEDeviceManager.getInstance().sendDataDelay(bleDevice, CMD.getQuerySystemModeType());
            BLEDeviceManager.getInstance().sendDataDelay(bleDevice, CMD.getQuerySystemStatusType());
        }, 2000);

    }

    @Override
    public void managerDidWarnCountOut() {
        BleEventUtils.postMsgWithObject(BleEventType.BLE_TOAST_TIP.toNumber(), "超出可使用的设备数量!");
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
