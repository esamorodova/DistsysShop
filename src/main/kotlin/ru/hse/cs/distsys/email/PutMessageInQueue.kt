package ru.hse.cs.distsys.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

class PutMessageInQueue: SendEmailInterface {
    @Autowired
    lateinit var emailSenderTemplate: JmsTemplate

    override fun sendMail(to: String, subject: String, body: String) {
        emailSenderTemplate.convertAndSend("sendMail", Email(to, subject, body))
    }
}