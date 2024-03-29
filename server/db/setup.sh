#!/bin/bash

# Load environment variables
set -a
source .env
set +a

# Extract database credentials from DATABASE_URL
DB_USER=$(echo $DATABASE_URL | sed -E 's/postgres:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)\/(.+)/\1/')
DB_PASS=$(echo $DATABASE_URL | sed -E 's/postgres:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)\/(.+)/\2/')
DB_HOST=$(echo $DATABASE_URL | sed -E 's/postgres:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)\/(.+)/\3/')
DB_PORT=$(echo $DATABASE_URL | sed -E 's/postgres:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)\/(.+)/\4/')
DB_NAME=$(echo $DATABASE_URL | sed -E 's/postgres:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)\/(.+)/\5/')

# Create a backup directory if it doesn't exist
mkdir -p backup

# Create a dump of the current database
echo "Creating a dump of the current database..."
PGPASSWORD=$DB_PASS pg_dump -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME > "backup/${DB_NAME}_$(date +%Y-%m-%d_%H-%M-%S).sql"

# Drop the current database
echo "Dropping the current database..."
PGPASSWORD=$DB_PASS dropdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME

# Create a new database
echo "Creating a new database..."
PGPASSWORD=$DB_PASS createdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME

# Apply the schema.sql file
echo "Applying the schema.sql file..."
PGPASSWORD=$DB_PASS psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f db/schema.sql

# Apply the init.sql file
echo "Applying the init.sql file..."
PGPASSWORD=$DB_PASS psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f db/init.sql

echo "Database reset and initialization completed successfully."
