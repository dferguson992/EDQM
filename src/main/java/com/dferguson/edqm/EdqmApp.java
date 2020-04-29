package com.dferguson.edqm;

import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.core.App;

import java.util.HashMap;
import java.util.Map;

public class EdqmApp {

    private static Map<String, String> tagMap = new HashMap<String, String>() {{
        put("Creator", "Dan Ferguson");
        put("Project", "Event-Driven Data Quality Management");
        put("Environment", "QualityAudit");
    }};

    private static Environment getEnv() {
        return Environment.builder()
                .account("153746176101")
                .region("us-east-1")
                .build();
    }

    public static void main(final String[] args) {
        App app = new App();

        String description = "CDK Application that uses events to populate secondary databases with data quality " +
                "information.";
        String stackName = "edqm";
        new EdqmStack(app, stackName, StackProps.builder()
                .tags(tagMap)
                .env(getEnv())
                .description(description)
                .stackName(stackName)
                .build());
        app.synth();
    }
}
