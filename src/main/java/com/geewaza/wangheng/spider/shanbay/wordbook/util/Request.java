package com.geewaza.wangheng.spider.shanbay.wordbook.util;


import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * @author william
 * 
 */
public class Request {

	private static Logger logger = Logger.getLogger(Request.class);

	public static String requestGet(String url) throws Exception {
		return requestGet(url, 5000);
	}

	/**
	 * 短链接
	 * 
	 * @throws Exception
	 */
	public static String requestGet1(String url, int timeout) throws Exception {
		HttpClient client = new HttpClient();
		String result = null;
		GetMethod method = new GetMethod(url);

		// method.setRequestHeader("Accept","*/*");
		// method.setRequestHeader("Connection","close");
		// method.setRequestHeader("Accept-Language","zh-cn");
		// method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// method.setRequestHeader("Cache-Control","no-cache");
//		timeout = 5000;//最大超时1分钟
		method.getParams().setSoTimeout(timeout);
		// 连接超时
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Cookie",cookie);
		// method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
//			long start = System.currentTimeMillis();
			client.executeMethod(method);
//			long time1 = System.currentTimeMillis() - start;
//			start = System.currentTimeMillis();
			result = method.getResponseBodyAsString();
//			long time2 = System.currentTimeMillis() - start;
//			getCost(url, result,time1, time2);
		} catch (Exception e) {
			logger.error("HTTP请求url失败,timeout:" + timeout + ",url:" + url);
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 短链接 超时尝试3次
	 * 
	 * @throws Exception
	 */
	public static String requestGet(String url, int timeout) throws Exception {
		HttpClient client = new HttpClient();
		String result = null;
		GetMethod method = new GetMethod(url);

		method.setRequestHeader("Accept","*/*");
		method.setRequestHeader("Connection","keep-alive");
		method.setRequestHeader("Accept-Language","zh-cn");
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36");
		method.setRequestHeader("Cache-Control","no-cache");
		method.getParams().setSoTimeout(timeout);
		// 连接超时
		client.getHttpConnectionManager().getParams().setConnectionTimeout(500);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Cookie",cookie);
		// method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		for (int i = 0; i < 3; i++) {
			try {
				client.executeMethod(method);
				result = method.getResponseBodyAsString();
			} catch (Exception e) {
				logger.error("HTTP请求url失败,timeout:" + timeout + ",url:" + url);
				logger.info("第" + (i + 1) + "次失败.e.getMessage() = "
						+ e.getMessage());
				if (i < 2) {
					TimeUnit.SECONDS.sleep(1);
					continue;
				}
				throw e;
			} finally {
				method.releaseConnection();
			}
			break;
		}
		return result;
	}
	
	/**
	 * 短链接 只请求一次 超时抛异常
	 * 
	 * @throws Exception
	 */
	public static String requestGetOnce(String url, int timeout)
			throws Exception {
		HttpClient client = new HttpClient();
		String result = null;
		GetMethod method = new GetMethod(url);

		// method.setRequestHeader("Accept","*/*");
		// method.setRequestHeader("Connection","close");
		// method.setRequestHeader("Accept-Language","zh-cn");
		// method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// method.setRequestHeader("Cache-Control","no-cache");
		method.getParams().setSoTimeout(timeout);
		// 连接超时
		client.getHttpConnectionManager().getParams().setConnectionTimeout(500);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Cookie",cookie);
		// method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(method);
			result = method.getResponseBodyAsString();

		} finally {
			method.releaseConnection();
		}
		return result;
	}
	
	public static String requestGet(String url, int soTimeout, int connTimeout, String proxyHost, int proxyPort) throws Exception {
		HttpClient client = new HttpClient();
		String result = null;
		GetMethod method = new GetMethod(url);

		method.setRequestHeader("Accept","*/*");
		method.setRequestHeader("Connection","keep-alive");
		method.setRequestHeader("Accept-Language","zh-cn");
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36");
		method.setRequestHeader("Cache-Control","no-cache");
		method.getParams().setSoTimeout(soTimeout);
		// 连接超时
		logger.info("proxyHost:" + proxyHost + "; proxyPort" + proxyPort);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeout);
		client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Cookie",cookie);
		// method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		for (int i = 0; i < 3; i++) {
			try {
				int resultCode = client.executeMethod(method);
				logger.info(resultCode + "");
				result = method.getResponseBodyAsString();
			} catch (Exception e) {
				logger.error("HTTP请求url失败,soTimeout:" + soTimeout + ",connTimeout:" + connTimeout + ",url:" + url);
				logger.info("第" + (i + 1) + "次失败.e.getMessage() = "
						+ e.getMessage());
				if (i < 2) {
					TimeUnit.SECONDS.sleep(1);
					continue;
				}
				throw e;
			} finally {
				method.releaseConnection();
			}
			break;
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		System.out
				.println(Request
						.requestGet("http://1.103.88.154/searches/soku/top/v1/rank_anime.json?genre=5014&headnum=1&tailnum=1&onesiteflag=1"));

	}

}
