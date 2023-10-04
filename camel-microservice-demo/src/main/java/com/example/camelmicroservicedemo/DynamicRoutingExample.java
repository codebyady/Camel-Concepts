package com.example.camelmicroservicedemo;

import org.apache.camel.ExchangeProperty;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.CamelContext;

public class DynamicRoutingExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Define a dynamic router that routes messages based on their content
                from("direct:start")
                        .choice()
                            .when(body().isNotNull())
                                .dynamicRouter(method(DynamicRoutingExample.class, "chooseRoute"))
                            .endChoice()
                        .end()
                        .log("Message body is null. Routing terminated.");

                // Define three routes with different endpoints
                from("direct:route1")
                        .to("log:route1")
                        .to("mock:result");

                from("direct:route2")
                        .to("log:route2")
                        .to("mock:result");

                from("direct:route3")
                        .to("log:route3")
                        .to("mock:result");
            }
        });

        // Start the Camel context
        context.start();

        // Send a message to the dynamic router with a non-null body
        context.createProducerTemplate().sendBody("direct:start", "Route1,Route2,Route3");

        // Send a message to the dynamic router with a null body
        context.createProducerTemplate().sendBody("direct:start", null);

        // Stop the Camel context when done
        context.stop();
    }

    // Dynamic router logic
    public String chooseRoute(@ExchangeProperty("camelReceivedMessage") String body) {
        // Check if the message body is null
        if (body == null) {
            return null; // End routing if the body is null
        }

        // Decide which route to go based on the message content
        if (body.contains("Route1")) {
            return "direct:route1";
        } else if (body.contains("Route2")) {
            return "direct:route2";
        } else if (body.contains("Route3")) {
            return "direct:route3";
        } else {
            // If no match, return null to end routing
            return null;
        }
    }
}
