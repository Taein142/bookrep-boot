<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/findPassword.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
            crossorigin="anonymous"></script>
</head>
<script>
    $(function(){
        // 받은 코드를 입력하고 전송하는 부분 숨기기
        $(".tbmg").hide();

        $("#sbtn").click(function(){
            let email = $("#email").val();
            if(email===""){
                alert("정보를 입력하세요.");
                return;
            }

            console.log(email);

            $.ajax({
                url: "mailConfirm",
                type: "post",
                data: {"email" : email},
                success: (res) => {
                    if(res == "ok"){
                        alert("인증코드를 메일로 전송했습니다.");
                        $(".tbmg").show();
                        $("#sbtn").attr("disabled", true)// 전송버튼 비활성화
                    }
                    else{
                        alert("메일 전송에 실패했습니다.");
                    }
                },
                error: (err) => {
                    console.log(err);
                    alert("메일 전송에 실패했습니다.");
                }
            })
        })
    });
    $(function () {
        $("#cbtn").click(function(){
            let vcode =$("#v_code").val();
            if(vcode === ""){
                alert("인증코드를 입력하세요.")
                return;
            }

            $.ajax({
                url: "codeAuth",
                type: "post",
                data: {"vCode" : vcode},
                success: (res) => {
                    if (res === "ok"){
                        location.href = "/pwd-change";
                    }else {
                        alert("코드가 맞지 않습니다");
                        location.reload();
                    }
                },
                error: (err) => {
                    console.log(err);
                    alert("코드 인증 실패");
                }
            });
        });
    });
</script>
<body>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="loggedHeader.jsp"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <jsp:include page="header.jsp"/>
</sec:authorize>

<div class="out-line">

    <fieldset class="out-line-box">
        <div class="findPw-box">

            <img class="lock-img" alt="자물쇠로고"
                 src="resources/images/free-icon-padlock-747305.png">

            <h2>로그인에 문제가 있나요?</h2>
            가입 당시 입력한 이메일로 <br> 비밀번호를 변경할 수 있습니다.

                <p>
                <div class="int-area">
                    <input type="text" name="email" id="email" autocomplete="off"
                           placeholder="email" required>
                </div>

                <p>
                <div class="findPw-page-button">
                    <button type="button" id="sbtn">인증번호 전송</button>
                </div>

                <div class="tbmg">
                    <p>
                    <div class="int-area">
                        <input type="text" id="v_code" placeholder="인증번호를 입력해주세요">
                    </div>

                    <p>
                    <div class="findPw-page-button">
                        <button type="button" id="cbtn">인증</button>
                    </div>
                </div>

            <h5>or</h5>

            <a href="${pageContext.request.contextPath}/sign-up">회원가입</a>
            <p>
        </div>

    </fieldset>

    <p>
    <fieldset>
        <div class="back-signin-link">
            <p>
                <a href="${pageContext.request.contextPath}/sign-in">로그인</a>
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
