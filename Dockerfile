FROM dajac/kfn-invoker:0.0.4
LABEL maintainer="David Jacot <david.jacot@gmail.com>"

COPY target/libs/* /usr/lib/kfn/

ARG JAR_FILE
COPY target/${JAR_FILE} /usr/lib/kfn/kfn-examples.jar
