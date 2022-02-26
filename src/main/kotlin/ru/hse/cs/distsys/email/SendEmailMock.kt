package ru.hse.cs.distsys.email

import org.slf4j.LoggerFactory

class SendEmailMock: SendEmailInterface {
    companion object {
        private val logger = LoggerFactory.getLogger(SendEmailMock::class.java)
    }

    override fun sendMail(to: String, subject: String, body: String) {
        logger.info("send: " + Email(to, subject, body))
    }
}