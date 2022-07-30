package com.example.wolfstown.ui.community.edit;


import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.lib.camerax.CameraImageEngine;
import com.luck.lib.camerax.SimpleCameraX;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectLimitType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.dialog.AudioPlayDialog;
import com.luck.picture.lib.engine.CompressEngine;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.ExtendLoaderEngine;
import com.luck.picture.lib.engine.SandboxFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnCallbackIndexListener;
import com.luck.picture.lib.interfaces.OnCallbackListener;
import com.luck.picture.lib.interfaces.OnCameraInterceptListener;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnInjectLayoutResourceListener;
import com.luck.picture.lib.interfaces.OnMediaEditInterceptListener;
import com.luck.picture.lib.interfaces.OnQueryAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryAllAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryDataResultListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.loader.SandboxFileLoader;
import com.luck.picture.lib.style.BottomNavBarStyle;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;
import com.luck.picture.lib.utils.SdkVersionUtils;
import com.luck.picture.lib.utils.ToastUtils;
import com.luck.pictureselector.GlideEngine;
import com.luck.pictureselector.ImageLoaderUtils;
import com.luck.pictureselector.adapter.FullyGridLayoutManager;
import com.luck.pictureselector.listener.DragListener;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

public class Selector extends BaseSelector {



    public Selector(Context context,RecyclerView recyclerView,List<LocalMedia> mData) {
        super(context,mData);
        this.mRecyclerView=recyclerView;


       //初始化
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context,
                3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(context, 8), false));
        mAdapter = new GridImageAdapter(context, mData);
        mAdapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(mAdapter);
        adapterInit();
        StyleInit();
    }



    void adapterInit(){
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ArrayList<LocalMedia> selectList = mAdapter.getData();
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String availablePath = media.getAvailablePath();
                    if (PictureMimeType.isHasAudio(media.getMimeType())) {
                        // 预览音频
                        AudioPlayDialog.showPlayAudioDialog(context, availablePath);
                    } else {
                        // 预览图片 or 预览视频
                        startPreview(position,true,selectList);
                    }
                }
            }

            @Override
            public void openPicture() {
                  startSelector();
            }
        });
//
        mAdapter.setItemLongClickListener((holder, position, v) -> {
            //如果item不是最后一个，则执行拖拽
            needScaleBig = true;
            needScaleSmall = true;
            int size = mAdapter.getData().size();
            if (size != maxSelectNum) {
                mItemTouchHelper.startDrag(holder);
                return;
            }
            if (holder.getLayoutPosition() != size - 1) {
                mItemTouchHelper.startDrag(holder);
            }
        });

        mDragListener = new DragListener() {
            @Override
            public void deleteState(boolean isDelete) {

            }

            @Override
            public void dragState(boolean isStart) {

            }
        };

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(0.7f);
                }
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到item原来的position
                try {
                    int fromPosition = viewHolder.getAdapterPosition();
                    //得到目标position
                    int toPosition = target.getAdapterPosition();
                    int itemViewType = target.getItemViewType();
                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(mAdapter.getData(), i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(mAdapter.getData(), i, i - 1);
                            }
                        }
                        mAdapter.notifyItemMoved(fromPosition, toPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (null == mDragListener) {
                        return;
                    }
                    if (needScaleBig) {
                        //如果需要执行放大动画
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100);
                        //执行完成放大动画,标记改掉
                        needScaleBig = false;
                        //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                        needScaleSmall = false;
                    }

                        if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                            //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                            mDragListener.dragState(false);
                        }
                        if (needScaleSmall) {//需要松手后才能执行
                            viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).setDuration(100);
                        }
                        mDragListener.deleteState(false);

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : GridImageAdapter.TYPE_CAMERA;
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        mDragListener.dragState(true);
                    }
                    super.onSelectedChanged(viewHolder, actionState);
                }
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                needScaleSmall = true;
                isUpward = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(1.0f);
                    super.clearView(recyclerView, viewHolder);
                    mAdapter.notifyDataSetChanged();
                    resetState();
                }
            }
        });

        // 绑定拖拽事件
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 清除缓存
//        clearCache();
    }
    void startPreview(int position,boolean isDisplayDelete,ArrayList<LocalMedia> list){
        PictureSelector.create(context)
                .openPreview()
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSelectorUIStyle(selectorStyle)
                .setLanguage(language)
                .isPreviewFullScreenMode(true)
                .isPreviewZoomEffect(true)
                .setExternalPreviewEventListener(new OnExternalPreviewEventListener() {
                    @Override
                    public void onPreviewDelete(int position) {
                        mAdapter.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public boolean onLongPressDownload(LocalMedia media) {
                        return false;
                    }
                })
                .startActivityPreview(position, isDisplayDelete, list);
    }
    void startSelector(){
        PictureSelectionModel model;
        if (mode) {
            // 进入相册
            model = PictureSelector.create(context)
                    .openGallery(chooseMode)
                    .setSelectorUIStyle(selectorStyle)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    //getCropEngine()
                    .setCropEngine(null)
                    .setCompressEngine(getCompressEngine())
                    .setSandboxFileEngine(new MeSandboxFileEngine())
                    .setCameraInterceptListener(getCustomCameraEvent())
                    .setSelectLimitTipsListener(new MeOnSelectLimitTipsListener())
                    .setEditMediaInterceptListener(getCustomEditMediaEvent())
                    //.setExtendLoaderEngine(getExtendLoaderEngine())
                    .setInjectLayoutResourceListener(getInjectLayoutResource())
                    //单选SelectModeConfig.SINGLE
                    .setSelectionMode(selectionMode)
                    .setLanguage(language)
                    .setOutputCameraDir(chooseMode == SelectMimeType.ofAudio()
                            ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
                    .setOutputAudioDir(chooseMode == SelectMimeType.ofAudio()
                            ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
                    .setQuerySandboxDir(chooseMode == SelectMimeType.ofAudio()
                            ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
                    //显示资源时间轴
                    .isDisplayTimeAxis(true)
                    //查询指定目录
                    .isOnlyObtainSandboxDir(false)
                    //分页
                    .isPageStrategy(true)
                    //原图
                    .isOriginalControl(false)
                    .isDisplayCamera(true)
                    //声音
                    .isOpenClickSound(false)
                    //.setOutputCameraImageFileName("luck.jpeg")
                    //.setOutputCameraVideoFileName("luck.mp4")
                    .isWithSelectVideoImage(false)
                    .isPreviewFullScreenMode(true)
                    .isPreviewZoomEffect(true)
                    .isPreviewImage(true)
                    .isPreviewVideo(false)
                    .isPreviewAudio(false)
                    //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                    .isMaxSelectEnabledMask(true)
                    .isDirectReturnSingle(false)
                    .setMaxSelectNum(maxSelectNum)
                    .setRecyclerAnimationMode(animationMode)
                    .isGif(true)
                    .setSelectedData(mAdapter.getData());
        } else {
            // 单独拍照
            model = PictureSelector.create(context)
                    .openCamera(chooseMode)
                    .setCameraInterceptListener(getCustomCameraEvent())
                    .setCropEngine(getCropEngine())
                    .setCompressEngine(getCompressEngine())
                    .setSandboxFileEngine(new MeSandboxFileEngine())
                    .isOriginalControl(false);
        }
        forResult(model);
    }
    void StyleInit(){
        // 主体风格
        SelectMainStyle numberSelectMainStyle = new SelectMainStyle();
        numberSelectMainStyle.setSelectNumberStyle(true);
        numberSelectMainStyle.setPreviewSelectNumberStyle(false);
        numberSelectMainStyle.setPreviewDisplaySelectGallery(true);
        numberSelectMainStyle.setSelectBackground(com.luck.picture.lib.R.drawable.ps_default_num_selector);
        numberSelectMainStyle.setPreviewSelectBackground(com.luck.picture.lib.R.drawable.ps_preview_checkbox_selector);
        numberSelectMainStyle.setSelectNormalBackgroundResources(com.luck.picture.lib.R.drawable.ps_select_complete_normal_bg);
        numberSelectMainStyle.setSelectNormalTextColor(ContextCompat.getColor(getContext(), com.luck.picture.lib.R.color.ps_color_53575e));
        numberSelectMainStyle.setSelectNormalText(context.getString(com.luck.picture.lib.R.string.ps_send));
        numberSelectMainStyle.setAdapterPreviewGalleryBackgroundResource(com.luck.picture.lib.R.drawable.ps_preview_gallery_bg);
        numberSelectMainStyle.setAdapterPreviewGalleryItemSize(DensityUtil.dip2px(getContext(), 52));
        numberSelectMainStyle.setPreviewSelectText(context.getString(com.luck.picture.lib.R.string.ps_select));
        numberSelectMainStyle.setPreviewSelectTextSize(14);
        numberSelectMainStyle.setPreviewSelectTextColor(ContextCompat.getColor(getContext(),com.luck.picture.lib. R.color.ps_color_white));
        numberSelectMainStyle.setPreviewSelectMarginRight(DensityUtil.dip2px(getContext(), 6));
        numberSelectMainStyle.setSelectBackgroundResources(com.luck.picture.lib.R.drawable.ps_select_complete_bg);
        numberSelectMainStyle.setSelectText(context.getString(com.luck.picture.lib.R.string.ps_send_num));
        numberSelectMainStyle.setSelectTextColor(ContextCompat.getColor(getContext(), com.luck.picture.lib.R.color.ps_color_white));
        numberSelectMainStyle.setMainListBackgroundColor(ContextCompat.getColor(getContext(),com.luck.picture.lib. R.color.ps_color_black));
        numberSelectMainStyle.setCompleteSelectRelativeTop(true);
        numberSelectMainStyle.setPreviewSelectRelativeBottom(true);
        numberSelectMainStyle.setAdapterItemIncludeEdge(false);

        // 头部TitleBar 风格
        TitleBarStyle numberTitleBarStyle = new TitleBarStyle();
        numberTitleBarStyle.setHideCancelButton(true);
        numberTitleBarStyle.setAlbumTitleRelativeLeft(true);
        numberTitleBarStyle.setTitleAlbumBackgroundResource(com.luck.picture.lib.R.drawable.ps_album_bg);
        numberTitleBarStyle.setTitleDrawableRightResource(com.luck.picture.lib.R.drawable.ps_ic_grey_arrow);
        numberTitleBarStyle.setPreviewTitleLeftBackResource(com.luck.picture.lib.R.drawable.ps_ic_back2);

        // 底部NavBar 风格
        BottomNavBarStyle numberBottomNavBarStyle = new BottomNavBarStyle();
        numberBottomNavBarStyle.setBottomPreviewNarBarBackgroundColor(ContextCompat.getColor(getContext(),com.luck.picture.lib. R.color.ps_color_half_grey));
        numberBottomNavBarStyle.setBottomPreviewNormalText(context.getString(com.luck.picture.lib.R.string.ps_preview));
        numberBottomNavBarStyle.setBottomPreviewNormalTextColor(ContextCompat.getColor(getContext(), com.luck.picture.lib.R.color.ps_color_9b));
        numberBottomNavBarStyle.setBottomPreviewNormalTextSize(16);
        numberBottomNavBarStyle.setCompleteCountTips(false);
        numberBottomNavBarStyle.setBottomPreviewSelectText(context.getString(com.luck.picture.lib.R.string.ps_preview_num));
        numberBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(getContext(), com.luck.picture.lib.R.color.ps_color_white));


        selectorStyle.setTitleBarStyle(numberTitleBarStyle);
        selectorStyle.setBottomBarStyle(numberBottomNavBarStyle);
        selectorStyle.setSelectMainStyle(numberSelectMainStyle);

    }
    private void forResult(PictureSelectionModel model) {
        switch (resultMode) {
            case 1:
                model.forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case 2:
                model.forResult(new MeOnResultCallbackListener());
                break;
            default:
                model.forResult(launcherResult);
                break;
        }
    }



}
