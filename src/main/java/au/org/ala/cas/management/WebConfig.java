package au.org.ala.cas.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apereo.cas.services.AbstractRegisteredService;
import org.apereo.cas.services.OidcRegisteredService;
import org.apereo.cas.services.RegexRegisteredService;
import org.apereo.cas.support.oauth.services.OAuthRegisteredService;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * WebMVC config - adds mixins to the Jackson to HttpMessage Converter that override the default
 * CAS behaviour of only serialising non-default values for Services to work around a UI bug that
 * deserialises a missing attribute release policy as DenyAll instead of ReturnAllowed
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper jacksonObjectMapper) {
        jacksonObjectMapper.addMixIn(AbstractRegisteredService.class, AbstractRegisteredServiceMixin.class);
        jacksonObjectMapper.addMixIn(RegexRegisteredService.class, RegexRegisteredServiceMixin.class);
        jacksonObjectMapper.addMixIn(OAuthRegisteredService.class, OAuthRegisteredServiceMixin.class);
        jacksonObjectMapper.addMixIn(OidcRegisteredService.class, OidcRegisteredServiceMixin.class);

        return new MappingJackson2HttpMessageConverter(jacksonObjectMapper);
    }
    @Bean
    Jackson2ObjectMapperBuilderCustomizer nonNullSerialisersMixins() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.mixIn(AbstractRegisteredService.class, AbstractRegisteredServiceMixin.class);
            jacksonObjectMapperBuilder.mixIn(RegexRegisteredService.class, RegexRegisteredServiceMixin.class);
            jacksonObjectMapperBuilder.mixIn(OAuthRegisteredService.class, OAuthRegisteredServiceMixin.class);
            jacksonObjectMapperBuilder.mixIn(OidcRegisteredService.class, OidcRegisteredServiceMixin.class);
        };
    }
}
