# Json Web Token, JwtsReference
이 프로젝트는 `JwtsProvider` 역할 수행의 **참고자료로 활용하는 것을 목적으로 작업한 결과물**이며, \
  <u>개발의 생산성과 편의성을 증대하고</u>, 프로젝트의 소스와 설정 파일의 규격을 통일하여 <u>코드의 품질과 가독성 향상</u>하기 위한 목표로 추진한다.


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