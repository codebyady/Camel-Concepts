package com.example.camelmicroservicedemo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file:src/main/java/com/example/camelmicroservicedemo/input?noop=true")  // Specify the input directory
//                .log("${body}")
                .log("Received file: ${header.CamelFileName}")
                .to("file:src/main/java/com/example/camelmicroservicedemo/output");       // Specify the output directory
    }
}

