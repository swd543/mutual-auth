# mutual-auth-server
The server which will host a single rest api secured with certificate based mutual authentication.
After ensuring that the certificates have been generated, run the following command

```
mvn package && java -jar target/mutual-auth-server-0.0.1-SNAPSHOT.jar
```

The path to the certificate files can be changed [here](./src/main/resources/application.properties), by the *store variables.
This is a source change and needs to be recompiled to take effect.
