# mutual-auth-client
The client which will do a mutual authentication ping request with the server.
The server needs to be running before the client is run, else the mojo-exec-plugin will fail.
After ensuring that the certificates have been generated and the server is up, run the following command

```
mvn exec:java
```
