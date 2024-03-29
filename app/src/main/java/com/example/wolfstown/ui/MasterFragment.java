package com.example.wolfstown.ui;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.wolfstown.R;

import com.example.wolfstown.ac.LoginActivity;
import com.example.wolfstown.ac.MainActivity;
import com.example.wolfstown.common.Utils;
import com.example.wolfstown.databinding.FragmentMasterBinding;
import com.example.wolfstown.modle.User;
import com.example.wolfstown.ui.community.listPager.MyPageAdapter;
import com.example.wolfstown.ui.community.listPager.ListFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import com.luck.picture.lib.utils.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class MasterFragment extends Fragment {

    MasterViewModel viewModel;

    private FragmentMasterBinding binding;

    ArrayList<Fragment> fragmentList = new ArrayList<>();

    ViewPager2 viewPager;

    MyPageAdapter myPageAdapter;

    TabLayout tabLayout;

    public static boolean isVisible=false;

    int itemSelected=0;
    List<String> list;

    NavigationMenuView navigationMenuView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setWindowStatusBarColor(getActivity(), getResources().getColor(R.color.cell));//有效
        //MainActivity.setWindowStatusBarColor(requireActivity(),R.color.cell);//无效
        binding = FragmentMasterBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel=new ViewModelProvider(this).get(MasterViewModel.class);

        clickInit();

        leftViewInit();
        HeaderLayoutInit();
        pagerInit();
        useBanner();
    }
    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    void clickInit(){
        binding.content.imCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.configureFragment);
            }
        });
        binding.content.imEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.enterFragment);
            }
        });
        binding.content.imMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.personFragment);
            }
        });
        //发帖按钮
        binding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(),"点击了",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Utils.getApp(), EditActivity.class));
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.editFragment);
            }
        });
        //菜单展开
        binding.imDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
                Log.d(TAG, "onClick: open");
            }
        });

        //展开监听
        binding.content.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //通过日志得出活动启动是两次，由于之前有setExpanded所以三次
                Log.d("启动活动调用监听次数","几次:"+verticalOffset);
                if(!isVisible&&binding.content.appbar.getTotalScrollRange()==Math.abs(verticalOffset)){
                    //折叠监听
                    Toast.makeText(requireContext(),"折叠了",Toast.LENGTH_SHORT).show();
                    isVisible=true;
                    binding.floating.setVisibility(View.VISIBLE);
                }
                if(verticalOffset==0&&isVisible){
                    //展开监听
                    Toast.makeText(requireContext(),"展开了",Toast.LENGTH_SHORT).show();
                    isVisible=false;
                    binding.floating.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
    @SuppressLint("ResourceType")
    void pagerInit(){
        getData();
        ListFragment tabFragment1 = new ListFragment();
        ListFragment tabFragment2 = new ListFragment();
        ListFragment tabFragment3 = new ListFragment();

        fragmentList.add(tabFragment1);
        fragmentList.add(tabFragment2);
        fragmentList.add(tabFragment3);

        viewPager = binding.content.viewPager;
        myPageAdapter = new MyPageAdapter(requireActivity(), fragmentList);
        viewPager.setAdapter(myPageAdapter);
        tabLayout = binding.content.tableLayout;
        tabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < list.size(); i++) {

                TabLayout.Tab tab= tabLayout.newTab();
                 View layout=View.inflate(requireActivity(),R.layout.item_table,null);
                TextView textView = (TextView) layout.findViewById(R.id.tv_txt);
                textView.setText(list.get(i));
                tab.setCustomView(layout);
                tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.setScrollPosition(tab.getPosition(),0f,true);
                tabLayout.selectTab(tab);

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }


        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.d(TAG, "onPageScrolled: "+position+"     offset"+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setScrollPosition(position,0f,true);
                tabLayout.selectTab(tabLayout.getTabAt(position));
                itemSelected=position;
                Log.d(TAG, "onPageSelected: ----------"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


    }
    public void getData(){
        list=new ArrayList<>();
        for(int i = 0; i <10; i++){
            list.add("闲聊" + i);
        }
        MMKV mk=MMKV.defaultMMKV();
        if (mk.contains("user")&&mk.contains("token")){
            ToastUtils.showToast(requireContext(),mk.decodeString("user"));

            viewModel.setUser(Utils.getGson().fromJson(mk.decodeString("user"), User.class));
            viewModel.setToken(mk.decodeString("token"));

        }else {
            ToastUtils.showToast(requireContext(),"未登录，请先登录");
            //toLoginActivity();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ----------");

        binding = null;
    }
    public void useBanner() {

        List<Integer> list=new ArrayList();
        list.add(R.mipmap.banner1);
        list.add(R.mipmap.banner2);
        list.add(R.mipmap.banner3);
        //—————————————————————————如果你想偷懒，而又只是图片轮播————————————————————————
        binding.content.banner.setAdapter(new BannerImageAdapter<Integer>(list) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }


        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(requireContext()));
        //更多使用方法仔细阅读文档，或者查看demo
    }
    void  HeaderLayoutInit(){
        View view=binding.navigationView.inflateHeaderView(R.layout.headerlayout);
        ImageView imAvatar=(ImageView) view.findViewById(R.id.avatar);
        imAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: im");
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.personFragment);
            }
        });


    }
    private void leftViewInit() {
        navigationMenuView = (NavigationMenuView) binding.navigationView.getChildAt(0);

        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }

        // NavigationView 监听

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.group_item_github:
                        Toast.makeText(requireActivity(), "项目主页", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_more:
                        Toast.makeText(requireActivity(), "更多内容", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_qr_code:
                        Toast.makeText(requireActivity(), "二维码", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_share_project:
                        Toast.makeText(requireActivity(), "分享项目", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_model:
                        Toast.makeText(requireActivity(), "夜间模式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_about:
                        Toast.makeText(requireActivity(), "关于", Toast.LENGTH_SHORT).show();
                        break;
                }
                item.setCheckable(false);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
    void toLoginActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(requireContext(), LoginActivity.class);
                requireActivity().startActivity(mainIntent);
                requireActivity().finish();
            }
        }, 500);
    }


}