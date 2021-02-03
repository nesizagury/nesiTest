package com.wixpress.exams.routing;

import com.wixpress.exams.routing.api.RouteRule;
import com.wixpress.exams.routing.api.RoutingResolverException;
import com.wixpress.exams.routing.api.RoutingResolverService;
import com.wixpress.exams.routing.api.RoutingService;
import com.wixpress.exams.routing.service.RoutingResolverServiceImpl;
import com.wixpress.exams.routing.service.RoutingServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import java.util.Random;
import java.util.UUID;


public class RoutingTest {

  int domainSize = 10;

  RoutingService routingService = new RoutingServiceImpl();
  RoutingResolverService routingResolverService = new RoutingResolverServiceImpl();

  @Test
  public void createRouteRule() {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com";

    UUID routeRuleId = routingService.create(url, target, true);
    assertEquals(new RouteRule(url, target, true, routeRuleId), routingService.get(routeRuleId));
  }

  @Test(expected = RoutingResolverException.class)
  public void resolveNonExistingSingleRuleExactMatch() throws Exception {
    UUID target = UUID.randomUUID();
    String existDomainName = RandomStringUtils.random(domainSize);
    String nonExistDomainName = RandomStringUtils.random(domainSize);

    String existUrl = "https://www." + existDomainName + ".com";
    String nonExistUrl = "https://www." + nonExistDomainName + ".com";

    routingService.create(existUrl, target, true);
    routingResolverService.resolve(nonExistUrl);
  }

  @Test(expected = RoutingResolverException.class)
  public void resolveNonExistingSingleRulePrefixMatch() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);

    String existUrl = "https://www." + domainName + ".com/abc";
    String nonExistUrl = "https://www." + domainName + ".com/abcd";

    routingService.create(existUrl, target, false);

    // resolving https://www.$domainName.com/abc/d instead, would expect a match.
    routingResolverService.resolve(nonExistUrl);
  }

  @Test
  public void resolveSingleRule() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com";

    routingService.create(url, target, true);
    assertEquals(target, routingResolverService.resolve(url));
  }

  @Test
  public void resolveMatchPathExactMatch() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com";
    routingService.create(url, target, true);

    UUID target2 = UUID.randomUUID();
    String url2 = "https://www." + domainName + ".com/a";
    routingService.create(url2, target2, true);

    UUID target3 = UUID.randomUUID();
    String url3 = "https://www." + domainName + ".com/a/b";
    routingService.create(url3, target3, true);

    assertEquals(target2, routingResolverService.resolve(url2));
  }

  @Test
  public void resolveMatchPathPrefixMatch() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName +  ".com";
    routingService.create(url, target, true);

    UUID target2 = UUID.randomUUID();
    String url2 = "https://www." + domainName + ".com/a";
    routingService.create(url2, target2, false);

    UUID target3 = UUID.randomUUID();
    String url3 = "https://www." + domainName + ".com/a/b";
    routingService.create(url3, target3, false);

    UUID target4 = UUID.randomUUID();
    String url4 = "https://www." + domainName + ".com/a/b/c";
    routingService.create(url4, target4, false);

    assertEquals(target3, routingResolverService.resolve("https://www." + domainName + ".com/a/b/d"));
  }

  @Test
  public void resolveMatchPathPreferExactOverPrefix() throws Exception {
    UUID target1 = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url1 = "https://www." + domainName + ".com/a/b";
    routingService.create(url1, target1, true);

    UUID target2 = UUID.randomUUID();
    String url2 = "https://www." + domainName + ".com/a";
    routingService.create(url2, target2, false);
    assertEquals(target1, routingResolverService.resolve("https://www." + domainName + ".com/a/b"));
  }

  @Test
  public void resolveMatchPathQueryParamsIncluded() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com?key=value";
    routingService.create(url, target, true);
    assertEquals(target, routingResolverService.resolve(url));
  }

  @Test(expected = RoutingResolverException.class)
  public void resolveNoMatchPathQueryParamsIncluded() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com?key1=value1&key2=value2";
    routingService.create(url, target, true);
    routingResolverService.resolve("https://www." + domainName + ".com?key1=value1");
  }

  @Test
  public void resolveMatchPathQueryParamsIncludedOutOfMonitoring() throws Exception {
    UUID target = UUID.randomUUID();
    String domainName = RandomStringUtils.random(domainSize);
    String url = "https://www." + domainName + ".com?key1=value1&key2=value2";
    routingService.create(url, target, true);
    assertEquals(target, routingResolverService.resolve("https://www." + domainName + ".com?key2=value2&key1=value1"));
  }

  @Test
  public void resolveMatchPath50kRulesDifferentDomains() throws Exception {
    String[] urls = new String[50000];
    UUID[] targets = new UUID[50000];

    for (int i=0; i < 50000; i++){
      UUID target = UUID.randomUUID();
      String domainSuffix = RandomStringUtils.random(domainSize);
      String domain = "www.domain" + domainSuffix + ".co.il";
      String url = "https://" + domain;

      urls[i] = url;
      targets[i] = target;

      routingService.create(url, target, true);
    }

    int randomRule = new Random().nextInt(50000);
    assertEquals(targets[randomRule], routingResolverService.resolve(urls[randomRule]));
  }

  @Test
  public void resolveMatchPath50kRules() throws Exception {
    String[] urls = new String[50000];
    UUID[] targets = new UUID[50000];

    for (int i=0; i < 50000; i++){
      UUID target = UUID.randomUUID();
      String path = RandomStringUtils.random(domainSize);
      String url = "https://www.domain.com/" + path;

      urls[i] = url;
      targets[i] = target;

      routingService.create(url, target, true);
    }

    int randomRule = new Random().nextInt(50000);
    assertEquals(targets[randomRule], routingResolverService.resolve(urls[randomRule]));
  }

}

