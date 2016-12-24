FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/async-server-0.0.1-SNAPSHOT-standalone.jar /async-server/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/async-server/app.jar"]
