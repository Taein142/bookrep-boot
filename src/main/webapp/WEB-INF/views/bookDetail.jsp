<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>책 상세 정보</title>
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/resources/css/page.css">
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/resources/css/bookDetail.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>
<input type="hidden" value="${isBookmark}" id="isBookmark">
<div id="total-body">
    <div id="br-body">
        <!-- 책이미지 -->
        <div id="book" class="image-container">
            <img src="${book.image}" alt="Book Image"
                 style="width: 300px; height: 400px;">
            <div class="overlay">
                <div id="bookmark-link" onclick="toggleBookmark()">
                    <img id="bookmark-icon" alt="북마크X"
                         src="<%=request.getContextPath()%>/resources/images/${isBookmark ? 'bookmark_icon.png' : 'bookmark_icon_blank.png'}">
                </div>
                <p>${book.name}</p>
                <br>
                <p>${book.author}</p>
                <br>
                <p>${book.publisher}</p>
            </div>
        </div>
        <!-- 독후감리스트 -->
        <div id="report">
            <c:if test="${not empty reportList}">
                <!-- 현재 페이지 설정 -->
                <c:choose>
                    <c:when test="${not empty param.pageNum}">
                        <c:set var="currentPageNum" value="${param.pageNum}"/>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${not empty reportList}">
                                <c:set var="currentPageNum" value="${reportList[0].pageNum}"/>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

                <!-- 현재 페이지 번호와 일치하는 독후감 등을 출력하는 부분 -->
                <c:if test="${not empty reportList}">
                    <c:forEach var="page" items="${reportList}">
                        <c:if test="${page.pageNum eq currentPageNum}">
                            <div id="report-container">
                                <c:forEach var="report" items="${page.objectList}">
                                    <div class="report-content">
                                        <div>
                                            <div class="report-user">
                                                <a href="/user/feed/${report.userEmail}">${report.userEmail}</a>
                                            </div>
                                            <div class="report-title">
                                                <a href="${pageContext.request.contextPath}/user/report-detail?id=${report.id}">${report.title}</a>
                                            </div>
                                        </div>
                                        <div>
                                            <p>${report.time}</p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
            </c:if>
        </div>
    </div>
    <div class="page">
        <!-- 처음 버튼 -->
        <a href="?isbn=${book.isbn}&pageNum=1" class="page-link">|◀</a>

        <!-- 이전 버튼 -->
        <c:if test="${currentPageNum > 3}">
            <a href="?isbn=${book.isbn}&pageNum=${currentPageNum - 5}"
               class="page-link">이전</a>
        </c:if>

        <!-- 페이지 번호 5개씩 표시 -->
        <c:forEach var="i"
                   begin="${currentPageNum - 2 > 0 ? currentPageNum - 2 : 1}"
                   end="${currentPageNum + 2 < reportList.size() ? currentPageNum + 2 : reportList.size()}">
            <c:choose>
                <c:when test="${i eq currentPageNum}">
                    <span class="page-link">${i}</span>
                </c:when>
                <c:otherwise>
                    <a href="?isbn=${book.isbn}&pageNum=${i}" class="page-link">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <!-- 다음 버튼 -->
        <c:if test="${currentPageNum + 2 < reportList.size()}">
            <a href="?isbn=${book.isbn}&pageNum=${currentPageNum + 3}"
               class="page-link">다음</a>
        </c:if>

        <!-- 마지막 버튼 -->
        <a href="?isbn=${book.isbn}&pageNum=${reportList.size()}"
           class="page-link">▶|</a>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script type="text/javascript">
    console.log(document.getElementById("isBookmark").value);
    var isBookmark = JSON.parse(document.getElementById("isBookmark").value);

    function toggleBookmark() {
        isBookmark = !isBookmark;

        $.ajax({
            type: "POST",
            url: "<%=request.getContextPath()%>/user/bookmark",
            data: {isbn: "${book.isbn}"},
            success: function (response) {
                if (isBookmark) {
                    $("#bookmark-icon").attr("src", "<%=request.getContextPath()%>/resources/images/bookmark_icon.png");
                    alert("북마크가 추가되었습니다.");
                } else {
                    $("#bookmark-icon").attr("src", "<%=request.getContextPath()%>/resources/images/bookmark_icon_blank.png");
                    alert("북마크가 해제되었습니다.");
                }

                console.log("Bookmark status updated successfully.");
            },
            error: function (xhr, status, error) {
                console.error("북마크 상태 업데이트 오류", error);
            }
        });
    }
</script>
</html>