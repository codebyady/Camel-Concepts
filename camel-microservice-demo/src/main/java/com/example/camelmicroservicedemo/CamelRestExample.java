package com.example.camelmicroservicedemo;

        import org.apache.camel.CamelContext;
        import org.apache.camel.builder.RouteBuilder;
        import org.apache.camel.component.http.HttpComponent;
        import org.apache.camel.impl.DefaultCamelContext;

public class CamelRestExample {
    public static void main(String[] args) throws Exception {
        // Create a Camel context
        CamelContext context = new DefaultCamelContext();

        // Configure HTTP component
        HttpComponent httpComponent = context.getComponent("http", HttpComponent.class);

        // Define a REST endpoint at /hello
//                from("rest:get:/hello").setBody(constant("Hello, World!"))// Listen for GET requests at /hello
//                        .to("log:jsonLog"); // Log the request
//
//                from("rest:post:/hellopost") // Listen for POST requests at /post
//                        .log("Received POST request with body: ${body}") // Log the POST request body
//                        //.to("log:jsonLog");
//                        .to("file:src/main/java/com/example/camelmicroservicedemo/output?fileName=postRequest.txt");

        // Define a route using RouteBuilder
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:myTimer?period=5000")
                        .to("direct:startRoute");

                from("direct:startRoute") // Start of your route
                        .to("http://localhost:8085/demo/getExample") // HTTP GET call to the REST endpoint
                        .log("Response from REST service: ${body}") // Log the response
                        .to("direct:endRoute");
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

