package com.library.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.library.base.BaseHttpDownloadManager;
import com.library.listener.OnDownloadListener;
import com.library.utils.Constant;
import com.library.utils.FileUtil;
import com.library.utils.LogUtil;
import com.library.utils.SharePreUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 库中默认的下载管理
 */
public class HttpDownloadManager extends BaseHttpDownloadManager {

    private static final String TAG = Constant.TAG + "HttpDownloadManager";
    private Context context;
    private boolean shutdown = false;
    private String apkUrl, apkName, downloadPath;
    private OnDownloadListener listener;

    public HttpDownloadManager(Context context, String downloadPath) {
        this.context = context;
        this.downloadPath = downloadPath;
    }

    @Override
    public void download(String apkUrl, String apkName, OnDownloadListener listener) {
        this.apkUrl = apkUrl;
        this.apkName = apkName;
        this.listener = listener;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(Constant.THREAD_NAME);
                return thread;
            }
        });
        executor.execute(runnable);
    }

    @Override
    public void cancel() {
        shutdown = true;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //删除之前的安装包
            int length = SharePreUtil.getInt(context, Constant.PROGRESS, 0);
            if (length == 0 && FileUtil.fileExists(downloadPath, apkName)) {
                FileUtil.delete(downloadPath, apkName);
            }
            //检查是否需要断点下载
            boolean breakpoint = DownloadManager.getInstance().getConfiguration().isBreakpointDownload();
            if (breakpoint) {
                breakpointDownload();
            } else {
                fullDownload();
            }
        }
    };

    /**
     * 断点下载
     */
    private void breakpointDownload() {
        listener.start();
        //  下载文件
        InputStream inputStream;
        RandomAccessFile saveFile;
        File file;
        Long length = getOkHttpContentLength();
        if (length < 0) {
            listener.error(new RuntimeException("获取到的文件大小为0！"));
            if (length == -1) {
                LogUtil.e(TAG, "此下载地址响应信息未设置 content-length，使用全量下载！");
                fullDownload();
            }
            return;
        }
        try {
            file = new File(downloadPath + "/" + apkName);
            // 判断上一次下载一半的文件是否存在
            if (!file.exists()) {
                // 不存在 则从头开始下载
                SharePreUtil.putInt(context, Constant.PROGRESS, 0);
            }
            // 上次下载到的位置
            long start = file.length();
            SharePreUtil.putInt(context, Constant.PROGRESS, (int) start);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + start + "-")
//                    .addHeader("Content-Type", "application/vnd.android.package-archive")
                    .url(apkUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                inputStream = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(start); // 跳过已经下载的字节
                byte[] bytes = new byte[1024 * 4];
                int progress = (int) start;
                int len;
                while ((len = inputStream.read(bytes)) != -1 && !shutdown) {
                    saveFile.write(bytes, 0, len);
                    progress += len;
                    // 计算 已经下载 文件 的百分比
                    SharePreUtil.putInt(context, Constant.PROGRESS, progress);
                    listener.downloading(length.intValue(), progress);
                }
                if (shutdown) {
                    // 取消了下载 同时再恢复状态
                    shutdown = false;
                    LogUtil.d(TAG, "breakpointDownload: 取消了下载");
                    listener.cancel();
                } else {
                    // 下载完成,将之前保存的进度清0
                    SharePreUtil.putInt(context, Constant.PROGRESS, 0);
                    // 释放资源
                    listener.done(file);
                }
                saveFile.close();
                response.body().close();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 全部下载
     */
    private void fullDownload() {
        listener.start();
        //  下载文件
        InputStream inputStream;
        File file;
        Long length = getOkHttpContentLength();
        try {
            file = new File(downloadPath + "/" + apkName);
            // 判断上一次下载一半的文件是否存在
            if (!FileUtil.fileExists(downloadPath, apkName)) {
                // 不存在 则从头开始下载
                SharePreUtil.putInt(context, Constant.PROGRESS, 0);
            }
            // 上次下载到的位置
            int start = SharePreUtil.getInt(context, Constant.PROGRESS, 0);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + start + "-" + length)
//                    .addHeader("Content-Type", "application/vnd.android.package-archive")
                    .url(apkUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                inputStream = response.body().byteStream();
                byte[] bytes = new byte[1024 * 4];
                int len;
                int progress = 0;
                FileOutputStream outputStream = new FileOutputStream(file);
                while ((len = inputStream.read(bytes)) != -1 && !shutdown) {
                    outputStream.write(bytes, 0, len);
                    // 计算 已经下载 文件 的百分比
                    progress += len;
                    listener.downloading(length.intValue(), progress);
                }
                response.body().close();
                if (shutdown) {
                    // 取消了下载 同时再恢复状态
                    shutdown = false;
                    LogUtil.d(TAG, "fullDownload: 取消了下载");
                    listener.cancel();
                } else {
                    listener.done(file);
                }
                // 完成io操作,释放资源
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } else {
                listener.error(new SocketTimeoutException("连接超时！"));
            }
        } catch (Exception e) {
            listener.error(e);
            e.printStackTrace();
        }
    }

    /**
     * 首先获取要下载文件的大小
     */
    private Long getOkHttpContentLength() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apkUrl).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                Long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
