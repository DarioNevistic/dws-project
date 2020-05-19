# DWS-Healthcare
A demo project explaining basic backend authentication and messages exchanging system using Spring Security, Spring Websockets, Spring Data JPA and MySQL.

Migration schema can be found at
[DWS Migration Schema](https://github.com/DarioNevistic/dws-healthcare/blob/master/src/configuration/src/main/resources/db/migration/V1__initial_schema.sql)


#### Authentication using Spring Security
 - Email based registration with admin support
 - Login using Spring Security and generation of JWT token
 - Support for expiration bases email verification. Mail is send upon registration via SendGrid API


#### Messages exchanging system using Spring Websockets
 - Creating private conversations
 - Support for exchanging instant messages and listening for errors via websockets
 - Tracking if user is online/offline
 - Fetching messages using pagination
 - Connecting to WS using STOMP protocol

#### MySql + PhpMyAdmin
You can run database containers using docker-compose with the command:

`docker-compose up -d`
  
  
#### Exception handling
The app throws custom exceptions each of which is extending `AbstractError` class from Chiwava error library.

REST API errors are expressed through Zalando problem implementation.


#### Add the default roles
 Add roles using bootstrap sql script which is located at:

`./src/configuration/src/main/resources/db/bootstrap.sql`



