# websocket-monitor
Spring boot/websocket/integration and JQuery/SockJS based monitoring web application

* Run web as Spring Boot app
* Run sender as Spring Boot app (web must be 1st or connect from browser client will fail!)
* open http://localhost:9090/monitor
* run SpringWebSocketStompClientTest (make sure correct java.library.path for Sigar is set) and watch sender Application and index.html receiving content sent by this JUnit test

Backend architecture
* Spring Boot
* Spring Web Socket
* Spring REST
* Spring JPA + MariaDB
* Spring Scheduler

Frontend architecture
* HTML 5
* jQuery
* HTML Kickstarter
* SockJS

Plan
* install an instance of websocket-monitor-sender Spring Boot App on each source host
* install mariadb and configure in the controller web application, which is the websocket-monitor-web spring boot app, with this central DB
* BooM! You got a web application which just needs to know which nodes exist...

