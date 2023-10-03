package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelBeanExample {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .to("bean:myBean?method=processMessage");
            }
        });

        // Create an instance of the MyBean class
        MyBean myBean = new MyBean();

        // Set the Camel context to use this instance as a bean
        context.getRegistry().bind("myBean", myBean);

        context.start();
        // Send a message to the route
        context.createProducerTemplate().sendBody("direct:start", "Hello, Camel!");
        Thread.sleep(2000);
        context.stop();
    }
}

class MyBean {
    public void processMessage(String message) {
        // You can perform any processing on the message here
        System.out.println("Received message: " + message);
    }
}
