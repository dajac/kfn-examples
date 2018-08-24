FROM maven:3.5.4-jdk-8 AS build
COPY src /usr/src/kfn/src
COPY pom.xml /usr/src/kfn
RUN mvn -f /usr/src/kfn/pom.xml clean package

FROM dajac/kfn-invoker:0.0.2
COPY --from=build /usr/src/kfn/target/kfn-examples-*.jar /usr/lib/kfn/kfn-examples.jar
COPY --from=build /usr/src/kfn/target/libs/* /usr/lib/kfn/
