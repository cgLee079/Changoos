package com.cglee079.changoos.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cglee079.changoos.model.BoardVo;
import com.cglee079.changoos.model.BoardComtVo;

@Repository
public class BoardComtDao {
	private static final String namespace = "com.cglee079.changoos.mapper.BoardComtMapper";
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<BoardComtVo> list() {
		return sqlSession.selectList(namespace +".list");
	}

	public List<BoardComtVo> list(int boardSeq, int startRow, int perPgLine) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("boardSeq", boardSeq);
		map.put("startRow", startRow);
		map.put("perPgLine", perPgLine);
		return sqlSession.selectList(namespace +".list", map);
	}

	public int count(int boardSeq) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("boardSeq", boardSeq);
		return sqlSession.selectOne(namespace +".count", map);
	}

	public boolean insert(BoardComtVo bcomt) {
		return sqlSession.insert(namespace + ".insert", bcomt) == 1;
	}

	public boolean delete(int seq) {
		return sqlSession.delete(namespace + ".delete", seq) == 1;
	}

	public BoardComtVo get(int seq) {
		return sqlSession.selectOne(namespace +".get", seq);
	}
	
	public boolean update(int seq, String contents) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("seq", seq);
		map.put("contents", contents);
		return sqlSession.update(namespace +".update", map) == 1;
	}


}