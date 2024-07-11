<img src="https://capsule-render.vercel.app/api?type=waving&color=BDBDC8&height=150&section=header" />

# 🛒 [땅땅땅] 우리동네 경매서비스

<img width="900" alt="제목을-입력해주세요_-001 (1)" src="https://github.com/IP-I-s-Protocol/DDang/assets/151606621/575a46f1-8128-44fd-ad01-9c158fe15148">

---
## 👫🏼 Team
|<img src="https://avatars.githubusercontent.com/u/151606621?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/97017924?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/148612321?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/120919984?v=4" width="150" height="150"/>|
|:-:|:-:|:-----------------------------------------------------------------------------------------:|:-:|
|[@beginninggrace](https://github.com/beginninggrace)<br/>|[@kimpangya](https://github.com/kimpangya)|           구동현<br/>[@kudongku](https://github.com/kudongku)                      |boy who loves potato<br/>[@potatobboi](https://github.com/potatobboi)|

--- 
## 📚 기술스택

> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<br/>

```CICD```
> <img src="https://img.shields.io/badge/github Actions-2088FF?style=for-the-badge&logo=GitHubActions&logoColor=white">
> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
> <img src="https://img.shields.io/badge/amazon ECR-FF8000?style=for-the-badge&logo=amazon&logoColor=white">
> <img src="https://img.shields.io/badge/amazon ecs-FF9900?style=for-the-badge&logo=amazonecs&logoColor=white">
<br/>

```DataBase```

> <img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
> <img src="https://img.shields.io/badge/amazon elasticache-C925D1?style=for-the-badge&logo=amazon&logoColor=white">
> <img src="https://img.shields.io/badge/amazon Aurora DB-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">

---
## 🏛️ 기술 아키텍쳐

<img width="950" alt="팀플 아키텍처 - 최최종" src="https://github.com/IP-I-s-Protocol/DDang/assets/151606621/126d756f-d8d9-421c-b782-c0a2c0d592d3">

---
## 🍀 ERD
<img width="950" alt="image (5)" src="https://github.com/IP-I-s-Protocol/DDang/assets/151606621/6d99c9aa-e501-451c-b691-0cf8b3781d80">

---

## 🎤 구현 기능

**CI/CD**

- 깃허브 액션 + 도커이미지 + ECR + ECS로 무중단 배포 구현
- TDD 중심의 개발
- AuroraDB로 CQRS 패턴 적용
- Elasticache로 Redis 캐싱 서비스 구현
- S3로 이미지 저장

**회원가입**

- Apache POI로 엑셀파일 읽어오기
- 대한민국 행정구역 위도 경도 DB 화
- 회원가입시 클라이언트의 IP로 클라이언트 위치를 파악
- 카카오 API로 지도 핀 서비스 구현
- 회원의 동네 주소로 인근 동네 파악 기능 구현

**경매**

- 경매 낙찰자에게 SMTP 알림 메일 전송
- 동네 경매 글 조회 시 Redis 캐싱
- Redis Pub/Sub + SSE로 실시간 최고가 입찰 데이터를 클라이언트에게 전달
- Redis 분산락 적용으로 경매입찰 동시성 이슈 해결
- Redis Keyspace Notification으로 경매의 상태 자동 변경

---

## 🗣️ 기술적 의사결정


- SSE
    
    경매의 최고입찰가가 갱신될 때마다 유저에게 데이터가 전달되어야할 필요성이 있습니다.
    저희가 선택할 후보로는 Polling, Web Socket, SSE가 있는데,
    Polling은 경매의 입찰과 상관없이 리소스를 계속 사용하는 단점이 있었습니다.
    
    Web Socket과 달리 SSE는 HTTP 프로토콜에 속해서 프로토콜을 따로 구현할 필요가 없고,
    사용하는 로직에서 클라이언트에게서 받을 정보가 없기 때문에 SSE를 선택하게 되었습니다.
    
- Aurora DB
    
    일반 RDS를 사용하기보다, CQRS 패턴을 적용해서 읽기와 쓰기의 DB를 분리하고
    성능을 개선하고 싶었습니다.
    AuroraDB를 사용할 경우, 자동으로 CQRS가 적용이 되고 Serverless의 특성상 서비스 확장성에도 도움이 되어 선택하게 되었습니다.
    
- Redis Keyspace Notification
    
    저희 서비스는 경매의 종료기한을 정해두고, 종료가 되었을 때 경매의 상태가 자동으로 경매 중에서 경매 마감으로 바뀝니다. 
    스케줄러를 통해 기능을 구현할 경우, 아무리 주기적으로 실행한다고 한들 경매의 실시간성에 적합하지 않았고, 오히려 서버에 부하를 준다고 판단하였습니다.
    TTL이 있는 DB 중에서 Redis를 이미 사용하고 있기 때문에, Redis Keyspace Notification 을 선택하여 옥션의 시간이 변경될 경우에는 pub/sub으로 만료 이벤트를 수신할 수 있도록 하였습니다.

- ECS Fargate
    
    EC2와 ECS에서 ECS를 선택한 이유는, main application이 돌아갈 서버와 SSE를 담당할 서버, 총 2개의 서버를 컨테이너 단위로 배포와 관리를 해줄 서비스로 ECS가 적합하다고 판단하였습니다.
    Fargate를 사용하면 태스크만 정의해주면 서버리스의 특성으로 인프라 영역을 관리하지 않고
    바로 실행할 수 있다는 장점이 있다는 것을 알았습니다.
    또, 컨테이너 수준에서 자원을 할당하므로 필요한 만큼의 자원을 할당하고 자원 사용량에 따라 자동으로 조절되어 리소스를 절약 할 수 있기에 선택하였습니다!

---

## 🛠 트러블슈팅


### 캐싱처리

- 유저가 속한 동네의 경매글 목록을 조회하는 기능이 가장 오래걸렸습니다. (평균 6629ms)
- Ehcache를 사용해서 캐싱처리를 한 결과 평균 192ms로 97% 정도 응답속도가 줄어든 것을 확인할 수 있었습니다.
- 하지만 서버가 2개이상인 저희 서비스의 경우, 데이터 부정합 문제가 발생할 경우를 대비해 Redis를 통해 캐싱서비스를 구현했고(평균 337ms), 이를 통해  95% 정도 응답속도를 줄일 수 있었습니다.
![image](https://github.com/kudongku/auction_ddang/assets/148612321/d482679b-d170-4137-9c11-5ce3800d4608)


### CloudFront로 이미지 조회

- 저희의 서비스는 S3에 경매 이미지를 저장하고, 저장된 이미지를 S3 버킷의 엔드포인트로 조회했습니다.
- 이미지 조회에 걸리는 시간을 줄이기 위해 CloudFront에 유저가 이미지를 요청하면, CloudFront는 S3에 조회를 하며 로컬캐시에 저장을 합니다.
- 저장된 데이터를 다시 요청할 경우, S3 대신 CloudFront의 로컬 캐시에서 조회를 합니다.
- 이를 통해 54ms에서 10ms로 성능을 개선할 수 있었습니다.
![image](https://github.com/kudongku/auction_ddang/assets/148612321/b8d44ef4-7457-446f-be59-613cad64237b)

    
### JPA에서 Entity 대신 DTO로 조회

- 기존에는 Repository에서 직접 entity를 받고, entity를 통해 DTO를 생성하는 과정이 있었습니다.
- QueryDSL의 Projection을 사용하여, Entity의 모든 컬럼을 조회하는 대신 필요한 컬럼만 조회하고자 했습니다.
- 355ms 에서 52ms로 조회 시간을 단축시킬 수 있었습니다.
- ![image](https://github.com/kudongku/auction_ddang/assets/148612321/3873b706-4cdb-4de9-9d98-55e8a471037a)



<img src="https://capsule-render.vercel.app/api?type=waving&color=BDBDC8&height=150&section=footer" />
