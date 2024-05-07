package com.hanamilink.activity;



import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hanamilink.R;
import com.hanamilink.databinding.ActivityBottomNavigationBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityBottomNavigationBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用 Data Binding 绑定底部导航栏相关的视图
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        // 将布局文件中的根视图设置为当前 activity 的内容视图
        setContentView(binding.getRoot());

        // 查找并获取底部导航栏的视图对象
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 创建用于配置顶部应用栏行为的 AppBarConfiguration 实例
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_eq, R.id.navigation_ble_manager)
                .build();

        // 获取与当前 activity 关联的导航控制器
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);

        // 配置底部导航栏与导航控制器之间的关联，确保正确处理导航操作
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

}
