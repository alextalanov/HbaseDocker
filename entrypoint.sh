#!/bin/sh

while [[ $1 ]]; do
case $1 in
     --command)
          shift
          COMMAND=$1
          shift
          ;;
     --config)
          shift
          CONFIG=$1
          shift
          ;;
     *)
          echo "'$1' arg is not supported"
          exit 1
          ;;
esac
done

if [[ ! $COMMAND ]]; then
	echo '--command must be passed'
	exit 1
elif [[ $COMMAND == "master" ]]; then
     COMMAND="start-hbase.sh"
elif [[ $COMMAND == "slave" ]]; then
     COMMAND="sleep 10m"
     echo 'Waiting command from hbase master.'
fi

if [[ $CONFIG ]]; then
	create-config.py "$CONFIG"
fi

ssh-keygen -t rsa -f ~/.ssh/hbase_rsa -N ""
cat ~/.ssh/hbase_rsa.pub >> ~/.ssh/hbase/authorized_keys

source $HBASE_HOME/conf/hbase-env.sh

/bin/bash -c "/etc/init.d/ssh start && $COMMAND"

DIR=/tmp
PID=$(cat $DIR/$(ls $DIR | grep ^.*\.pid$))

sleep 1m
echo "Monitoring for process=$PID ..."
while [ -e /proc/$PID ]; do
    sleep 5m
done
echo "Process $PID has finished"