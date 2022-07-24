package com.example.wolfstown.ac;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.wolfstown.R;
import com.example.wolfstown.databinding.ActivityLoginBinding;
import com.example.wolfstown.ac.base.BaseActivity;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //状态栏背景颜色
        MainActivity.setWindowStatusBarColor(this, this.getResources().getColor(R.color.login_top));
        //状态栏文字颜色随主题色变化
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_login_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }
}
