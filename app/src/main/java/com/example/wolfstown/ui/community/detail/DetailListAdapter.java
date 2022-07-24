package com.example.wolfstown.ui.community.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wolfstown.R;
import com.example.wolfstown.common.UtilMoreText;
import com.example.wolfstown.common.Utils;
import com.example.wolfstown.databinding.DetailItemCommentBinding;
import com.example.wolfstown.modle.Comment;
import com.example.wolfstown.modle.Topic;

import java.text.SimpleDateFormat;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.MyViewHolder> {
    final Integer NormalView_type=0;
    final Integer FooterView_type=1;
    Context context;
    List<Comment> strList;
    public MyViewHolder holder;

    private boolean isMoreCommentsVisible=true;


    public DetailListAdapter(Context context, List<Comment> strList){
        this.context = context;
        this.strList = strList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==NormalView_type){
            View view = LayoutInflater.from(context).inflate(R.layout.detail_item_comment, parent, false);
            holder=new MyViewHolder(view);
            if (isMoreCommentsVisible){
                holder.binding.layoutMore.setVisibility(View.VISIBLE);

                holder.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("---------------", "onClick:------- ");
                        CommentDialog.Builder builder=new CommentDialog.Builder(context).setTitle();
                        builder.show();
                    }
                });


            }




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
        String msg=" /**\n" +
                "     * @param textView  文本框\n" +
                "     * @param oriMsg    原始信息\n" +
                "     * @param textOpen  展开性质的文字\n" +
                "     * @param textClose 关闭性质的文字\n" +
                "     */\n" +
                "    public UtilMoreText(final TextView textView, String oriMsg, String textOpen, String textClose) {\n" +
                "    }";
        new UtilMoreText(holder.tv_content,msg).setSpanTextColor(com.luck.pictureselector.R.color.picture_list_blue_text_color).createString();

      /*  holder.tv_name.setText(strList.get(position).getName());
        holder.tv_time.setText(Utils.millis2String(strList.get(position).getTime(),new SimpleDateFormat("h:mm a")));
        holder.tv_like.setText(""+strList.get(position).getLikeNum());
        Glide.with(Utils.getApp().getApplicationContext()).load("https://dss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=572734183,263400261&fm=74&app=80&f=JPEG?w=200&h=200")
                .into(holder.im_picture);;
                holder.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController controller= Navigation.findNavController(v);
                        controller.navigate(R.id.fragmentDetails);
                    }
                });

       */
        Log.d("comment", "onBindViewHolder: -------------");

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
        TextView tv_name,tv_time,tv_content,tv_like,tv_reply;
        ImageView im_avatar,im_like,im_reply,im_more;
        DetailItemCommentBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=DetailItemCommentBinding.bind(itemView);
            tv_name=binding.tvName;
            tv_time=binding.tvTime;
            im_more=binding.imgMore;
            tv_content=binding.tvContent;
            tv_like=binding.tvGood;

            im_avatar= binding.imgAvatar;
            im_like=binding.imgGood;
        }
        TextView tv;
        public MyViewHolder(@NonNull View itemView, Integer type) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv_loading);
        }
    }

    public boolean isMoreCommentsVisible() {
        return isMoreCommentsVisible;
    }

    public void setMoreCommentsVisible(boolean moreCommentsVisible) {
        isMoreCommentsVisible = moreCommentsVisible;
    }
}
