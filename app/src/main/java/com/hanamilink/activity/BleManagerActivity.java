package com.hanamilink.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanamiLink.ble.BLEDeviceManager;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.eventbus.BleEventType;
import com.hanamiLink.eventbus.BleEventUtils;
import com.hanamiLink.utils.PermissionUtils;
import com.hanamiLink.utils.StatusBarUtil;
import com.hanamiLink.utils.ToastUtil;
import com.hanamilink.R;
import com.hanamilink.ble.BleManagerAdapter;
import com.hanamilink.bluetooth.adapter.DeviceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BleManagerActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = BleManagerActivity.class.getSimpleName();

    private static int REQUEST_ENABLE_BLUETOOTH = 1;//请求码

    private TextView scanDevices;//扫描设备
    private LinearLayout loadingLay;//加载布局
    private RecyclerView rv;//蓝牙设备展示列表

    DeviceAdapter mAdapter;//蓝牙设备适配器
    List<BleDevice> deviceList;//蓝牙数据

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Message msg) {
        if (msg.what == BleEventType.BLE_SCAN_START.toNumber()) {
            // 执行蓝牙扫描开始后的操作
            showDevicesData(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_manager);

        StatusBarUtil.StatusBarLightMode(this);//状态栏黑色字体
        initBLE();
        initView();//初始化控件

    }

    /**
     * 初始化控件
     */
    private void initView() {

        scanDevices = findViewById(R.id.scan_devices);
        rv = findViewById(R.id.rv);
        scanDevices.setOnClickListener(this);
    }

    /**
     * 动态权限申请
     */

    private void permissionsRequest() {
        EasyPermissions.requestPermissions(
                this,
                "请授予蓝牙权限以实现相关功能",
                REQUEST_ENABLE_BLUETOOTH,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 初始化蓝牙配置
     */
    private void initBLE() {
        // 检查权限
        final String[] rationale = {""};
        List<String> permissions = new ArrayList<String>() {{
            if (PermissionUtils.isAndroid12OrAbove()) {
                add(Manifest.permission.BLUETOOTH_SCAN);
                add(Manifest.permission.BLUETOOTH_CONNECT);
                rationale[0] += getString(R.string.permission_bluetooth_description);
            } else {
                add(Manifest.permission.ACCESS_FINE_LOCATION);
                add(Manifest.permission.ACCESS_COARSE_LOCATION);
                rationale[0] += getString(R.string.permission_location_description);
            }
        }};
        String[] permissionsArray = permissions.toArray(new String[0]);

        if (EasyPermissions.hasPermissions(this, permissionsArray)) {
            Log.v(TAG, "checkPermission passed");
            // 单例模式BLE初始化
            BLEDeviceManager.getInstance().init(this, new BleManagerAdapter());

            BLEDeviceManager.getInstance().readAllDeviceConnected();

            deviceList = BLEDeviceManager.getInstance().getAllUsedDevices();
            if (!deviceList.isEmpty()) {
                BLEDeviceManager.getInstance().setSelectedIdString(deviceList.get(0).getIdString());
                // 只有搜索到这个设备才能重连接
                BLEDeviceManager.getInstance().startBLEScan();
            }

            BLEDeviceManager.getInstance().startReconnectAuto();
            //BLEManager.getInstance().startConnectTimeoutTimer();
        } else {
            // 请求权限
            permissionsRequest();
        }
    }

    /**
     * 消息提示
     *
     * @param msg 消息内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 控件点击事件
     *
     * @param v 视图
     */
    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.scan_devices) {
            showMsg("扫描蓝牙");
            BLEDeviceManager.getInstance().startBLEScan();
            // 发布蓝牙扫描
            BleEventUtils.postEmptyMsg(BleEventType.BLE_SCAN_START.toNumber());
        }
    }

    /**
     * 结果返回
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                showMsg("蓝牙打开成功");
            } else {
                showMsg("蓝牙打开失败");
            }
        }
    }


    /**
     * 显示蓝牙设备信息
     *
     * @param context 上下文参数
     */
    private void showDevicesData(Context context) {
        deviceList = BLEDeviceManager.getInstance().getAllDevices();
        mAdapter = new DeviceAdapter(R.layout.item_device_list, deviceList);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);  // 确保适配器已经被设置到RecyclerView上
        mAdapter.notifyDataSetChanged();  // 在适配器被设置到RecyclerView上之后调用
    }


    /**
     * @param requestCode:
     * @param perms:
     * @return void
     * @description 当用户授予所需的权限后，该方法会展示一个提示消息，并在此之后调用initBLE()来初始化蓝牙配置。
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        ToastUtil.toast(this, this.getResources().getString(R.string.tip_Permission_Granted));
        initBLE();
    }
    /*
      若是在权限弹窗中，用户勾选了NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
      这时候，需要跳转到设置界面去，让用户手动开启。
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtil.toast(this, this.getResources().getString(R.string.tip_Permission_Denied));
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
