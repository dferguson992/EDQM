package com.dferguson.edqm.services;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.iam.IRole;

public class EventStorageService extends Construct {

    public EventStorageService(@NotNull Construct scope, @NotNull String id, IRole role) {
        super(scope, id);

        ITable table = new Table(scope, id + "-table",
                TableProps.builder()
                        .billingMode(BillingMode.PAY_PER_REQUEST)
                        .tableName("edqm-table")
                        .sortKey(Attribute.builder().name("timestamp").type(AttributeType.STRING).build())
                        .partitionKey(Attribute.builder().name("sourceSor").type(AttributeType.STRING).build())
                        .serverSideEncryption(true)
                        .build());

        table.grantReadWriteData(role);
    }
}
