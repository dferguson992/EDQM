package com.dferguson.edqm.services;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.services.events.*;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.lambda.IFunction;

import java.util.*;

public class EventBridgeService extends Construct {

    public EventBridgeService(@NotNull Construct scope, @NotNull String id, IFunction targetFunction, Environment env,
                              IRole role) {
        super(scope, id);
        EventBus bus = EventBus.Builder.create(scope, id + "-bus")
                .eventBusName("edqm-bus")
                .build();

        EventBus.grantPutEvents(role);

        String[] accountArray = new String[1];
        String[] regionArray = new String[1];
        accountArray[0] = env.getAccount();
        regionArray[0] = env.getRegion();

        List<String> accountsList = new ArrayList<>(Arrays.asList(accountArray));
        List<String> regionList = new ArrayList<>(Arrays.asList(regionArray));

        List<String> source = new ArrayList<>();
        source.add("edqm.airflow.hook");
        source.add("edqm.snowflake.hook");
        source.add("edqm.db.logs");

        List<String> type = new ArrayList<>();
        type.add("edqm.audit");
        type.add("edqm.balance");
        type.add("edqm.control");

        EventPattern detailSourcePattern = EventPattern.builder()
                .account(accountsList)
                .region(regionList)
                .source(source)
                .build();

        EventPattern detailTypePattern = EventPattern.builder()
                .account(accountsList)
                .region(regionList)
                .detailType(type)
                .build();

        List<IRuleTarget> ruleTargets = new ArrayList<>();
        ruleTargets.add(new LambdaFunction(targetFunction));

        Rule detailSourceRule = new Rule(scope, id + "-sourceRule", new RuleProps.Builder()
                .eventBus(bus)
                .eventPattern(detailSourcePattern)
                .targets(ruleTargets)
                .ruleName("edqm-detail-source-rule")
                .enabled(true)
                .build()
        );

        Rule detailTypeRule = new Rule(scope, id + "-typeRule", new RuleProps.Builder()
                .eventBus(bus)
                .eventPattern(detailTypePattern)
                .targets(ruleTargets)
                .ruleName("edqm-detail-type-rule")
                .enabled(true)
                .build()
        );

        IRuleTarget target = new LambdaFunction(targetFunction);
        target.bind(detailSourceRule);
        target.bind(detailTypeRule);
    }
}
