package com.decouvrezvotreville.apispring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfiguration {

    //    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(
//                        new ApiInfoBuilder().description("")
//                                .title("DecouvrezVotreVille Api Documentation")
//                                .version("1.0")
//                                .description("Api Documentation")
//                                .build()
//                )
//                .groupName("Rest API V1")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.decouvrezvotreville.apisprin"))
//                .paths(PathSelectors.ant(APP_ROOT+"/**"))
//                .build();
//    }..
    @Bean
    public OpenAPI publicApi() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )).info(
                        new Info().description("")
                                .title("DecouvrezVotreVille Api Documentation")
                                .version("1.0")
                                .description("Api Documentation")


                );

    }

}
