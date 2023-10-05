package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelDatabaseExample {

    public static void main(String[] args) throws Exception {

        // Create a Camel context
        CamelContext context = new DefaultCamelContext();


        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                // Connect to the database using JDBC component
                from("jdbc:mysql://localhost:3306/user_data?username=root&password=''")

                        .to("sql:SELECT * FROM user")

                        .log("Received message from database: ${body}");
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
