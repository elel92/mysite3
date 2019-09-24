package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.itcen.mysite.vo.BoardVo;

public class BoardDao {
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.1.80:3306/webdb?characterEncoding=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public boolean first_insert(BoardVo vo) {
		boolean result = false;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			int g_no = 0;
			
			String sql = "select ifnull(max(g_no), 0) from board";
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				g_no = rs.getInt(1) + 1;
			}
			
			sql = "insert into board values(null, ?, ?, 0, now(), ?, 1, 0, ?, ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, g_no);
			pstmt.setString(4, "N");
			pstmt.setInt(5, vo.getUser_no());
			
			int count = pstmt.executeUpdate();
			result = (count == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public BoardVo reply_select(int no, int user_no) {
		BoardVo result = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select g_no, o_no, depth from board where no = ? and user_no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.setInt(2, user_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int g_no = rs.getInt(1);
				int o_no = rs.getInt(2);
				int depth = rs.getInt(3);
				
				result = new BoardVo();
				
				result.setNo(no);
				result.setG_no(g_no);
				result.setO_no(o_no);
				result.setDepth(depth);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public void update_reply(int g_no, int o_no) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "update board set o_no = o_no+1 where no = any(select * from (select no from board where g_no = ? and o_no >= ?) as sub)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, g_no);
			pstmt.setInt(2, o_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean reply_insert(BoardVo vo) {
		boolean result = false;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			int o_no = 0;
			
			String sql = "select o_no from board where g_no = ? and no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, vo.getG_no());
			pstmt.setInt(2, vo.getNo());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				o_no = rs.getInt(1)+1;
			}
			
			update_reply(vo.getG_no(), o_no);
			sql = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?, ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getG_no());
			pstmt.setInt(4, o_no);
			pstmt.setInt(5, vo.getDepth() + 1);
			pstmt.setString(6, "N");
			pstmt.setInt(7, vo.getUser_no());
			
			int count = pstmt.executeUpdate();
			
			result = (count == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public BoardVo select(int no, int user_no) {
		BoardVo result = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select title, contents, status from board where no=? and user_no=?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.setInt(2, user_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = new BoardVo();
				String title = rs.getString(1);
				String contents = rs.getString(2);
				String status = rs.getString(3);
				
				result.setTitle(title);
				result.setContents(contents);
				result.setStatus(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public void update(String title, String contents, int no, int user_no) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = getConnection();
			
			String sql = "update board set title = ?, contents = ? where no = ? and user_no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setInt(3, no);
			pstmt.setInt(4, user_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete(int no, int user_no) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = getConnection();
			
			String sql = "update board set status = ? where no = ? and user_no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "Y");
			pstmt.setInt(2, no);
			pstmt.setInt(3, user_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update_hit(int no, int user_no) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = getConnection();
			
			String sql = "update board set hit = hit+1 where no = ? and user_no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.setInt(2, user_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<BoardVo> searchList(String kwd) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select b.no, b.title, b.contents, b.hit, b.reg_date, b.g_no, b.o_no, b.depth, b.status, b.user_no, u.name from board b, user u where b.user_no = u.no and b.status = 'N' and (b.title like ? or b.contents like ?) order by b.g_no desc, b.depth, b.o_no asc";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "%" + kwd + "%");
			pstmt.setString(2, "%" + kwd + "%");
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int g_no = rs.getInt(6);
				int o_no = rs.getInt(7);
				int depth = rs.getInt(8);
				String status = rs.getString(9);
				int user_no = rs.getInt(10);
				String user_name = rs.getString(11);
				
				BoardVo vo= new BoardVo();
				
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setG_no(g_no);
				vo.setO_no(o_no);
				vo.setDepth(depth);
				vo.setStatus(status);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public List<BoardVo> searchList_page(String kwd, int page_no) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select b.no, b.title, b.contents, b.hit, b.reg_date, b.g_no, b.o_no, b.depth, b.status, b.user_no, u.name from board b, user u where b.user_no = u.no and b.status = 'N' and (b.title like ? or b.contents like ?) order by b.g_no desc, b.depth, b.o_no asc limit ?, 10";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "%" + kwd + "%");
			pstmt.setString(2, "%" + kwd + "%");
			pstmt.setInt(3, page_no*10);
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int g_no = rs.getInt(6);
				int o_no = rs.getInt(7);
				int depth = rs.getInt(8);
				String status = rs.getString(9);
				int user_no = rs.getInt(10);
				String user_name = rs.getString(11);
				
				BoardVo vo= new BoardVo();
				
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setG_no(g_no);
				vo.setO_no(o_no);
				vo.setDepth(depth);
				vo.setStatus(status);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public List<BoardVo> getList() {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select b.no, b.title, b.contents, b.hit, b.reg_date, b.g_no, b.o_no, b.depth, b.status, b.user_no, u.name from board b, user u where b.user_no = u.no and b.status = 'N' order by b.g_no desc, b.o_no asc";
			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int g_no = rs.getInt(6);
				int o_no = rs.getInt(7);
				int depth = rs.getInt(8);
				String status = rs.getString(9);
				int user_no = rs.getInt(10);
				String user_name = rs.getString(11);
				
				BoardVo vo= new BoardVo();
				
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setG_no(g_no);
				vo.setO_no(o_no);
				vo.setDepth(depth);
				vo.setStatus(status);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public List<BoardVo> limit_getList(int page_no) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select b.no, b.title, b.contents, b.hit, b.reg_date, b.g_no, b.o_no, b.depth, b.status, b.user_no, u.name from board b, user u where b.user_no = u.no and b.status = 'N' order by b.g_no desc, b.o_no asc limit ?, 10";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, page_no*10);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int g_no = rs.getInt(6);
				int o_no = rs.getInt(7);
				int depth = rs.getInt(8);
				String status = rs.getString(9);
				int user_no = rs.getInt(10);
				String user_name = rs.getString(11);
				
				BoardVo vo= new BoardVo();
				
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setG_no(g_no);
				vo.setO_no(o_no);
				vo.setDepth(depth);
				vo.setStatus(status);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
