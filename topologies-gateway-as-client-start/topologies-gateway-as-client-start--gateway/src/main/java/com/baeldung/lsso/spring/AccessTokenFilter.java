package com.baeldung.lsso.spring;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AccessTokenFilter implements GlobalFilter, Ordered {

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE;
  }

  // last filter that gets the access token from the request header
  // that was gets from the map by the user session and set to the request header
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
    System.out.println("Access Token is " + accessToken);
    return chain.filter(exchange);
  }
}

