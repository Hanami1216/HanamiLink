package com.hanamiLink.eventbus;

public enum BleEventType {

    // BLE 扫描开始
    BLE_SCAN_START(100),
    // BLE 扫描结束
    BLE_SCAN_END(101),
    // 更新设备列表
    BLE_UPDATE_DEVICE_LIST(102),
    // 更新设备信息
    BLE_UPDATE_DEVICE_INFO(103),
    // 添加设备连接
    BLE_ADD_DEVICE_CONNECT(104),
    // 删除设备
    BLE_DELETE_DEVICE(105),
    // BLE 提示消息
    BLE_TOAST_TIP(106),
    // BLE 设备错误
    BLE_DEVICE_ERROR(107),
    // BLE 设备已连接
    BLE_DEVICE_CONNECTED(108),
    // BLE 设备断开连接
    BLE_DEVICE_DISCONNECTED(109),
    // BLE 已启用
    BLE_ISENABLE_YES(110),
    // BLE 未启用
    BLE_ISENABLE_NO(111);


    private int iNum = 0;

    BleEventType(int i) {
        this.iNum = i;
    }


    public int toNumber() {
        return this.iNum;
    }
}
