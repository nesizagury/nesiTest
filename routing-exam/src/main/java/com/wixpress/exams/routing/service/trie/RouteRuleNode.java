package com.wixpress.exams.routing.service.trie;

import com.wixpress.exams.routing.api.RouteRule;

import java.util.HashMap;

public class RouteRuleNode {
    // c is part of the word we need it just for testing and debugging
    private char c;
    // each Character hold a RouteRuleNode
    private final HashMap<Character, RouteRuleNode> children = new HashMap<>();
    // isLeaf=True says we are holding here a RouteRule
    private boolean isLeaf;
    // RouteRule is a rule matching the word so far
    private RouteRule routeRule;

    public RouteRuleNode() {}

    public RouteRuleNode(char c){
        this.c = c;
    }

    public HashMap<Character, RouteRuleNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public RouteRule getRouteRule() {
        return routeRule;
    }

    public void setRouteRule(RouteRule routeRule) {
        this.routeRule = routeRule;
    }
}