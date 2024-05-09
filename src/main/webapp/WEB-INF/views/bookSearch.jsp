<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>책 검색</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bookSearch.css">
</head>
<body>

<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>


<div class="container">
    <h2>책 검색</h2>

    <!-- 검색어 입력 폼 -->
    <form action="<c:url value='/user/book-search' />" method="get">
        <div class="form-group">
            <input type="text"
                   id="searchKeyword" name="keyword" class="form-control" required>
            <button type="submit" class="btn btn-info">검색</button>
        </div>
    </form>
    <br>
    <hr>
    <br>
    <c:if test="${not empty bookList}">
        <!-- 현재 페이지 설정 -->
        <c:choose>
            <c:when test="${not empty param.pageNum}">
                <c:set var="currentPageNum" value="${param.pageNum}"/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${not empty bookList}">
                        <c:set var="currentPageNum" value="${bookList[0].pageNum}"/>
                    </c:when>
                </c:choose>
            </c:otherwise>
        </c:choose>

        <div class="book-container">
            <c:if test="${not empty bookList}">
                <c:forEach var="page" items="${bookList}">
                    <c:if test="${page.pageNum eq currentPageNum}">
                        <c:if test="${not empty bookList}">
                            <div class="book-list">
                                <c:forEach var="book" items="${page.objectList}">
                                    <!-- 책 정보 표시 -->
                                    <div class="book-info">
                                        <img src="${book.image}" alt="Book Image"
                                             style="width: 70%; height: 70%;">
                                        <p>제목: ${book.name}</p>
                                        <p>저자: ${book.author}</p>
                                        <!-- 선택 버튼 -->
                                        <form action="<c:url value='/user/apply' />" method="get">
                                            <input type="hidden" name="name" value="${book.name}">
                                            <input type="hidden" name="author" value="${book.author}">
                                            <input type="hidden" name="publisher" value="${book.publisher}">
                                            <input type="hidden" name="isbn" value="${book.isbn}">
                                            <input type="hidden" name="image" value="${book.image}">
                                            <input class="select-btn" type="submit" value="선택">
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </c:if>
                </c:forEach>
            </c:if>
        </div>

        <div class="page">
            <!-- 처음 버튼 -->
            <a href="?keyword=${keyword}&pageNum=1" class="page-link">|◀</a>

            <!-- 이전 버튼 -->
            <c:if test="${currentPageNum > 3}">
                <a href="?keyword=${keyword}&pageNum=${currentPageNum - 5}"
                   class="page-link">이전</a>
            </c:if>


            <!-- 페이지 번호 5개씩 표시 -->
            <c:forEach var="i"
                       begin="${currentPageNum - 2 > 0 ? currentPageNum - 2 : 1}"
                       end="${currentPageNum + 2 < bookList.size() ? currentPageNum + 2 : bookList.size()}">
                <c:choose>
                    <c:when test="${i eq currentPageNum}">
                        <span class="page-link">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?keyword=${keyword}&pageNum=${i}" class="page-link">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 다음 버튼 -->
            <c:if test="${currentPageNum + 2 < bookList.size()}">
                <a href="?keyword=${keyword}&pageNum=${currentPageNum + 3}"
                   class="page-link">다음</a>
            </c:if>

            <!-- 마지막 버튼 -->
            <a href="?keyword=${keyword}&pageNum=${bookList.size()}" class="page-link">▶|</a>
        </div>
    </c:if>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script type="text/javascript">

    function updatePageLink(keyword, pageNum) {
        var link = document.getElementById("pageLink");
        if (link) {
            var updatedHref = "?keyword=" + encodeURIComponent(keyword) + "&pageNum=" + pageNum;
            link.setAttribute("href", updatedHref);
        }
    }

    function submitSelectedBook(book) {
        var form = document.createElement("form");
        form.setAttribute("method", "get");
        form.setAttribute("action", "<c:url value='/user/apply' />");

        addHiddenField(form, "selectedBook.isbn", book.isbn);
        addHiddenField(form, "selectedBook.book_name", book.name);
        addHiddenField(form, "selectedBook.book_author", book.author);
        addHiddenField(form, "selectedBook.book_publisher", book.publisher);
        addHiddenField(form, "selectedBook.book_image", book.image);

        // 폼을 문서에 추가
        document.body.appendChild(form);

        // 폼 제출
        form.submit();
    }

    function addHiddenField(form, name, value) {
        var input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("name", name);
        input.setAttribute("value", value);
        form.appendChild(input);
    }
</script>
</html>