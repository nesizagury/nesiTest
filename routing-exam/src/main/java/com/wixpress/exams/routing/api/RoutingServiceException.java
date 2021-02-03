package com.wixpress.exams.routing.api;

import java.util.UUID;

public class RoutingServiceException extends Exception {

    public RoutingServiceException(UUID routingRuleId) {
        super("no route rule for: " + routingRuleId);
    }
}
