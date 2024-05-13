<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인헤더</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/loggedHeader.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css">
</head>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous">
	</script>
<body>
	<header>
		<div class="header">
			<div class="profile">
					<sec:authorize access="isAuthenticated()">
						<sec:authentication property="principal" var="principal"/>
						<a href="/user/feed/${principal.username}" id="userImage"></a>
					</sec:authorize>
					<sec:authorize>
						<i class="fa fa-user-circle-o fa-2x" aria-hidden="true"></i>
					</sec:authorize>
			</div>
			
			<div class="write-button">
				<button onclick="moveToWrite()">글 쓰기</button>
			</div>
			<div class="write-button">
				<button onclick="moveToMyShare()">My Share</button>
			</div>
			<div class="write-button">
				<button onclick="moveToShareHouse()">Share House</button>
			</div>

			<div class="logo" id="headerCenterContent">
				<img src="<%=request.getContextPath()%>/resources/images/newFullLogo.png"
					alt="로고" onclick="moveToHome()">
				<div id="headerLogoText">
					<h5>BOOKREP</h5>
				</div>
			</div>
			<div class="buttons">
				<div class="search-box">
					<form action="${pageContext.request.contextPath}/user/search" method="GET">
						<input type="text" name="keyword" placeholder="검색어를 입력하세요">
						<button type="submit" class="search">검색</button>
					</form>
				</div>
				<div class="logout-button">
					<a href="${pageContext.request.contextPath}/user/sign-out"><button>로그아웃</button></a>
				</div>
			</div>
		</div>
	</header>
</body>
<script>
	var logggggggggggemail = "${principal.username}";
	var userImage = document.getElementById("userImage");
	$.ajax({
		url : '<%=request.getContextPath()%>/user/get-image',
		type : 'post', 
		data : {
			email : logggggggggggemail
		},
		success : function(imageAjax) { //컨트롤러에서 넘어온 cnt값을 받는다 
			console.log(imageAjax);
			var imagePath = '<%=request.getContextPath()%>/resources/images/' + imageAjax;
			userImage.innerHTML = '<img src="' + imagePath + '">';
		},
		error : function() {
			alert("에러입니다");
		}
	});
	const moveToHome = () => {

		var email = "${principal.username}";
		
		if(email != null) {
			location.href = "/user/home";
		} else {
			location.href = "/";
		}
	}
	
	const moveToFeed = () => {
		
		var email1 = "${principal.username}";
		
		if(email1 != null){
			location.href = "/user/feed";
		}
	}
	
	const moveToWrite = () => {
		location.href = "/user/write";
	}

	const moveToShareHouse = () => {
		location.href = "/user/share-house";
	}

	const moveToMyShare = () => {
		location.href = "/user/my-share";
	}
	
</script>

</html>