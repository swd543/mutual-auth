# mutual-auth-client
The client which will do a mutual authentication ping request with the server.
The server needs to be running before the client is run, else the mojo-exec-plugin will fail.
After ensuring that the certificates have been generated and the server is up, run the following command

```
mvn package && mvn exec:java
```

The path to the certificate files can be changed [here](/src/main/java/com/buga/boxes/main/MutualAuthClient.java), by the *StoreUrl final variables.
This is a source change and needs to be recompiled to take effect.
