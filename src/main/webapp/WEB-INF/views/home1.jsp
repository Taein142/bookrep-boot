<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>
<script>
    $(function () {
        let m = "${msg}";
        if (m != "") {
            alert(m);
        }
    });
    $(document).ready(function() {
        if (isAuthenticated()) {
            location.href = "/user/home";
        }
    });
</script>
<head>
    <meta charset="UTF-8">
    <title>로그인 하세요</title>
    <style>
        body {
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>
<div class="mainLogo" style="text-align: center;">
    <img alt="mainLogo" src="<%=request.getContextPath()%>/resources/images/newFullLogo.png"
         style="width: 750px;height: 750px; padding-top: 50px;">
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>