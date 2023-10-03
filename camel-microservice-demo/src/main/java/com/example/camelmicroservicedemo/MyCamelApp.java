package com.example.camelmicroservicedemo;

import com.example.camelmicroservicedemo.routes.FileRoute;
import com.example.camelmicroservicedemo.routes.HelloCamelRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Component;

@Component
public class MyCamelApp {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new HelloCamelRoute());

        context.addRoutes(new FileRoute());

//        context.addRoutes(new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("timer:myTimer?period=1000")
//                        .setBody().simple("Hello, Camel!")
//                        .to("log: myLog");
//            }
//        });
        context.start();
        Thread.sleep(5000); // Keep the context running for a while
        context.stop();
//    }
    }
}


