package org.test.records.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {

	/**
	 * 添加ShiroDialect 为了在thymeleaf里使用shiro的标签的bean
	 * @return
	 */
	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect(){
		return new ShiroDialect();
	}

	@Bean(name="shirFilter")
	public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") SecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		//自定义拦截器
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		//配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/users/login", "anon");
		//静态资源
		filterChainDefinitionMap.put("/static/**", "anon");
		//swagger放行
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
		filterChainDefinitionMap.put("/swagger-resources/configuration/security", "anon");
		filterChainDefinitionMap.put("/swagger-resources/configuration/ui", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
		//配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");

		//登录请求
		shiroFilterFactoryBean.setLoginUrl("/");
		//未授权界面
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");

		Map<String,Filter> filters = new HashMap<String,Filter>();
		Filter loginFilter = new LoginFormFilter();
		filters.put("authc", loginFilter); //此处使用自定义的拦截器,autho默认使用FormAuthenticationFilter拦截器
		shiroFilterFactoryBean.setFilters(filters);

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间3600s
		sessionManager.setGlobalSessionTimeout(3600000L);
		return sessionManager;
	}

	//配置核心安全事务管理器
	@Bean(name="securityManager")
	public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm){
		DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
		securityManager.setRealm(authRealm);
		// 自定义缓存实现 使用redis
		//securityManager.setCacheManager(cacheManager());
		// 自定义session管理
		//securityManager.setSessionManager(sessionManager());
		//注入记住我管理器;
		//securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	//配置自定义的权限登录器
	@Bean(name="authRealm")
	public AuthRealm authRealm(){
		return new AuthRealm();
	}

	/**
	 *  开启shiro aop注解支持.
	 * @param manager
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(manager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean(name="simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
		mappings.setProperty("UnauthorizedException","403");
		r.setExceptionMappings(mappings);  // None by default
		r.setDefaultErrorView("error");    // No default
		r.setExceptionAttribute("ex");     // Default is "exception"
		//r.setWarnLogCategory("example.MvcLogger");     // No default
		return r;
	}
}