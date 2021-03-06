package com.cglee079.changoos.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cglee079.changoos.config.annotation.SolrData;
import com.cglee079.changoos.model.AdminVo;
import com.cglee079.changoos.model.StudyVo;
import com.cglee079.changoos.model.Role;

@Repository
public class StudyDao {
	private static final String namespace = "com.cglee079.changoos.mapper.StudyMapper";
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public StudyVo get(int seq) {
		return sqlSession.selectOne(namespace +".get", seq);
	}
	
	public StudyVo getBefore(int seq, String category) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("seq", seq);
		map.put("category", category);
		return sqlSession.selectOne(namespace +".getBefore", map);
	}
	
	public StudyVo getAfter(int seq, String category) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("seq", seq);
		map.put("category", category);
		return sqlSession.selectOne(namespace +".getAfter", map);
	}

	public List<StudyVo> list(Map<String, Object> params) {
		return sqlSession.selectList(namespace +".list", params);
	}
	
	public int count(Map<String, Object> params) {
		return sqlSession.selectOne(namespace +".count", params);
	}

	public List<String> getCategories() {
		return sqlSession.selectList(namespace +".getCategories");
	}
	
	@SolrData
	public int insert(StudyVo study) {
		sqlSession.insert(namespace +".insert", study);
		return study.getSeq();
	}

	@SolrData
	public boolean delete(int seq) {
		return  sqlSession.delete(namespace +".delete", seq) == 1;
	}

	@SolrData
	public boolean update(StudyVo study) {
		return  sqlSession.update(namespace +".update", study) == 1;
	}
}
