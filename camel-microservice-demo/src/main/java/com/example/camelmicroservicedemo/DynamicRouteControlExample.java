package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class DynamicRouteControlExample {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                // Route 1: Logs messages
                from("direct:startRoute")
                .routeId("myRoute")
                        .to("log:myLog");

                // Route 2: Producer route to start myRoute
                from("direct:startMyRoute")
                .routeId("startMyRoute")
                        .startupOrder(2)
                        // Ensure this route starts after myRoute
                        .to("controlbus:route?routeId=myRoute&action=start");


                // Route 3: Producer route to stop myRoute
                from("direct:stopMyRoute")
                        .routeId("stopMyRoute")
                        .startupOrder(1)
                        // Ensure this route starts before myRoute
                        .to("controlbus:route?routeId=myRoute&action=stop");
            }
        });

        context.start();
        // Start the "myRoute" dynamically
        context.createProducerTemplate().sendBody("direct:startMyRoute", "Start myRoute");
        // Wait for a while to allow "myRoute" to log messages
        Thread.sleep(5000);
        // Stop the "myRoute" dynamically
        context.createProducerTemplate().sendBody("direct:stopMyRoute", "Stop myRoute");
        context.stop();
    }
}
