package ru.hse.cs.distsys.auth

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MessageType
import ru.hse.cs.distsys.email.PutMessageInQueue
import javax.jms.ConnectionFactory

import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
@Profile("auth")
class AuthConfiguration {
    @Bean
    fun putMessageInQueue() = PutMessageInQueue()

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
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }
}
