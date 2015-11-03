FROM mysql:5.7

ADD docker-entrypoint-initdb.d/* docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=root-password
ENV MYSQL_DATABASE=events
ENV MYSQL_USER=events
ENV MYSQL_PASSWORD=events0
