#!/bin/sh
cassandra -R

# Wait for Cassandra to start up
while ! cqlsh -e 'describe cluster' ; do
    sleep 1
done

echo "Cassandra has started"

cqlsh --file '/docker-entrypoint-initdb.d'

echo "Cassandra has been initialised"

tail -f /dev/null