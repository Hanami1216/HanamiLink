package com.hanamiLink.utils;

public enum BleEventType {

    BLE_SCAN_START(100),
    BLE_SCAN_END(101),
    BLE_UPDATE_DEVICE_LIST(102),
    BLE_UPDATE_DEVICE_INFO(103),
    BLE_ADD_DEVICE_CONNECT(104),
    BLE_DELETE_DEVICE(105),
    BLE_TOAST_TIP(106),
    BLE_DEVICE_ERROR(107),
    BLE_DEVICE_CONNECTED(108),
    BLE_DEVICE_DISCONNECTED(109),
    BLE_ISENABLE_YES(110),
    BLE_ISENABLE_NO(111);

    private int iNum = 0;

    BleEventType(int i) {
        this.iNum = i;
    }


    public int toNumber() {
        return this.iNum;
    }
}
