MPushServices
=============

Message Push Service for mobile phone. 

Import:
1. git clone https://github.com/bravesmiles/MPushServices.git
2. Eclipse import as existing maven project
3. com.smiles.messaging.Application run as java application


Maven and run:
1. mvn clean install
2. put beanx.xml into target
3. cd target
4. java -jar java -jar MPushServices-VERSION.jar (VERSION is configured in pom.xml)

Usage:
1. localhost:8080/notifyAll?content=CONTENT (CONTENT is the content send to all client  via JPush)

Client:
Mobile phone that install JPush client with appKey: 946343519c3023fbfb39cf18


