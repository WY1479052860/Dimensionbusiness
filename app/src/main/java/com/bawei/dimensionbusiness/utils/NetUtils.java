package com.bawei.dimensionbusiness.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>文件描述：<p>
 * <p>作者：DELL<p>
 * <p>创建时间：2020/1/9<p>
 * <p>版本号：　使用单例模式封装网络工具类。<p>
 */
public class NetUtils  {
    //1.单例模式
    public static NetUtils netUtils=new NetUtils();

    public NetUtils() {
    }

    public static NetUtils getNetUtils() {
        return netUtils;
    }
    //2.网络判断
    public boolean isNet(Context context){
        //网络判断工具类
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null){
            return true;
        }
        return false;
    }
    //2.移动网络
    public boolean isModle(Context context){
        //网络判断工具类
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null&&ConnectivityManager.TYPE_MOBILE==info.getType()){
            return true;
        }
        return false;
    }
    //2.isWifi
    public boolean isWifi(Context context){
        //网络判断工具类
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null&&ConnectivityManager.TYPE_WIFI==info.getType()){
            return true;
        }
        return false;
    }

    //6,获取网络JSON
    public void getJson(final String jsonurl, final ICallBack iCallBack){
        //线程+Handler
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建URL
                    URL url = new URL(jsonurl);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    //封装GET
                    conn.setRequestMethod("GET");
                    //设置连接超时为5秒钟，
                    conn.setConnectTimeout(5000);
                    // 读取超时为5秒钟
                    conn.setReadTimeout(5000);
                    //获取状态码
                    int responseCode = conn.getResponseCode();
                    if(responseCode==200){
                        InputStream inputStream = conn.getInputStream();
                        int len=0;
                        byte[] by=new byte[1024];
                        StringBuffer sb = new StringBuffer();
                        while((len=inputStream.read(by))!=-1){
                            String s = new String(by, 0, len);
                            sb.append(s);
                        }
                        //关闭流
                        inputStream.close();
                        //转换成字符串
                        final String json = sb.toString();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(iCallBack!=null){
                                    iCallBack.Success(json);
                                }
                            }
                        });

                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(iCallBack!=null){
                                    iCallBack.Error("请求失败");
                                }
                            }
                        });
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(iCallBack!=null){
                                iCallBack.Error(e.toString());
                            }
                        }
                    });
                }
            }
        }).start();

    }
    //5、创建Handler
    private Handler handler=new Handler();
    //7、接口回调
    public interface ICallBack{
        void Success(String json);
        void Error(String msg);
    }
}
