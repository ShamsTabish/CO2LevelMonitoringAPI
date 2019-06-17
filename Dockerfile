
# Derived from official mysql image (our base image)
FROM mysql


# Add a database
ENV MYSQL_DATABASE co2Status
# Add the content of the dataAccessLayes.sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY databaseScripts /docker-entrypoint-initdb.d/