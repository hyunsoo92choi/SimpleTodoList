# Simple Todo List API Project

## Getting Started

간단한 할일을 관리하는 API를 개발 하였습니다.
주요 기능은 다음과 같습니다.
 * 사용자는 문자열로 된 todo 항목을 추가 할 수 있다.
 * todo는 다른 todo 들을 참조 할 수 있다.
 * 사용자는 todo 목록을 조회할 수 있다.
 * 사용자는 todo 를 수정할 수 있다.
 * 사용자는 todo 를 삭제할 수 있다.
 * 사용자는 todo 를 완료 또는 미완료로 상태 변경을 할 수 있다.
   - 참조하고 있는 todo들이 모두 완료 상태가 아니라면 todo를 완료할 수 없다.

### Prerequisites

Java 11 or higher
Kotlin 1.3.61 or higher

### Build

Git Download or Clone

./gradlew clean build 

java -jar build/libs/todo-0.0.1-SNAPSHOT.jar 실행.

APIs 동작 확인.

http://localhost:8080/h2-console DB 확인. 

## Built With
* [Language]  - Kotlin 1.3.61
* [Framework] - Spring boot 2.2.5.RELEASE
* [Database]  - H2
* [ORM]       - Spring Data JPA
* [Gradle]    - Build & Dependency Management

## APIs

### 기본 구조
 * Data Type : JSON
 
 <table>
	<thead>
	<tr>
	<th align="left">Property</th>
	<th align="left">Description</th>
	<th align="left">Comment</th>
	</tr>
	</thead>
	<tbody>
	<tr>
	<td align="left">code</td>
	<td align="left">응답코드</td>
	<td align="left">200, 400, 404와 같이 Http Status code로 처리결과를 나타냄</td>
	</tr>
	<tr>
	<td align="left">message</td>
	<td align="left">HTTP Reason Phrase 메시지</td>
	<td align="left"></td>
	</tr>
	<tr>
	<td align="left">data</td>
	<td align="left">반환 데이터</td>
	<td align="left"></td>
	</tr>
	</tbody>
</table>

### 할일 등록
<blockquote>
<p>URL : /api/v1/tasks</p>
<p>Method : POST</p>
</blockquote>
<table>
<thead>
<tr>
<th align="left">Property</th>
<th align="left">Data Type</th>
<th align="left">Mandatory</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left">subTaskNo</td>
<td align="left">LongArray</td>
<td align="left">N</td>
</tr>
<tr>
<td align="left">title</td>
<td align="left">String</td>
<td align="left">Y</td>
</tr>
<tr>
<td align="left">description</td>
<td align="left">String</td>
<td align="left">N</td>
</tr>
</tbody>
</table>

### 할일 전체 조회
<blockquote>
<p>URL : /api/v1/tasks?page=1&size=50</p>
<p>Method : GET</p>
<p>* request param page size 는 원하는 값을 넣어 주세요. </p>
</blockquote>

### 할일 단건 조회
<blockquote>
<p>URL : /api/v1/task/{id}</p>
<p>Method : GET</p>
</blockquote>

### 할일 수정
<blockquote>
<p>URL : /api/v1/task/{id}</p>
<p>Method : PUT</p>
</blockquote>
<table>
<thead>
<tr>
<th align="left">Property</th>
<th align="left">Data Type</th>
<th align="left">Mandatory</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left">taskStatus</td>
<td align="left">String</td>
<td align="left">N</td>
</tr>
<tr>
<td align="left">title</td>
<td align="left">String</td>
<td align="left">Y</td>
</tr>
<tr>
<td align="left">description</td>
<td align="left">String</td>
<td align="left">N</td>
</tr>
</tbody>
</table>

### 할일 삭제
<blockquote>
<p>URL : /api/v1/task/{id}</p>
<p>Method : DELETE</p>
</blockquote>

## 문제해결 전략

### 할일을 등록 전략
할일을 상위 할일과 하위 할일로 구분 하여 등록하도록 하였습니다.
예를 들어 집안일이라는 상위 할 일이 있다면 그에 따른 하위 할일이 있을 것이라 생각하였고, 
상위 할일을 등록할 때 같이 등록 해 주지 않을까 싶었습니다.
따라서 할일을 등록 시 하위 할일이 있다면 미리 생각해 놓고 상위 할일을 등록할 때 하위 할일에 대한
아이디를 받는 것이 시나리오 상 좋지 않을까 생각 했습니다.<br/>
예시> <br/>
```
{
"subTaskNo":[2, 3],
"title":"할일 저장 개발 완료 ",
"description":"할일 저장 개발 완료."
}
```
```
{
"subTaskNo":[],
"title":"상위 할일 저장 개발 완료 ",
"description":"상위 할일 저장 개발 완료."
}
```
```
{
"subTaskNo":[],
"title":"하위 할일 저장 개발 완료 ",
"description":"하위 할일 저장 개발 완료."
}
```

### 엔티티 설계
상위 할일과 하위 할일간의 관계가 필요 했고 하나의 상위 할일은 다수의 하위 할일을 갖는 구조로 설계 했습니다.

## Acknowledgments
Spring Boot 2.2로 버전 업을 하면서 PageRequest도 변경 되었고, 스프링 버전도 올릴 겸 Kotlin으로
작성한 첫 토이프로젝트를 진행 하였습니다. <br/>
간단한 할일을 관리하는 API를 개발하는데도 생각 할 것들이 많이 있었습니다. <br/>
최대한 Restful하게 개발 하려고 노력하였고, 컨트롤러 테스트부터 Repository 테스트까지 다 하면서, 화면도
같이 구성해 보고 싶었는데, 화면 개발을 한지 너무 오래되어서 화면 개발은 일단 프로젝트 범위에서 제외 하였습니다. <br/>
2주간 시간을 잡고 진행하였는데, 현재 회사에서 프로젝트를 신규로 생성해서 개발을 진행하고 있고,
중간에 인력 공백도 있다보니, 본 프로젝트를 진행 할 여력이 늦은 심야밖에 없었고, 또한 두번의 주말이 있었지만
혼자 개발만 하면서 보낼 수 있는 여건이 아닌것이 조금 아쉬웠습니다. <br/>
2주의 시간은 모두 소비 하였지만, 긴 호흡을 가지고 미진한 내용을 조금 보강하고 오랫만에 프런트 화면도 구성해
보려고 합니다.
