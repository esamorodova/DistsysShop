package ru.hse.cs.distsys.email

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.ConnectionFactory

@Configuration
@Profile("email")
@EnableJms
class EmailConfiguration {
    @Bean
    fun sendEmailImpl(): SendEmailInterface = SendEmailImpl()

    @Bean
    fun receiver(sendEmail: SendEmailInterface) = Receiver(sendEmail)

    @Bean
    fun myFactory(connectionFactory: ConnectionFactory?,
                  configurer: DefaultJmsListenerContainerFactoryConfigurer): JmsListenerContainerFactory<*>? {
        val factory = DefaultJmsListenerContainerFactory()
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory)
        // You could still override some of Boot's default if necessary.
        return factory
    }

    @Bean // Serialize message content to json using TextMessage
    fun jacksonJmsMessageConverter(): MessageConverter? {
        val converter = MappingJackson2MessageConverter()
        val objectMapper = Jackson2ObjectMapperBuilder()
                .modules(KotlinModule())
                .build<ObjectMapper>()
        converter.setObjectMapper(objectMapper)
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }
}