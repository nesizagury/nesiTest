package com.wixpress.exams.routing.api;

public class RoutingResolverException extends Exception {

    public RoutingResolverException(String url) {
        super("no matching route found for url: " + url);
    }

}
