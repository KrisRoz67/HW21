FROM postgres:16.0-alpine
ENV POSTGRES_USER docker_user
ENV POSTGRES_PASSWORD docker_pw
ENV POSTGRES_DB docker_db
COPY data.sql /docker-entrypoint-initdb.d/