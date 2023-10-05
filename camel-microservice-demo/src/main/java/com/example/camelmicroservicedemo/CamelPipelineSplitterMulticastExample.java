package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelPipelineSplitterMulticastExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")

                        // Splitter pattern: Split a message into multiple parts
                        .split().tokenize("\n")

                        // Multicast pattern: Process multiple parts concurrently
                        .multicast()
                        .parallelProcessing()

                        // First branch: Uppercase the message part
                        .to("direct:uppercase")

                        // Second branch: Lowercase the message part
                        .to("direct:lowercase")
                        .end();

                // Uppercase branch
                from("direct:uppercase")
                        .transform().simple("${body.toUpperCase()}")
                        .log("Uppercase: ${body}");

                // Lowercase branch
                from("direct:lowercase")
                        .transform().simple("${body.toLowerCase()}")
                        .log("Lowercase: ${body}");
            }
        });

        // Start the Camel context
        context.start();

        // Send a message to the start of the route
        context.createProducerTemplate().sendBody("direct:start", "Hello\nWorld");

        // Stop the Camel context when done
        context.stop();
    }
}
