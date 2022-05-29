package com.example.wolfstown.ui.community.edit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wolfstown.databinding.FragmentEditBinding;

import com.example.wolfstown.ui.community.edit.adapter.GridImageAdapter;

import com.luck.picture.lib.entity.LocalMedia;


import java.util.ArrayList;
import java.util.List;

public class EditFragment extends Fragment {
    FragmentEditBinding binding;

    private int maxSelectNum = 9;

    RecyclerView mRecyclerView;
    GridImageAdapter mAdapter;

    private final List<LocalMedia> mData = new ArrayList<>();
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEditBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView= binding.recycler;



    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
