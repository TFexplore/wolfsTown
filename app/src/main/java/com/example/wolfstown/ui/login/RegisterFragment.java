package com.example.wolfstown.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wolfstown.R;
import com.example.wolfstown.ac.MainActivity;
import com.example.wolfstown.common.Utils;
import com.example.wolfstown.databinding.FragmentRegisterBinding;
import com.example.wolfstown.easyhttp.api.ApiRegist;
import com.example.wolfstown.easyhttp.model.HttpData;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.luck.picture.lib.utils.ToastUtils;
import com.tencent.mmkv.MMKV;


public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //点击事件注册
        clickInit();
    }
    void clickInit(){
        //注册
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });
        //登录跳转
        binding.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }
    void regist(){
       String name=binding.userName.getText().toString();
       String tel=binding.email.getText().toString();
       String pwd=binding.pwd.getText().toString();

        EasyHttp.post(getParentFragment())
                .api(new ApiRegist(pwd,tel,name))
                .request(new OnHttpListener<HttpData<ApiRegist.Bean>>() {
                    @Override
                    public void onSucceed(HttpData<ApiRegist.Bean> result) {
                        Log.d("http", "onSucceed: "+ Utils.getGson().toJson(result.getData().getToken()));
                        MMKV mmkv=MMKV.defaultMMKV();
                        if (!mmkv.encode("user",Utils.getGson().toJson(result.getData().getUser()))||
                                !mmkv.encode("token",result.getData().getToken().getToken())){

                            ToastUtils.showToast(requireActivity(),"用户信息保存失败，请重试");
                        }else ToastUtils.showToast(requireActivity(),"注册成功");

                        toMainActivity();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Log.d("http", "onFail: ");
                    }

                });


    }
    void toMainActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(requireContext(), MainActivity.class);
                requireActivity().startActivity(mainIntent);
                requireActivity().finish();
            }
        }, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
