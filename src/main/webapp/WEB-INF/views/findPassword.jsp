<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="resources/css/findPassword.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
            crossorigin="anonymous"></script>
</head>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"></jsp:include>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"></jsp:include>
</sec:authorize>

<div class="out-line">

    <fieldset class="out-line-box">
        <div class="findPw-box">

            <img class="lock-img" alt="자물쇠로고"
                 src="resources/images/free-icon-padlock-747305.png">

            <h2>로그인에 문제가 있나요?</h2>
            가입 당시 입력한 이메일과 이름으로 <br> 비밀번호를 찾을 수 있습니다.

            <form id="findPwForm">
                <p>
                <div class="int-area">
                    <input type="text" name="email" id="email" autocomplete="off"
                           placeholder="email" required>
                </div>

                <p>
                <div class="int-area">
                    <input type="text" name="name" id="name" autocomplete="off"
                           placeholder="이름" required>
                </div>

                <p>
                <div class="findPw-page-button">
                    <button type="button" onclick="findPassword()">비밀번호 찾기</button>
                </div>
            </form>

            <h5>or</h5>

            <a href="/sign-up">회원가입</a>
            <p>
        </div>

    </fieldset>

    <p>
    <fieldset>
        <div class="back-signin-link">
            <p>
                <a href="/sign-in">로그인</a>
            <p>
        </div>
    </fieldset>
</div>
</body>
<script>
    function findPassword() {
        // 입력 값 가져오기
        var email = $("#email").val();
        var name = $("#name").val();

        // AJAX 요청 보내기
        $.ajax({
            url: 'find-password', // 컨트롤러에서 요청 받을 주소
            type: 'post', // POST 방식으로 전달
            data: {
                email: email,
                name: name
            },
            // 성공 시 처리
            success: function (result) {
                alert(result); // 컨트롤러에서 받은 결과 출력
            },
            // 에러 시 처리
            error: function () {
                alert("에러가 발생했습니다.");
            }
        });
    }
</script>

</html>
