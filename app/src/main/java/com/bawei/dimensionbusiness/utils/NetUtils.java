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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * <p>文件描述：<p>
 * <p>作者：DELL<p>
 * <p>创建时间：2020/1/9<p>
 * <p>版本号：　使用单例模式封装网络工具类。<p>
 */
public class NetUtils  {
    private static NetUtils netutls=new NetUtils();

    private  NetUtils(){

    }

    public static NetUtils getNetutls() {

        return netutls;
    }
    public Boolean getwifi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo!=null){
            return true;
        }else{
            return false;
        }
    }

    Handler handler=new Handler();
    public interface  Contiontper{
        void onCtion(String json);
        void onError(String msg);
    }
    public void getregister(final String strm, final Map<String,String>stringStringMap, final Contiontper contiontper){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strm);
                    final HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Map.Entry<String,String>entry:stringStringMap.entrySet()){
                        String key=entry.getKey();
                        String value=entry.getValue();
                        stringBuilder.append(key+"="+value+"&");
                    }
                    String string = stringBuilder.toString();
                    String s = string.substring(0, string.length() - 1);
                    Log.i("xxx",s);
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(s.getBytes());
                    outputStream.flush();

                    conn.connect();
                    int i = conn.getResponseCode();
                    if(i==200){
                        InputStream inputStream = conn.getInputStream();
                        int len=0;
                        byte[]by=new byte[1024];
                        StringBuilder sb = new StringBuilder();
                        while ((len=inputStream.read(by))!=-1){
                            String s1 = new String(by, 0, len);
                            sb.append(s1);
                        }
                        inputStream.close();
                        outputStream.close();
                        final String string1 = sb.toString();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (contiontper!=null){
                                    contiontper.onCtion(string1);
                                }
                            }
                        });
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(contiontper!=null){
                                    contiontper.onError("请求错误");
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
