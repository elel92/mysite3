package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.GuestbookDao;
import kr.co.itcen.mysite.vo.GuestbookVo;

@Service
public class GusetbookService {
	@Autowired
	private GuestbookDao guestbookDao;
	
	public List<GuestbookVo> getList() {
		return guestbookDao.getList();
	}

	public void insert(GuestbookVo vo) {
		guestbookDao.insert(vo);
	}
	
	public void delete(int no, String password) {
		guestbookDao.set_delete(no, password);
	}
}
