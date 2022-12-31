FROM apache/spark:v3.2.1

COPY target/scala-2.12/dataloader_2.12-0.1.jar /opt/target/scala-2.12/

ADD run.sh /opt/run.sh

WORKDIR /opt

USER root

RUN chmod 777 /opt/run.sh

ENTRYPOINT "./run.sh"