package com.baeldung.lss.spring;

import java.util.Objects;
import javax.sql.DataSource;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LssGlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Value("${spring.datasource.url}")
  private String dataSourceURL;

  @Value("${spring.datasource.username}")
  private String dataSourceUserName;

  @Value("${spring.datasource.password}")
  private String dataSourcePassword;

  @Override
  public MethodSecurityExpressionHandler createExpressionHandler() {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(aclPermissionEvaluator());
    return expressionHandler;
  }

  @Bean
  public AclPermissionEvaluator aclPermissionEvaluator() {
    return  new AclPermissionEvaluator(aclService());
  }

  @Bean
  public JdbcMutableAclService aclService() {
    var aclService = new JdbcMutableAclService(dataSource(), lookupStrategy(), aclCache());
    aclService.setClassIdentityQuery("SELECT @@IDENTITY");
    aclService.setSidIdentityQuery("SELECT @@IDENTITY");
    return aclService;
  }

  @Bean
  public LookupStrategy lookupStrategy() {
    return new BasicLookupStrategy(dataSource(),
                                  aclCache(),
                                  aclAuthorizationStrategy(),
                                  permissionGrantingStrategy());
  }

  @Bean
  public AclCache aclCache() {
    EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
    cacheManager.setAcceptExisting(true);
    cacheManager.setCacheManagerName(CacheManager.getInstance().getName());
    cacheManager.afterPropertiesSet();

    EhCacheFactoryBean cacheFactoryBean = new EhCacheFactoryBean();
    cacheFactoryBean.setName("aclCache");
    cacheFactoryBean.setCacheManager(Objects.requireNonNull(cacheManager.getObject()));
    cacheFactoryBean.setMaxBytesLocalHeap("16M");
    cacheFactoryBean.setMaxEntriesLocalHeap(0L);
    cacheFactoryBean.afterPropertiesSet();

    return new EhCacheBasedAclCache(cacheFactoryBean.getObject(),
                                    permissionGrantingStrategy(),
                                    aclAuthorizationStrategy());
  }

  @Bean
  public AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ADMIN"));
  }

  @Bean
  public PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setPassword(dataSourcePassword);
    dataSource.setUsername(dataSourceUserName);
    dataSource.setUrl(dataSourceURL);
    return dataSource;
  }




}
