package com.lqk.web.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.lqk.web.utils.GlideImageEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * @author LQK
 * @time 2020/5/26 9:51
 * @remark
 */
object VideoOrImageUtil {

    fun openImage(context: Context, requestCode: OnResultCallbackListener<LocalMedia>) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .isCamera(false)
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .isCamera(false)
                .forResult(requestCode)
        }
    }

    fun openImage(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .isCamera(false)
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .isCamera(false)
                .forResult(requestCode)
        }
    }

    fun openCamera(context: Context, requestCode: OnResultCallbackListener<LocalMedia>) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    fun openCamera(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    fun openVideo(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofVideo())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofVideo())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    fun openVideo(context: Context, requestCode: OnResultCallbackListener<LocalMedia>) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofVideo())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofVideo())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    fun openImageOrVideo(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    fun openImageOrVideo(context: Context, requestCode: OnResultCallbackListener<LocalMedia>) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideImageEngine())
                .forResult(requestCode)
        }
    }

    /**
     * 个人信息页面头像上传（带剪切）
     */
    fun openCropImage(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .enableCrop(true)//允许裁剪
                .isCamera(false)//不显示拍照按钮
                .withAspectRatio(150, 150)//裁剪宽高比
                .maxSelectNum(1)//最大图片选择数量
                .freeStyleCropEnabled(true)//裁剪框可移动
                .isDragFrame(true)
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .enableCrop(true)
                .isCamera(false)
                .withAspectRatio(150, 150)
                .maxSelectNum(1)
                .isDragFrame(true)
                .freeStyleCropEnabled(true)
                .forResult(requestCode)
        }
    }

    /**
     * 拍摄+裁剪
     */
    fun openCropCamera(context: Context, requestCode: Int = PictureConfig.CHOOSE_REQUEST) {
        if (context is Activity) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .enableCrop(true)//允许裁剪
                .withAspectRatio(150, 150)//裁剪宽高比
                .maxSelectNum(1)//最大图片选择数量
                .freeStyleCropEnabled(true)//裁剪框可移动
                .isDragFrame(true)
                .forResult(requestCode)
        } else if (context is Fragment) {
            PictureSelector.create(context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideImageEngine())
                .enableCrop(true)
                .withAspectRatio(150, 150)
                .maxSelectNum(1)
                .isDragFrame(true)
                .freeStyleCropEnabled(true)
                .forResult(requestCode)
        }
    }
}