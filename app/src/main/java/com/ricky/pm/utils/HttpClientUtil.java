package com.ricky.pm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpClientUtil {
	private static String TAG = "HttpUpDownUtil";
	
	public static int NET_WIFI = 1;
	public static int NET_MOBILENET = 2;
	public static int NET_NULL = 3;
	
	public static String INPUT_URL_ENCODEING = HTTP.UTF_8;
	public static String URL_ENCODEING = HTTP.UTF_8;
	public static String cookieString = null;
	public static int REQUEST_TIMEOUT = 10 * 1000;
	public static int REQUEST_OS_TIMEOUT = 10 * 1000;
	public static int REQUEST_OS_TIMEOUT_FORMAIL = 60 * 1000;
	public static int REQUEST_OS_FILE_DOWNLOAD_TIMEOUT = 200 * 1000;
	public static int REQUEST_OS_FILE_UPLOAD_TIMEOUT = 200 * 1000;

	private static Log LogUtil;



	public static boolean checkURL(String url){
		boolean flag = false;
		try{
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) new URL(url).openConnection();
			int code = mHttpURLConnection.getResponseCode();
			if(code==200){
				flag = true;
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return flag;
	}


	/**
	 * 获取网络状态
	 * @param context
	 * @return
	 */
	public static int getNetWorkType(Context context) {
		try {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
			if (info == null) {
				return NET_NULL;
			}
			int netType = info.getType();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return NET_WIFI;
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				return NET_MOBILENET;
			} else {
				return NET_NULL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return NET_NULL;
		}
		
	}
	
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {

		int netType = getNetWorkType(context);
		if (netType == NET_NULL) {
			return false;
		}else {
			return true;
		}
	}
	
	//用get方法下载信息
	//return String or null
	public static String downWithGetMethod(String url,Map<String, String> paraMap,Map<String, String> headerMap) {
		return downWithGetMethod(url,paraMap,headerMap, REQUEST_OS_TIMEOUT);
	}
	
	//用get方法下载信息
	//return String or null
	public static String downWithGetMethod(String url,Map<String, String> paraMap,Map<String, String> headerMap,int socketTimeOut) {
		LogUtil.e(TAG,"---- getMethod start -----");
		LogUtil.e(TAG,"url:" + url);
		String returnString = null;
		byte[] data = downWithGetMethodByte(url,paraMap,headerMap,socketTimeOut);
		if (data != null) {
			try {
				returnString = new String(data, INPUT_URL_ENCODEING);
				LogUtil.e(TAG,"getData:" + returnString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtil.e(TAG,"---- getMethod end -----");
		LogUtil.e(TAG,"    ");
		LogUtil.e(TAG,"    ");
		return returnString;
	}
	
	
	//用get方法下载信息
	//return byte[] or null
	public static byte[] downWithGetMethodByte(String url,Map<String, String> paraMap,Map<String, String> headerMap,int socketTimeOut) {
		LogUtil.e(TAG,"    ## byte getMethod start ####");
		byte[] data = null;
		DefaultHttpClient httpClient= null;
		try {
			httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT); 
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeOut);

			
			
			//para
			if (paraMap != null && paraMap.size() > 0) {
				String paraString = "";
				for (Map.Entry<String, String> entry : paraMap.entrySet()) {
					if (paraString.equalsIgnoreCase("")) {
						paraString = entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),HTTP.UTF_8);
					}else {
						paraString = paraString + "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),HTTP.UTF_8);
					}
		            LogUtil.e(TAG,"para---" + entry.getKey() + ":" + entry.getValue());  
		        }
				url = url + "?" + paraString;
			}
			HttpGet httpGet = new HttpGet(url);
			//header
			if (headerMap != null) {
				Iterator<String> iterator = headerMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					if (key != null && !key.equalsIgnoreCase("")) {
						try {
							httpGet.setHeader(key,URLEncoder.encode(headerMap.get(key), HTTP.UTF_8));
							LogUtil.e(TAG, "header---" + key + ":" + headerMap.get(key));
							
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
			//cookie
			if(cookieString != null && !cookieString.equalsIgnoreCase("")){
				httpGet.setHeader("Cookie",cookieString);
				LogUtil.e(TAG, "header---Cookie" + ":" + cookieString);
			}
			
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			data = EntityUtils.toByteArray(entity);
			entity.consumeContent();
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
			String cookeStringT = "";
			for (Cookie cookie: cookies) {
				if (cookeStringT.equalsIgnoreCase("")) {
					cookeStringT = cookie.getName() + "=" + cookie.getValue();
				}else {
					cookeStringT = cookeStringT + "; " + cookie.getName() + "=" + cookie.getValue();
				}
				
			}
			if (!cookeStringT.equalsIgnoreCase("") && !cookeStringT.equalsIgnoreCase(cookieString)) {
				cookieString = cookeStringT;
				LogUtil.e("NewCookie", "    ----------------------");
				LogUtil.e("NewCookie", "    getNewCookie:" + cookeStringT);
				LogUtil.e("NewCookie", "    ----------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
		//	httpclient.getConnectionManager().shutdown();
			httpClient.getConnectionManager().closeExpiredConnections();
			int datalength = 0;
			if (data != null) {
				datalength = data.length;
			}
			LogUtil.e(TAG,"getDataByte[].length:" + String.valueOf(datalength));
			LogUtil.e(TAG,"    ## byte getMethod end ####");
		}
		return data;
	}
	
	//用post方法下载信息
		//return String or null
		public static String downWithPostMethod(String url,Map<String, String> paraMap,Map<String, String> headerMap) {
			return downWithPostMethod(url,paraMap,headerMap,REQUEST_OS_TIMEOUT,null);
		}
	
	//用post方法下载信息
	//return String or null
	public static String downWithPostMethod(String url,Map<String, String> paraMap,Map<String, String> headerMap,DefaultHttpClient httpClient) {
		return downWithPostMethod(url,paraMap,headerMap,REQUEST_OS_TIMEOUT,httpClient);
	}
	
	//用post方法上传文件流信息
	//return String or null
	public static String uploadFileStreamWithPostMethod(String url,String filePath,Map<String, String> headerMap) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		String result = uploadStreamWithPostMethod(url,inputStream,new File(filePath).length(),headerMap,REQUEST_OS_FILE_UPLOAD_TIMEOUT,null);
	
		try {
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	//用post方法上传文件流信息
		//return String or null
		public static String uploadFileStreamWithPostMethod(String url,String filePath,Map<String, String> headerMap,DefaultHttpClient httpClient) {
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(filePath);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			String result = uploadStreamWithPostMethod(url,inputStream,new File(filePath).length(),headerMap,REQUEST_OS_FILE_UPLOAD_TIMEOUT,httpClient);
		
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	
	
	//用post方法下载信息
	//return String or null
	public static String downWithPostMethod(String url,Map<String, String> paraMap,Map<String, String> headerMap,int socketTimeOut,DefaultHttpClient client) {
		LogUtil.e(TAG,"---- postMethod start -----");
		LogUtil.e(TAG,"url:" + url);
		String returnString = null;
		byte[] data = downWithPostMethodByte(url,paraMap,headerMap,socketTimeOut,client);
		if (data != null) {
			try {
				returnString = new String(data, INPUT_URL_ENCODEING);
				LogUtil.e(TAG,"postData:" + returnString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtil.e(TAG,"---- postMethod end -----");
		LogUtil.e(TAG,"    ");
		LogUtil.e(TAG,"    ");
		return returnString;
	}
	
	
	//用post方法上传流信息
	//return String or null
	public static String uploadStreamWithPostMethod(String url,InputStream inputStream,long streamLength,Map<String, String> headerMap,int socketTimeOut,DefaultHttpClient httpClient) {
		LogUtil.e(TAG,"---- postMethod start -----");
		LogUtil.e(TAG,"url:" + url);
		String returnString = null;
		byte[] data = uploadStreamWithPostMethodByte(url,inputStream,streamLength,headerMap,socketTimeOut,httpClient);
		if (data != null) {
			try {
				returnString = new String(data, INPUT_URL_ENCODEING);
				LogUtil.e(TAG,"postData:" + returnString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtil.e(TAG,"---- postMethod end -----");
		LogUtil.e(TAG,"    ");
		LogUtil.e(TAG,"    ");
		return returnString;
	}
	
	
	
	//用post方法下载信息
	//return String or null
	public static byte[] downWithPostMethodByte(String url,Map<String, String> paraMap,Map<String, String> headerMap,int socketTimeOut,DefaultHttpClient httpClientT) {
		
		LogUtil.e(TAG,"    ## byte postMethod start ####");
		byte[] data = null;
		DefaultHttpClient httpClient= null;
		try {
			if (httpClientT != null) {
				httpClient = httpClientT;
			}else {
				httpClient = new DefaultHttpClient();
			}
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT); 
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeOut);
			httpClient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,  Boolean.FALSE);
			HttpPost httpPost = new HttpPost(url);
			//header
			if (headerMap != null) {
				Iterator<String> iterator = headerMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					if (key != null && !key.equalsIgnoreCase("")) {
						try {
							String keyT = URLEncoder.encode(headerMap.get(key),HTTP.UTF_8);
							httpPost.setHeader(key, keyT);
							LogUtil.e(TAG, "header---" + key + ":" + headerMap.get(key) + " -- " + keyT);
						} catch (Exception e) {
						}
						
					}
				}
			}
			//cookie
			if(cookieString != null && !cookieString.equalsIgnoreCase("")){
				httpPost.setHeader("Cookie",cookieString);
				LogUtil.e(TAG, "header---Cookie" + ":" + cookieString);
			}
			//para
			if (paraMap != null && paraMap.size() > 0) {
				
				List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();  
				for (Map.Entry<String, String> entry : paraMap.entrySet()) {  
		            postData.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
		            LogUtil.e(TAG,"para---" + entry.getKey() + ":" + entry.getValue());  
		        }  
		        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData,HTTP.UTF_8);   
		        httpPost.setEntity(entity); 
			}
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			data = EntityUtils.toByteArray(entity);
			entity.consumeContent();
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
			String cookeStringT = "";
			for (Cookie cookie: cookies) {
				if (cookeStringT.equalsIgnoreCase("")) {
					cookeStringT = cookie.getName() + "=" + cookie.getValue();
				}else {
					cookeStringT = cookeStringT + "; " + cookie.getName() + "=" + cookie.getValue();
				}
				
				
				
			}
			if (!cookeStringT.equalsIgnoreCase("") && !cookeStringT.equalsIgnoreCase(cookieString)) {
				cookieString = cookeStringT;
				LogUtil.e("NewCookie", "    ----------------------");
				LogUtil.e("NewCookie", "    getNewCookie:" + cookeStringT);
				LogUtil.e("NewCookie", "    ----------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
		//	httpclient.getConnectionManager().shutdown();
			httpClient.getConnectionManager().closeExpiredConnections();
			int datalength = 0;
			if (data != null) {
				datalength = data.length;
			}
			LogUtil.e(TAG,"postDataByte[].length:" + String.valueOf(datalength));
			LogUtil.e(TAG,"    ## byte postMethod end ####");
		}
		return data;
	}
	
	//用post方法上传流信息
	//return String or null
	public static byte[] uploadStreamWithPostMethodByte(String url,InputStream inputStream,long streamLength,Map<String, String> headerMap,int socketTimeOut,DefaultHttpClient httpClientT) {
		
		LogUtil.e(TAG,"    ## byte postMethod start ####");
		byte[] data = null;
		DefaultHttpClient httpClient= null;
		try {
			if (httpClientT != null) {
				httpClient = httpClientT;
			}else {
				httpClient = new DefaultHttpClient();
			}
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT); 
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeOut);
			
			HttpPost httpPost = new HttpPost(url);
			//header
			if (headerMap != null) {
				Iterator<String> iterator = headerMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					if (key != null && !key.equalsIgnoreCase("")) {
						String keyT = URLEncoder.encode(headerMap.get(key),HTTP.UTF_8);
						httpPost.setHeader(key, keyT);
						LogUtil.e(TAG, "header---" + key + ":" + headerMap.get(key));
					}
				}
			}
			//cookie
			if(cookieString != null && !cookieString.equalsIgnoreCase("")){
				httpPost.setHeader("Cookie",cookieString);
				LogUtil.e(TAG, "header---Cookie" + ":" + cookieString);
			}
			//para stream
			if (inputStream != null) {
				InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, streamLength);
		        httpPost.setEntity(inputStreamEntity); 
			}
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			data = EntityUtils.toByteArray(entity);
			entity.consumeContent();
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
			String cookeStringT = "";
			for (Cookie cookie: cookies) {
				if (cookeStringT.equalsIgnoreCase("")) {
					cookeStringT = cookie.getName() + "=" + cookie.getValue();
				}else {
					cookeStringT = cookeStringT + "; " + cookie.getName() + "=" + cookie.getValue();
				}
				
				
				
			}
			if (!cookeStringT.equalsIgnoreCase("") && !cookeStringT.equalsIgnoreCase(cookieString)) {
				cookieString = cookeStringT;
				LogUtil.e("NewCookie", "    ----------------------");
				LogUtil.e("NewCookie", "    getNewCookie:" + cookeStringT);
				LogUtil.e("NewCookie", "    ----------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
		//	httpclient.getConnectionManager().shutdown();
			httpClient.getConnectionManager().closeExpiredConnections();
			int datalength = 0;
			if (data != null) {
				datalength = data.length;
			}
			LogUtil.e(TAG,"postDataByte[].length:" + String.valueOf(datalength));
			LogUtil.e(TAG,"    ## byte postMethod end ####");
		}
		return data;
	}

}
