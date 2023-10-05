package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelErrorHandlingExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Define an exception handler for specific exceptions
                onException(RuntimeException.class)
                        .handled(true) // Mark the exception as handled
                        .log("Handled exception: ${exception.message}") // Log the exception
                        .to("direct:errorRoute"); // Route to handle the error

                // Main route
                from("direct:start")
                        .choice()
                        .when(simple("${body} contains 'error'"))
                        .throwException(new RuntimeException("Simulated error"))
                        .otherwise()
                        .to("mock:success"); // A mock endpoint for successful processing

                // Error route to handle exceptions
                from("direct:errorRoute")
                        .log("Processing error: ${body}")
                        .to("mock:error"); // A mock endpoint for error handling
            }
        });

        // Start the Camel context
        context.start();

        // Simulate a message with an error condition
        context.createProducerTemplate().sendBody("direct:start", "This is an error message");

        // Stop the Camel context when done
        context.stop();
    }
}
