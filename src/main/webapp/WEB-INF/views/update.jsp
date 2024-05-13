<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원정보 수정</title>
    <link href="https://getbootstrap.com/docs/5.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://getbootstrap.com/docs/5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/update.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
            crossorigin="anonymous"></script>
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
        $(function(){
            // 받은 코드를 입력하고 전송하는 부분 숨기기
            $(".tbmg").hide();
            $(".confirm").hide();

            $("#sbtn").click(function(){
                let email = $("#email").val();
                if(email===""){
                    alert("정보를 입력하세요.");
                    return;
                }

                spinOn();
                console.log(email);

                $.ajax({
                    url: "/mailConfirm",
                    type: "post",
                    data: {"email" : email},
                    success: (res) => {
                        if(res == "ok"){
                            spinOff();
                            alert("인증코드를 메일로 전송했습니다.");
                            $(".tbmg").show();
                            $("#sbtn").attr("disabled", true);
                        }
                        else{
                            spinOff();
                            alert("메일 전송에 실패했습니다.");
                        }
                    },
                    error: (err) => {
                        console.log(err);
                        spinOff();
                        alert("에러 발생. 메일 전송에 실패했습니다.");
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
                    url: "/codeAuth",
                    type: "post",
                    data: {"vCode" : vcode},
                    success: (res) => {
                        if (res === "ok"){
                            $(".v_code").attr("disabled", true);
                            $(".confirm").show();
                            $("#mailConfirm").val("true");
                        } else {
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
            <h2>회원정보 수정</h2>
            <br>
            <form id="userInfo" method="post" enctype="multipart/form-data">
                <p>
                <div class="enter-area">
                    <input type="file" class="input-profile" id="file" name="files" multiple>
                </div>
                <p>
                <div class="enter-area">
                    <label class="label1" for="email">이 메 일</label>
                    <input type="text" class="input1" id="email" name="email" value="${user.email}" readonly>
                </div>
                <p>
                <div class="findPw-page-button">
                    <button type="button" id="sbtn">인증번호 전송</button>
                </div>
                <p>
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
                <p>
                <div class="confirm">
                    <div>인증되었습니다.</div>
                    <input type="hidden" value="false" name="mailConfirm" id="mailConfirm">
                </div>
                <p>
                <div class="enter-area">
                    <label class="label2" for="name"> 이   름 </label> <input class="input2" type="text" id="name" name="name" value="${user.name}">
                </div>
                <p>
                <div class="enter-area">
                    <label class="label3" for="password"> 새 비 밀 번 호 </label> <input class="input3" type="password" id="password" name="password" required
                                                                                placeholder="패스워드를 입력하세요">
                </div>
                <p>
                <div class="enter-area">
                    <label class="label4" for="password_re"> 비밀번호 확인 </label> <input class="input4" type="password" id="password_re" name="password_re"
                                                                                   required placeholder="패스워드 확인" onblur="password_check()"> <br>
                    <span id="password_check"></span>
                </div>

                <!-- 우편번호 찾기 -->
                <p>
                <div class="int-area">
                    <input type="text" name="postcode" id="sample4_postcode" placeholder="우편번호" readonly disabled>
                </div>
                <div class="int-area">
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
<div class="w-100 h-100 fixed-top justify-content-center align-items-center bg-opacity-10 bg-success" id="spin" style="display: none;">
    <button class="btn btn-primary" type="button" disabled>
        <span class="spinner-border spinner-border-sm" aria-hidden="true"></span>
        <span role="status">Loading...</span>
    </button>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script>
    const password_check = () => {
        const password = document.getElementById("password").value;
        const password_re = document.getElementById("password_re").value;
        const pwCheck = document.getElementById("password_check");

        if (password == password_re) {
            pwCheck.innerHTML = "패스워드가 일치합니다.";
            pwCheck.style.color = "green";
        } else {
            pwCheck.innerHTML = "패스워드가 일치하지 않습니다.";
            pwCheck.style.color = "red";
        }
    }

    function resignAlert() {
        var confirmResign = confirm("계정을 삭제하시겠습니까?");
        if (confirmResign) {
            location.href = "resign";
            alert("회원탈퇴가 성공적으로 처리되었습니다.");
        } else {
            location.reload();
        }
    }

    function updateAlert() {
        var confirmUpdate = confirm("정보를 수정하시겠습니까?");
        var password = document.getElementById("password").value;
        var password_re = document.getElementById("password_re").value;

        if (confirmUpdate) {
            if (password === "" || password_re === "") {
                alert("비밀번호를 입력하세요.");
                return false;
            } else {
                document.getElementById("userInfo").submit();
            }
        } else {
            return false;
        }
    }

    // 우편번호 찾기 함수
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraRoadAddr += data.bname;
                }

                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }

                if (extraRoadAddr !== '') {
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;

                if (roadAddr !== '') {
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");

                if (data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if (data.autoJibunAddress) {
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

    // 페이지 로딩 시 실행할 초기화 코드
    $(function () {
        let msg = "${msg}";
        if (msg !== "" && msg !== null) {
            alert(msg);
        }
    });

    function spinOn() {
        $("#spin").addClass("d-flex");
    }

    function spinOff() {
        $("#spin").removeClass("d-flex");
    }
</script>
</html>