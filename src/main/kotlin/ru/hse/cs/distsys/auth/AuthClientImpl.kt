package ru.hse.cs.distsys.auth

import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder

class AuthClientImpl(private val restTemplate: RestTemplate, private val baseUrl: String): AuthenticationService {

    override fun validate(email: String, token: String): Boolean{
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/auth/validate")
                .build().toUri()

        val requestBody = AuthenticationController.AccessTokenRequest(email, token);
        return restTemplate.postForObject<Boolean>(url, requestBody)
    }
}