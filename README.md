# mutual-auth
Figure out how to get mutual authentication running with self-signed certificates, spring, embedded tomcat and java.

## Building
The building and execution needs to be in this order.
1. Generate self signed certificates
2. Run server
3. Run client

### 1. Generate self signed certificates
Run the following command in the cert directory
```
./createKey.sh
```
Put password as 'password' whenever prompted.

### 2. Run server
Please refer to the readme in the [MutualAuthServer](./MutualAuthServer/) folder.

### 3. Run client
Please refer to the readme in the [MutualAuthClient](./MutualAuthClient/) folder.

## Check mutual authentication with curl
From the certs directory, issue the following command while the server is running
```
curl 'https://localhost:8443/api/ping' --key MyClient.key.pem --cert MyClient.crt.pem:password -k
```
