package com.example.camelmicroservicedemo;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelSwitchExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Define a switch-like behavior based on a message header
                from("direct:start")
                        .choice()
                        .when(header("eventType").isEqualTo("order"))
                        .to("direct:processOrder")
                        .when(header("eventType").isEqualTo("invoice"))
                        .to("direct:processInvoice")
                        .otherwise()
                        .to("direct:processOther")
                        .end(); // End the choice block

                // Process order messages
                from("direct:processOrder")
                        .log("Processing order: ${body}");

                // Process invoice messages
                from("direct:processInvoice")
                        .log("Processing invoice: ${body}");

                // Process other messages
                from("direct:processOther")
                        .log("Processing other event: ${body}");
            }
        });

        // Start the Camel context
        context.start();

        // Send messages to the route with different event types
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Order details", "eventType", "order");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Invoice details", "eventType", "unknown");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Unknown event", "eventType", "invoice");

        // Stop the Camel context when done
        context.stop();
    }
}
