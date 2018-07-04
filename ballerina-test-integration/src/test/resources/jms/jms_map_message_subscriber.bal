import ballerina/jms;
import ballerina/io;
import ballerina/http;


// Initialize a JMS connection with the provider.
jms:Connection conn = new ({
        initialContextFactory: "bmbInitialContextFactory",
        providerUrl: "amqp://admin:admin@carbon/carbon?brokerlist='tcp://localhost:5772'"
    });

// Initialize a JMS session on top of the created connection.
jms:Session jmsSession = new (conn, {
        // Optional property. Defaults to AUTO_ACKNOWLEDGE
        acknowledgementMode: "AUTO_ACKNOWLEDGE"
    });

// Initialize a Queue consumer using the created session.
endpoint jms:TopicSubscriber topicSubscriber {
    session: jmsSession,
    topicPattern: "testMapMessageSubscriber"
};

// Bind the created consumer to the listener service.
service<jms:Consumer> jmsListener bind topicSubscriber {

    // OnMessage resource get invoked when a message is received.
    onMessage(endpoint subscriber, jms:Message message) {
        map messageRetrieved = check message.getMapMessageContent();
        io:print(messageRetrieved["a"]);
        io:print(messageRetrieved["b"]);
        io:print(messageRetrieved["c"]);
        io:print(messageRetrieved["d"]);
        byte[] retrievedBlob = check <byte[]>messageRetrieved["e"];
        io:println(retrievedBlob.toString("UTF-8"));
    }
}

// This is to make sure that the test case can detect the PID using port. Removing following will result in
// intergration testframe work failing to kill the ballerina service.
endpoint http:Listener helloWorldEp {
    port:9090
};

@http:ServiceConfig {
    basePath:"/jmsDummyService"
}
service<http:Service> helloWorld bind helloWorldEp {

    @http:ResourceConfig {
        methods:["GET"],
        path:"/"
    }
    sayHello (endpoint client, http:Request req) {
        // Do nothing
    }
}
