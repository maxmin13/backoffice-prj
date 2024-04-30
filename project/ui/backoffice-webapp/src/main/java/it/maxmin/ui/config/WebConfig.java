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
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "it.maxmin" })
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Bean
	public ViewResolver templateResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/*");
	//	registry.addInterceptor(themeChangeInterceptor());
	//	registry.addInterceptor(webChangeInterceptor());
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
		var cookieLocaleResolver = new CookieLocaleResolver("locale");
		cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
		cookieLocaleResolver.setCookieMaxAge(Duration.ofSeconds(3600));
		return cookieLocaleResolver;
	}

	/*
	 * @Override public void configureDefaultServletHandling(final
	 * DefaultServletHandlerConfigurer configurer) { configurer.enable(); }
	 */
 
 	/*
	@Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
	StandardServletMultipartResolver multipartResolver() {
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		return multipartResolver;
	}

	
	@Bean
	ResourceBundleThemeSource themeSource() {
		return new ResourceBundleThemeSource();
	}

	@Bean
	public Validator validator() {
		final var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Override
	public Validator getValidator() {
		return validator();
	}*/

	// Declare our static resources. I added cache to the java config but it?s not
	// required.
	/*
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/images/**", "/styles/**").addResourceLocations("/images/", "/styles/");
	}
 
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/home");
	}

	

	@Bean
	ThemeChangeInterceptor themeChangeInterceptor() {
		var themeChangeInterceptor = new ThemeChangeInterceptor();
		themeChangeInterceptor.setParamName("theme");
		return themeChangeInterceptor;
	}

	

	@Bean
	CookieThemeResolver themeResolver() {
		var cookieThemeResolver = new CookieThemeResolver();
		cookieThemeResolver.setDefaultThemeName("green");
		cookieThemeResolver.setCookieMaxAge(3600);
		cookieThemeResolver.setCookieName("theme");
		return cookieThemeResolver;
	}

	@Bean
	WebContentInterceptor webChangeInterceptor() {
		var webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(0);
		webContentInterceptor.setSupportedMethods("GET", "POST", "PUT", "DELETE");
		return webContentInterceptor;
	}*/

}
