package com.wixpress.exams.routing.api;

import java.util.UUID;

public interface RoutingResolverService {

  UUID resolve(String url) throws Exception;

}
