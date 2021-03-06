package example.developermodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/7/28 0028.
 * 功能：方便管理程序内一些全局的状态信息，比如Context等等
 * 默认情况下，每当程序启动的时候，系统会自动将Application类进行初始化。
 * 因此MyApplication创建成功后需要告知系统，当程序启动的时候应该初始化 MyApplication 类，而不是默认的Application类
 * 告知系统只需要在清单文件 application 标签中声明即可。注意在声明的时候一定要加上完整的包名，否则系统将找不到这个类。
 */
public class MyApplication extends Application {

    /**
     * 全局Context
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取一个应用程序级别的 Context
        context = getApplicationContext();
        initImageLoader();
    }

    /**
     * ImageLoader的初始化信息，更多详情参考官方文档
     * 需要的权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     */
    private void initImageLoader() {
        //根据路径在SK卡中创建程序的缓存目录，并且返回该缓存目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
                "aOrangeLe/Cache");// 获取到盘符缓存的目录地址
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)//启用内存缓存
                .cacheOnDisk(true)//启用盘符缓存
                .bitmapConfig(Bitmap.Config.ARGB_4444)//Bitmap ARGB每个像素占4个字节
                .displayer(new RoundedBitmapDisplayer(10))//设置图片4个角的弧度，值越大弧度越大。也可以设置加载图片时的动画效果
                .build();
        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480,800)//default = 设备屏幕尺寸（Bitmap的大小，IamgeView的width 和 heigh），有助于节省内存
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY)//设置线程的数量以及线程的优先级
                .memoryCache(new LruMemoryCache(10*1024*1024))//最近最少使用的（default 的大小  ）
                .memoryCacheSize(30*1024*1024)//内存缓存的大小
                .memoryCacheSizePercentage(10)//内存缓存占用的百分比
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义盘符缓存目录
                .diskCacheSize(100*1024*1024)//盘符缓存大小
                .diskCacheFileCount(100)//盘符缓存的文件数量
                .writeDebugLogs()//开启Log打印
                .build();
        ImageLoader.getInstance().init(conf);
    }

    public static Context getContext(){
        return context;
    }
}
