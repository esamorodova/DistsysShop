package ru.hse.cs.distsys.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class SendEmailImpl: SendEmailInterface {
    @Autowired
    lateinit var emailSender: JavaMailSender
    @Value("\${kate.email.from}") lateinit var emailFrom: String

    override fun sendMail(to: String, subject: String, body: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(body)
        message.setFrom(emailFrom)
        emailSender.send(message)
    }
}