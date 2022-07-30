package com.example.wolfstown.ui.community.detail;

import android.content.Intent;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wolfstown.R;
import com.example.wolfstown.ac.MainActivity;
import com.example.wolfstown.databinding.FragmentDetailsBinding;
import com.example.wolfstown.modle.Comment;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;
import com.wanglu.photoviewerlibrary.Utils;


import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private List<String> mDatas;
    private GridView mGridView;
    private GridViewAdapter adapter;

    private DetailListAdapter listAdapter;
    private RecyclerView recyclerView;

    private FragmentDetailsBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.cell));
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mDatas=new ArrayList<>();
        commentsInit();
        mGridView = binding.content.gridView;
        mDatas.add("https://img.zcool.cn/community/01dafd58ec38dea8012049efb3b0d5.png@1280w_1l_2o_100sh.png");
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
                    Log.d("detail", "onItemClick: ");
                    PhotoViewer.INSTANCE

                            //设置图片数据
                            .setData((ArrayList<String>) mDatas)
                            //设置当前位置
                            .setCurrentPage(position)
                            //设置图片控件容器
                            //他需要容器的目的是
                            //显示缩放动画
                            .setImgContainer(mGridView)
                            //设置图片加载回调
                            .setShowImageViewInterface((imageView, url) -> {
                                //使用Glide显示图片
                                Glide.with(requireContext())
                                        .load(url)
                                        .into(imageView);
                            })
                            .setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public void onLongClick(@NonNull View view) {
                                }
                            })
                            //启动界面
                            .start(requireParentFragment());
                }

        });


    }
    void clickInit(){
        binding.imLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.firstFragment);
            }
        });
    }
    private List<Comment> comments;
    void commentsInit(){
        comments=new ArrayList<>();
        comments.add(new Comment());
        recyclerView=binding.content.listRecycler;
        listAdapter=new DetailListAdapter(requireContext(),comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
