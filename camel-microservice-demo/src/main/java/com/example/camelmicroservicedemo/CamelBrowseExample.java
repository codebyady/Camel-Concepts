package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelBrowseExample {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("timer:MyTimer?period=1000&repeat-Count=10")
                        .to("browse:log:myBrowseLog");
            }
        });

        context.start();
        // Keep the context running for a while
        Thread.sleep(10000);
        context.stop();
    }
}
