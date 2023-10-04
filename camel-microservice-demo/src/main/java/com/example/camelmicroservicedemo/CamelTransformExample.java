package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelTransformExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Transform the message body in various ways
                from("direct:start")
                        .setBody().constant("Constant Body") // Set a constant body
                        .to("log:log1?showHeaders=true")
                        // Log the transformed message

                        .setBody().simple("Simple Body ${body}") // Use a Simple language expression
                        .to("log:log2?showHeaders=true") // Log the transformed message

                        .transform().body(String.class, s -> "Lambda Body") // Use a Lambda expression
                        .to("log:log4?showHeaders=true") // Log the transformed message

                        .bean(MyBean.class, "transformBody") // Updated method name
                        .to("log:log5?showHeaders=true");
            }
        });

        // Start the Camel context
        context.start();

        // Send a message with the initial body
        context.createProducerTemplate().sendBody("direct:start", "Initial Body");

        // Stop the Camel context when done
        context.stop();
    }
}

// Custom Java bean for transformation
class MyBean {
    public static String transformBody(String body) {
        return "Bean Body";
    }
}
