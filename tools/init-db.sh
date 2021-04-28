#!/usr/bin/env bash

PASSWORD="${1:-$DB_PASSWORD}"

MAIN_DB="${2:-$DB_DATABASE}"

DB_USER="${3:-$DB_USER}"

cat << EOF | su - postgres -c psql
-- Create the database user:
CREATE ROLE "$DB_USER" WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$PASSWORD';
-- Create the database:
CREATE DATABASE "$MAIN_DB" WITH OWNER="$DB_USER"
                                  LC_COLLATE="en_US.utf8"
                                  LC_CTYPE="en_US.utf8"
                                  ENCODING="UTF8"
                                  TEMPLATE=template0;
-- Grant necessary privileges
GRANT ALL PRIVILEGES ON DATABASE "$MAIN_DB" TO "$DB_USER";
EOF
