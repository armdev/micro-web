# micro-web

Microservices in the frontend.

wildfly-swarm JSF microservice

How to run?

1. mvn clean package -U
2. mvn wildfly-swarm:run

Access: http://localhost:9090/micro-web/index.jsf

Web page show post list from https://jsonplaceholder.typicode.com/posts , the same list you can access as REST resource

http://localhost:9090/micro-web/rest/posts/list which is cached.

So you have Java EE microservice for front end, this is a nice solution for developing modern applications.



