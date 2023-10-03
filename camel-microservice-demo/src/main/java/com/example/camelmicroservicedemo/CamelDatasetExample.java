package com.example.camelmicroservicedemo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.dataset.DataSet;
import org.apache.camel.component.dataset.DataSetSupport;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.dataset.SimpleDataSet;

public class CamelDatasetExample {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        // Define a custom DataSet (SimpleDataSet is used here)
        DataSet dataSet = new SimpleDataSet(5);
        context.getRegistry().bind("myDataset", dataSet);

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("dataset:myDataset")
                        .log("Received message: ${body}");
            }
        });

        context.start();
        // Use a ProducerTemplate to send messages from the DataSet to the route
        context.createProducerTemplate().sendBody("dataset:myDataset", dataSet);
        Thread.sleep(1000);
        context.stop();
    }
}
