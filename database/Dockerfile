FROM postgres:9.6.17-alpine
ENV POSTGRES_USER docker
ENV POSTGRES_PASSWORD s3cr3t
ENV POSTGRES_DB monitor
COPY init.sql /docker-entrypoint-initdb.d/