# mutual-auth-server
The server which will host a single rest api secured with certificate based mutual authentication.
After ensuring that the certificates have been generated, run the following command

```
mvn package && java -jar target/mutual-auth-client.0.0.1-SNAPSHOT.jar
```
