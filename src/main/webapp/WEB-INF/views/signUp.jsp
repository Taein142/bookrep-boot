<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="resources/css/signUp.css">
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
        <div class="sign-in-box">

            <h1>회원가입</h1>

            <form id="signup-form" action="sign-up" method="post">
                <p>
                <div class="int-area">
                    <input type="text" name="email" id="email" autocomplete="off"
                           placeholder="email" onblur="checkId()" required> <br>
                    <span id="checkEmail"></span>
                </div>
                <p>
                <div class="int-area">
                    <input type="text" name="name" id="name" autocomplete="off"
                           placeholder="이름" required>
                </div>

                <p>
                <div class="int-area">
                    <input type="password" name="password" id="password"
                           autocomplete="off" placeholder="password" required>
                </div>

                <p>
                <div class="int-area">
                    <input type="number" name="longitude" id="longitude"
                           autocomplete="off" placeholder="경도" required>
                </div>

                <p>
                <div class="int-area">
                    <input type="number" name="latitude" id="latitude"
                           autocomplete="off" placeholder="위도" required>
                </div>

                <p>
                <div class="signUp-page-button">
                    <button>회원가입</button>
                </div>
            </form>
        </div>

    </fieldset>

    <p>
    <fieldset>
        <div class="sign-up-link">
            <p>
                이미 계정이 있나요? <a href="/sign-in">로그인</a>
            <p>
        </div>
    </fieldset>
</div>
</body>
<script>

    function checkId() {

        var email = $('#email').val(); //id값이 "id"인 입력란의 값을 저장

        var checkEmail = document.getElementById("checkEmail");

        $.ajax({
            url: 'emailCheck', //Controller에서 요청 받을 주소
            type: 'post', //POST 방식으로 전달
            data: {
                email: email
            },
            success: function (cnt) { //컨트롤러에서 넘어온 cnt값을 받는다
                if (cnt == 0) { //cnt가 1이 아니면(=0일 경우) -> 사용 가능한 아이디
                    checkEmail.innerHTML = "사용 가능한 이메일입니다.";
                } else { // cnt가 1일 경우 -> 이미 존재하는 아이디
                    console.log(cnt);
                    checkEmail.innerHTML = "중복된 이메일이에요.";
                    alert("아이디를 다시 입력해주세요");
                    $('#email').val('');
                }
            },
            error: function () {
                alert("에러입니다");
            }
        });
    };
</script>
</html>