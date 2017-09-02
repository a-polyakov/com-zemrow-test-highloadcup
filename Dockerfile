FROM tomcat:8.5.20-jre8
RUN rm -r /usr/local/tomcat/webapps/*
RUN sed -i 's/port="8080"/port="80"/g' /usr/local/tomcat/conf/server.xml
COPY /target/ROOT.war /usr/local/tomcat/webapps/
EXPOSE 80/tcp