<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath}/board?a=search" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:set var='count' value = '${fn:length(list)}'></c:set>
					<c:forEach items='${page_list}' var='li' varStatus='st'>
						<tr>
							<td>${count-((page_num-1)*10)-st.index}</td>
							<c:choose>
								<c:when test="${li.depth > 0}">
									<td class="label" style="padding-left:${30*li.depth-20}px;text-align:left">
										<img src='${pageContext.servletContext.contextPath }/assets/images/reply.png'/>
										<a href="${pageContext.servletContext.contextPath}/board?a=view&no=${li.no}&user_no=${li.user_no}">${li.title}</a>
									</td>
								</c:when>
								<c:otherwise>
									<td class="label" style="text-align:left">
										<a href="${pageContext.servletContext.contextPath}/board?a=view&no=${li.no}&user_no=${li.user_no}">${li.title}</a>
									</td>
								</c:otherwise>
							</c:choose>
							<td>${li.user_name}</td>
							<td>${li.hit}</td>
							<td>${li.reg_date}</td>
							<td>
							<c:if test="${li.user_no == authUser.no}">
							<a href="${pageContext.servletContext.contextPath}/board?a=delete&no=${li.no}&user_no=${li.user_no}" class="del">삭제</a>
							</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				
				<div class="pager">
					<ul>
						<c:choose>
							<c:when test="${kwd != null}">
								<c:set var='search_page_count' value = '${fn:length(list)}'></c:set>
								
								<c:if test="${page_num > 10}">
									<li><a href="${pageContext.servletContext.contextPath}/board?a=search&page_no=${(next_page_count-2)*10}&kwd=${kwd}&next_page_count=${next_page_count-1}">◀</a></li>
								</c:if>
								
								<c:forEach var='c'  begin ='1' end='${(search_page_count-1)/10+1}' step='1'>
									<c:if test="${next_page_count*10 - c < 10 && next_page_count*10 - c >= 0}">
										<c:choose>
											<c:when test="${page_num == c}">
												<li class="selected">
													<a href="${pageContext.servletContext.contextPath}/board?a=search&page_no=${c-1}&kwd=${kwd}&next_page_count=${next_page_count}">${c}</a>
												</li>
											</c:when>
											<c:otherwise>
												<li>
													<a href="${pageContext.servletContext.contextPath}/board?a=search&page_no=${c-1}&kwd=${kwd}&next_page_count=${next_page_count}">${c}</a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								
								<c:if test="${page_num <= (search_page_count-1)/10 && (search_page_count-1)/10 > 10}">
									<li><a href="${pageContext.servletContext.contextPath}/board?a=search&page_no=${next_page_count*10}&kwd=${kwd}&next_page_count=${next_page_count+1}">▶</a></li>
								</c:if>
							</c:when>
							
							<c:otherwise>
								<c:set var='page_count' value='${fn:length(list)}'></c:set>
								
								<c:if test="${page_num > 10}">
									<li><a href="${pageContext.servletContext.contextPath}/board?page_no=${(next_page_count-2)*10}&next_page_count=${next_page_count-1}">◀</a></li>
								</c:if>
								
								<c:forEach var='c' begin ='1' end='${(page_count-1)/10+1}' step='1'>
									<c:if test="${next_page_count*10 - c < 10 && next_page_count*10 - c >= 0}">
										<c:choose>
											<c:when test="${page_num == c}">
												<li class="selected">
													<a href="${pageContext.servletContext.contextPath}/board?page_no=${c-1}&next_page_count=${next_page_count}">${c}</a>
												</li>
											</c:when>
											<c:otherwise>
												<li>
													<a href="${pageContext.servletContext.contextPath}/board?page_no=${c-1}&next_page_count=${next_page_count}">${c}</a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								
								<c:if test="${page_num <= (page_count-1)/10 && (page_count-1)/10 > 10}">
									<li><a href="${pageContext.servletContext.contextPath}/board?page_no=${next_page_count*10}&next_page_count=${next_page_count+1}">▶</a></li>
								</c:if>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				
				<c:if test="${not empty authUser}">
					<div class="bottom">
						<a href="${pageContext.servletContext.contextPath}/board?a=writeform" id="new-book">글쓰기</a>
					</div>		
				</c:if>	
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>