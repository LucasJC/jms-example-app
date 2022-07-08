# weblogic-jms-example

This is a simple project that shows how JMS messages can be produced and consumed using JMS resources inside a Weblogic 
Server.
Thanks to Spring Framework and JMS abstractions, this code is compatible with any other JMS server implementation after 
only some configuration changes.

## Weblogic client

In the case of Weblogic, if you are developing an application that will not run inside the server, you have to provide
one of the client implementations available since it uses a custom communication protocol.
Just having the jar of one of the implementations in the classpath is enough for it to work.

More information in: https://docs.oracle.com/cd/E17904_01/web.1111/e13717/wlthint3client.htm#SACLT380

_This project declares a dependency on the wlthint3client JAR.
This file is available inside a weblogic installation ('/u01/oracle/wlserver/server/lib')._

## Running a Weblogic server using docker
Weblogic images are available in the oracle container registry: 
https://container-registry.oracle.com/ords/f?p=113:10:9676483614677:::::

Log into the registry using docker (this requires an oracle registration):
```bash
docker login container-registry.oracle.com
```

```bash
docker pull container-registry.oracle.com/middleware/weblogic:12.2.1.4-dev-ol8
```

Create a file named domain.properties with the following content:
```properties
username=myadminusername
password=myadminpassword
```

Launch a container using the image (execute from the same directory where the properties file is located):
```bash
# In linux
docker run -d -p 7001:7001 -p 9002:9002 -v $PWD:/u01/oracle/properties container-registry.oracle.com/middleware/weblogic:12.2.1.4-dev-ol8
# In windows
docker run -d -p 7001:7001 -p 9002:9002 -v %cd%:/u01/oracle/properties container-registry.oracle.com/middleware/weblogic:12.2.1.4-dev-ol8
```

After the container starts, weblogic console will be available at 'https://localhost:9002/console'.
Your new server will have no JMS resources set up, you will have to create at least:
- A JMS Server targeted to the Admin Server
- A JMS Module
- A Secondary Deployment targeted to the Admin Server
- A Connection Factory inside the module, attached to the Secondary Deployment from before
- A Queue inside the module, attached to the Secondary Deployment from before

_The JNDI names for both the Connection Factory and the Queue are the ones that have to be set up in the 
application.properties file._
