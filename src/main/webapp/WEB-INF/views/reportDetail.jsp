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
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
            crossorigin="anonymous"></script>
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
        <div style="width: 50%">
            <div class="r_content">${report.content}</div>
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
        </div>
        <div style="width: 50%; align-items: center">
            <div class="comment">
                <!-- 댓글 입력 창 & 입력 버튼 -->
                <form action="${pageContext.request.contextPath}/user/comment" method="post">
                    <input type="hidden" name="id" value="${report.id}">
                    <textarea name="comment" placeholder="댓글을 입력하세요"></textarea>
                    <button type="submit">등록</button>
                </form>
                <br>
                <hr>
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
                                                    <a href="feed/${comment.userEmail}" class="c_user">${comment.userEmail}</a>
                                                    <p class="c_time">${comment.time}</p>
                                                </div>
                                                <div class="c_body" data-comment-id="${comment.id}">
                                                    <p class="c_content" >${comment.content}</p>
                                                    <c:if test="${loggedInUserEmail eq comment.userEmail}">
                                                        <div class="comment_btn">
                                                            <button class="comment_update_btn" onclick="updateComment(${comment.id})">수정</button>
                                                            <button class="comment_delete_btn" onclick="deleteComment(${comment.id})">삭제</button>
                                                        </div>
                                                    </c:if>
                                                </div>
                                                <form action="${pageContext.request.contextPath}/user/comment-update" method="post"
                                                      class="update-form" style="display: none" data-comment-form-id="${comment.id}">
                                                    <input type="hidden" name="id" value="${comment.id}">
                                                    <input type="hidden" name="report_id" value="${report.id}">
                                                    <textarea name="comment" placeholder="댓글을 입력하세요" required>${comment.content}</textarea>
                                                    <button type="submit" class="comment_update_btn">수정 완료</button>
                                                </form>
                                                <%--                                                <c:if test="${loggedInUserEmail eq comment.userEmail}">--%>
                                                <%--                                                    <div class="comment_btn">--%>
                                                <%--                                                        <button class="comment_update_btn" onclick="updateComment(${comment.id})">수정</button>--%>
                                                <%--                                                        <button class="comment_delete_btn" onclick="deleteComment(${comment.id})">삭제</button>--%>
                                                <%--                                                    </div>--%>
                                                <%--                                                </c:if>--%>
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
    <br><br>
</div>
<jsp:include page="footer.jsp"/>
</body>
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
            }
        }
    }

    function toggleLike(reportId) {
        if (loginEmail === reportUserEmail) {
            alert("본인 글에는 좋아요 할 수 없습니다.");
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

    function updateComment(commentId) {
        console.log("updateComment id = " + commentId);
        // 해당 댓글의 내용을 수정할 폼을 보여줍니다.
        var updateForm = $(".update-form[data-comment-form-id='" + commentId + "']");
        updateForm.show();
        // 해당 댓글의 내용을 담고 있는 <p> 요소를 숨깁니다.
        $(".c_body[data-comment-id='" + commentId + "']").hide();
    }

    function deleteComment(commentId) {
        console.log(commentId);
        if (confirm("해당 댓글을 삭제하시겠습니까?")) {
            $.ajax({
                url: '/user/comment-delete',
                method: 'POST',
                data: {commentId: commentId},
                success: function (response) {
                    if (response === "ok") {
                        console.log(response);
                        document.location.reload(true);
                        alert('해당 댓글이 삭제되었습니다.');
                    } else {
                        alert('해당 댓글이 삭제되지 않았습니다.');
                    }
                },
                error: function () {
                    alert('서버와의 통신 중 문제가 발생했습니다.');
                }
            });
        }
    }
</script>
</html>