package com.hanamilink.ui.activity;



import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hanamiLink.utils.StatusBarUtil;
import com.hanamilink.R;
import com.hanamilink.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private ActivityBottomNavigationBinding binding;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.StatusBarLightMode(this);//状态栏黑色字体


        // 使用 Data Binding 绑定底部导航栏相关的视图
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // 将布局文件中的根视图设置为当前 activity 的内容视图
        setContentView(binding.getRoot());

        // 创建用于配置顶部应用栏行为的 AppBarConfiguration 实例
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_eq, R.id.navigation_ble_manager)
                .build();
        // 获取NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);

        NavigationUI.setupWithNavController(binding.navView, navController);

    }

}
