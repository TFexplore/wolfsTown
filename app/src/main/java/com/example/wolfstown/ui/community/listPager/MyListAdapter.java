package com.example.wolfstown.ui.community.listPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.wolfstown.R;
import com.example.wolfstown.common.Utils;
import com.example.wolfstown.databinding.ListItemCardBinding;
import com.example.wolfstown.modle.Topic;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyViewHolder> {
    final Integer NormalView_type=0;
    final Integer FooterView_type=1;
    Context context;
    List<Topic> strList;

    private Activity fragment;
    public MyListAdapter(Context context, List<Topic> strList){
        this.context = context;
        this.strList = strList;
    }

    public void setFragment(Activity fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder;
        if (viewType==NormalView_type){
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_card, parent, false);
            holder=new MyViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.footer, parent, false);
            holder=new MyViewHolder(view,1);

        }


        return holder;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (position==getItemCount()-1){//没有数据时
            //code 加载数据请求

            return;
        }
        holder.tv_name.setText(strList.get(position).getName());
        holder.tv_time.setText(Utils.millis2String(strList.get(position).getTime(),new SimpleDateFormat("h:mm a")));
        holder.tv_like.setText(""+strList.get(position).getLikeNum());
        Glide.with(Utils.getApp().getApplicationContext()).load("https://img.zcool.cn/community/01dafd58ec38dea8012049efb3b0d5.png@1280w_1l_2o_100sh.png")
                .into(holder.im_picture);;
                holder.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController controller= Navigation.findNavController(v);
                        controller.navigate(R.id.fragmentDetails);
                    }
                });
                holder.im_picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoViewer.INSTANCE

                                //设置图片数据
                                .setClickSingleImg("https://img.zcool.cn/community/01dafd58ec38dea8012049efb3b0d5.png@1280w_1l_2o_100sh.png",v)
                                //设置当前位置
                                .setCurrentPage(position)
                                //设置图片加载回调
                                .setShowImageViewInterface((imageView, url) -> {
                                    //使用Glide显示图片
                                    Glide.with(Utils.getApp())
                                            .load(url)
                                            .into(imageView);
                                })
                                .setOnLongClickListener(new OnLongClickListener() {
                                    @Override
                                    public void onLongClick(@NonNull View view) {

                                    }
                                })
                                //启动界面
                                .start((AppCompatActivity) fragment);
                    }
                });

    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
         return FooterView_type;
        }
        else return NormalView_type;
    }

    @Override
    public int getItemCount() {
        return strList.size()+1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_time,tv_content,tv_topic,tv_like,tv_reply;
        ImageView im_avatar,im_picture,im_like,im_reply,im_more;
        ListItemCardBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ListItemCardBinding.bind(itemView);
            tv_name=binding.tvName;
            tv_time=binding.tvTime;
            im_more=binding.icMore;
            tv_content=binding.tvContent;
            tv_topic=binding.tvFenlei;
            tv_like=binding.tvDianzanNum;
            tv_reply=binding.tvHuifu;
            im_avatar= binding.imAvatar;
            im_picture= binding.imPicture;
            im_like=binding.icDianzan;
            im_reply=binding.icHuifu;
        }
        TextView tv;
        public MyViewHolder(@NonNull View itemView, Integer type) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv_loading);
        }
    }

}
