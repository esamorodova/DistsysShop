package ru.hse.cs.distsys.shop

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import ru.hse.cs.distsys.auth.AuthClientImpl
import ru.hse.cs.distsys.auth.AuthenticationService

@Configuration
@Profile("shop")
class ShopConfiguration {
    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun authService(restTemplate: RestTemplate,
                    @Value("\${KATE.AUTH.BASEURL}") baseUrl: String): AuthenticationService = AuthClientImpl(restTemplate, baseUrl)
}