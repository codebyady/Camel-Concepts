package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelProcessorExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Define a route that reads from a direct:start endpoint
                // and processes the message using a custom Processor
                from("direct:start")
                        .process(new MyProcessor()) // Using a custom Processor
                        .log(LoggingLevel.valueOf("INFO"), "My Custom Log Message:\n${body}"); // Log the processed message to the console

            }
        });

        // Start the Camel context
        context.start();

        // Create an Exchange with a message
        Exchange exchange = context.createProducerTemplate().send("direct:start", exchange1 -> {
            exchange1.getIn().setBody("Hello, Camel!");
        });
        System.out.println(exchange.getIn().getHeader("newHeader"));
        // Stop the Camel context when done
        context.stop();
    }
}

// Custom Processor class
class MyProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Access the message in the exchange and perform custom processing
        String input = exchange.getIn().getBody(String.class);
        String output = "Processed: " + input;

        // Set the processed message back into the exchange
        exchange.getIn().setBody(output);
        // Modify or set message headers
        exchange.getIn().setHeader("newHeader", "New Header Value");

        // Modify or set message properties
        exchange.setProperty("myProperty", "Property Value");
    }
}