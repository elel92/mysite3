package kr.co.itcen.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.BoardVo;

@Repository
public class BoardDao {
	@Autowired
	SqlSession sqlSession;
	
	public boolean first_insert(BoardVo vo) {
		boolean result = false;
		int g_no = sqlSession.selectOne("board.select_g_no");
		vo.setG_no(g_no+1);
		vo.setStatus("N");
		
		sqlSession.insert("board.first_insert", vo);
		result = true;
		
		return result;
	}
	
	public BoardVo reply_select(int no, int user_no) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("no", no);
		map.put("user_no", user_no);
		
		BoardVo result = sqlSession.selectOne("board.reply_select", map);
		
		return result;
	}
	
	public void update_reply(int g_no, int o_no) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("g_no", g_no);
		map.put("o_no", o_no);
		
		sqlSession.update("board.update_reply", map);
	}
	
	public boolean reply_insert(BoardVo vo) {
		boolean result = false;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("g_no", vo.getG_no());
		map.put("no", vo.getNo());
		
		int o_no = sqlSession.selectOne("board.select_o_no", map);
		
		vo.setStatus("N");
		vo.setO_no(o_no + 1);
		vo.setDepth(vo.getDepth()+1);
		
		sqlSession.insert("board.reply_insert", vo);
		
		result = true;
		
		return result;
	}
	
	public BoardVo select(int no, int user_no) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("no", no);
		map.put("user_no", user_no);
		
		BoardVo result = sqlSession.selectOne("board.select", map);
		
		return result;
	}
	
	public void update(String title, String contents, int no, int user_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("title", title);
		map.put("contents", contents);
		map.put("no", no);
		map.put("user_no", user_no);
		
		sqlSession.selectOne("board.update", map);
	}
	
	public void delete(int no, int user_no) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("status", "Y");
		map.put("no", no);
		map.put("user_no", user_no);
		
		sqlSession.update("board.delete", map);
	}
	
	public void update_hit(int no, int user_no) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("no", no);
		map.put("user_no", user_no);
		
		sqlSession.update("board.update_hit", map);
	}
	
	public List<BoardVo> searchList(String kwd) {
		kwd = "%" + kwd + "%";
		
		List<BoardVo> result = sqlSession.selectList("board.searchList", kwd);
		
		return result;
	}
	
	public List<BoardVo> searchList_page(String kwd, int page_no) {
		kwd = "%" + kwd + "%";
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("kwd", kwd);
		map.put("page_no", page_no);
		
		List<BoardVo> result = sqlSession.selectList("board.searchList_page", map);
		
		return result;
	}
	
	public List<BoardVo> getList() {
		List<BoardVo> result = sqlSession.selectList("board.getList");
		
		return result;
	}
	
	public List<BoardVo> limit_getList(int page_no) {
		List<BoardVo> result = sqlSession.selectList("board.limit_getList", page_no);
		
		return result;
	}
}
