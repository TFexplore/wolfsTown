package com.example.wolfstown.ui.community.listPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wolfstown.databinding.FragmentListBinding;
import com.example.wolfstown.modle.Topic;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private List<Topic> strList;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        getDate();
        binding = FragmentListBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }
    void getDate(){
        strList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Topic topic=new Topic();
            topic.setName("听风");
            topic.setTime(System.currentTimeMillis());
            topic.setContent("");
            topic.setLikeNum(99);
            topic.setResumNum(99);
            strList.add(topic);
        }
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView;
        MyListAdapter myListAdapter;

        myListAdapter = new MyListAdapter(requireContext(), strList);
        myListAdapter.setFragment(requireActivity());
        recyclerView = binding.recylerViewMaster;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(myListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
