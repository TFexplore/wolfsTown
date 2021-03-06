package com.example.wolfstown.ui.community.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.wolfstown.databinding.FragmentDetailsBinding;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;
import com.wanglu.photoviewerlibrary.Utils;


import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private List<String> mDatas;
    private GridView mGridView;
    private GridViewAdapter adapter;

    private FragmentDetailsBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatas=new ArrayList<>();
        mGridView = binding.gridView;
        mDatas.add("https://tse1-mm.cn.bing.net/th/id/R-C.92ef5dc76360c1db82c67eadafa3ba16?rik=X1cCqWYh4cauVQ&riu=http%3a%2f%2fim.rediff.com%2fmovies%2f2019%2fjan%2f10uri20.jpg&ehk=oL03z7HW%2bW%2fsIA38qkJuBEg9nG2gE4PZD5Zideq5cxM%3d&risl=&pid=ImgRaw&r=0");
        mDatas.add("https://tse4-mm.cn.bing.net/th/id/OIP-C.F-HLU2HXU7mqGVniAaudkwHaE8?w=267&h=180&c=7&r=0&o=5&dpr=1.25&pid=1.7");
        mDatas.add("https://tse1-mm.cn.bing.net/th/id/OIP-C.GoxNDTP_SlIUQlh42dzVcAHaE7?w=267&h=180&c=7&r=0&o=5&dpr=1.25&pid=1.7");
        mDatas.add("https://tse2-mm.cn.bing.net/th/id/OIP-C.kn26Rqn4m2E-WdZo8A8OZgHaFj?w=239&h=180&c=7&r=0&o=5&dpr=1.25&pid=1.7");
        mDatas.add("https://tse3-mm.cn.bing.net/th/id/OIP-C.Hp-sIpwnJKDUanvybQ8viwHaEw?w=277&h=180&c=7&r=0&o=5&dpr=1.25&pid=1.7");
    adapter = new GridViewAdapter(requireContext(), mDatas);

        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    mDatas.add("https://dss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=572734183,263400261&fm=74&app=80&f=JPEG?w=200&h=200");
                    Toast.makeText(requireContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                    adapter = new GridViewAdapter(requireContext(), mDatas);
                    mGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    Log.d("detail", "onItemClick: ");

                    PhotoViewer.INSTANCE

                            //??????????????????
                            .setData((ArrayList<String>) mDatas)

                            //??????????????????
                            .setCurrentPage(position)

                            //????????????????????????
                            //???????????????????????????
                            //??????????????????
                            .setImgContainer(mGridView)

                            //????????????????????????
                            .setShowImageViewInterface((imageView, url) -> {
                                //??????Glide????????????
                                Glide.with(requireContext())
                                        .load(url)
                                        .into(imageView);
                            })
                            .setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public void onLongClick(@NonNull View view) {

                                }
                            })
                            //????????????
                            .start(requireParentFragment());
                }

            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
