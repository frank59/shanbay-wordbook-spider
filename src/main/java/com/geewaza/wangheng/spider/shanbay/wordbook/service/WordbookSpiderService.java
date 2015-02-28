package com.geewaza.wangheng.spider.shanbay.wordbook.service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geewaza.wangheng.spider.shanbay.wordbook.config.ConstantValues;
import com.geewaza.wangheng.spider.shanbay.wordbook.dao.WordDao;
import com.geewaza.wangheng.spider.shanbay.wordbook.model.Word;
import com.geewaza.wangheng.spider.shanbay.wordbook.util.Request;

public class WordbookSpiderService {
	private static Logger logger = LoggerFactory.getLogger(WordbookSpiderService.class);
	
	private String outputPath;
	private String outputFileName;
	private String wordbookURL;
	private String bookId;
	private WordDao wordDao;
	
	public void setWordbookURL(String wordbookURL) {
		this.wordbookURL = wordbookURL;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public void setWordDao(WordDao wordDao) {
		this.wordDao = wordDao;
	}
	
	public void loadWordbookDataToDB() throws Exception {
		logger.info("开始爬取单词信息： bookid=" + bookId);
		String tableName = "t_wordbook_" + bookId;
		
		List<Word> result = new ArrayList<Word>();
		List<String> wordListUrls = getWordlistUrls(this.wordbookURL.replaceAll("@BOOKID", bookId));
		for (String wordListUrl : wordListUrls) {
			List<Word> listWords = getListWord(wordListUrl);
			if (listWords != null) {
				result.addAll(listWords);
			}
		}
		
		wordDao.dropTable(tableName);
		wordDao.createTable(tableName);
		for (Word word : result) {
			wordDao.insert(tableName, word);
		}
		
		logger.info("爬取单词信息： bookid=" + bookId + "完成， tableName：" + tableName);
	}
	
	
	public void loadWordbookDataToFile() throws Exception {
		logger.info("开始爬取单词信息： bookid=" + bookId);
		File dir = new File(outputPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String outputFileFullName = outputPath + outputFileName.replaceAll("@BOOKID", bookId);
		File outputFile = new File(outputFileFullName);
		outputFile.createNewFile();
		List<Word> result = new ArrayList<Word>();
		List<String> wordListUrls = getWordlistUrls(this.wordbookURL.replaceAll("@BOOKID", bookId));
		for (String wordListUrl : wordListUrls) {
			List<Word> listWords = getListWord(wordListUrl);
			if (listWords != null) {
				result.addAll(listWords);
			}
		}
		
		FileWriter writer = new FileWriter(outputFile);
		try {
			for (Word word : result) {
				writer.write(getWordLine(word) + "\n");
			}
		} finally {
			writer.close();
		}
		logger.info("爬取单词信息： bookid=" + bookId + "完成， 写入：" + outputFile.getAbsolutePath());
	}
	
	/**
	 * 获取指定单词书下的个个List的URL
	 * @param wordbookUrl
	 * @return
	 * @throws Exception
	 */
	private List<String> getWordlistUrls(String wordbookUrl) throws Exception {
		logger.info("单词list : " + wordbookUrl);
		List<String> resultList = new ArrayList<String>();
		String response = Request.requestGet(wordbookUrl);
		Document doc = Jsoup.parse(response);
		Elements listElements = doc.select("td.wordbook-wordlist-name a");
		for (int i = 0; i < listElements.size(); i++) {
			Element item = listElements.get(i);
			resultList.add(ConstantValues.SHANBAY_HOST + item.attr("href"));
		}
		return resultList;
	}
	
	/**
	 * 逐页获取单词
	 * @param listUrl
	 * @return
	 * @throws Exception
	 */
	private List<Word> getListWord(String listUrl) throws Exception {
		List<Word> resultList = new ArrayList<Word>();
		for (int page = 1; ; page++) {
			String pageUrl = listUrl + "?page=" + page;
			List<Word> pageWords = null;
			Exception error = null;
			for (int i = 0; i < 3; i++) {
				try {
					pageWords = getPageWord(pageUrl);
					error = null;
					break;
				} catch (Exception e) {
					error = e;
					TimeUnit.SECONDS.sleep(1);
				}
			}
			if (error != null) {
				logger.error(error.getMessage(), error);
			}
			if (pageWords == null) {
				break;
			}
			resultList.addAll(pageWords);
		}
		return resultList;
	}
	
	/**
	 * 获取单词页的所有单词数据
	 * @param pageUrl
	 * @return 如果值为null 说明该页没有单词
	 * @throws Exception 
	 */
	private List<Word> getPageWord(String pageUrl) throws Exception {
		List<Word> resultList = new ArrayList<Word>();
		String response = Request.requestGet(pageUrl);
		Document doc = Jsoup.parse(response);
		Elements trElements = doc.select("table.table.table-bordered.table-striped tr");
		if (trElements.size() == 0) {
			return null;
		}
		for (int i = 0; i < trElements.size(); i++) {
			Element tr = trElements.get(i);
			Word word = new Word(tr.select("strong").first().text(), tr.select("td.span10").first().text());
			resultList.add(word);
		}
		return resultList;
	}
	
	/**
	 * 将单词转换成标准的一行
	 * @param word
	 * @return
	 */
	public static String getWordLine(Word word) {
		StringBuilder builder = new StringBuilder();
		builder.append(word.getWord()).append("\t").append(word.getExpression());
		return builder.toString();
	}
}
