package com.twigcodes.uaa.config;

import com.fasterxml.classmate.TypeResolver;
import lombok.RequiredArgsConstructor;

import com.google.common.base.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * 配置 Swagger 以提供 API 文档
 *
 * @author Peng Wang (wpcfan@gmail.com)
 */
@RequiredArgsConstructor
@EnableSwagger2
@ComponentScan(basePackages = Constants.BASE_PACKAGE_NAME + ".web.rest")
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
@Configuration
public class SwaggerConfig {

    private static final String securitySchemaOAuth2 = "oauth2";
    private static final String authorizationScopeGlobal = "read";
    private static final String authorizationScopeGlobalDesc = "accessEverything";

    private final AppProperties appProperties;
    /**
     * 配置 Swagger 扫描哪些 API （不列出那些监控 API）
     *
     * @return Docket
     */
    @Bean
    public Docket apiDoc() {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(postPaths())
            .apis(RequestHandlerSelectors.basePackage(Constants.BASE_PACKAGE_NAME + ".web.rest"))
            .paths(springBootActuatorJmxPaths())
            .build()
            .pathMapping("/")
            .securitySchemes(Collections.singletonList(oauth()))
            .securityContexts(Collections.singletonList(securityContext()))
            .directModelSubstitute(LocalDate.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .apiInfo(apiInfo());
    }

    @Bean
    public AlternateTypeRuleConvention myPageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Collections.singletonList(
                    newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
                );
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
            .fullyQualifiedClassName(
                String.format("%s.generated.%s", Pageable.class.getPackage().getName(),
                    Pageable.class.getSimpleName()))
            .withProperties(
                Arrays.asList(property(Integer.class, "page"), property(Integer.class, "size"),
                    property(String.class, "sort")))
            .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder().withName(name).withType(type).withCanRead(true).withCanWrite(true);
    }

    @Bean
    public SecurityScheme oauth() {
        return new OAuthBuilder()
                .name(securitySchemaOAuth2)
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }

    @Bean
	public List<GrantType> grantTypes() {
		List<GrantType> grantTypes = new ArrayList<>();
		LoginEndpoint loginEndpoint = new LoginEndpoint(appProperties.getSecurity().getOAuth2().getAuthorizeUrl());
        grantTypes.add(new ImplicitGrant(loginEndpoint, "token"));
        return grantTypes;
	}

    private List<AuthorizationScope> scopes() {
		List<AuthorizationScope> list = new ArrayList<AuthorizationScope>();
		list.add(new AuthorizationScope("read","Grants read access"));
		list.add(new AuthorizationScope("write","Grants write access"));
		return list;
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return SecurityConfigurationBuilder.builder()
            .clientId(appProperties.getSecurity().getOAuth2().getClientId())
            .clientSecret(appProperties.getSecurity().getOAuth2().getClientSecret())
            .useBasicAuthenticationWithAccessCodeGrant(true)
            .build();
    }

    private Predicate<String> postPaths() {
		return regex("/.*");
	}

    private Predicate<String> springBootActuatorJmxPaths() {
		return regex("^/(?!env|restart|pause|resume|refresh).*$");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    private List<SecurityReference> defaultAuth() {

        final AuthorizationScope authorizationScope =
            new AuthorizationScope(authorizationScopeGlobal, authorizationScopeGlobalDesc);
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections
            .singletonList(new SecurityReference(securitySchemaOAuth2, authorizationScopes));
    }
    /**
     * 对 API 的概要信息进行定制
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("推码科技通用授权鉴权服务 UAA 交互式文档", "基于 Rest 标准定义的 API 接口，供 Android， iOS 和 Web 客户端调用", "1.0",
            "http://www.twigcodes.com/apicube/tos.html",
            new Contact("推码科技", "http://www.twigcodes.com", "wangpeng@twigcodes.com"), "API 授权协议",
            "http://www.twigcodes.com/smartoffice/api-license.html", Collections.emptyList());
    }
}
