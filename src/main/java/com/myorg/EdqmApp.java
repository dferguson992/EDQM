package com.myorg;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class EdqmApp {
    public static void main(final String[] args) {
        App app = new App();

        new EdqmStack(app, "EdqmStack");

        app.synth();
    }
}
