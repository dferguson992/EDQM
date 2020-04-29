package com.dferguson.edqm;

import com.dferguson.edqm.services.EventAPIService;
import com.dferguson.edqm.services.EventMessengerService;
import com.dferguson.edqm.services.EventBridgeService;
import com.dferguson.edqm.services.EventStorageService;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;


public class EdqmStack extends Stack {

    EdqmStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        new EventMessengerService(this, "messengers");
        new EventAPIService(this, "api", EventMessengerService.getProducerFunction());
        new EventBridgeService(this, "eventbridge", EventMessengerService.getConsumerFunction(),
                props.getEnv(), EventMessengerService.getProducerFunction().getRole());
        new EventStorageService(this, "storage",
                EventMessengerService.getConsumerFunction().getRole());
    }
}
