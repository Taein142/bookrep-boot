<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>검색결과</title>
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/resources/css/search.css">
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/resources/css/page.css">
</head>
<script type="text/javascript">

</script>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>

<h1 id="search-title">"${keyword}"에 대한 검색결과</h1>
<div class="container">

    <div class="section">
        <h2>유저</h2>
        <c:if test="${empty userList}">
            <p>해당 유저가 존재하지 않습니다</p>
        </c:if>
        <div class="page-container">
            <ul>
                <c:forEach items="${userList}" var="user">
                    <li class="display">
                        <a href="/user/feed/${user.email}">
                                ${user.name} </a>
                        <br>
                        <a href="/user/feed/${user.email}">
                                ${user.email} </a>
                        <br>
                        <a href="/user/feed/${user.email}">
                            <img src="<%=request.getContextPath()%>/resources/images/${user.image}"> </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<div class="page">
    <!-- 이전 버튼 -->
    <c:if test="${currentUserPageNum > 1}">
        <a href="?keyword=${keyword}&userPageNum=${currentUserPageNum - 1}&bookPageNum=${currentBookPageNum}"
           class="page-link">이전</a>
    </c:if>
    <!-- 다음 버튼 -->
    <c:if test="${currentUserPageNum * 6 < totalUserSize}">
        <a href="?keyword=${keyword}&userPageNum=${currentUserPageNum + 1}&bookPageNum=${currentBookPageNum}"
           class="page-link">다음</a>
    </c:if>
</div>
<div class="container">
    <div class="section">
        <h2>도서</h2>
        <c:if test="${empty bookList}">
            <p>해당 도서가 존재하지 않습니다</p>
        </c:if>
        <div class="page-container">
            <ul>
                <c:forEach items="${bookList}" var="book">
                    <li class="display">
                        <a href="/user/book-detail?isbn=${book.isbn}">
                                ${book.name} </a>
                        <br>
                        <a href="/user/book-detail?isbn=${book.isbn}">
                            <img src="${book.image}"> </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<div class="page">
    <!-- 이전 버튼 -->
    <c:if test="${currentBookPageNum > 1}">
        <a href="?keyword=${keyword}&userPageNum=${currentUserPageNum}&bookPageNum=${currentBookPageNum - 1}"
           class="page-link">이전</a>
    </c:if>
    <!-- 다음 버튼 -->
    <c:if test="${currentBookPageNum * 6 < totalBookSize}">
        <a href="?keyword=${keyword}&userPageNum=${currentUserPageNum}&bookPageNum=${currentBookPageNum + 1}"
           class="page-link">다음</a>
    </c:if>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
