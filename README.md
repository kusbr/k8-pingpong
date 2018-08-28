# k8-pingpong

(1) Setup Spring-boot Redis dependency 
    
    (1a) In pom.xml
            <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>

    (1b) use env variables in Application.settings
            spring.redis.host = ${CACHE_HOST}

(2) Build and test the services
    (2a) mvn clean package
    (2b) $env:CACHE_HOST="127.0.0.1"
    (2c) java -jar .\target\ping-0.0.1-SNAPSHOT.jar
    (2d) java -jar .\target\pong-0.0.1-SNAPSHOT.jar

(3) Docker build
   
   (3a) update pom.xml
    
            (i) under properies, add 
                <docker.image.prefix>springio</docker.image.prefix>

            (ii) under build plugins, add spotify
                <!-- tag::plugin[] -->
                <plugin>
                    <groupId>com.spotify</groupId>
                    <artifactId>dockerfile-maven-plugin</artifactId>
                    <version>1.3.6</version>
                    <configuration>
                        <repository>${docker.image.prefix}/${project.artifactId}</repository>
                        <buildArgs>
                            <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                        </buildArgs>
                    </configuration>
                </plugin>
                <!-- end::plugin[] -->

                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-dependency-plugin</artifactId>
                    <executions>
                        <execution>
                            <id>unpack</id>
                            <phase>package</phase>
                            <goals>
                                <goal>unpack</goal>
                            </goals>
                            <configuration>
                                <artifactItems>
                                    <artifactItem>
                                        <groupId>${project.groupId}</groupId>
                                        <artifactId>${project.artifactId}</artifactId>
                                        <version>${project.version}</version>
                                    </artifactItem>
                                </artifactItems>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>

        (3b) Create Dockerfile for each service
                FROM openjdk:8-jdk-alpine
                VOLUME /tmp
                ARG JAR_FILE
                COPY ${JAR_FILE} app.jar
                ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
                EXPOSE <PORT>

        (3c) Build docker image for each service
                docker build --build-arg JAR_FILE="./target/ping-0.0.1-SNAPSHOT.jar" -t kumsub/ping:v1 .
                docker build --build-arg JAR_FILE="./target/ping-0.0.1-SNAPSHOT.jar" -t kumsub/pong:v1 .

    (4) Test Docker Compose
            version: '3'
            services:
            pingsvc:
                image: kumsub/ping:v1
                container_name: ping
                ports:
                - 8090:8090
                environment:
                - CACHE_HOST=redis
                depends_on:
                - redis

            redis:
                image: redis:latest
                container_name: redis
                ports:
                - 6379:6379

            pongsvc:
                image: kumsub/pong:v1
                container_name: pong
                ports:
                    - 9090:9090
    
    (5) Setup Jenkins
        (5a)   https://docs.microsoft.com/en-us/azure/virtual-machines/linux/tutorial-jenkins-github-docker-cicd?view=vsts

        (5b)   https://docs.microsoft.com/en-us/azure/aks/jenkins-continuous-deployment#create-jenkins-project



  

