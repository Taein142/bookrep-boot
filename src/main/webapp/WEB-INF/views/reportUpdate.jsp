<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>독후감 수정</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reportUpdate.css">
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
    <sec:authentication property="principal" var="principal"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>
<div id="total-body">
    <div id="update-form">
        <form action="${pageContext.request.contextPath}/user/apply-update" method="post">
            <div id="report-top">
                <input type="hidden" name="id" value="${report.id}">
                <input type="hidden" name="userEmail" value="${principal.username}">
                <input type="hidden" name="bookIsbn" value="${report.bookIsbn}">
                <label for="title">제목:</label>
                <input type="text" id="title" name="title" value="${report.title}" required>
            </div>
            <div id="report-body">
                <label for="content">내용:</label>
                <textarea id="content" name="content" rows="5" required>${report.content}</textarea>
                <input type="hidden" name="publicBool" value="${report.publicBool}">
                <br>
            </div>
            <br>
            <button type="submit">수정완료</button>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script type="text/javascript">

</script>
</html>