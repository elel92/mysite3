package kr.co.itcen.mysite.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@RequestMapping("")
	public String list(@RequestParam(value = "kwd", required = false) String kwd,
			           @RequestParam(value = "page_no", required = false, defaultValue = "0") int page_no,
			           @RequestParam(value = "next_page_count", required = false, defaultValue = "1") int next_page_count,
			           Model model) {

		List<BoardVo> page_list = null;
		List<BoardVo> list = null;
		
		if(kwd != null) {
			model.addAttribute("kwd", kwd);
			
			page_list = boardService.searchList_page(kwd, page_no);
			list = boardService.searchList(kwd);
		} else {
			page_list = boardService.limit_getList(page_no);
			list = boardService.getList();
		}
		
		model.addAttribute("page_list", page_list);
		model.addAttribute("page_num", page_no+1);
		model.addAttribute("list", list);
		model.addAttribute("next_page_count", next_page_count);
		
		return "board/list";
	}
	
	@RequestMapping(value="view", method=RequestMethod.GET)
	public String view(@RequestParam(value = "no", required = false, defaultValue = "0") int no,
			           @RequestParam(value = "user_no", required = false, defaultValue = "0") int user_no,
			           Model model) {
		
		BoardVo vo = boardService.select(no, user_no);
		
		model.addAttribute("read_list", vo);
		boardService.update_hit(no, user_no);
		
		return "board/view";
	}
	
	@RequestMapping(value="write", method=RequestMethod.GET)
	public String write() {
		
		return "board/write";
	}
	
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(@ModelAttribute BoardVo boardVo, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		
		if(userVo != null) {
			boardVo.setUser_no(userVo.getNo());
			
			boardService.write(boardVo);
		}
		
		return "redirect:/board";
	}
	
	@RequestMapping(value="reply", method=RequestMethod.GET)
	public String reply(@RequestParam(value = "no", required = false, defaultValue = "0") int no,
			            @RequestParam(value = "user_no", required = false, defaultValue = "0") int user_no,
	                    Model model) {
		
		BoardVo vo = boardService.reply_select(no, user_no);
		
		model.addAttribute("reply_vo", vo);
		
		return "board/write";
	}
	
	@RequestMapping(value="reply", method=RequestMethod.POST)
	public String reply(@ModelAttribute BoardVo boardVo, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		boardVo.setUser_no(userVo.getNo());
		
		boardService.reply_insert(boardVo);
		
		return "redirect:/board";
	}
	
	@RequestMapping(value="modify", method=RequestMethod.GET)
	public String modify(@RequestParam(value = "no", required = false, defaultValue = "0") int no,
                         @RequestParam(value = "user_no", required = false, defaultValue = "0") int user_no,
                         Model model) {
		
		BoardVo update_list = boardService.select(no, user_no);
		
		model.addAttribute("update_list", update_list);
		
		return "board/modify";
	}
	
	@RequestMapping(value="modify", method=RequestMethod.POST)
	public String modify(@ModelAttribute BoardVo vo) {
		boardService.update(vo);
		
		return "redirect:/board/view?no="+vo.getNo()+"&user_no="+vo.getUser_no();
	}
	
	@RequestMapping(value="delete", method=RequestMethod.GET)
	public String delete(@RequestParam(value = "no", required = false, defaultValue = "0") int no,
                         @RequestParam(value = "user_no", required = false, defaultValue = "0") int user_no) {
		
		boardService.delete(no, user_no);
		return "redirect:/board";
	}
}
