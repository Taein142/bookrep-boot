FROM gradle:8.6.0-jdk21 AS BUILD
WORKDIR /home/app
#COPY . /home/app
#RUN gradle clean build

#FROM openjdk:21
#EXPOSE 5000
#COPY --from=BUILD /home/app/build/libs/*.jar app.jar
#ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]

COPY ./build.gradle /home/app/build.gradle
COPY ./src/main/java/com/rep/book/bookrepboot/BookrepBootApplication.java	/home/app/src/main/java/com/rep/book/bookrepboot/BookrepBootApplication.java

RUN gradle clean build

COPY . /home/app
RUN gradle clean build

FROM openjdk:21
EXPOSE 5000
COPY --from=BUILD /home/app/build/libs/*.war app.war
ENTRYPOINT [ "sh", "-c", "java -jar /app.war" ]