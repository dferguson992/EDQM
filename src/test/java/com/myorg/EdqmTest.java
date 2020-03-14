package com.myorg;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import software.amazon.awscdk.core.App;

import static org.junit.Assert.assertEquals;

public class EdqmTest {
    private final static ObjectMapper JSON =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() {
        App app = new App();
        EdqmStack stack = new EdqmStack(app, "test");

        // synthesize the stack to a CloudFormation template and compare against
        // a checked-in JSON file.
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        assertEquals(new ObjectMapper().createObjectNode(), actual);
    }
}
