package com.hanamilink;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanamiLink.ble.BLEDeviceManager;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.utils.PermissionUtils;
import com.hanamiLink.utils.StatusBarUtil;
import com.hanamiLink.utils.ToastUtil;
import com.hanamilink.activity.BleManagerActivity;
import com.hanamilink.ble.BleManagerAdapter;
import com.hanamilink.bluetooth.adapter.DeviceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private Button btnNext;
    private Button sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNext = findViewById(R.id.button);
        sendData = findViewById(R.id.button1);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent对象，指定目标Activity的类名
                Intent intent = new Intent(MainActivity.this, BleManagerActivity.class);
                // 传递字符串参数
                intent.putExtra("message", "Hello from MainActivity!");
                // 启动目标Activity
                startActivity(intent);
            }
        });
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在主页显示
                String idString = BLEDeviceManager.getInstance().getSelectedIdString();
                BleDevice device = BLEDeviceManager.getInstance().getDeviceById(idString);
                byte data[] = new byte[]{0x12, 0x34};
                if(device != null)
                {
                    BLEDeviceManager.getInstance().sendData(device,data);
                }
                ToastUtil.toast(MainActivity.this,"发送数据"+data);

                //BLEDeviceManager.getInstance().sendData(BLEDeviceManager.getInstance().getDeviceById(BleManagerAdapter.key_idString), );

            }
        });
    }

}
