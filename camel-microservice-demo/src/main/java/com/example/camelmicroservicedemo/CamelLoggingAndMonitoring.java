package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelLoggingAndMonitoring {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Route to capture messages and log them
                from("direct:start")
                        .log("Received message: ${body}")
                        .to("log:receivedMessages?showHeaders=true");

                // Route for monitoring: Forward messages to another endpoint
                from("direct:monitor")
                        .to("log:monitoringLog?level=INFO&showHeaders=true") // Log for monitoring
                        .to("mock:monitoringEndpoint"); // Forward messages to a monitoring endpoint (e.g., a message queue)

                // Route to simulate incoming messages
                from("timer:myTimer?period=5000") // Simulate incoming messages every 5 seconds
                        .setBody().constant("Hello, Camel!") // Set the message body
                        .to("direct:start"); // Send the message to the start of the logging route

//                from("direct:start")
//                        .to("log:MyLog?level=INFO") // Log INFO level messages
//                        .to("log:MyLog?level=DEBUG") // Log DEBUG level messages
//                        .to("log:MyLog?level=ERROR") // Log ERROR level messages
//                        .to("mock:result");
            }
        });

        // Start the Camel context
        context.start();

        // Keep the application running
        Thread.sleep(Long.MAX_VALUE);

        // Stop the Camel context when done
        context.stop();
    }
}
