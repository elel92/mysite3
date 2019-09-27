package kr.co.itcen.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	@Autowired
	private SqlSession sqlSession;
	
	public Boolean insert(GuestbookVo vo) {
		int count = sqlSession.insert("guestbook.insert", vo);
		
		return count == 1;		
	}
	
	public void set_delete(int no, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("no", no);
		map.put("password", password);
		
		sqlSession.delete("guestbook.set_delete", map);
	}
	
	public void delete() {
		sqlSession.delete("guestbook.delete");
	}
	
	public List<GuestbookVo> getList() {
		List<GuestbookVo> result = sqlSession.selectList("guestbook.getList");
		
		return result;
	}
}
