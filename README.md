
ZOOKEEPER=zookeeper:2181
KAFKA=kafka-headless:9092

davids-macbook-pro:kfn dajac$ k exec -ti kafka-client bin/sh

# /usr/bin/kafka-topics --zookeeper zookeeper:2181 --create --topic kfn.users.json --partitions 10 --replication-factor 1
# /usr/bin/kafka-topics --zookeeper zookeeper:2181 --create --topic kfn.users.avro --partitions 10 --replication-factor 1
# /usr/bin/kafka-topics --zookeeper zookeeper:2181 --create --topic kfn.users.avro.hashed --partitions 10 --replication-factor 1

# /usr/bin/kafka-console-producer --broker-list kafka-headless:9092 --topic kfn.users.json

# {"name": "David", "number": "+41791234567"}

# /usr/bin/kafka-console-consumer --bootstrap-server kafka-headless:9092 --topic kfn.users.avro.hashed --from-beginning

/usr/bin/kafka-topics /usr/bin/kafka-topics --zookeeper zookeeper:2181 --delete --topic kfn.users.json
/usr/bin/kafka-topics /usr/bin/kafka-topics --zookeeper zookeeper:2181 --delete --topic kfn.users.avro

