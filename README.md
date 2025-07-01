## 방학숙제 : 선생님 AI와 함께하는 특별한 일기쓰기
* 개발 기간 : `2025.06 ~ 2025.07`
* 도메인 : `라이프스타일/일기 앱`
* 팀 및 역할 : `BE/FE 1인, AI 1인 中 BB/FE 개발자로 참여`

</br>
</br>

## 서비스 소개

> 초등학생 시절 방학숙제였던 일기장 컨셉의 앱입니다. </br>
일기를 작성하면, 일정시간 후 일기에 대한 **코멘트와 칭찬도장**이 AI선생님으로부터 도착합니다.

![Frame 2](https://github.com/user-attachments/assets/cd4c3cbf-3155-4b2f-9002-ebbb71654345)

</br>

- [Apple 앱스토어 바로가기](https://apps.apple.com/kr/app/%EB%B0%A9%ED%95%99%EC%88%99%EC%A0%9C-%EC%84%A0%EC%83%9D%EB%8B%98ai%EC%99%80%EC%9D%98-%EC%9D%BC%EA%B8%B0/id6747587236)

</br>
</br>


## 주요기능 소개

**(1) 일기쓰기**
- 사용자가 작성한 일기(제목, 본문, 감정 등)를 Spring 서버에 저장합니다.
- 저장 시점에 homework.created 이벤트를 RabbitMQ로 발행하여, 이후 AI 분석 프로세스가 **비동기적으로 수행**됩니다.

</br>


**(2) AI 코멘트 자동 생성 및 푸시 알림 전송**
- AI서버((FastAPI)는 MQ 이벤트를 구독해 일기 데이터를 가져오고, AI 분석 결과를 기반으로 코멘트를 생성합니다.
- 생성된 코멘트는 다시 Spring 서버로 전송되어 해당 일기에 연결됩니다.
- 위 로직이 완료된 일기의 경우, 상세 조회 시 코멘트가 함께 노출됩니다.
- 메시지 큐 기반으로 설계해 **AI 서버 부하나 장애 상황에서도 시스템이 영향을 받지 않도록 안정성을 확보**했습니다.
- 코멘트 저장 이후, **Firebase Cloud Messaging(FCM)** 을 통해 사용자 디바이스로 "**선생님의 코멘트가 도착했어요!**"와 같은 알림을 전송합니다.



</br>
</br>

## 시스템 아키텍처
![image](https://github.com/user-attachments/assets/29be7367-4731-4f48-9c11-00c4a0d520c4)

</br>
</br>

## 📚 기술스택
> **Backend** </br>
`Java 17`,  `SpringBoot 3.2.4`,  `JPA`,  `MySQL 8.0.41`, `RabbtMQ`

> **CI/CD & Infra** </br>
`GithubActions`, `Docker`, `Docker-compose`, `NginX`, `portainer`

> **Frontend** </br>
`Flutter`

> **AI** </br>
`Fast API`,  `Ollama`
