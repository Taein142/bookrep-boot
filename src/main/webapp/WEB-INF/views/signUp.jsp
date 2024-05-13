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
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <style>
        .agreed-label {
            font-size: 13px;
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

<div class="out-line">
    <fieldset class="out-line-box">
        <div class="sign-in-box">

            <h1>회원가입</h1>

            <form id="signup-form" action="sign-up" method="post">
                <p>
                <div class="int-area">
                    <input type="text" name="email" id="email" autocomplete="off"
                           placeholder="email" onblur="checkId()" required style="width: 70px">
                    <span>@</span>
                    <input type="text" name="domain" id="domain" style="width: 70px" required><br>
                    <select name="domainList" id="domainList" onblur="checkId()" onchange="toggleInput()">
                        <option value="naver.com">naver.com</option>
                        <option value="gmail.com">gmail.com</option>
                        <option value="daum.net">daum.net</option>
                        <option value="nate.com">nate.com</option>
                        <option value="self">직접 입력</option>
                    </select>
                    <br>
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
                    <input type="text" name="postcode" id="sample4_postcode" placeholder="우편번호" readonly disabled>
                <input type="button" onclick="sample4_execDaumPostcode()" value="우편번호 찾기"><br>
                </div>

                <p>
                <div class="int-area">
                <input type="text" name="address" id="sample4_roadAddress" placeholder="도로명주소" readonly>
                </div>

                <p>
                <div class="int-area">
                <input type="text" id="sample4_jibunAddress" placeholder="지번주소" readonly disabled>
                </div>
                <span id="guide" style="color:#999;display:none"></span>
                <p>
                <div class="int-area">
                <input type="text" name="detail" id="sample4_detailAddress" placeholder="상세주소" required>
                </div>

                <p>
                <div class="int-area">
                <input type="text" id="sample4_extraAddress" placeholder="참고항목" readonly disabled>
                </div>

                <p>
                <div>
                    <div>
                        <input type="checkbox" id="agreedToTerms" name="agreedToTerms" value="agree" required disabled>
                        <label for="agreedToTerms" class="agreed-label">[필수] 약관에 동의합니다.</label>
                    </div>
                    <div>
                        <input type="button" id="terms" value="내용보기" onclick="openWindow('/sign-in-terms', 'agreedToTerms')"/>
                    </div>
                </div>

                <p>
                <div>
                    <div>
                        <input type="checkbox" id="agreedToPrivacyPolicy" name="agreedToPrivacyPolicy" value="agree" required disabled>
                        <label for="agreedToPrivacyPolicy" class="agreed-label">[필수] 개인정보 처리 방침에 동의합니다.</label>
                    </div>
                    <div>
                        <input type="button" id="privacyPolicy" value="내용보기" onclick="openWindow('/sign-in-privacy-policy', 'agreedToPrivacyPolicy')"/>
                    </div>
                </div>

                <p>
                <div class="signUp-page-button">
                    <button id="sbtn">회원가입</button>
                </div>
            </form>

        </div>

    </fieldset>

    <p>
    <fieldset>
        <div class="sign-up-link">
            <p>
                이미 계정이 있나요? <a href="${pageContext.request.contextPath}/user/sign-in">로그인</a>
            <p>
        </div>
    </fieldset>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script>

    function checkId() {
        const first = $('#email').val();
        const last = $('#domain').val();
        const email = first + "@" + last; //id값이 "id"인 입력란의 값을 저장
        console.log(email);
        var checkEmail = document.getElementById("checkEmail");
        let disable = $('#sbtn').prop("disabled");
        const pattern = /\s/g;

        $.ajax({
            url: 'emailCheck', //Controller에서 요청 받을 주소
            type: 'post', //POST 방식으로 전달
            data: {
                email: email
            },
            success: function (cnt) { //컨트롤러에서 넘어온 cnt값을 받는다
                if (cnt == 0) { //cnt가 1이 아니면(=0일 경우) -> 사용 가능한 아이디
                    if (email.match(pattern)){
                        checkEmail.innerText = "공백은 입력 불가합니다";
                        $('#email').val('');
                        disable = true;
                    }else if(last === ""){
                        checkEmail.innerText = "도메인을 입력해주세요";
                        disable = true;
                    } else if(first === ""){
                        checkEmail.innerText = "이메일을 입력해주세요";
                        disable = true;
                    } else {
                        checkEmail.innerHTML = "사용 가능한 이메일입니다.";
                        disable = false;
                    }
                } else { // cnt가 1일 경우 -> 이미 존재하는 아이디
                    console.log(cnt);
                    checkEmail.innerHTML = "중복된 이메일이에요.";
                    alert("아이디를 다시 입력해주세요");
                    $('#email').val('');
                    disable = true;
                }
            },
            error: function () {
                alert("에러입니다");
                disable = true;
            }
        });
    }
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;

                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }).open();
    }

    function openWindow(url, id) {
        var newWindow = window.open(url, "_blank", "width=600,height=400");
        newWindow.onload = function() {
            document.getElementById(id).disabled = false;
        };
    }
    $(
        function () {
            let selection = $('#domainList').val();
            if (selection === "self"){
                    console.log("activateInput");
                    let domain =  $('#domain');
                    if (domain.prop('readonly') === true){
                        domain.prop('readonly', false);
                    }else {
                        return;
                    }
                    domain.val("");
            }else {
                console.log("disableInput");
                let domain =  $('#domain');
                if (domain.prop('readonly') === false){
                    domain.prop('readonly', true);
                }
                domain.val(selection);
            }
        }
    );
    function toggleInput(){
        let selection = $('#domainList').val();
        if (selection === "self"){
                console.log("activateInput");
                let domain =  $('#domain');
                if (domain.prop('readonly') === true){
                    domain.prop('readonly', false);
                }else {
                    return;
                }
                domain.val("");
        }else {
            console.log("disableInput");
            let domain =  $('#domain');
            if (domain.prop('readonly') === false){
                domain.prop('readonly', true);
            }
            domain.val(selection);
        }
    }

    $(
        function (){
            let msg = "${msg}";
            if (msg !== "" && msg !== null){
                alert(msg);
            }
        }
    );


</script>
</html>