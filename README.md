# ShuttleAdvisory Microservice

This is a microservice that gives the duration for next shuttle by leveraging metro transit real time apis.

This is secured by oauth2 and is fault tolerant.

Log into H2 database console to check the user roles who have access as below:

http://localhost:8080/h2-console

Enter JDBC URL as jdbc:h2:mem:testdb


Get the access token by below steps:

POST Request:

http://localhost:8080/oauth/token?grant_type=password&username=user&password=user

Authorization:

Type :: Basic Auth

Username :: my-trusted-client

Password :: secret

Get the duration for next shuttle with the below api using the above access token:

GET Request:

http://localhost:8080/shuttles/{RouteName}/{Direction}/{StopName}/duration

http://localhost:8080/shuttles/METRO Green Line/EAST/Prospect Park/duration?access_token=xxxxx-xxxx-xxxx-xxx-xxxxxx

HTTP Responses:

200 - Success
400 - Bad Request for UserInput
401 - Unauthorized
500 - Server error
