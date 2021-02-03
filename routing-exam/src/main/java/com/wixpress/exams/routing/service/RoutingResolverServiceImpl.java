package com.wixpress.exams.routing.service;

import com.wixpress.exams.routing.api.RoutingResolverException;
import com.wixpress.exams.routing.api.RoutingResolverService;
import com.wixpress.exams.routing.api.RoutingService;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


public class RoutingResolverServiceImpl implements RoutingResolverService {

    private static final RoutingService routingService = new RoutingServiceImpl();

    @Override
    public UUID resolve(String url) throws RoutingResolverException, UnsupportedEncodingException {
        // sort param side of url
        url = routingService.sortUrl(url);

        // search url in prefix tree
        UUID target = routingService.getRouteRuleTrie().search(url);

        if (target == null) {
            // target was found
            throw new RoutingResolverException(url);
        }

        // target was found
        return target;
    }

}

