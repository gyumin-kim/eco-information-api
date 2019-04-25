# eco-information-api (생태 관광 정보 서비스 API)

대한민국 지역기반 생태 관광 정보 서비스 개발을 위한 API.  
한국 관광공사가 보유하고 있는 생태 관광 데이터를 지역별로 제목, 개요, 연락처 등의 정보를 활용하여 API 기능 명세와 제약 사항에 맞게 개발한다.
생태 관광 정보 데이터는 CSV 파일이며, 각 레코드의 컬럼값은 `,` 구분자로 저장되어 있다.  
모든 입출력은 JSON 형태로 주고 받는다.


## API 기능 명세 및 문제해결 전략
### 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
- **문제해결 전략**
  - Entity는 `Program`, `Region` 2가지가 존재한다.
  - 하나의 Program은 여러 Region에서 서비스될 수 있고, 동일한 하나의 Region에서 여러 Program이 서비스될 수 있으므로, 
  `Program`과 `Region`은 *Many-To-Many*(다대다) 관계이다.
  - API 기능 명세에 따르면, 프로그램 필드를 통해 지역 정보를 검색하는 경우와 지역 정보를 통해 프로그램 정보를 검색하는 경우 모두 필요하다. 
  따라서 다대다 양방향 관계로 구현한다.
  - 특정 지역의 이름으로 프로그램의 정보를 검색하는 API를 사용할 때, 검색어가 CSV 파일에 주어진 각 프로그램의 '서비스 지역'과 
  완전히 일치하지 않는 경우가 있을 수 있다. 예를 들어 '서비스 지역'이 "강원도 평창군 일대", "강원도 평창군 진부면"인 각각의 Program이 존재한다고 했을 때, 
  클라이언트가 "평창군"으로 검색하는 것이다. 이 경우 해당 2개 Program이 모두 검색되어야 하며, "평창군"이라는 Region의 고유한 지역코드가 출력되어야 한다. 
  따라서 각 Program의 '서비스 지역'이 `,`를 기준으로 여러 개가 있으면 각각을 별도의 Region으로 간주하되, 
  각 Region을 공백을 기준으로 split하여 별도의 Region을 생성하고 고유의 지역코드를 부여한다. split하지 않은 전체 지역명도 하나의 Region이다.
    - ex) "충청북도 단양군 가곡면 어의곡리"가 서비스 지역일 경우 => "충청북도", "단양군", "가곡면", "어의곡리", "충청북도 단양군 가곡면 어의곡리" 
    총 5개의 Region이 생성되며 각각은 고유한 지역코드를 갖는다.

### 생태 관광정보 데이터를 조회/추가/수정할 수 있는 API
- 단, 조회는 서비스 지역 코드를 기준으로 검색한다.
- **문제해결 전략**
  - 조회 기능
    - 서비스 지역 코드를 JSON 형태로 담아서 `/api/regioncode`로 요청 (POST)
    - 입력 예시
    ```json5
    {
      "region_code": "reg3702"
    }
    ```
    - 출력 예시
    ```json5
      {
        "region_code": "reg3702",
        "programs":[
          {
            "id": 2,
            "prgm_name": "[설악산] 설악산에서 길을 묻다(설악을 내 품에)",
            "prgm_code": "prg0524",
            "theme": "아동·청소년 체험학습,",
            "region_name": "속초",
            "intro": "소규모 학생을 위한 수학여행 프로그램!",
            "detailed_intro": " 보는 수학여행이 아닌 하는 수학여행!\n 해설이 있고, 즐거운 산으로의 여행!\n 마을에서 놀면서 배우는 여행!",
          },
          {...},
        ]
      }
    ```
  - 추가 기능
    - 추가하고자 하는 Program 정보를 담아서 `/api/new`로 요청 (POST)
    - 입력 예시
      ```json5
        {
          "prgm_name": "예시 프로그램",
          "theme": "자연생태체험, 건강나누리캠프",
          "regions": "인천광역시 연수구 동춘동, 송도동",
          "intro": "예시 프로그램 소개",
          "detailed_intro": "예시 프로그램의 상세한 소개입니다!"
        }
      ```
  - 수정 기능
    - (예정)
      
### 생태 관광지 중에 '서비스 지역' 컬럼에서 특정 지역에서 진행되는 프로그램명과 테마를 출력하는 API
- ex) `평창군`이라는 문자열을 입력 받으면 아래와 같은 결과를 출력한다.
- 단, 출력 결과에 지역은 지역 코드를 출력한다.
- 입력 예시
```json
{
  "region":"평창군"
}
```
- 출력 예시
```json
{
  "region": "reg3726",
  "programs": [ 
    {
      "prgm_name":"오감만족! 오대산 에코 어드벤처 투어",
      "theme":"아동 청소년 체험학습" 
    },
    {
      "prgm_name":"오대산국립공원 힐링캠프", 
      "theme":"숲 치유"
    },
    ... 
  ]
}
```
- **문제해결 전략**
  - (예정)
  
### "프로그램 소개" 컬럼에서 특정 문자열이 포함된 레코드에서 서비스 지역 개수를 세어 출력하는 API
- ex) `세계문화유산` 문자열을 입력 받아, 포함된 레코드에서 서비스 지역 개수와 지역정보를 출력
- 입력 예시
```json
{
  "keyword":"세계문화유산"
}
```
- 출력 예시
```json
{
  "keyword": "세계문화유산", 
  "programs" : [
    {
      "region":"경상북도 경주시", 
      "count":"2"
    } 
  ]
}
```
- **문제해결 전략**
  - (예정)
  
### 모든 레코드의 프로그램 상세 정보를 읽어와서 입력 단어의 출현 빈도수를 계산하여 출력하는 API
- 입력 예시
```json
{
  "keyword":"문화"
}
```
- 출력 예시
```json
{
  "keyword": "문화", 
  "count" : "20"
}
```
- **문제해결 전략**
  - (예정)
  

## 사용한 기술
- Spring Boot
- JPA (Spring Data JPA)
- MySQL
- Maven
- OpenCSV

## 실행 방법
#### Database
1. `mysql -u root -p`
2. `create user 'kp'@'%' identified by 'kp';`
3. `grant all on kpdb.* to 'kp'@'%';`
4. `create user 'kp'@'localhost' identified by 'kp';`
5. `grant all on kpdb.* to 'kp'@'localhost';`
6. `flush privileges;`

#### Project
1. `git clone https://github.com/gyumin-kim/eco-information-api.git`
2. `cd eco-information-api`
3. `mvn package`
4. `java -jar target/eco-information-api-0.0.1-SNAPSHOT.jar`
5. `http://localhost:8080`으로 접속