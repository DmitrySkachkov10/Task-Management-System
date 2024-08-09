FROM swaggerapi/swagger-ui:v4.18.2

ADD user-service/user-open-api.yml /usr/share/nginx/html/doc/user-open-api.yml
ADD task-service/task-open-aip.yml /usr/share/nginx/html/doc/task-open-api.yml


ENV URLS="[{ url: 'doc/user-open-api.yml', name: 'User'}, { url: 'doc/task-open-api.yml', name: 'Task'}]"
ENV URLS_PRIMARY_NAME="User"