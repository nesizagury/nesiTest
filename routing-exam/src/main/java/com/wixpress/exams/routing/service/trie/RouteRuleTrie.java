package com.wixpress.exams.routing.service.trie;

import com.wixpress.exams.routing.api.RouteRule;

import java.util.HashMap;
import java.util.UUID;

public class RouteRuleTrie {

    private final RouteRuleNode root;

    public RouteRuleTrie() {
        root = new RouteRuleNode();
    }

    public void insert(String word, RouteRule routeRule) {
        // adding all characters in a word to the tree of prefix
        // adding the rule as the leaf of the last character
        HashMap<Character, RouteRuleNode> children = root.getChildren();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            RouteRuleNode node;
            if (children.containsKey(c)) {
                node = children.get(c);
            } else {
                node = new RouteRuleNode(c);
                children.put(c, node);
            }
            children = node.getChildren();

            // in case we got to the last part of the word
            // we will mark as leaf and save the rule
            if (i == word.length() - 1) {
                node.setLeaf(true);
                node.setRouteRule(routeRule);
            }
        }
    }

    public UUID search(String word) {
        // moving on the prefix tree and try to find a rule matching
        HashMap<Character, RouteRuleNode> children = root.getChildren();

        RouteRuleNode node;
        // a node that will match as prefix
        RouteRuleNode nodePrefix = null;
        // a node that will need to match the whole word
        RouteRuleNode nodeExact = null;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else {
                // once we did not find a match - we stop searching
                break;
            }

            // if this is not the last character - we will use a prefix rule
            if (i < (word.length() - 1)) {
                // we must check it ends with "/"
                char next_c = word.charAt(i + 1);
                if (node.isLeaf() && !node.getRouteRule().getExactMatch() && next_c == '/') {
                    nodePrefix = node;
                }
            // if this is the final character - we will use it as exact rule
            } else if (node.isLeaf()) {
                nodeExact = node;
            }
        }

        if (nodeExact != null) {
            // in case of nodeExact we need the whole word to be the same
            if (nodeExact.getRouteRule().getExactMatch() && nodeExact.getRouteRule().getUrl().equals(word)) {
                return nodeExact.getRouteRule().getTarget();
            } else if (!nodeExact.getRouteRule().getExactMatch()) {
                nodePrefix = nodeExact;
            }
        }
        // in case of prefix we return the last one found
        // so it will be the longest one
        if (nodePrefix != null) {
            return nodePrefix.getRouteRule().getTarget();
        }

        return null;
    }

}