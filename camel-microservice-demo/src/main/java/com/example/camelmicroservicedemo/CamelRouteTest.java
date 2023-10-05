package com.example.camelmicroservicedemo;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.mock.MockEndpoint;

public class CamelRouteTest {

    public static void main(String[] args) throws Exception {

        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Add a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {

        @Override
        public void configure() throws Exception {

            // Define a simple route
            from("direct:start") // Input from a direct endpoint
            .to("mock:result"); // Send the message to a mock endpoint
            }
        });

        // Start the Camel context
        context.start();

        // Create a mock endpoint for testing
        MockEndpoint mockEndpoint = context.getEndpoint("mock:result", MockEndpoint.class);


        // Define expectations for the mock endpoint
        mockEndpoint.expectedBodiesReceived("Hello, Camel!"); // Expect this message

        // Send a message to the direct:start endpoint
        context.createProducerTemplate().sendBody("direct:start", "Hello, Camel!");

        // Assert that the mock endpoint received the expected message
        mockEndpoint.assertIsSatisfied();

        // Stop the Camel context when done
        context.stop();

    }
}
