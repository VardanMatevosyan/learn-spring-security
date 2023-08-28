package com.baeldung.lss.filter;

import java.io.IOException;
import java.util.Optional;
import org.apache.log4j.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class LoggingFilter extends GenericFilterBean {

  private final Logger log = Logger.getLogger(LoggingFilter.class);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    log.info("Running through the new custom filter");
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String url = httpServletRequest.getRequestURL().toString();
    String queryString =
        Optional.ofNullable(httpServletRequest.getQueryString()).map(value -> "?" + value).orElse("");
    log.info(String.format("applying LssLoggingFilter for URI: %s%s", url, queryString));
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
