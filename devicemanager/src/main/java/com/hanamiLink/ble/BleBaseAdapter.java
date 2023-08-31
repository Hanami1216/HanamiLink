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

    /**
     * 当蓝牙状态改变时调用此方法
     * @param var1 蓝牙是否可用的标志位
     */
    public abstract void managerIsBluetoothEnable(boolean var1);

    /**
     * 获取已连接设备的数量
     * @return 已连接设备的数量
     */
    public abstract int managerDeviceConnectCount();

    /**
     * 开始扫描设备
     */
    public abstract void managerStartScan();

    /**
     * 停止扫描设备
     */
    public abstract void managerStopScan();

    /**
     * 更改设备显示名称
     * @param var1 要更改名称的设备对象
     */
    public abstract void managerChangeNameForDisplay(BleDevice var1);

    /**
     * 获取服务UUID
     * @return 服务UUID
     */
    public abstract UUID managerWithServiceUUID();

    /**
     * 获取服务UUID的前缀字符串
     * @return 服务UUID的前缀字符串
     */
    public abstract String managerWithServiceUUIDPrefixString();

    /**
     * 获取设备UUID数组
     * @return 设备UUID数组
     */
    public abstract UUID[] managerWithDeviceUUIDArray();

    /**
     * 获取设备扫描过滤器列表
     * @return 设备扫描过滤器列表
     */
    public abstract List<ScanFilter> managerWithDeviceScanFilters();

    /**
     * 获取设备名称前缀
     * @return 设备名称前缀
     */
    public abstract String managerWithDeviceNameHasPrefix();

    /**
     * 使用扫描过滤器过滤设备
     * @param var1 要过滤的蓝牙设备对象
     * @param var2 设备广播数据
     * @return 是否通过过滤器
     */
    public abstract boolean managerWithScanFilter(BluetoothDevice var1, byte[] var2);

    /**
     * 设备发现改变时调用此方法
     */
    public abstract void managerDiscoverDeviceChange();

    /**
     * 添加新设备时调用此方法
     * @param var1 新添加的设备对象
     */
    public abstract void managerDidAddNewDevice(BleDevice var1);

    /**
     * 设备连接成功时调用此方法
     * @param var1 已连接的设备对象
     */
    public abstract void managerDidConnect(BleDevice var1);

    /**
     * 设备断开连接时调用此方法
     * @param var1 断开连接的设备对象
     */
    public abstract void managerDidDisconnect(BleDevice var1);

    /**
     * 准备移除设备时调用此方法
     * @param var1 要移除的设备的UUID字符串
     */
    public abstract void managerReadyRemoveDevice(String var1);

    /**
     * 移除设备时调用此方法
     */
    public abstract void managerDidRemoveDevice();

    /**
     * 获取用于通知特征的UUID字符串
     * @return 通知特征的UUID字符串
     */
    public abstract String deviceUUID4CharacteristicNotify();

    /**
     * 获取用于写入特征的UUID字符串
     * @return 写入特征的UUID字符串
     */
    public abstract String deviceUUID4CharacteristicWrite();

    /**
     * 设备准备好写入和通知时调用此方法
     * @param var1 准备好写入和通知的设备对象
     */
    public abstract void managerDidReadyWriteAndNotify(BleDevice var1);

    /**
     * 警告计数超出时调用此方法
     */
    public abstract void managerDidWarnCountOut();

    /**
     * 接收设备数据时调用此方法
     * @param var1 接收数据的设备对象
     * @param var2 接收到的数据
     */
    public abstract void deviceReceiveDatawithDevice(BleDevice var1, byte[] var2);
}
