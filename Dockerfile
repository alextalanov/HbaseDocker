FROM dockeralexandrtalan/java8

ARG APP=/usr/local/bin
ARG HBASE_ARHIVE=hbase-2.2.2-bin.tar.gz

RUN apt-get install -y  python3
RUN apt-get install -y openssh-client
RUN apt-get install -y openssh-server

COPY ./create-config.py $APP
COPY ./entrypoint.sh $APP

RUN chmod 755 $APP/create-config.py $APP/entrypoint.sh

COPY ./ssh/sshd_config /etc/ssh
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_hbase_rsa_key -N ""

ARG HOME=/root
WORKDIR $HOME

RUN mkdir $HOME/.ssh
COPY ./ssh/config $HOME/.ssh


RUN wget --no-check-certificate https://www.dropbox.com/s/hdaew0mee6ht2xy/hbase-2.2.2-bin.tar.gz?dl=0 -O $HBASE_ARHIVE
RUN tar -xvzf $HBASE_ARHIVE
RUN rm -f $HBASE_ARHIVE

ENV HBASE_HOME=$HOME/hbase-2.2.2
ENV PATH=$PATH:$HBASE_HOME/bin
ENV HBASE_CONFIG=$HBASE_HOME/conf

CMD ["entrypoint.sh"]
