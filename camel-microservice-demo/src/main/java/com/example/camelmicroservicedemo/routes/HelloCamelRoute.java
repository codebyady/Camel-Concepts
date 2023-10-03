package com.example.camelmicroservicedemo.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class HelloCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Define a timer-based route to log a message every second
        from("timer:myTimer?period=5000")
                .setBody().simple("Hello, Camel!")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // Transform the message here
                        String originalBody = exchange.getIn().getBody(String.class);
                        String transformedBody = "Transformed: " + originalBody;
                        exchange.getIn().setBody(transformedBody);
                    }
                })
                .to("log: myLog");
    }
}
