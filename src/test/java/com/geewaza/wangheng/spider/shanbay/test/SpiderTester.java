package com.geewaza.wangheng.spider.shanbay.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.geewaza.wangheng.spider.shanbay.wordbook.util.Request;

public class SpiderTester {
	
	private static String shanbay_Host = "http://www.shanbay.com";
	
	public static void main(String[] args) throws Exception {
		test04();
	}

	private static void test04() throws Exception {
		String url = "http://www.shanbay.com/wordlist/5440/36976/?page=";
		for (int page = 1; ; page++) {
			String response = Request.requestGet(url + page);
			Document doc = Jsoup.parse(response);
			Elements trElements = doc.select("table.table.table-bordered.table-striped tr");
			if (trElements.size() == 0) {
				System.out.println(trElements.size());
				break;
			}
			for (int i = 0; i < trElements.size(); i++) {
				Element tr = trElements.get(i);
				System.out.println(tr.select("strong").first().text() + "\t" + tr.select("td.span10").first().text());
			}
		}
		
	}

	private static void test03() throws Exception {
		String url = "http://www.shanbay.com/wordlist/5440/36976/?page=12";
		String response = Request.requestGet(url);
		Document doc = Jsoup.parse(response);
		Elements trElements = doc.select("table.table.table-bordered.table-striped tr");
		if (trElements.size() == 0) {
			System.out.println(trElements.size());
		}
		for (int i = 0; i < trElements.size(); i++) {
			Element tr = trElements.get(i);
			System.out.println(tr.select("strong").first().text());
			System.out.println(tr.select("td.span10").first().text());
		}
	}

	private static void test02() throws Exception {
		String url = "http://www.shanbay.com/wordbook/5440/";
		String response = Request.requestGet(url);
		Document doc = Jsoup.parse(response);
		Elements listElements = doc.select("td.wordbook-wordlist-name a");
		for (int i = 0; i < listElements.size(); i++) {
			Element item = listElements.get(i);
			System.out.println(shanbay_Host + item.attr("href"));
		}
	}

	private static void test01() throws Exception {
		String url = "http://www.shanbay.com/wordbook/5440/";
		String response = Request.requestGet(url);
		System.out.println(response.length());
	}
	
	

}
