package com.xixb.lovenotice.utils.http;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yeahmobi53 on 2015/1/29.
 *
 */
public class HttpClientUtils {

    private static final String basePath = "http://127.0.0.1:80/";

//    public void run() {
//        // 设置访问的Web站点
//        String path = "http://127.0.0.1:80/user";
//        //设置Http请求参数
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("username", username);
//        params.put("password", password);
//
//        String result = sendHttpClientPost(path, params, "utf-8");
//        //把返回的接口输出
//        System.out.println(result);
//    }

    /**
     * 发送Http请求到Web站点
     * @param path Web站点请求地址
     * @param map Http请求参数
     * @param encode 编码格式
     * @return Web站点响应的字符串
     */
    public static String sendHttpClientPost(String path,Map<String, String> map,String encode)
    {
        path = basePath + path;

        if (encode == null || "".equals(encode)){
            encode = "utf-8";
        }
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        if(map!=null&&!map.isEmpty())
        {
            for(Map.Entry<String, String> entry:map.entrySet())
            {
                //解析Map传递的参数，使用一个键值对对象BasicNameValuePair保存。
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            //实现将请求 的参数封装封装到HttpEntity中。
            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list, encode);
            //使用HttpPost请求方式
            HttpPost httpPost=new HttpPost(path);
            //设置请求参数到Form中。
            httpPost.setEntity(entity);
            //实例化一个默认的Http客户端，使用的是AndroidHttpClient
            HttpClient client= AndroidHttpClient.newInstance("");
            //执行请求，并获得响应数据
            HttpResponse httpResponse= client.execute(httpPost);
            //判断是否请求成功，为200时表示成功，其他均问有问题。
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                //通过HttpEntity获得响应流
                InputStream inputStream=httpResponse.getEntity().getContent();
                return changeInputStream(inputStream,encode);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }
    /**
     * 把Web站点返回的响应流转换为字符串格式
     * @param inputStream 响应流
     * @param encode 编码格式
     * @return 转换后的字符串
     */
    private  static String changeInputStream(InputStream inputStream,
                                      String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result="";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data,0,len);
                }
                result=new String(outputStream.toByteArray(),encode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
