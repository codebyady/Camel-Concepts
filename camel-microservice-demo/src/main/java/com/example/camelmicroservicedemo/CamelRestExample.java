package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;

public class CamelRestExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder with REST configuration
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Configure Jetty component for REST
                restConfiguration()
                        .component("jetty")
                        .host("localhost")
                        .port(8080);

                // Define a REST endpoint at /hello
                from("rest:get:/hello").setBody(constant("Hello, World!"))// Listen for GET requests at /hello
                        .to("log:jsonLog"); // Log the request

                from("rest:post:/hellopost") // Listen for POST requests at /post
                        .log("Received POST request with body: ${body}") // Log the POST request body
                        .to("log:jsonLog");
            }

        });

        System.out.println("Listening to http://localhost:8080/hello");
        // Start the Camel context
        context.start();

        // Keep the application running
        Thread.sleep(Long.MAX_VALUE);

        // Stop the Camel context when done
        context.stop();
    }
}
