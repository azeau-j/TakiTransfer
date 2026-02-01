#!/bin/bash

DB_NAME="takitransfer-local-db"
DB_USER="postgres"
DB_PASSWORD="Password1234"
VOLUME_NAME="takitransfer-local-db-data"
DB_PORT="5432"

if ! docker volume inspect $VOLUME_NAME &>/dev/null; then
    echo "The volume $VOLUME_NAME doesn't exist, creation..."
    docker volume create $VOLUME_NAME
fi

echo "Starting PostgreSQL container..."
docker run -d \
    --name takitransfer-db \
    -e POSTGRES_USER=$DB_USER \
    -e POSTGRES_PASSWORD=$DB_PASSWORD \
    -e POSTGRES_DB=$DB_NAME \
    -v $VOLUME_NAME:/var/lib/postgresql/data \
    -p $DB_PORT:$DB_PORT \
    --restart unless-stopped \
    postgres:15.2-alpine

if [ $? -eq 0 ]; then
    echo "The PostgreSQL container has been started."
else
    echo "Error while starting PostgreSQL container."
    exit 1
fi
