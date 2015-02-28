package com.geewaza.wangheng.spider.shanbay.wordbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.geewaza.wangheng.spider.shanbay.wordbook.model.Word;

public class WordDao extends JdbcDaoSupport {
	
	public void createTable(String tableName) {
		String sql = "CREATE TABLE `" + tableName + "`("
				+ "`id` int(20) NOT NULL AUTO_INCREMENT,"
				+ "`word` varchar(255) NOT NULL,"
				+ "`expression` varchar(255) DEFAULT NULL,"
				+ "PRIMARY KEY (`id`)"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;";
				
		getJdbcTemplate().execute(sql);
	}
	
	public List<Map<String, Object>> find(String tableName) {
		String sql = "select id, word, expression from " + tableName ;
		return getJdbcTemplate().queryForList(sql);
	}
	
	public int insert(String tableName, Word word) {
		String sql = "insert into " + tableName + "(word, expression) values(?, ?)";
		Object[] obj = {word.getWord(), word.getExpression()};
		return getJdbcTemplate().update(sql, obj);
	}
	
	public void dropTable(String tableName) {
		String sql = "drop table if exists `" + tableName +"`";
		getJdbcTemplate().execute(sql);
	}
}
