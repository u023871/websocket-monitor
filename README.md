# websocket-monitor
Spring boot/websocket/integration and JQuery/SockJS based monitoring web application

* Run web as Spring Boot app
* Run sender as Spring Boot app
* open http://localhost:9090
* run SpringWebSocketStompClientTest and watch sender Application and index.html receiving content sent by this JUnit test
* using "HTML Kickstarter" framework for HTML layout

Plan
* create a class for storing connected hosts in DB,
* then make a REST IF for it,
* then read it out on web using jquery,
* then install an instance on each target host,
* install mariadb and configure in the controller web application, which is the websocket-monitor-web spring boot app, with this central DB
* BooM! You got a web application which just needs to know which nodes exist... enhancement would be to feed this webapp with these infos from the nodes directly via websocket push...

