package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelDataFormatExample {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Route to marshal Java objects to JSON
                from("direct:marshal")
                        .marshal().json()
                        .to("log:jsonLog");

                // Route to unmarshal JSON to Java objects
                from("direct:unmarshal")
                        .unmarshal().json(MyPojo.class) // Specify the target class
                        .to("log:javaObjectLog");
            }
        });

        context.start();

        // Marshal Java object to JSON
        MyPojo myPojo = new MyPojo("John", 30);
        context.createProducerTemplate().sendBody("direct:marshal", myPojo);

        // Unmarshal JSON to Java object
        String jsonString = "{\"name\":\"Alice\",\"age\":25}";
        MyPojo unmarshaledPojo = context.createProducerTemplate().requestBody("direct:unmarshal", jsonString, MyPojo.class);
        System.out.println("Unmarshaled Java Object: " + unmarshaledPojo);

        context.stop();
    }
}

class MyPojo {
    public String name;
    public int age;

    public MyPojo() {
    }

    public MyPojo(String name, int age) {
        this.name = name;
        this.age = age;
    }


}