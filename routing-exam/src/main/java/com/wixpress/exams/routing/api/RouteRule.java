package com.wixpress.exams.routing.api;

import java.util.UUID;

public class RouteRule {

    private String url;
    private UUID target;
    private Boolean exactMatch;
    private UUID routingRuleId;

    public RouteRule(String url, UUID target, Boolean exactMatch, UUID routingRuleId) {
        this.url = url;
        this.target = target;
        this.exactMatch = exactMatch;
        this.routingRuleId = routingRuleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public Boolean getExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(Boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    public UUID getRoutingRuleId() {
        return routingRuleId;
    }

    public void setRoutingRuleId(UUID routingRuleId) {
        this.routingRuleId = routingRuleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteRule routeRule = (RouteRule) o;

        if (url != null ? !url.equals(routeRule.url) : routeRule.url != null) return false;
        if (target != null ? !target.equals(routeRule.target) : routeRule.target != null) return false;
        if (exactMatch != null ? !exactMatch.equals(routeRule.exactMatch) : routeRule.exactMatch != null) return false;
        if (routingRuleId != null ? !routingRuleId.equals(routeRule.routingRuleId) : routeRule.routingRuleId != null) return false;

        return true;
    }

}
