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

    public void dopostAndgetStream(final String path, final Map<String,String> map, final ICallBack iCallBack){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setUseCaches(false);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    StringBuilder builder = new StringBuilder();
                    for (Map.Entry<String,String> entry:map.entrySet()){
                        String key = entry.getKey();
                        String value = entry.getValue();
                        builder.append(key+"="+value+"&");
                    }
                    String user = builder.toString();
                    user = user.substring(0,user.length()-1);
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(user.getBytes());
                    outputStream.flush();
                    con.connect();
                    int responseCode = con.getResponseCode();
                    if (responseCode == 200){
                        InputStream inputStream = con.getInputStream();
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        StringBuffer buffer = new StringBuffer();
                        while ((len = inputStream.read(bytes))!=-1){
                            buffer.append(new String(bytes,0,len));
                        }
                        inputStream.close();
                        outputStream.close();
                        final String s = buffer.toString();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iCallBack.Success(s);
                            }
                        });
                    }else {
                        Log.i("xxx","网络请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
