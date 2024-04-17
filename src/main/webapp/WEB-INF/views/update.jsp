<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원정보 수정</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/update.css">
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

<div class="real-out-line">

    <div class="out-line-box">
        <fieldset class="update-frm">

            <h1>회원정보 수정</h1>

            <form id="userInfo" method="post" enctype="multipart/form-data">
                <p>
                <div class="enter-area">
                    <input type="file" class="input-profile" id="file" name="files"
                           multiple>
                </div>
                <p>
                <div class="enter-area">
                    <label class="label1" for="email">이 메 일</label> <input
                        type="email" class="input1" id="email" name="email"
                        value="${user.email}" readonly>
                </div>
                <p>
                <div class="enter-area">
                    <label class="label2" for="name">이 름</label> <input class="input2"
                                                                        type="text" id="name" name="name"
                                                                        value="${user.name}">
                </div>
                <p>
                <div class="enter-area">
                    <label class="label3" for="password">비 밀 번 호</label> <input
                        class="input3" type="password" id="password" name="password"
                        required placeholder="패스워드를 입력하세요">
                </div>
                <p>
                <div class="enter-area">
                    <label class="label4" for="password_re">비밀번호 확인</label> <input
                        class="input4" type="password" id="password_re"
                        name="password_re" required placeholder="패스워드 확인"
                        onblur="password_check()"> <br> <span
                        id="password_check"></span>
                </div>
            </form>

            <p>
            <div class="btn-box">
                <button type="button" onclick="updateAlert()" name="update-btn">수정완료</button>
                <button type="button" onclick="resignAlert()" name="resign-btn">회원탈퇴</button>
            </div>
            <p>
        </fieldset>
    </div>
</div>
</body>
<script>
    const password_check = () => {
        const password = document.getElementById("password").value;
        const password_re = document.getElementById("password_re").value;

        const pwCheck = document.getElementById("password_check");

        if (password == password_re) {
            pwCheck.innerHTML = "패스워드 일치가 일치합니다.";
            pwCheck.style.color = "green";
        } else {
            pwCheck.innerHTML = "다시 확인하세요.";
            pwCheck.style.color = "red";
        }
    }

    function resignAlert() {

        var confirmResign = confirm("계정을 삭제하시겠어요?");

        if (confirmResign) {
            location.href = "resign";
            alert("회원탈퇴 성공");
        } else {
            location.reload();
        }
    }

    function updateAlert() {

        var confirmUpdate = confirm("정보를 수정하시겠어요?");

        var password = document.getElementById("password").value;
        var password_re = document.getElementById("password_re").value;

        if (confirmUpdate) {
            if (password === "" || password_re === "") {
                alert("비밀번호를 입력하세요.");
                return false;
            } else {
                alert("회원정보 수정 완료");
                document.getElementById("userInfo").submit();
            }
        } else {
            return false;
        }
    }
</script>
</html>