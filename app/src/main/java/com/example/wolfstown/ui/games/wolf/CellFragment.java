package com.example.wolfstown.ui.games.wolf;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wolfstown.R;
import com.example.wolfstown.ac.MainActivity;
import com.example.wolfstown.databinding.FragmentCellBinding;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MessageRecyclerAd myAdapter;
    RecyclerView recyclerView;

    private FragmentCellBinding binding;
    private int isexpand = 0;
    private int select = 0;

    WoveskillViewModle woveskillViewModle;

    MediaPlayer player;

    boolean flag_vote = false;

    public CellFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CellFragment newInstance(String param1, String param2) {
        CellFragment fragment = new CellFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCellBinding.inflate(getLayoutInflater());
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.cell));
        return binding.getRoot();//inflater.inflate(R.layout.fragment_cell, container, false);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");

        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        woveskillViewModle = new ViewModelProvider(requireActivity()).get(WoveskillViewModle.class);

        myAdapter = new MessageRecyclerAd(requireActivity());
        recyclerView = binding.recyl;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(myAdapter);


        onMasterTouch_init();
        try {
            userImag_inti();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            sitesInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        onUserTouch_init();
        binding.roomNo.setText(String.valueOf(woveskillViewModle.getKey_Room()));
    }

    void sitesInit() throws NoSuchFieldException, IllegalAccessException {
        updataPlayer();
        woveskillViewModle.userIn.observe(getViewLifecycleOwner(), userIn -> {
            binding.users.setText(new String("" + userIn));
        });
    }//ob

    void onUserTouch_init() {
        binding.febMore.setOnClickListener(v -> {//More
            // Log.d(TAG, "onUserTouch_init: more");


            }

        );
        binding.febSkill.setOnClickListener(v -> {//使用技能
            Log.d(TAG, "onUserTouch_init: skill");

            // MyOkhttp.getINSTANCE().getRoom("" + woveskillViewModle.getKey_Room(), myViewModle.getToken(), 5);
        });
        binding.febCheck.setOnClickListener(v -> {//查看身份
            Log.d(TAG, "onUserTouch_init: check");

        });
        binding.febToupiao.setOnClickListener(v -> {//投票
            Log.d(TAG, "onUserTouch_init: toupiao");
            if (select == 0) {
                Toast.makeText(requireActivity().getApplicationContext(), "请选择目标", Toast.LENGTH_SHORT).show();
            }
            woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(select);

        });
        //返回键
        binding.imMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("是否退出");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavController controller = Navigation.findNavController(requireView());
                        controller.navigate(R.id.firstFragment);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        //菜单
        binding.feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//菜单图标
                if (binding.febMenu2.isExpanded()) {
                    binding.febMenu2.collapse();
                    return;
                }
                if (binding.febMenu1.isExpanded()) {
                    binding.febMenu1.collapse();
                    binding.febMenu2.expand();
                } else if (woveskillViewModle.isMaster()) {
                    binding.febMenu1.expand();
                } else binding.febMenu2.expand();
            }
        });
    }

    void onMasterTouch_init() {

    }

    void userImag_inti() throws IllegalAccessException, NoSuchFieldException {


    }



    void updataPlayer() throws IllegalAccessException, NoSuchFieldException {
        for (int i = 1; i < 13; i++) {//初始化
            Class<?> d = R.id.class;
            Field field = d.getField("name" + i);
            TextView textView = binding.getRoot().findViewById(field.getInt(requireActivity()));
            textView.setText("空座");

            Field imgId = d.getField("user" + i);
            ImageView imageView = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
            imageView.setImageResource(R.drawable.icon_gray);

            select = 0;
        }
        woveskillViewModle.userIn.setValue(woveskillViewModle.users.size());
        for (Integer key:woveskillViewModle.users.keySet()){
            try {
                Class<?> d = R.id.class;
                Field field = d.getField("name" + woveskillViewModle.users.get(key).getNum());
                TextView textView = binding.getRoot().findViewById(field.getInt(requireActivity()));
                textView.setText(woveskillViewModle.users.get(key).getName());

                Class<?> c = R.mipmap.class;
                Field idfield = c.getField("icon_" + woveskillViewModle.users.get(key).getAvatar());
                int avatar = idfield.getInt(requireActivity());//头像
                Field imgId = d.getField("user" + woveskillViewModle.users.get(key).getNum());
                ImageView imageView = null;

                imageView = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
                imageView.setImageResource(avatar);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }


}