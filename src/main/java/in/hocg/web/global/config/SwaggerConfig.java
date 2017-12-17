package in.hocg.web.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(_apiInfo())
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("in.hocg.web.modules.api"))
                .paths(PathSelectors.any())
                .build();
    }
    
    private ApiInfo _apiInfo() {
        return new ApiInfoBuilder()
                .title("服务文档")
                .description("服务文档")
                .termsOfServiceUrl("http://hocg.in/")
                .contact(new Contact("hocgin", "http://hocg.in", "hocgin@gmail.com"))
                .version("1.0")
                .build();
    }
}
