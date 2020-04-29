package com.dferguson.edqm.services;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.amazon.awscdk.services.lambda.IFunction;

public class EventAPIService extends Construct {
    public EventAPIService(@NotNull Construct scope, @NotNull String id, IFunction backendHandler) {
        super(scope, id);

        LambdaRestApiProps props = LambdaRestApiProps.builder()
                .proxy(false)
                .handler(backendHandler)
                .restApiName("EDQM")
                .build();
        LambdaRestApi api = new LambdaRestApi(scope, id + "-api", props);
        api.getRoot().addMethod("POST", new LambdaIntegration(backendHandler));
    }
}
