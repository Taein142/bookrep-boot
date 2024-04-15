# com.rep.book.bookrepboot에 APIKEY.class 만들기
```
package com.rep.book.bookrepboot;

public class APIKEY {
	public static String ID = "your naver api key";
	
	public static String SECRET = "your naver api secret";

	public static String GEO_KEY = "your v-world api key";
	
}

```
# github & git 사용법
## 1. 작업 시작 시
### i. Repository의 상단바의 Issues 클릭
### ii. New issue 등록
### iii. 이슈 상세 페이지 우측 중단의 Create a branch 클릭
### iv. 브랜치 생성(source는 무조건 develop, Checkout locally)
### v. IntelliJ의 좌측 최하단 Git에 들어가서 Remote 클릭
### vi. Fetch All Remotes (좌측하단을 가르키는 점선 화살표) 클릭
### vii. 만들었던 브랜치 우클릭 후 Checkout 클릭
### viii. Local에 있는 해당 브랜치 더블클릭
### ix. 작업 시작!

## 2. 작업 완료 시
### i. IntelliJ 좌측 Commit 클릭
### ii. Commit || Commit and Push 선택
### iii. Commit message 입력후 commit (어디로 push 하는지 꼭 확인)
### iv. github로 돌아가 pull request 클릭
### v. 변경사항 확인 후 merge
### vi. branch 삭제
### vii. IntelliJ로 돌아와 Git의 Remote 클릭 후 Fetch All Remotes 클릭
### viii. local의 develop 더블클릭(Checkout)
### ix. local의 develop 우클릭 후 Update 클릭
### x. local의 작업했던 브랜치 삭제