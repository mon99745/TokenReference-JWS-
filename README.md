# Json Web Token, JwtReference
이 프로젝트는 `io.jsonwebtoken:jjwt-api` 라이브러리를 대체하여 `JwtProvider` 역할을 수행하는 목적으로 작성된 결과물이며, \
Json Web Token을 커스텀 발행 및 검증하여 관련 카테고리 개발의 시간 비용을 절감하고, 프로젝트의 소스와 설정 파일의 규격을 통일하여 코드의 품질과 가독성 향상하기 위한 목표로 증가 시키기 위한 목표로 추진한다.



## Release
- [v1.1.0](./) `-ing`
- [v1.0.0](./RELEASENOTE.md#v100-2024-01-30-)

## 기능 소개
### 1. 토큰 발행
#### 1-1) 호출 방식 
메소드명 : api/v1/createToken

| Request Type | Value  | Description |
|--------------|--------|-------------|
| POST         | String | String      |

#### 1-2) Request Parameters
| Key       | Value   | Description |
|-----------|---------|----------|
| uniqueId  | String  | Unique Value |
| name      | String  | 사용자 이름 |
| num       | String  | 임의의 값  |

```text
{
  "uniqueId": "12345678",
  "name" : "testUser",
  "num" : "1000"
}
```

![](src/main/resources/static/image/createToken.png)

### 2. 토큰 검증
#### 2-1) 호출 방식
메소드명 : api/v1/verifyToken

| Request Type | Value  | Description |
|--------------|--------|-------------|
| POST         | String | String      |

#### 2-2) Request Parameters
| Key   | Value  | Description          |
|-------|--------|----------------------|
| Token | String | Authentization Token |

```text
wtEhRDrZpioF.29Le3YBWnhCnozVCv9Abj2AwT5b8eWkZDivMEBw3eXgbPL13HgvJZyRJWzrHkbfovcEv4B
DGaiZePdDRXjpN9F9m.OxJt6IARXWnNSY1gbh/qAKMNnoZcx5s6gHj....
```

![](src/main/resources/static/image/verifyToken.png)
