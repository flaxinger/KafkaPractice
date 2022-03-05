# Spring-kafka practice

## 요약
아래 출처에 명시된 카프카 연습 문서는 하나의 SpringBoot 서버가 Producer, Consumer 역할을 모두 하는 예제를 포함하고 있다. 
3개의 파티션을 만들고 3개의 Listener(Consumer)를 만들어 각각 할당한 후 Class, String, Byte array로 Deserialization을 한다.
연습 문서와 달리 해당 연습 프로젝트에서는 두개의 서버를 사용했는데, Producer와 Consumer가 동일 서버였다면 카프카로 보낸 클래스와 받는 클래스가 동일하여 문제가 없었겠지만,
서버가 다른 경우 보낸 클래스를 찾지 못했다는 ClassNotFoundException이 발생하며 무한 루프를 돌았다.(컴퓨터가 갑자기 엄청 느려졌다 ㅠㅠ)
이를 위해 문서를 참고하여 역직렬화 과정에서 Client의 클래스로 매핑을 해주는 설정을 하였다. - [설정파일](/kafka-consumer/src/main/java/com/flaxingerkafka/kafkaconsumer/kafka/config/KafkaConfig.java)
구동을 위해서는 아래 순서대로 구동을 진행하면 된다.
> Record 때문에 JDK 14 이상이 필요하며 로컬에서는 JDK 16 기준으로 작업했습니다. 
```
cd docker
docker-compose up -f kafka.yml -d
cd ..
cd kafka-consumer
./mvnw spring-boot:run
cd ..
cd kafka-producer
./mvnw spring-boot:run
```

## references
[Spring Boot and Kafka - Practical Example](https://thepracticaldeveloper.com/spring-boot-kafka-config/)


