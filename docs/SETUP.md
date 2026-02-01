# Setup

## Front
All the code for the fronted is in `apps/frontend`.

There is no setup needed, but you can set the environment `PUBLIC_API_BASE_URL` if you change the port of the backend

## Back
All the code for the fronted is in `apps/backend`.

For the setup you can copy the `.env.sample`, if you setup the database with the provided scripts you can use the following .env:
```
DATABASE_HOST=localhost
DATABASE_NAME=takitransfer-local-db
DATABASE_PASSWORD=Password1234
DATABASE_PORT=5432
DATABASE_USER=postgres
```

## Database
There is a script to quickly launch a PostgreSQL container locally. Run it to setup the database
```shell
cd scripts
./setup-db.sh
```