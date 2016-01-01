package com.miss.schoolmarket2.until;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.miss.schoolmarket2.entity.IndexInfor;
import com.miss.schoolmarket2.entity.UserInfor;

import android.app.Activity;
import android.widget.Toast;

public class HttpUtils {
	private Activity activity;
	public static final String Success = "Success";
	public static final String False = "False";

	public HttpUtils(Activity activity) {
		this.activity = activity;
	}

	public static String connServerForResult(String url) {
		// 获取HttpGet对象
		String strResult = null;
		String url2 = null;
		byte[] url1 = url.getBytes();
		try {
			url2 = new String(url1, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			System.out.println("字符编码错误");
			e1.printStackTrace();
		}
		if (url2 != null) {
			HttpGet httpRequest = new HttpGet(url2);
			try {
				// HttpClient对象
				HttpClient httpClient = new DefaultHttpClient();
				// 获得HttpResponse对象
				HttpResponse httpResponse = httpClient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得返回的数据
					strResult = EntityUtils.toString(httpResponse.getEntity(),
							"utf-8");
				}
			} catch (ClientProtocolException e) {
				System.out.println("网络连接有误1");
			} catch (IOException e) {
				System.out.println("网络连接有误2");
				e.printStackTrace();
			}

			System.out.println("网络请求结果" + strResult);

		}

		return strResult; // 返回结果
	}

	public static String decode(String str) throws IOException {
		String string;
		byte[] b = null;
		Decoder.BASE64Decoder a = new Decoder.BASE64Decoder();

		b = a.decodeBuffer(str);

		string = new String(b, "GB2312");
		return string;
	}

	public static UserInfor parseJsonTouserInfor(String strResult) {
		UserInfor userInfor = null;
		try {
			userInfor = new UserInfor();
			JSONObject jsonObj = new JSONObject(strResult);
			userInfor.setuserId(jsonObj.getString("userId"));
			userInfor.setuserName(jsonObj.getString("userName"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("得到用户信息时出现错误");
			e.printStackTrace();
		}

		return userInfor;
	}

	// 首页得到数据
	// 分类
	public static List<IndexInfor> parseJsonToIndexInfo(String jsonString) {
		List<IndexInfor> list = new ArrayList<IndexInfor>();
		List<Map<String, String>> list1;
		if (jsonString != null) {
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				for (int i = 0; i < jsonArray.length(); i++) {
					IndexInfor sortSearchDemo = new IndexInfor();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					sortSearchDemo.setIsSale(jsonObject.getInt("isSale"));
					sortSearchDemo.setGoodsId(jsonObject.getInt("goodsId"));
					sortSearchDemo.setGoodsConnect(jsonObject
							.getString("goodsConnect"));
					sortSearchDemo.setUserId(jsonObject.getString("userId"));
					sortSearchDemo.setGoodsDescribe(jsonObject
							.getString("goodsDescribe"));
					sortSearchDemo.setGoodsTypeId(jsonObject
							.getInt("goodsTypeId"));
					sortSearchDemo.setGoodsPrice(jsonObject
							.getString("goodsPrice"));
					sortSearchDemo.setGoodsWanted(jsonObject
							.getInt("goodsWanted"));
					sortSearchDemo.setGoodsPublishTime(jsonObject
							.getString("goodsPublishTime"));
					sortSearchDemo.setGoodsName(jsonObject
							.getString("goodsName"));
					JSONArray array = jsonObject.getJSONArray("goodsPictureAD");
					String[] picList = { "", "", "" };
					for (int j = 0; j < array.length(); j++) {
						picList[j] = array.getString(j);
					}
					sortSearchDemo.setList(picList);
					list.add(sortSearchDemo);
				}

				return list;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
