package com.wixpress.exams.routing.service;

import com.wixpress.exams.routing.api.RouteRule;
import com.wixpress.exams.routing.api.RoutingService;
import com.wixpress.exams.routing.service.trie.RouteRuleTrie;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class RoutingServiceImpl implements RoutingService {

    // rules map to get a rule by uuid unique key
    private static final HashMap<UUID, RouteRule> uuidToRule = new HashMap<>();
    // route trie to search a url char by char for prefix matching
    private static final RouteRuleTrie routeRuleTrie = new RouteRuleTrie();

    @Override
    public RouteRule get(UUID routingRuleId) {
        return uuidToRule.get(routingRuleId);
    }

    @Override
    public UUID create(String url, UUID target, Boolean exactMatch) {
        // uuid as a key for storing the rule
        UUID uuid = UUID.randomUUID();

        // sort url query params
        url = this.sortUrl(url);

        // creating the new rule
        RouteRule routeRule = new RouteRule(url, target, exactMatch, uuid);

        // saving rule by key
        uuidToRule.put(uuid, routeRule);
        // insert the rule to the "prefix" tree
        routeRuleTrie.insert(url, routeRule);

        // return uuid unique key
        return uuid;
    }

    @Override
    public RouteRuleTrie getRouteRuleTrie() {
        return routeRuleTrie;
    }

    @Override
    public String sortUrl(String url) {
        // sorts the query params so all url in tree will be ordered
        int charIndex = url.indexOf("?");

        // if charIndex is -1 : no params in url
        if (charIndex > -1) {
            // saving params in sorted tree map by param key
            Map<String, String> paramsMap = new TreeMap<>();

            // url without params
            String baseUrl = url.split("\\?")[0];

            // params side of url
            String searchURL = url.substring(url.indexOf("?") + 1);
            String params[] = searchURL.split("&");

            for (String param : params) {
                String temp[] = param.split("=");
                try {
                    paramsMap.put(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    return url;
                }
            }

            // after we have sorted map of params - build the url again
            StringBuilder queryUrl = new StringBuilder("");
            for (String param : paramsMap.keySet()) {
                queryUrl.append(param).append("=").append(paramsMap.get(param)).append("&");
            }

            // return sorted url
            return baseUrl + "?" + queryUrl.toString();
        }
        return url;
    }

}