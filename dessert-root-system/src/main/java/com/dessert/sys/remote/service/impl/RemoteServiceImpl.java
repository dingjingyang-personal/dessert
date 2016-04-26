package com.dessert.sys.remote.service.impl;

import com.dessert.sys.common.constants.SysSettings;
import com.dessert.sys.common.tool.SysToolHelper;
import com.dessert.sys.remote.service.RemoteService;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service
public class RemoteServiceImpl implements RemoteService {

	@Override
	public Map<String, Object> invokeRemoteMethod(String beanId,
			String methodName, Map<String, Object> params) {
		if(StringUtils.isEmpty(beanId)||StringUtils.isEmpty(methodName)){
			return null;
		}
		Map<String, String> temp=new HashMap<String, String>(3);
		temp.put("beanId", beanId);
		temp.put("methodName", methodName);
		if(params!=null){
			temp.put("params", SysToolHelper.getJsonOfCollection(params));
		}
		String resString=postMap(SysSettings.REMOTE_INVOKE_URL, temp);
		try {
			resString=URLDecoder.decode(resString, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(temp.containsKey("error")){
			if(params!=null){
				params.put("error", temp.get("error"));
			}
			return null;
		}
		return JSONObject.fromObject(resString);
	}
	private  class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}
		@Override
		public String getRequestCharSet() {			
			return "UTF-8";
		}
	}
	private  String postMap(String url,Map<String, String> params) {
		UTF8PostMethod  post = null;
		try {
			HttpClient client = new HttpClient();	
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10*1000);  
			client.getHttpConnectionManager().getParams().setSoTimeout(10*1000); 
			post = new UTF8PostMethod(url);	
			post.addRequestHeader("contentType", "text/html;charset=UTF-8");
			int i=0;
			NameValuePair[] values = new NameValuePair[params.size()];
			Set<Entry<String, String>> entries=params.entrySet();
			for (Entry<String, String> m : entries) {
				values[i++] = new NameValuePair(m.getKey(), m.getValue());
			}
			post.setRequestBody(values);
			int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				params.put("error", "connect fail");
				return null;
			}
			return  post.getResponseBodyAsString();
		} catch (HttpException e) {
			params.put("error", String.valueOf(e));
			SysToolHelper.error("postXml",e);
		} catch (IOException e) {
			params.put("error", String.valueOf(e));
			SysToolHelper.error("postXml",e);
		}catch (RuntimeException re){
			params.put("error", String.valueOf(re));
			SysToolHelper.error("postXml",re);
		} finally {
			if(post != null){
				post.releaseConnection();
			}
		}
		return null;
	}
}
