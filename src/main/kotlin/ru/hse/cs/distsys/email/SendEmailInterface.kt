package ru.hse.cs.distsys.email

interface SendEmailInterface {
    fun sendMail(to: String, subject: String, body: String)
}