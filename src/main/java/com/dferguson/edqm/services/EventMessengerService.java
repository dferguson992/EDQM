package com.dferguson.edqm.services;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;

import java.util.HashMap;
import java.util.Map;


public class EventMessengerService extends Construct {

    private static IFunction consumerFunction = null;
    private static IFunction producerFunction = null;

    public EventMessengerService(@NotNull Construct scope, @NotNull String id) {
        super(scope, id);
        Map<String, String> consumerVars = new HashMap<>();
        consumerVars.put("TABLE_NAME", "edqm-table");

        Map<String, String> producerVars = new HashMap<>();
        producerVars.put("BUS_NAME", "edqm-bus");

        consumerFunction = new Function(scope, id + "-consumer", FunctionProps.builder()
                .handler("consumer.handler")
                .code(Code.fromAsset("src/resources/edqm.zip"))
                .functionName("EDQM-Event-Consumer")
                .runtime(Runtime.NODEJS_12_X)
                .description("Persists events from EDQM.")
                .environment(consumerVars)
                .timeout(Duration.seconds(10))
                .build()
        );

        producerFunction = new Function(scope, id + "-producer", FunctionProps.builder()
                .handler("producer.handler")
                .code(Code.fromAsset("src/resources/edqm.zip"))
                .functionName("EDQM-Event-Publisher")
                .runtime(Runtime.PYTHON_3_7)
                .description("Produces events for EDQM.")
                .environment(producerVars)
                .timeout(Duration.seconds(10))
                .build()
        );
    }

    public static IFunction getConsumerFunction() {
        return consumerFunction;
    }

    public static IFunction getProducerFunction() {
        return producerFunction;
    }
}
