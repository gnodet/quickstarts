Introduction
============
This quickstart demonstrates how policy can be used to control the security characteristics of a
service invocation.  The only service in the application is a Bean service called "WorkService".
SSL is used for "confidentiality", and SAML Assertion is used for "clientAuthentication".


Running the quickstart
======================

1. Build the quickstart:

        mvn clean install

2. Create a keystore to support SSL:

        cd ${AS}/standalone/configuration
    keytool -genkey -alias tomcat -keyalg RSA -keypass changeit -keystore tomcat.jks
    
        (password is "changeit")

3. Add the required https connector to the web subsystem in standalone.xml to support SSL. (include contents of connector.xml)
4. Add the required security-domain sections to the security subsystem in standalone.xml to support SSL. (include contents of security-domain.xml)
5. Copy the sts-client.properties configuration file:

        cp sts-client.properties ${AS}/standalone/configuration

6. Deploy the PicketLink STS service:

        cp picketlink-sts.war ${AS}/standalone/deployments

7. Deploy the quickstart

        mvn jboss-as:deploy

8. Start JBoss AS 7 in standalone mode:

        ${AS}/bin/standalone.sh

9. Execute the test
    See "Options" section below.
10. Check the server console for output from the service.
11. Undeploy the application

        mvn jboss-as:undeploy



Options
=======

When running with no options:

    mvn exec:java

, you will be hitting the http (non-SSL) URL, and see this in your log:

    Caused by: org.switchyard.exception.SwitchYardException: Required policies have not been provided: clientAuthentication confidentiality

When running with this option:

    mvn exec:java -Dexec.args="confidentiality clientAuthentication"

, you will be hitting the https (SSL) URL and providing authentication information, and see this in your log:

    INFO  [org.switchyard.quickstarts.demo.policy.security.saml.WorkServiceBean] (http--127.0.0.1-8443-1) :: WorkService :: Received work command => CMD-1345738943385

You can play with the exec.args and only specify one of "confidentiality" or "clientAuthentication". I bet you can guess what will happen... ;)
