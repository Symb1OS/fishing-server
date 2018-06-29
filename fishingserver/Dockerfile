FROM tomcat:8.0-jre8

RUN apt-get update

RUN apt-get install -y python3
RUN apt-get install -y python3-pip

RUN pip3 install http://download.pytorch.org/whl/cpu/torch-0.4.0-cp35-cp35m-linux_x86_64.whl
RUN pip3 install torchvision

RUN mkdir /usr/local/fishing-resources

COPY target/fishingserver.war /usr/local/tomcat/webapps/fishingserver.war