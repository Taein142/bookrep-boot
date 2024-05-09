<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" href="../resources/css/header.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body>
<header>
    <div class="header">
        <div class="logo">
            <img src="../resources/images/newFullLogo.png" alt="로고"
                 onclick="return moveToHome()">
        </div>
        <div class="buttons">
            <c:choose>
                <c:when test="${empty sessionScope.loggedEmail}">
                    <div class="login-button">
                        <a href="${pageContext.request.contextPath}/user/sign-in">
                            <button>로그인</button>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- 세션에 로그인 정보가 있는 경우 -->
                    <div class="search-box">
                        <form action="${pageContext.request.contextPath}/user/search" method="GET">
                            <input type="text" name="query" placeholder="검색어를 입력하세요">
                            <button type="submit" class="search">search</button>
                        </form>
                    </div>
                    <div class="logout-button">
                        <a href="${pageContext.request.contextPath}/sign-out">
                            <button>로그아웃</button>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
</body>
<script>
    const moveToHome = () => {


        location.href = "/";
    }

</script>

</html>