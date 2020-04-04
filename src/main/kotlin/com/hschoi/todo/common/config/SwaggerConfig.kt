package com.hschoi.todo.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.paths.RelativePathProvider
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.security.Principal

/**
 * Created by hschoi.
 * User: nate
 * Date: 2020/04/05
 */
@EnableSwagger2
@Configuration
class SwaggerConfig(@Value("\${swagger.base-path}") val pathProvider: String?) {
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.hschoi.todo.app.controllers"))
            .paths(PathSelectors.any())
            .build().pathProvider(object : RelativePathProvider(null) {
                override fun getApplicationBasePath(): String {
                    return pathProvider ?: "/"
                }
            })
            .ignoredParameterTypes(Principal::class.java)
            .securityContexts(listOf(securityContext())).securitySchemes(listOf(apiKey()))
            .useDefaultResponseMessages(false) // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build()
    }

    private fun apiKey(): ApiKey = ApiKey("JWT", "Authorization", "header")

    private fun defaultAuth(): List<SecurityReference> {
        return listOf(SecurityReference(
            "JWT",
            arrayOf<AuthorizationScope>(AuthorizationScope("global", "accessEverything")))
        )
    }

    private fun swaggerInfo(): ApiInfo {
        return ApiInfoBuilder().title("TODO API Documentation")
            .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
            .version("0.1").build()
    }
}