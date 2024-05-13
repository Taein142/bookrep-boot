<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <title>푸터</title>
    <style>
        .footer-bar {
            bottom: 0px;
            width: 100%;
            height: 40px;
            background-color: rgb(186, 182, 182);
            border-top: 1px solid black;
            padding: 10px 0;
        }

        #footerContent {
            text-align: center;
        }

        #footerContent img {
            height: 30px; /* 로고 이미지 높이 조정 */
            margin-right: 10px; /* 로고와 링크 사이의 간격 조정 */
            width: auto;
        }

        #footerContent img {
            height: 20px;
            margin-right: 10px;
        }

        #footerContent a {
            text-decoration: none; /* 밑줄 제거 */
            color: black; /* 텍스트 색상을 검은색으로 설정 */
            margin-right: 10px;
            font-weight: bolder;
        }
    </style>
</head>
<body>
<div class="footer-bar">
    <div id="footerContent">
        <img src="<%=request.getContextPath()%>/resources/images/newFullLogo.png" alt="">
        <a href="${pageContext.request.contextPath}/terms">이용약관</a>
        <a href="${pageContext.request.contextPath}/privacy-policy">개인정보 처리방침</a>
        <a href="${pageContext.request.contextPath}/contact-us">문의안내</a>
    </div>
</div>
</body>
</html>
