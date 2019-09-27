package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	public List<BoardVo> searchList_page(String kwd, int page_no) {
		List<BoardVo> list = boardDao.searchList_page(kwd, page_no);
		
		return list;
	}
	
	public List<BoardVo> searchList(String kwd) {
		List<BoardVo> list = boardDao.searchList(kwd);
		
		return list;
	}
	
	public List<BoardVo> limit_getList(int page_no) {
		List<BoardVo> list = boardDao.limit_getList(page_no);
		
		return list;
	}
	
	public List<BoardVo> getList() {
		List<BoardVo> list = boardDao.getList();
		
		return list;
	}
	
	public BoardVo select(int no, int user_no) {
		BoardVo vo = boardDao.select(no, user_no);
		
		return vo;
	}
	
	public void update_hit(int no, int user_no) {
		boardDao.update_hit(no, user_no);
	}
	
	public void write(BoardVo vo) {
		boardDao.first_insert(vo);
	}
	
	public BoardVo reply_select(int no, int user_no) {
		return boardDao.reply_select(no, user_no);
	}
	
	public void reply_insert(BoardVo vo) {
		boardDao.reply_insert(vo);
	}
	
	public void update(BoardVo vo) {
		boardDao.update(vo.getTitle(), vo.getContents(), vo.getNo(), vo.getUser_no());
	}
	
	public void delete(int no, int user_no) {
		boardDao.delete(no, user_no);
	}
}
