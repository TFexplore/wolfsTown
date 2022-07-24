package com.example.wolfstown.ui.community.detail;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wolfstown.R;
import com.example.wolfstown.databinding.CommentDialogBinding;
import com.example.wolfstown.modle.Comment;
import com.hjq.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public final class CommentDialog {


    public final static class Builder extends BaseDialog.Builder<Builder> {
        private List<Comment> comments;
        private DetailListAdapter listAdapter;
        private RecyclerView recyclerView;

        CommentDialogBinding binding;
        public Builder(Context context) {
            super(context);
            setContentView(R.layout.comment_dialog);//R.layout.comment_dialog
            binding=CommentDialogBinding.bind(getContentView());
            commentsInit();



           binding.backView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Builder.super.dismiss();
               }
           });
        }
        void commentsInit(){
            comments=new ArrayList<>();
            comments.add(new Comment());
            recyclerView=binding.rvDialog;
            listAdapter=new DetailListAdapter(super.getContext(),comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
            recyclerView.setAdapter(listAdapter);
            listAdapter.setMoreCommentsVisible(false);
        }
        public Builder setTitle(){
            return this;
        }
    }


}
