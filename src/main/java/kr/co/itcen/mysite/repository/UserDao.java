package kr.co.itcen.mysite.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.exception.UserDaoException;
import kr.co.itcen.mysite.vo.UserVo;

@Repository
public class UserDao {
	@Autowired
	private SqlSession sqlSession;
	
	public Boolean insert(UserVo vo) throws UserDaoException {
		int count = sqlSession.insert("user.insert", vo);
		
		return count == 1;		
	}

	public UserVo get(UserVo vo) {
		UserVo result = sqlSession.selectOne("getByEmailAndPassword1", vo);
		
		return result;
	}
	
	public UserVo get(String email, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		
		UserVo result = sqlSession.selectOne("getByEmailAndPassword2", map);
		
		return result;
	}
	
	public void update(int no, String name, String gender, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("no", no);
		map.put("name", name);
		map.put("gender", gender);
		map.put("password", password);
		
		sqlSession.update("user.update", map);
	}
}