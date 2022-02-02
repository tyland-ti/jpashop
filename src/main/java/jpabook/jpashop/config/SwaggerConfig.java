package jpabook.jpashop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

        @Bean
        public Docket api() {
                return new Docket(DocumentationType.SWAGGER_2)
                        .select()
                        .paths(PathSelectors.any())
                        .build()
                        .apiInfo(apiInfo())
                        .useDefaultResponseMessages(false); // Response 응답 메시지 디폴트값 적용 X
        }

        private ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                        .title("API")
                        .description("스프링부트 JPA 샘플")
                        .version("v1")
                        .build();
        }
}