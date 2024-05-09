<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>글쓰기</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/write.css">
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"
            integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ="
            crossorigin="anonymous"></script>
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
    <sec:authentication property="principal" var="principal"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>
<div class="wrap">

    <br> <br> <br>
    <div class="content">
        <!-- 검색어 입력 폼 -->
        <form action="<c:url value='/user/book-search'/>" method="get"
              accept-charset="UTF-8">
            <div class="form-group">
                <input type="text" id="searchKeyword" name="keyword" class="form-control" required>
                <button type="submit" class="btn btn-info">책 검색</button>
            </div>
        </form>
        <br>
        <hr>
        <h2 class="form-header">독후감 작성</h2>
        <div>
            <input type="text" class="write-input" name="title" placeholder="도서명" readonly="readonly"
                   value="${not empty book ? book.name : 'No Book Title'}">
        </div>
        <br>
        <form class="form-write" action="/user/save" method="post" accept-charset="UTF-8">
            <input type="hidden" name="userEmail" value="${principal.username}">
            <input type="text" class="write-input" name="title" placeholder="독후감 제목(필수)" required>
            <input type="text" class="write-input" name="bookIsbn" placeholder="ISBN"
                   value="${not empty book ? book.isbn : 'No Book ISBN'}" readonly="readonly">
            <textarea class="write-input report-input" name="content" placeholder="내용(필수)" required></textarea>
            <input type="checkbox" name="publicBool" checked="checked">공개
            <div class="btn-area">
                <input type="submit" class="btn-write" value="작성" onclick="msg()" required>
            </div>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        let m = "${msg}";
        if (m !== "") {
            alert(m);
        }
    });
</script>
</html>