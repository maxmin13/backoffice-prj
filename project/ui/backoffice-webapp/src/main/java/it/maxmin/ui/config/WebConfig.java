package it.maxmin.ui.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "it.maxmin.ui.controller" })
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {
	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
	StandardServletMultipartResolver multipartResolver() {
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		return multipartResolver;
	}

	@Bean
	public InternalResourceViewResolver resolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	// Declare our static resources. I added cache to the java config but it?s not
	// required.
	/*
	 * @Override public void addResourceHandlers(final ResourceHandlerRegistry
	 * registry) { WebMvcConfigurer.super.addResourceHandlers(registry);
	 * registry.addResourceHandler("/images/**", "/styles/**")
	 * .addResourceLocations("/images/", "/styles/"); }
	 */

	/*
	 * @Override public void configureDefaultServletHandling(final
	 * DefaultServletHandlerConfigurer configurer) { configurer.enable(); }
	 */

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/home");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/*");
		registry.addInterceptor(webChangeInterceptor());
	}

	@Bean
	MessageSource messageSource() {
		var messageResource = new ReloadableResourceBundleMessageSource();
		messageResource.setBasename("classpath:i18n/global");
		messageResource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageResource.setUseCodeAsDefaultMessage(true);
		messageResource.setFallbackToSystemLocale(true);
		// # -1 : never reload, 0 always reload
		// messageResource.setCacheSeconds(0);
		return messageResource;
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		var localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	CookieLocaleResolver localeResolver() {
		var cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
		cookieLocaleResolver.setCookieMaxAge(Duration.ofSeconds(3600));
		cookieLocaleResolver.setCookieName("locale");
		return cookieLocaleResolver;
	}

	@Bean
	WebContentInterceptor webChangeInterceptor() {
		var webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(0);
		webContentInterceptor.setSupportedMethods("GET", "POST", "PUT", "DELETE");
		return webContentInterceptor;
	}

}
