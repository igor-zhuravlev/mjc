package com.epam.esm.web.config;

import com.epam.esm.service.config.ServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm.web")
@Import(ServiceConfig.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String HEADER_LANG = "Accept-Language";

    @Bean
    public LocaleResolver localeResolver() {
        LocaleResolver localeResolver = new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader(HEADER_LANG);
                return headerLang == null || headerLang.isEmpty() || headerLang.isBlank()
                        ? Locale.getDefault()
                        : StringUtils.parseLocaleString(headerLang);
            }
        };
        return localeResolver;
    }
}
