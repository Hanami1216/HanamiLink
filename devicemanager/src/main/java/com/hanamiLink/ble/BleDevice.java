package com.hanamiLink.ble;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.HashMap;

public class BleDevice {
    private static final String TAG = BleDevice.class.getSimpleName();
    private String nameString; // 名称
    private String idString;  // UUID
    private long lastTimeReceiveData; // 上次接受数据时间
    private HashMap<String, Object> paramMap = new HashMap<>();
    private HashMap<String, Object> paramTempMap = new HashMap<>();
    private BluetoothGattCharacteristic characteristicRead = null; // 蓝牙设备特征 读
    private BluetoothGattCharacteristic characteristicWrite = null;// 蓝牙设备特征 写
    private BluetoothGattCharacteristic characteristicNotify = null;// 蓝牙设备特征 广播
    private DevStatus status;// 设备状态
    private BluetoothDevice device; // 设备类
    private BluetoothGatt bluetoothGatt;    // 蓝牙Gatt连接

    public BleDevice() {
        this.status = DevStatus.disable;
    }

    @SuppressLint("MissingPermission")
    public BleDevice(BluetoothGatt gatt) {
        this.status = DevStatus.disable;
        this.bluetoothGatt = gatt;
        this.device = gatt.getDevice();
        this.idString = this.device.getAddress();
        this.nameString = this.device.getName();
    }

    public BleDevice(HashMap<String, Object> _paramMap, String _idString) {
        this.status = DevStatus.disable;
        this.paramMap = _paramMap;
        this.idString = _idString;
    }

    @SuppressLint("MissingPermission")
    public void resumeDeviceWithBluetoothDevice(BluetoothDevice device) {
        this.device = device;
        this.idString = device.getAddress();
        this.nameString = device.getName();
    }

    public BluetoothGattCharacteristic getCharacteristicRead() {
        return this.characteristicRead;
    }

    public void setCharacteristicRead(BluetoothGattCharacteristic characteristicRead) {
        this.characteristicRead = characteristicRead;
    }

    public BluetoothGattCharacteristic getCharacteristicWrite() {
        return this.characteristicWrite;
    }

    public void setCharacteristicWrite(BluetoothGattCharacteristic characteristicWrite) {
        this.characteristicWrite = characteristicWrite;
    }

    public BluetoothGattCharacteristic getCharacteristicNotify() {
        return this.characteristicNotify;
    }

    public void setCharacteristicNotify(BluetoothGattCharacteristic characteristicNotify) {
        this.characteristicNotify = characteristicNotify;
    }

    public DevStatus getStatus() {
        return this.status;
    }

    public void setStatus(DevStatus status) {
        this.status = status;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getNameString() {
        return this.nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getIdString() {
        return this.idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public BluetoothGatt getBluetoothGatt() {
        return this.bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public void putIntParam(String key, int obj) {
        this.paramMap.put(key, obj);
    }

    public int getIntParam(String key, int defaultObj) {
        Integer obj = (Integer)this.paramMap.get(key);
        return obj != null ? obj : defaultObj;
    }

    public void putBooleanParam(String key, Boolean obj) {
        this.paramMap.put(key, obj);
    }

    public Boolean getBooleanParam(String key, Boolean defaultObj) {
        Boolean obj = (Boolean)this.paramMap.get(key);
        return obj != null ? obj : defaultObj;
    }

    public void putStringParam(String key, String obj) {
        this.paramMap.put(key, obj);
    }

    public String getStringParam(String key, String defaultObj) {
        String obj = (String)this.paramMap.get(key);
        return obj != null ? obj : defaultObj;
    }

    public void putObjectParam(String key, Object obj) {
        this.paramMap.put(key, obj);
    }

    public void putObjectParamTemp(String key, Object obj) {
        this.paramTempMap.put(key, obj);
    }

    public Object getObjectParam(String key) {
        return this.paramMap.get(key);
    }

    public Object getObjectParamTemp(String key) {
        return this.paramTempMap.get(key);
    }

    public long getLastTimeReceiveData() {
        return this.lastTimeReceiveData;
    }

    public void setLastTimeReceiveData(long lastTimeReceiveData) {
        this.lastTimeReceiveData = lastTimeReceiveData;
    }

    public HashMap<String, Object> getParamMap() {
        return this.paramMap;
    }

    public HashMap<String, Object> getParamTempMap() {
        return this.paramTempMap;
    }

    public long getIndex() {
        Long indexObj = (Long)this.paramMap.get("key_index");
        return indexObj == null ? 0L : indexObj;
    }

    public void setIndex(long index) {
        this.paramMap.put("key_index", index);
    }


}
