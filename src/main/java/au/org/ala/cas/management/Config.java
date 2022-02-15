package au.org.ala.cas.management;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
import java.time.Duration;
import java.util.stream.Collectors;

@Configuration(
        value = "mongoSessionConfiguration",
        proxyBeanMethods = false
)
@ConditionalOnProperty(value = "spring.session.enabled", havingValue = "true", matchIfMissing = false)
@EnableMongoHttpSession
@EnableConfigurationProperties(MongoProperties.class)
public class Config {

    @Bean
    @Qualifier("mongoOperations")
    @Primary
    MongoOperations mongoOperations(MongoClient mongoClient, MongoProperties properties) {
        var factory = new SimpleMongoClientDatabaseFactory(mongoClient, properties.getMongoClientDatabase());
        return new MongoTemplate(factory);
    }
    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
        return new JdkMongoSessionConverter(Duration.ofMinutes(15L));
    }

    @Bean
    @ConditionalOnMissingBean(MongoClient.class)
    public MongoClient mongoClient(MongoProperties properties, Environment environment,
                             ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
                             ObjectProvider<MongoClientSettings> settings) {
        return new MongoClientFactory(properties, environment,
                builderCustomizers.orderedStream().collect(Collectors.toList()))
                .createMongoClient(settings.getIfAvailable());
    }

    // Spring Boot 2.5
//    @Bean
//    @ConditionalOnMissingBean(MongoClient.class)
//    MongoClient mongo(
//            ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers,
//            MongoClientSettings settings
//    ) {
//        return new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))
//                .createMongoClient(settings);
//    }

    @Bean
    @ConditionalOnMissingBean(MongoClientSettings.class)
    MongoClientSettings mongoClientSettings()  {
        return MongoClientSettings.builder().build();
    }

    // Spring Boot 2.5
//    @Bean
//    MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(
//            MongoProperties properties,
//            Environment environment
//    ) {
//        return MongoPropertiesClientSettingsBuilderCustomizer(properties, environment)
//    }

}
