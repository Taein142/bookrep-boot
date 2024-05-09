<%@ page language="java" contentType="text/html; charset=UTF-8"

         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>북마크</title>
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

<h1 id="search-title">"${email}"님의 북마크 목록</h1>
<div class="container">

    <div class="section">
        <h2>북마크</h2>
        <c:if test="${empty bookmarkList}">
            <p>북마크한 도서가 없습니다</p>
        </c:if>
        <div class="page-container">
            <ul>
                <c:forEach items="${bookmarkList}" var="book">
                    <li class="display">
                        <a href="${pageContext.request.contextPath}/user/book-detail?isbn=${book.isbn}">
                                ${book.name} </a>
                        <br>
                        <a href="${pageContext.request.contextPath}/user/book-detail?isbn=${book.isbn}">
                            <img src="${book.image}"></a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<div class="page">
    <!-- 이전 버튼 -->
    <c:if test="${currentPageNum > 1}">
        <a
                href="?pageNum=${currentPageNum - 1}"
                class="page-link">이전</a>
    </c:if>
    <!-- 다음 버튼 -->
    <c:if test="${currentPageNum * 6 < totalBookSize}">
        <a
                href="?pageNum=${currentPageNum + 1}"
                class="page-link">다음</a>
    </c:if>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
