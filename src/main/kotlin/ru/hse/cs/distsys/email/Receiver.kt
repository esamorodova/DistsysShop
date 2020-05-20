package ru.hse.cs.distsys.email

import org.springframework.jms.annotation.JmsListener

class Receiver(private val emailInterface: SendEmailInterface) {
    @JmsListener(destination = "sendMail")
    fun receiveEmail(email: Email) {
        emailInterface.sendMail(email.to, email.subject, email.body)
    }
}