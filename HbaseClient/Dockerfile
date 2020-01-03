FROM dockeralexandrtalan/java11

ARG HOME=/root/hbase_client

RUN mkdir $HOME

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN ln -s /usr/lib/apt/methods/http /usr/lib/apt/methods/https
RUN apt-get update
RUN apt-get install curl -y
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add

RUN apt-get update
RUN apt-get install sbt -y

WORKDIR $HOME

ENTRYPOINT sbt
