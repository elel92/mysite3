<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div id="navigation">
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">강민호</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/guest">방명록</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/board">게시판</a></li>
	</ul>
</div>