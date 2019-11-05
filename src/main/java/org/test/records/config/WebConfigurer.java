package org.test.records.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	/**
	 * 添加静态资源--过滤swagger-api (开源的在线API文档)
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//指定了静态资源文件的位置
		registry.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/");

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	// 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

	}

	//解决跨域问题
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
				.allowCredentials(true).maxAge(3600);
	}
}