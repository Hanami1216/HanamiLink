package com.hanamiLink.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hanamiLink.eventbus.BleEventType;
import com.hanamiLink.eventbus.BleEventUtils;
import com.hanamiLink.utils.BlePrefUtil;
import com.hanamiLink.utils.BLEUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class BLEDeviceManager {
    private static final String TAG = BLEDeviceManager.class.getSimpleName();

    private static BLEDeviceManager instance;  // BLEDeviceManager的单例实例
    private Context mContext;  // 上下文对象
    private BleBaseAdapter bleBaseAdapter;  // 蓝牙基础适配器
    private BluetoothAdapter mBluetoothAdapter;  // 蓝牙适配器
    private BluetoothManager mBluetoothManager;  // 蓝牙管理器
    private Handler mBleMainHandler = null;  // 主线程Handler
    private static final int CONNECT_TIMEOUT = 6000;  // 连接超时时间
    private int deviceConnectCount = 1;  // 设备连接计数
    private Timer mConnectTimeoutTimer;  // 连接超时定时器
    private TimerTask mConnectTimeoutTask;  // 连接超时任务
    private Timer mConnectAutoTimer;  // 自动连接定时器
    private TimerTask mConnectAutoTimerTask;  // 自动连接任务
    public HashMap<String, BleDevice> deviceMapDiscover = new HashMap<>();  // 发现的设备映射表
    public HashMap<String, BleDevice> deviceMapConnected = new HashMap<>();  // 已连接的设备映射表
    BluetoothAdapter.LeScanCallback mLeScanCallback;  // 蓝牙扫描回调
    private BluetoothLeScanner mLeScanner = null;  // 蓝牙扫描器
    private ScanCallback mBleScanCallback = null;  // 蓝牙扫描回调
    public boolean isBleScanning ;  // 是否正在进行蓝牙扫描
    private String selectedIdString;  // 选定的设备ID字符串
    private Timer delaySendTimer;  // 延迟发送定时器
    private TimerTask delaySendTimerTask;  // 延迟发送任务
    private LinkedBlockingQueue<byte[]> delaySendQueue;  // 延迟发送队列
    private LinkedBlockingQueue<String> delaySendIdStringQueue;  // 延迟发送设备ID字符串队列
    private final int delaySendPeriod = 300;  // 延迟发送周期
    private final Comparator<BleDevice> deviceComparator = (lhs, rhs) -> (int) (lhs.getIndex() - rhs.getIndex());  // 设备比较器
    Runnable disconnectRunnable = null;  // 断开连接的Runnable任务

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * 当连接状态发生改变时调用的方法。
         *
         * @param gatt    BluetoothGatt对象，表示与远程设备的GATT连接。
         * @param status  连接状态的状态码。
         * @param newState 新的连接状态。
         */
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            String address;
            BleDevice device;
            if (newState == 2) {
                Log.e(BLEDeviceManager.TAG, "Connected to GATT server.");
                address = gatt.getDevice().getAddress();
                device = BLEDeviceManager.this.deviceMapConnected.get(address);
                if (device != null) {
                    device.setBluetoothGatt(gatt);
                    device.setDevice(gatt.getDevice());
                    device.setStatus(DevStatus.connected);
                } else {
                    device = new BleDevice(gatt);
                    device.setStatus(DevStatus.connected);
                    BLEDeviceManager.this.deviceMapConnected.put(device.getIdString(), device);
                    if (BLEDeviceManager.this.bleBaseAdapter != null) {
                        BLEDeviceManager.this.bleBaseAdapter.managerDidAddNewDevice(device);
                    }
                }

                BLEDeviceManager.this.deviceMapDiscover.remove(device.getIdString());
                if (BLEDeviceManager.this.bleBaseAdapter != null) {
                    BLEDeviceManager.this.bleBaseAdapter.managerDidConnect(device);
                }

                BLEDeviceManager.this.saveAllDeviceConnected();
                BLEDeviceManager.this.mBleMainHandler.post(gatt::discoverServices);
            } else if (newState == 0) {
                Log.e(BLEDeviceManager.TAG, "Disconnected from GATT server.");
                address = gatt.getDevice().getAddress();
                BLEDeviceManager.this.deviceMapDiscover.remove(address);
                device = BLEDeviceManager.this.deviceMapConnected.get(address);
                if (device != null) {
                    device.setStatus(DevStatus.disconnect);
                    device.setBluetoothGatt(null);
                }

                gatt.close();
                if (BLEDeviceManager.this.bleBaseAdapter != null && device != null) {
                    BLEDeviceManager.this.bleBaseAdapter.managerDidDisconnect(device);
                }
            }
        }

        /**
         * 当服务发现完成时调用的方法。
         *
         * @param gatt   BluetoothGatt对象，表示与远程设备的GATT连接。
         * @param status 服务发现的状态码。
         */
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                String address = gatt.getDevice().getAddress();
                BLEDeviceManager.this.displayGattServices(BLEDeviceManager.this.deviceMapConnected.get(address), gatt);
            }
        }

        /**
         * 当特征读取完成时调用的方法。
         *
         * @param gatt           BluetoothGatt对象，表示与远程设备的GATT连接。
         * @param characteristic 被读取的特征。
         * @param status         读取操作的状态码。
         */
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        /**
         * 当特征值发生改变时调用的方法。
         *
         * @param gatt           BluetoothGatt对象，表示与远程设备的GATT连接。
         * @param characteristic 改变的特征。
         */
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String idString = gatt.getDevice().getAddress();
            if (BLEDeviceManager.this.bleBaseAdapter != null) {
                BleDevice device = BLEDeviceManager.this.deviceMapConnected.get(idString);
                if (device != null) {
                    BLEDeviceManager.this.bleBaseAdapter.deviceReceiveDatawithDevice(device, characteristic.getValue());
                }
            }
        }
    };


    private BLEDeviceManager() {
    }

    /**
     * @param :
     * @return BLEDeviceManager
     * @description 单例模式,获取实例
     */
    public static BLEDeviceManager getInstance() {
        if (instance == null) { // 如果实例为空
            Class<BLEDeviceManager> var0 = BLEDeviceManager.class;
            synchronized (BLEDeviceManager.class) { // 同步锁，确保线程安全
                if (instance == null) { // 双重检查，避免多个线程同时创建实例
                    instance = new BLEDeviceManager(); // 创建实例
                }
            }
        }

        return instance; // 返回实例
    }

    /**
     * 开始自动重连
     */
    public void startReconnectAuto() {
        // 停止之前的自动重连任务
        this.stopReconnectAuto();

        // 创建新的定时任务
        this.mConnectAutoTimerTask = new TimerTask() {
            public void run() {
                // 如果蓝牙适配器未处于扫描状态或者BLE设备管理器未处于扫描状态
                if (!BLEDeviceManager.this.mBluetoothManager.getAdapter().isDiscovering() || !BLEDeviceManager.this.isBleScanning) {
                    // 获取已连接的BLE设备集合
                    Collection<BleDevice> devices = BLEDeviceManager.this.deviceMapConnected.values();
                    // 移除消息队列中的重连消息
                    BLEDeviceManager.this.mBleMainHandler.removeMessages(3);

                    // 遍历已连接的BLE设备
                    for (BleDevice device : devices) {
                        // 如果设备状态为断开连接
                        if (device.getStatus() == DevStatus.disconnect) {
                            // 创建消息对象
                            Message message = new Message();
                            message.what = 3;
                            message.obj = device.getIdString();
                            // 发送重连消息
                            BLEDeviceManager.this.mBleMainHandler.sendMessage(message);
                        }
                    }
                }
            }
        };

        // 创建定时器并启动定时任务
        this.mConnectAutoTimer = new Timer();
        this.mConnectAutoTimer.schedule(this.mConnectAutoTimerTask, 1000L, 3000L);
    }

    /**
     * 停止自动重连
     */
    public void stopReconnectAuto() {
        // 取消定时器
        if (this.mConnectAutoTimer != null) {
            this.mConnectAutoTimer.cancel();
            this.mConnectAutoTimer = null;
        }

        // 取消定时任务
        if (this.mConnectAutoTimerTask != null) {
            this.mConnectAutoTimerTask.cancel();
            this.mConnectAutoTimerTask = null;
        }
    }

    /**
     * 开始连接超时定时器
     */
    public void startConnectTimeoutTimer() {
        // 停止之前的连接超时定时器
        this.stopConnectTimeoutTimer();

        // 创建一个新的定时任务
        this.mConnectTimeoutTask = new TimerTask() {
            public void run() {
                // 检查蓝牙适配器是否正在扫描，并且是否正在进行BLE扫描
                if (!BLEDeviceManager.this.mBluetoothManager.getAdapter().isDiscovering() || !BLEDeviceManager.this.isBleScanning) {
                    // 获取已连接的BLE设备列表
                    Collection<BleDevice> devices = BLEDeviceManager.this.deviceMapConnected.values();

                    // 遍历设备列表
                    for (BleDevice device : devices) {
                        // 检查设备状态是否为已连接
                        if (device.getStatus() == DevStatus.connected) {
                            long now = System.currentTimeMillis();
                            // 检查设备最后接收数据的时间是否超过连接超时时间
                            if (now - device.getLastTimeReceiveData() > (long) BLEDeviceManager.CONNECT_TIMEOUT) {
                                // 断开设备连接
                                BLEDeviceManager.this.disconnectDeviceByIDString(device.getIdString());
                                // 更新设备状态为断开
                                device.setStatus(DevStatus.disconnect);
                            }
                        }
                    }
                }
            }
        };

        // 创建一个新的定时器，并将定时任务添加到定时器中
        this.mConnectTimeoutTimer = new Timer();
        this.mConnectTimeoutTimer.schedule(this.mConnectTimeoutTask, 1000L, 6000L);
    }

    /**
     * 停止连接超时定时器
     */
    public void stopConnectTimeoutTimer() {
        // 取消定时器
        if (this.mConnectTimeoutTimer != null) {
            this.mConnectTimeoutTimer.cancel();
            this.mConnectTimeoutTimer = null;
        }

        // 取消定时任务
        if (this.mConnectTimeoutTask != null) {
            this.mConnectTimeoutTask.cancel();
            this.mConnectTimeoutTask = null;
        }
    }

    /**
     * 初始化BLE设备管理器。
     *
     * @param ctx 上下文对象
     * @param _bleBaseAdapter BLE基础适配器
     * @return 初始化是否成功的布尔值
     */
    public boolean init(Context ctx, BleBaseAdapter _bleBaseAdapter) {
        if (ctx == null) {
            return false;
        } else {
            this.mContext = ctx.getApplicationContext();
            this.bleBaseAdapter = _bleBaseAdapter;

            if (this.mBluetoothManager == null) {
                if (this.mContext == null) {
                    return false;
                }
                // 初始化BluetoothManager
                this.mBluetoothManager = (BluetoothManager) this.mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                if (this.mBluetoothManager == null) {
                    //无法初始化BluetoothManager
                    return false;
                }
            }
            //获取适配器
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
            if (this.mBluetoothAdapter == null) {
                //无法获取BluetoothAdapter
                return false;
            } else {
                // 启用蓝牙适配器
                if (this.mBluetoothAdapter.enable()) {
                    this.mLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
                    if (this.mLeScanner == null) {
                        Log.e(TAG, "mLeScanner为null，尝试重新获取。");
                        this.mLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
                    }
                }

                // 设置设备连接计数
                if (this.bleBaseAdapter != null) {
                    this.deviceConnectCount = this.bleBaseAdapter.managerDeviceConnectCount();
                    if (this.deviceConnectCount <= 0) {
                        this.deviceConnectCount = 1;
                    }
                }

                // 处理低版本设备的扫描结果
                this.mLeScanCallback = (device, rssi, scanRecord) -> {
                    Log.e(BLEDeviceManager.TAG, "onLeScan搜索到的设备:" + device.getName() + "  address:" + device.getAddress() + " uuids:" + Arrays.toString(device.getUuids()));
                    ParcelUuid[] uuids = device.getUuids();
                    if (uuids == null) {
                        BLEDeviceManager.this.scanResult(device, scanRecord);
                    }
                };

                // 处理高版本设备的扫描结果
                this.mBleScanCallback = new ScanCallback() {
                    public void onScanResult(int callbackType, ScanResult result) {
                        if (result != null) {
                            BluetoothDevice device = result.getDevice();
                            if (device != null && result.getScanRecord() != null) {
                                Log.e(BLEDeviceManager.TAG,
                                "mBleScanCallback.onScanResult搜索到设备:"
                                        + device.getAlias()
                                        + "  address:" + device.getAddress()
                                        + "  uuids:" + Arrays.toString(device.getUuids()));
                                BLEDeviceManager.this.scanResult(device, result.getScanRecord().getBytes());
                                BleEventUtils.postEmptyMsg(BleEventType.BLE_SCAN_ING.toNumber());
                            }
                        }
                    }
                };

                // 设置主线程Handler
                this.mBleMainHandler = new Handler(this.mContext.getMainLooper()) {
                    public void handleMessage(@NonNull Message msg) {
                        String idString4;
                        switch (msg.what) {
                            case 1:
                                Log.e(BLEDeviceManager.TAG, "开始搜索设备......");
                                if (VERSION.SDK_INT >= 21) {
                                    if (BLEDeviceManager.this.mBluetoothAdapter.enable()) {
                                        BLEDeviceManager.this.mLeScanner = BLEDeviceManager.this.mBluetoothAdapter.getBluetoothLeScanner();
                                    }
                                    if (BLEDeviceManager.this.mLeScanner != null) {
                                        if (BLEDeviceManager.this.bleBaseAdapter != null) {
                                            BLEDeviceManager.this.mLeScanner.startScan(BLEDeviceManager.this.bleBaseAdapter.managerWithDeviceScanFilters(), (new ScanSettings.Builder()).build(), BLEDeviceManager.this.mBleScanCallback);
                                        } else {
                                            BLEDeviceManager.this.mLeScanner.startScan(BLEDeviceManager.this.mBleScanCallback);
                                        }
                                    }
                                } else if (BLEDeviceManager.this.bleBaseAdapter != null) {
                                    BLEDeviceManager.this.mBluetoothAdapter.startLeScan(BLEDeviceManager.this.bleBaseAdapter.managerWithDeviceUUIDArray(), BLEDeviceManager.this.mLeScanCallback);
                                } else {
                                    BLEDeviceManager.this.mBluetoothAdapter.startLeScan(BLEDeviceManager.this.mLeScanCallback);
                                }
                                break;
                            case 2:
                                BLEDeviceManager.this.stopBLEScan();
                                break;
                            case 3:
                                idString4 = (String) msg.obj;
                                if (idString4 != null) {
                                    BleDevice device = BLEDeviceManager.this.getDeviceById(idString4);
                                    if (device == null || device.getDevice() == null) {
                                        return;
                                    }
                                    if (!BLEDeviceManager.this.connectDevice(device)) {
                                        device.setStatus(DevStatus.disconnect);
                                    }
                                }
                            case 4:
                                idString4 = (String) msg.obj;
                                byte[] data = msg.getData().getByteArray("data");
                                if (idString4 != null) {
                                    BleDevice devicex = BLEDeviceManager.this.getDeviceById(idString4);
                                    if (devicex == null || devicex.getDevice() == null) {
                                        return;
                                    }
                                    BLEDeviceManager.this.sendData(devicex, data);
                                }
                        }
                    }
                };

                return true;
            }
        }
    }


    private void scanResult(BluetoothDevice device, byte[] scanRecord) {
        String deviceName = device.getName();
        if (this.bleBaseAdapter == null || this.bleBaseAdapter != null && this.bleBaseAdapter.managerWithScanFilter(device, scanRecord)) {
            // 检查蓝牙适配器是否为空，或者满足特定的扫描过滤条件
            String idString = device.getAddress();
            BleDevice bleDevice;
            if (this.deviceMapConnected.containsKey(idString)) {
                // 如果已连接设备列表中包含该设备地址，则更新已连接设备的状态
                bleDevice = this.deviceMapConnected.get(idString);
                bleDevice.resumeDeviceWithBluetoothDevice(device);
                bleDevice.setStatus(DevStatus.disconnect);
            } else {
                // 如果已连接设备列表中不包含该设备地址，则创建一个新的设备对象
                bleDevice = new BleDevice();
                bleDevice.setDevice(device);
                bleDevice.setNameString(device.getName());
                bleDevice.setIdString(device.getAddress());
                bleDevice.setStatus(DevStatus.Unknown);
                //添加进入deviceMapDiscover列表中
                this.deviceMapDiscover.put(bleDevice.getIdString(), bleDevice);

                if (this.bleBaseAdapter != null) {
                    this.bleBaseAdapter.managerChangeNameForDisplay(bleDevice);
                }
            }

            if (this.bleBaseAdapter != null) {
                this.bleBaseAdapter.managerDiscoverDeviceChange();
            }
        }
    }

    /**
     * 检查蓝牙是否启用
     *
     * @return 如果蓝牙已启用，则返回true；否则返回false
     */
    public boolean isBluetoothEnable() {
        if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        }

        if (this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled()) {
            if (this.bleBaseAdapter != null) {
                this.bleBaseAdapter.managerIsBluetoothEnable(true);
                return true;
            }
        } else if (this.bleBaseAdapter != null) {
            this.bleBaseAdapter.managerIsBluetoothEnable(false);
            return false;
        }

        return false;
    }

    public void startBLEScan() {
        if (!this.isBLESupported()) {
            this.isBleScanning = false;
            //Log.e(TAG, "该设备不支持BLE");
        } else if (this.isBluetoothEnable()) {//判断蓝牙是否已启用
            //判断`mBluetoothAdapter`是否为空、是否正在进行蓝牙设备的发现以及是否已经在进行BLE扫描
            if (this.mBluetoothAdapter != null && !this.mBluetoothAdapter.isDiscovering() && !this.isBleScanning) {
                if (this.bleBaseAdapter != null) {
                    // 通知eventbus扫描蓝牙
                    this.bleBaseAdapter.managerStartScan();
                }
                this.isBleScanning = true;
                // 清空扫描设备
                this.deviceMapDiscover.clear();
                //遍历deviceMapConnected
                Collection<BleDevice> devices = this.deviceMapConnected.values();
                Iterator devicesIterator = devices.iterator();

                while (devicesIterator.hasNext()) {
                    BleDevice device = (BleDevice) devicesIterator.next();
                    if (device.getStatus() == DevStatus.disconnect) {
                        device.setStatus(DevStatus.disable);
                    }
                }

                this.mBleMainHandler.removeMessages(1);
                this.mBleMainHandler.sendEmptyMessage(1);
                this.mBleMainHandler.removeMessages(2);
                //扫描周期
                int SCAN_PERIOD = 30000;
                //
                this.mBleMainHandler.sendEmptyMessageDelayed(2, SCAN_PERIOD);
            }

        }
    }

    public void stopBLEScan() {
        if (this.isBluetoothEnable()) {
            if (this.mBluetoothAdapter != null && (this.mBluetoothAdapter.isDiscovering() || this.isBleScanning)) {
                this.mLeScanner.stopScan(this.mBleScanCallback);

                this.isBleScanning = false;
                Log.e(TAG, "停止搜索设备......");
                if (this.bleBaseAdapter != null) {
                    this.bleBaseAdapter.managerStopScan();
                }
            }

        }
    }

    public BleDevice getDeviceById(String idString) {
        if (idString == null) {
            return null;
        } else {
            BleDevice device = null;
            device = this.deviceMapConnected.get(idString);
            if (device == null) {
                device = this.deviceMapDiscover.get(idString);
            }

            return device;
        }
    }

    public List<BleDevice> getAllDevices() {
        //BLEDeviceManager.this.startBLEScan();
        List<BleDevice> list = this.getAllUsedDevices();
        list.addAll(this.getScanDevices());
        return list;
    }

    public List<BleDevice> getAllUsedDevices() {
        List<BleDevice> list = new ArrayList();
        Collection<BleDevice> cols = this.deviceMapConnected.values();
        list.addAll(cols);
        Collections.sort(list, this.deviceComparator);
        return list;
    }

    /**
     * @param :
     * @return List<BleDevice>
     * @description 获取蓝牙扫描数据
     */
    public List<BleDevice> getScanDevices() {
        List<BleDevice> list = new ArrayList<>();
        Collection<BleDevice> cols = this.deviceMapDiscover.values();
        list.addAll(cols);
        return list;
    }

    public boolean isBLESupported() {
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this.mContext, "your device is not blesupported", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void connectDeviceByIDString(String idString) {
        if (this.isBluetoothEnable()) {
            BleDevice device = this.getDeviceById(idString);
            if (device != null && device.getStatus() != DevStatus.connecting) {
                this.mBleMainHandler.removeMessages(3);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = idString;
                this.mBleMainHandler.sendMessage(msg);
            }

        }
    }

    public void disconnectDeviceByIDString(String idString) {
        if (this.isBluetoothEnable()) {
            final BleDevice device = this.deviceMapConnected.get(idString);
            if (device != null && device.getBluetoothGatt() != null) {
                this.mBleMainHandler.postDelayed(new Runnable() {
                    public void run() {
                        device.getBluetoothGatt().disconnect();
                    }
                }, 1000L);
            }

        }
    }

    public void disconnectAllDevice() {
        if (this.isBluetoothEnable()) {
            List<BleDevice> list = new ArrayList();
            list.addAll(this.deviceMapConnected.values());
            Iterator var2 = list.iterator();

            while (var2.hasNext()) {
                BleDevice device = (BleDevice) var2.next();
                String idString = device.getIdString();
                this.disconnectDeviceByIDString(idString);
            }

            list.clear();
            list = null;
        }
    }

    public void disconnectDeviceAndRemoveByIDString(String idString) {
        if (this.isBluetoothEnable()) {
            if (this.bleBaseAdapter != null) {
                this.bleBaseAdapter.managerReadyRemoveDevice(idString);
            }

            this.disconnectDeviceByIDString(idString);
            if (this.bleBaseAdapter != null) {
                this.bleBaseAdapter.managerDidRemoveDevice();
            }

            this.deviceMapConnected.remove(idString);
            this.saveAllDeviceConnected();
            Log.e(TAG, "deviceMapConnected.size:" + this.deviceMapConnected.size());
        }
    }

    public boolean connectDevice(BleDevice device) {
        if (!this.isBluetoothEnable()) {
            return false;
        } else {
            Log.e(TAG, "connectDevice:" + device.getIdString());
            if (this.isBleScanning || this.mBluetoothAdapter.isDiscovering()) {
                this.stopBLEScan();
            }

            if (device != null && device.getDevice() != null) {
                if (device.getStatus() == DevStatus.connecting) {
                    Log.e(TAG, device.getIdString() + "  connecting , cancel");
                } else if (device.getStatus() == DevStatus.disconnect || device.getStatus() == DevStatus.Unknown) {
                    BluetoothGatt bluetoothGatt;
                    BluetoothDevice btDevice;
                    if (this.deviceMapConnected.containsKey(device.getIdString())) {
                        device.setStatus(DevStatus.connecting);
                        bluetoothGatt = device.getBluetoothGatt();
                        if (bluetoothGatt != null) {
                            return bluetoothGatt.connect();
                        }

                        btDevice = this.mBluetoothAdapter.getRemoteDevice(device.getIdString());
                        if (btDevice == null) {
                            Log.w(TAG, "Device not found.  Unable to connect.");
                            return false;
                        }

                        bluetoothGatt = btDevice.connectGatt(this.mContext, false, this.mGattCallback);
                        if (bluetoothGatt != null) {
                            device.setBluetoothGatt(bluetoothGatt);
                        } else {
                            device.setStatus(DevStatus.disconnect);
                        }

                        Log.e(TAG, "connect device");
                    } else {
                        if (this.deviceMapConnected.size() >= this.deviceConnectCount && this.deviceConnectCount != 1) {
                            if (this.bleBaseAdapter != null) {
                                this.bleBaseAdapter.managerDidWarnCountOut();
                            }

                            return false;
                        }

                        if (this.deviceConnectCount == 1 && this.deviceMapConnected.size() == 1) {
                            BleDevice deviceExist = this.getAllUsedDevices().get(0);
                            if (deviceExist != null && !deviceExist.getIdString().equals(device.getIdString())) {
                                this.disconnectDeviceAndRemoveByIDString(deviceExist.getIdString());
                            }
                        }

                        device.setStatus(DevStatus.connecting);
                        this.deviceMapConnected.put(device.getIdString(), device);
                        if (this.bleBaseAdapter != null) {
                            this.bleBaseAdapter.managerDidAddNewDevice(device);
                        }

                        bluetoothGatt = device.getBluetoothGatt();
                        if (bluetoothGatt != null) {
                            return bluetoothGatt.connect();
                        }

                        btDevice = this.mBluetoothAdapter.getRemoteDevice(device.getIdString());
                        if (btDevice == null) {
                            Log.w(TAG, "Device not found.  Unable to connect.");
                            return false;
                        }

                        bluetoothGatt = btDevice.connectGatt(this.mContext, false, this.mGattCallback);
                        if (bluetoothGatt != null) {
                            device.setBluetoothGatt(bluetoothGatt);
                        } else {
                            device.setStatus(DevStatus.disconnect);
                        }

                        Log.e(TAG, "connect device");
                    }
                }
            }

            return true;
        }
    }

    public void sendData(BleDevice device, byte[] data) {
        if (this.isBluetoothEnable()) {
            if (device != null && data != null && device.getCharacteristicWrite() != null && device.getBluetoothGatt() != null) {
                device.getCharacteristicWrite().setValue(data);
                device.getBluetoothGatt().writeCharacteristic(device.getCharacteristicWrite());
                Log.e(TAG, "send data:" + BLEUtils.bytesToHexString(data));
            }

        }
    }

    public void sendDataDelay(BleDevice device, byte[] data) {
        if (this.isBluetoothEnable()) {
            if (this.delaySendQueue == null) {
                this.delaySendQueue = new LinkedBlockingQueue<>();
            }

            if (this.delaySendIdStringQueue == null) {
                this.delaySendIdStringQueue = new LinkedBlockingQueue();
            }

            if (this.delaySendQueue.size() != this.delaySendIdStringQueue.size()) {
                this.delaySendQueue.clear();
                this.delaySendIdStringQueue.clear();
            }

            this.delaySendQueue.add(data);
            this.delaySendIdStringQueue.add(device.getIdString());
            if (this.delaySendTimerTask == null || this.delaySendTimer == null) {
                this.stopDelaySendTimer();
                this.delaySendTimerTask = new TimerTask() {
                    public void run() {
                        if (BLEDeviceManager.this.delaySendQueue != null && !BLEDeviceManager.this.delaySendQueue.isEmpty()) {
                            BleDevice deviceDelay = BLEDeviceManager.this.getDeviceById(BLEDeviceManager.this.delaySendIdStringQueue.poll());
                            byte[] data = BLEDeviceManager.this.delaySendQueue.poll();
                            if (deviceDelay != null && data != null) {
                                Message msg = new Message();
                                msg.what = 4;
                                msg.obj = deviceDelay.getIdString();
                                Bundle dataB = new Bundle();
                                dataB.putByteArray("data", data);
                                msg.setData(dataB);
                                BLEDeviceManager.this.mBleMainHandler.sendMessage(msg);
                            }
                        }

                        if (BLEDeviceManager.this.delaySendQueue == null || BLEDeviceManager.this.delaySendIdStringQueue == null || BLEDeviceManager.this.delaySendQueue.isEmpty() || BLEDeviceManager.this.delaySendIdStringQueue.isEmpty()) {
                            BLEDeviceManager.this.stopDelaySendTimer();
                        }

                    }
                };
                this.delaySendTimer = new Timer();
                this.delaySendTimer.schedule(this.delaySendTimerTask, this.delaySendPeriod, this.delaySendPeriod);
            }

        }
    }

    public void stopDelaySendTimer() {
        if (this.delaySendTimerTask != null) {
            this.delaySendTimerTask.cancel();
            this.delaySendTimerTask = null;
        }

        if (this.delaySendTimer != null) {
            this.delaySendTimer.cancel();
            this.delaySendTimer = null;
        }

    }

    private void setCharacteristicNotification(BleDevice device, boolean enabled, BluetoothGatt gatt) {
        if (this.mBluetoothAdapter != null && device.getBluetoothGatt() != null) {
            gatt.setCharacteristicNotification(device.getCharacteristicNotify(), enabled);
            Log.w(TAG, "setCharacteristicNotification.......");
            List<BluetoothGattDescriptor> descriptors = device.getCharacteristicNotify().getDescriptors();
            Iterator var5 = descriptors.iterator();

            while (var5.hasNext()) {
                BluetoothGattDescriptor dp = (BluetoothGattDescriptor) var5.next();
                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                device.getBluetoothGatt().writeDescriptor(dp);
                Log.w(TAG, "setCharacteristicNotification  successful.......");
            }

        } else {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
    }

    private void displayGattServices(BleDevice device, BluetoothGatt gatt) {
        if (device != null && device.getBluetoothGatt() != null) {
            List<BluetoothGattService> gattServices = device.getBluetoothGatt().getServices();
            device.setCharacteristicNotify(null);
            device.setCharacteristicWrite(null);
            device.setCharacteristicRead(null);
            Iterator var4 = gattServices.iterator();

            while (var4.hasNext()) {
                BluetoothGattService gattService = (BluetoothGattService) var4.next();
                int type = gattService.getType();
                Log.e(TAG, "-->service includedServices size:" + gattService.getIncludedServices().size());
                Log.e(TAG, "-->service uuid:" + gattService.getUuid());
                if (gattService.getUuid() != null && this.bleBaseAdapter.managerWithServiceUUIDPrefixString() != null && gattService.getUuid().toString().toLowerCase().startsWith(this.bleBaseAdapter.managerWithServiceUUIDPrefixString())) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                    Iterator var8 = gattCharacteristics.iterator();

                    while (var8.hasNext()) {
                        BluetoothGattCharacteristic gattCharacteristic = (BluetoothGattCharacteristic) var8.next();
                        int permission = gattCharacteristic.getPermissions();
                        int property = gattCharacteristic.getProperties();
                        int writeType = gattCharacteristic.getWriteType();
                        Log.e(TAG, "-->Characteristic uuid:" + gattCharacteristic.getUuid());
                        Log.e(TAG, "-->Characteristic DescPermission:" + BLEUtils.getDescPermission(property));
                        Log.e(TAG, "-->Characteristic permission:" + BLEUtils.getCharPermission(permission));
                        Log.e(TAG, "-->Characteristic property:" + BLEUtils.getCharPropertie(property));
                        if (gattCharacteristic.getUuid() != null && this.bleBaseAdapter != null && gattCharacteristic.getUuid().toString().startsWith(this.bleBaseAdapter.deviceUUID4CharacteristicNotify())) {
                            device.setCharacteristicNotify(gattCharacteristic);
                            this.setCharacteristicNotification(device, true, gatt);
                        }

                        if (gattCharacteristic.getUuid() != null && this.bleBaseAdapter != null && gattCharacteristic.getUuid().toString().startsWith(this.bleBaseAdapter.deviceUUID4CharacteristicWrite())) {
                            gattCharacteristic.setWriteType(2);
                            device.setCharacteristicWrite(gattCharacteristic);
                        }

                        if (device.getCharacteristicNotify() != null && device.getCharacteristicWrite() != null) {
                            Log.e(TAG, "设备已获取所有Characteristic");
                            break;
                        }
                    }

                    if (device.getCharacteristicNotify() != null && device.getCharacteristicWrite() != null) {
                        Log.e(TAG, "发现可用的设备");
                        if (this.bleBaseAdapter != null) {
                            this.bleBaseAdapter.managerDidReadyWriteAndNotify(device);
                        }
                        break;
                    }
                }
            }

        }
    }

    public void destroy() {
        this.stopDelaySendTimer();
        this.stopConnectTimeoutTimer();
        this.stopReconnectAuto();
        if (this.delaySendQueue != null) {
            this.delaySendQueue.clear();
            this.delaySendQueue = null;
        }

        if (this.delaySendIdStringQueue != null) {
            this.delaySendIdStringQueue.clear();
            this.delaySendIdStringQueue = null;
        }

        if (this.mBleMainHandler != null && this.disconnectRunnable != null) {
            this.mBleMainHandler.removeCallbacks(this.disconnectRunnable);
        }

        this.disconnectAllDevice();
        this.deviceMapConnected.clear();
        this.deviceMapDiscover.clear();
        instance = null;
        this.mContext = null;
    }

    public void saveAllDeviceConnected() {
        HashMap<String, HashMap<String, Object>> deviceMap = new HashMap();
        Set<String> keyIt = this.deviceMapConnected.keySet();
        Iterator var3 = keyIt.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            BleDevice dev = this.deviceMapConnected.get(key);
            deviceMap.put(key, dev.getParamMap());
        }

        try {
            BlePrefUtil.saveData(this.mContext, this.mContext.getPackageName(), "ble.device.connected", deviceMap);
        } catch (Exception var6) {
            Log.e(TAG, var6.getMessage(), var6);
        }

    }

    public void readAllDeviceConnected() {
        HashMap<String, HashMap<String, Object>> deviceMap = BlePrefUtil.getData(this.mContext, this.mContext.getPackageName(), "ble.device.connected");
        if (deviceMap != null && deviceMap.size() > 0) {
            Set<String> devSet = deviceMap.keySet();
            Iterator var3 = devSet.iterator();
            do {
                String idString;
                HashMap pMap;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    idString = (String) var3.next();
                    pMap = deviceMap.get(idString);
                } while (this.deviceMapConnected.containsKey(idString));

                HashMap<String, Object> paramMap = new HashMap();
                Set<String> keySet = pMap.keySet();
                Iterator var8 = keySet.iterator();

                while (var8.hasNext()) {
                    String key = (String) var8.next();
                    Object object = pMap.get(key);
                    if (object != null) {
                        paramMap.put(key, object);
                    }
                }

                BleDevice device = new BleDevice(paramMap, idString);
                device.setStatus(DevStatus.disable);
                this.deviceMapConnected.put(idString, device);
            } while (this.deviceConnectCount != 1);
        }

    }

    public String getSelectedIdString() {
        return this.selectedIdString;
    }

    public void setSelectedIdString(String selectedIdString) {
        this.selectedIdString = selectedIdString;
    }



}
