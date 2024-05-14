package com.hanamilink.ui.ble;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hanamiLink.ble.BLEDeviceManager;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.utils.PermissionUtils;
import com.hanamiLink.utils.ToastUtil;
import com.hanamilink.R;
import com.hanamilink.data.adapter.BleManagerAdapter;
import com.hanamilink.data.adapter.DeviceAdapter;
import com.hanamilink.databinding.FragmentBleManagerBinding;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class BleManagerFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    private FragmentBleManagerBinding binding;

    private static final String TAG = BleManagerFragment.class.getSimpleName();

    private static int REQUEST_ENABLE_BLUETOOTH = 1;//请求码

    DeviceAdapter mAdapter;//蓝牙设备适配器
    List<BleDevice> deviceList;//蓝牙数据

    private BleManagerViewModel mBleManagerViewModel;
    /**
     * 在Fragment中创建并返回视图的方法。
     *
     * @param inflater           用于从XML布局文件中创建视图的布局填充器。
     * @param container          父视图的容器，如果有的话，可以用于确定新创建的视图应该附加到哪个父视图。
     * @param savedInstanceState 保存Fragment状态信息的Bundle对象，用于在Fragment重新创建时恢复状态。
     * @return 创建的Fragment视图对象。
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 创建 BleManagerViewModel 实例来处理 BLE 设备通信逻辑
        BleManagerViewModel bleManagerViewModel =
                new ViewModelProvider(this).get(BleManagerViewModel.class);
        // 使用 Data Binding 将 Fragment 的布局文件与视图关联起来
        binding = FragmentBleManagerBinding.inflate(inflater, container, false);

        mBleManagerViewModel = new ViewModelProvider(this).get(BleManagerViewModel.class);
        initView();
        // 返回根视图作为 Fragment 的视图
        return binding.getRoot();
    }


    /**
     * 初始化控件
     */
    private void initView() {

        binding.scanDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(requireContext(),"点击");
            }
        });
        // 蓝牙设备列表
        deviceList =  BLEDeviceManager.getInstance().getAllUsedDevices();
        if(deviceList != null){
            mAdapter = new DeviceAdapter(R.layout.item_device_list,deviceList);
            binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.rv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (position < deviceList.size()) {
                    BleDevice device = deviceList.get(position);
                    if (BLEDeviceManager.getInstance().connectDevice(device)){
                        ToastUtil.toast(requireContext(),"连接蓝牙成功"+position+device.getIdString());
                        BLEDeviceManager.getInstance().stopBLEScan();
                    }
                }
            });
        } else {
            // 没有蓝牙设备，不注册点击事件
            mAdapter = new DeviceAdapter(R.layout.item_device_list,null);
            binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.rv.setAdapter(mAdapter);
        }
    }

    /**
     * 显示蓝牙设备信息
     */

    private void showDevicesData() {
        deviceList = BLEDeviceManager.getInstance().getAllDevices();
        if(deviceList!=null){
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (position < deviceList.size()) {
                    BleDevice device = deviceList.get(position);
                    if (BLEDeviceManager.getInstance().connectDevice(device)){
                        ToastUtil.toast(requireContext(),"连接蓝牙成功"+position+device.getIdString());
                        BLEDeviceManager.getInstance().stopBLEScan();
                    }
                }
            });
        }
        mAdapter.setList(deviceList);
    }

    /**
     * 动态权限申请
     */

    @RequiresApi(api = Build.VERSION_CODES.S)
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

        if (EasyPermissions.hasPermissions(requireContext(), permissionsArray)) {
            Log.v(TAG, "checkPermission passed");
            // 单例模式BLE初始化
            BLEDeviceManager.getInstance().init(requireContext(), new BleManagerAdapter());

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionsRequest();
            }
        }
    }

    /**
     * 消息提示
     *
     * @param msg 消息内容
     */
    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * @param requestCode:
     * @param perms:
     * @return void
     * @description 当用户授予所需的权限后，该方法会展示一个提示消息，并在此之后调用initBLE()来初始化蓝牙配置。
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initBLE();
    }

    /*
      若是在权限弹窗中，用户勾选了NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
      这时候，需要跳转到设置界面去，让用户手动开启。
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
