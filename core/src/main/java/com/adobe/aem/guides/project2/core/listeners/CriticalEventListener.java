package com.adobe.aem.guides.project2.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component(service = EventHandler.class, immediate = true, property = {
        "event.topics=org/apache/sling/api/resource/Resource/ADDED",
        "event.topics=org/apache/sling/api/resource/Resource/CHANGED",
        "event.topics=org/apache/sling/api/resource/Resource/REMOVED"
})
public class CriticalEventListener implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(CriticalEventListener.class);

    // SMTP configuration for testing
    private String smtpHost = "localhost"; // Use localhost for Fake SMTP server
    private int smtpPort = 25; // Standard non-SSL SMTP port for testing
    private String emailFrom = "samuelraju469@gmail.com"; // Use any valid email format
    private String emailTo = "samuelraju.a0594@gmail.com"; // Use recipient email

    @Override
    public void handleEvent(Event event) {
        String eventTopic = event.getTopic();
        log.info("Received event: {}", eventTopic);

        // Only trigger email for REMOVED events or other critical events
        if (isCriticalEvent(event)) {
            sendEmailNotification(event);
        } else {
            log.info("Non-critical event received: {}", eventTopic);
        }
    }

    private boolean isCriticalEvent(Event event) {
        // Treat REMOVED and other events as critical based on custom logic
        String eventTopic = event.getTopic();
        return eventTopic.contains("REMOVED"); // Only treat REMOVED as critical for now
    }

    private void sendEmailNotification(Event event) {
        log.info("Preparing to send email for event: {}", event.getTopic());

        // Extract additional details from the event
        String resourcePath = (String) event.getProperty("path"); // Resource path from the event
        String actionType = getActionType(event.getTopic()); // Action type based on event topic

        // Set properties for the mail session (non-SSL setup for Fake SMTP)
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));
        properties.put("mail.smtp.auth", "false"); // Disable auth for localhost SMTP
        properties.put("mail.smtp.starttls.enable", "false"); // Disable SSL/TLS

        // Create session (no authentication for Fake SMTP)
        Session session = Session.getInstance(properties);

        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject("Critical System Event Notification");

            // Set the email message body directly
            String emailBody = "A critical event has occurred.\n\n"
                    + "Action: " + actionType + "\n"
                    + "Resource Path: " + resourcePath + "\n"
                    + "Event Topic: " + event.getTopic();

            // Directly set the plain text content
            message.setText(emailBody, "utf-8");

            // Send the message
            Transport.send(message);
            log.info("Sent email notification for event: {}", event.getTopic());
        } catch (MessagingException e) {
            log.error("Failed to send email notification", e);
        }
    }

    // Helper method to derive the action type (ADDED, CHANGED, REMOVED) from the
    // event topic
    private String getActionType(String eventTopic) {
        if (eventTopic.contains("ADDED")) {
            return "ADDED";
        } else if (eventTopic.contains("CHANGED")) {
            return "CHANGED";
        } else if (eventTopic.contains("REMOVED")) {
            return "REMOVED";
        }
        return "UNKNOWN";
    }
}
