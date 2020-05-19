package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.domain.exception.ExceptionTitles;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nsoft.chiwava.core.commons.json.conversion.LocalDateTimeDeserializer;
import com.nsoft.chiwava.core.commons.json.conversion.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile("local")
public class SpringFoxConfig extends WebMvcConfigurationSupport {

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                JavaTimeModule javaTimeModule = new JavaTimeModule();
                javaTimeModule
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
                javaTimeModule
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

                ObjectMapper objectMapper = new ObjectMapper()
                        .registerModules(
                                new ProblemModule(),
                                new ConstraintViolationProblemModule(), javaTimeModule);
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        }
    }

    @Bean
    public Docket recordApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.dnevi.hcs.iam"))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo())
                .additionalModels(new TypeResolver().resolve(Problem.class))
                .globalResponseMessage(RequestMethod.GET, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.PATCH, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.DELETE, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.POST, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.PUT, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.OPTIONS, this.buildCommonErrorMessages())
                .globalResponseMessage(RequestMethod.HEAD, this.buildCommonErrorMessages())
                .useDefaultResponseMessages(false);
    }

    private List<ResponseMessage> buildCommonErrorMessages() {
        String message = "Field `title` can be one of the following values: <br/><br/>";
        String problemRef = "Problem";
        String brDelimiter = "<br/>";
        List<ResponseMessage> responses = new ArrayList<>();
        responses.add(new ResponseMessageBuilder().code(404)
                .message(message + String.join(brDelimiter, this.get404Errors()))
                .responseModel(new ModelRef(problemRef)).build());
        responses.add(new ResponseMessageBuilder().code(422)
                .message(message + String.join(brDelimiter, this.get400Errors()))
                .responseModel(new ModelRef(problemRef)).build());

        return responses;
    }

    private List<String> get404Errors() {
        return Arrays.asList(ExceptionTitles.INVITATION_NOT_FOUND.toString(),
                ExceptionTitles.USER_NOT_FOUND.toString(),
                ExceptionTitles.INVITATION_TOKEN_NOT_FOUND.toString());
    }

    private List<String> get400Errors() {
        return Arrays.asList(ExceptionTitles.EMAIL_COULD_NOT_BE_SENT.toString(),
                ExceptionTitles.INVALID_INVITATION_TOKEN.toString(),
                ExceptionTitles.RESOURCE_ALREADY_IN_USE.toString(),
                ExceptionTitles.USER_LOGIN_FAILED.toString(),
                ExceptionTitles.BAD_CREDENTIALS.toString(),
                ExceptionTitles.INVITATION_EXPIRED.toString());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("HCS Identity Management", "Identity Management service", "V1", "",
                new Contact("Dario Nevistic", "url@url.com",
                        "foo@bar.com"),
                "Private API ", "", Collections.emptyList());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
