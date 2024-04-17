<%@ page import="com.rep.book.bookrepboot.util.SecurityUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
    String loginEmail = SecurityUtil.getCurrentUserEmail();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>독후감 상세</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reportDetail.css">
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>

<input type="hidden" value="${isLike}" id="isLike">
<input type="hidden" value="${likeValue}" id="likeValue">
<input type="hidden" value="${report.userEmail}" id="reportUserEmail">

<div id="total-body">
    <div id="report-top">
        <div class="r_title">${report.title}</div>
        <div>
            <div class="r_user">글쓴이: ${report.userEmail}</div>
            <div class="r_date">작성시간: ${report.time}</div>
        </div>
    </div>
    <br>
    <hr class="separator">
    <br>
    <div id="report-body">
        <div class="r_content">${report.content}</div>
        <div class="comment">
            <!-- 댓글 입력 창 & 입력 버튼 -->
            <form action="/user/comment" method="post">
                <input type="hidden" name="id" value="${report.id}">
                <textarea name="comment" placeholder="댓글을 입력하세요"></textarea>
                <button type="submit">등록</button>
            </form>
            <br>
            <hr>
            <br>
            <c:if test="${not empty commentList}">
                <!-- 현재 페이지 설정 -->
                <c:choose>
                    <c:when test="${not empty param.pageNum}">
                        <c:set var="currentPageNum" value="${param.pageNum}"/>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${not empty commentList}">
                                <c:set var="currentPageNum" value="${commentList[0].pageNum}"/>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

                <!-- 현재 페이지 번호와 일치하는 댓글을 출력하는 부분 -->
                <c:if test="${not empty commentList}">
                    <c:forEach var="page" items="${commentList}">
                        <c:if test="${page.pageNum eq currentPageNum}">
                            <div id="comment-container">
                                <c:choose>
                                    <c:when test="${not empty page.objectList}">
                                        <c:forEach var="comment" items="${page.objectList}">
                                            <div class="c_top">
                                                <a href="feed/${comment.userEmail}"
                                                   class="c_user">${comment.userEmail}</a>
                                                <p class="c_time">${comment.time}</p>
                                            </div>
                                            <p class="c_content">${comment.content}</p>
                                            <br>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="no-comment">댓글이 없습니다.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${empty commentList}">
                    <p class="no-comment">댓글이 없습니다.</p>
                </c:if>
            </c:if>
        </div>
    </div>
    <br>
    <hr class="separator">
    <br>
    <div id="report-bottom">
        <div id="report_bottom_left">
            <button class="delete-btn"
                    onclick="confirmDelete(${report.id}, '${report.userEmail}')">삭제
            </button>
            <div class="r_like" onclick="toggleLike(${report.id})">
                <img class="like-heart"
                     src="<%=request.getContextPath()%>/resources/images/${isLike ? 'heart_white.png' : 'heart_blank.png'}">
                <div>${likeValue}</div>
            </div>
            <button class="update-btn"
                    onclick="location.href='/user/report-update?id=${report.id}'">수정
            </button>
        </div>
        <div id="report_bottom_right">
            <div class="page">
                <c:if test="${not empty commentList}">
                    <!-- 처음 버튼 -->
                    <a href="?id=${report.id}&pageNum=1" class="page-link">|◀</a>

                    <!-- 이전 버튼 -->
                    <c:if test="${currentPageNum > 3}">
                        <a href="?id=${report.id}&pageNum=${currentPageNum - 5}" class="page-link">이전</a>
                    </c:if>

                    <!-- 페이지 번호 5개씩 표시 -->
                    <c:forEach var="i"
                               begin="${currentPageNum - 2 > 0 ? currentPageNum - 2 : 1}"
                               end="${currentPageNum + 2 < commentList.size() ? currentPageNum + 2 : commentList.size()}">
                        <c:choose>
                            <c:when test="${i eq currentPageNum}">
                                <span class="page-link">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="?id=${report.id}&pageNum=${i}" class="page-link">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <!-- 다음 버튼 -->
                    <c:if test="${currentPageNum + 2 < commentList.size()}">
                        <a href="?id=${report.id}&pageNum=${currentPageNum + 3}" class="page-link">다음</a>
                    </c:if>

                    <!-- 마지막 버튼 -->
                    <a href="?id=${report.id}&pageNum=${commentList.size()}" class="page-link">▶|</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script type="text/javascript">
    var loginEmail = '<%=loginEmail%>';
    var isLike = JSON.parse(document.getElementById("isLike").value);
    var likeCnt = parseInt(document.getElementById("likeValue").value);
    var reportUserEmail = document.getElementById("reportUserEmail").value;

    function confirmDelete(reportId, reportUserEmail) {
        if (confirm("게시글을 삭제하시겠습니까?")) {
            location.href = '/user/delete?id=' + reportId + '&reportUserEmail=' + reportUserEmail;
            var msg = "${msg}";
            if (msg) {
                alert(msg);
                return;
            }
        }
    }

    function toggleLike(reportId) {

        if (loginEmail == reportUserEmail) {
            alert("본인 글에는 좋아요 할 수 없습니다.");
            return;
        } else {


            $.ajax({
                type: "POST",
                url: "<%=request.getContextPath()%>/user/like",
                data: {id: reportId},
                success: function (response) {
                    isLike = !isLike;
                    if (isLike) {
                        $(".like-heart").attr("src", "<%=request.getContextPath()%>/resources/images/heart_white.png");
                        likeCnt = likeCnt + 1;
                        alert("좋아요를 추가했습니다!");
                    } else {
                        $(".like-heart").attr("src", "<%=request.getContextPath()%>/resources/images/heart_blank.png");
                        likeCnt = likeCnt - 1;
                        alert("좋아요를 취소했습니다.");
                    }
                    $(".r_like div").text(likeCnt);

                },
                error: function (error) {
                    console.error("좋아요 설정 실패 : ", error);
                }
            });
        }
    }
</script>
</html>