package com.adobe.aem.guides.project2.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Email Configuration for Critical Event Listener")
public @interface EmailConfig {

    @AttributeDefinition(name = "SMTP Host", description = "The SMTP server host address")
    String smtpHost() default "smtp.gmail.com";

    @AttributeDefinition(name = "SMTP Port", description = "The SMTP server port")
    int smtpPort() default 465;

    @AttributeDefinition(name = "Sender Email", description = "The sender's email address")
    String emailFrom();

    @AttributeDefinition(name = "Email Password", description = "The sender's email password")
    String emailPassword();

    @AttributeDefinition(name = "Recipient Email", description = "The recipient's email address")
    String emailTo();

    @AttributeDefinition(name = "Use SSL", description = "Use SSL for email", defaultValue = "true")
    boolean sslEnabled() default true;
}
