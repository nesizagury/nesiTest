package com.wixpress.exams.routing.api;

import com.wixpress.exams.routing.service.trie.RouteRuleTrie;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface RoutingService {

  RouteRule get(UUID routingRuleId);

  UUID create(String url, UUID target, Boolean exactMatch);

  RouteRuleTrie getRouteRuleTrie();

  String sortUrl(String url) throws UnsupportedEncodingException;
}


