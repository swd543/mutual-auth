#!/bin/sh

KEYSTORESERVER=MyServer
KEYSTORECLIENT=MyClient

echo "Cleanup of existing certificates and stores"
rm $KEYSTORESERVER* $KEYSTORECLIENT*

#echo "Creating trust store for server (jks)"
#keytool -genkey -alias $KEYSTORESERVER -keyalg RSA -validity 1825 -keystore $KEYSTORESERVER.jks -storetype JKS -dname "CN=myserver.com,OU=My Company Name,O=My Organization,L=My Location,ST=My State,C=My Country Short Code" -keypass password -storepass password
echo "Creating trust store for server (pkcs12)"
#keytool -importkeystore -srckeystore $KEYSTORESERVER.jks -destkeystore $KEYSTORESERVER.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass password
keytool -genkey -alias $KEYSTORESERVER -keyalg RSA -validity 1825 -keystore $KEYSTORESERVER.p12 -storetype PKCS12 -dname "CN=myserver.com,OU=My Company Name,O=My Organization,L=My Location,ST=My State,C=My Country Short Code" -keypass password -storepass password -ext "SAN=ip:127.0.0.1,dns:localhost"

#echo "Creating trust store for client (jks)"
#keytool -genkey -alias $KEYSTORECLIENT -keyalg RSA -validity 1825 -keystore $KEYSTORECLIENT.jks -storetype JKS -dname "CN=myclient.com,OU=My Company Name,O=My Organization,L=My Location,ST=My State,C=My Country Short Code" -keypass password -storepass password
echo "Creating trust store for client (pkcs12)"
#keytool -importkeystore -srckeystore $KEYSTORECLIENT.jks -destkeystore $KEYSTORECLIENT.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass password
keytool -genkey -alias $KEYSTORECLIENT -keyalg RSA -validity 1825 -keystore $KEYSTORECLIENT.p12 -storetype PKCS12 -dname "CN=myclient.com,OU=BugaBoxesInc,O=R&D,L=My Location,ST=My State,C=My Country Short Code" -keypass password -storepass password

echo "Export server certificate"
keytool -exportcert -alias $KEYSTORESERVER -keystore $KEYSTORESERVER.p12 -file $KEYSTORESERVER.cer
echo "Export client certificate"
keytool -exportcert -alias $KEYSTORECLIENT -keystore $KEYSTORECLIENT.p12 -file $KEYSTORECLIENT'Public'.cer

echo "Making client trust server"
keytool -importcert -alias $KEYSTORESERVER -keystore $KEYSTORECLIENT.p12 -file $KEYSTORESERVER.cer
echo "Making server trust client"
keytool -importcert -alias $KEYSTORECLIENT -keystore $KEYSTORESERVER.p12 -file $KEYSTORECLIENT'Public'.cer

if [ $# -ne 0 ]; then
	echo "Creating pem files"
	echo "Use the following command to test your api"
	echo "curl 'https://localhost:8443/api/ping' -H 'Upgrade-Insecure-Requests: 1' -H 'Cookie: JSESSIONID=6F15812966C1F8001D89AE96E0DBFE0C' --key MyClient.key.pem --cert MyClient.crt.pem:password -k"
	openssl pkcs12 -in $KEYSTORECLIENT.p12 -out $KEYSTORECLIENT.key.pem -nocerts -nodes -passin pass:password
	openssl pkcs12 -in $KEYSTORECLIENT.p12 -out $KEYSTORECLIENT.crt.pem -clcerts -nokeys -passin pass:password
fi
