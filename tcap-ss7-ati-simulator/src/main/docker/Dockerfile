FROM java:8
VOLUME /tmp
ADD bell-sam-tcap-ss7-ati-simulator-0.0.3-SNAPSHOT.jar app.jar
COPY log4j2.xml /opt/vennetics/sam/config/
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dlogging.config=file:/opt/vennetics/sam/config/log4j2.xml","-Dlogging.file=tcap-ss7-ati-simulator.log","-jar","/app.jar"]
