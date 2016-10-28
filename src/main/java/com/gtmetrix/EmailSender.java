package com.gtmetrix;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read to resolve config issues
 * https://www.siteground.com/kb/google_free_smtp_server/
 * https://www.google.com/settings/security/lesssecureapps
 * https://accounts.google.com/b/0/DisplayUnlockCaptcha
 */

public class EmailSender {

    public void send(String report){

		Properties config = new Properties();

		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
			config.load(in);
			final String username = config.getProperty("email");
			final String password = config.getProperty("password");
			final String recipient = config.getProperty("recipient");


			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true"); //Enables Transport Layer Security
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});


			javax.mail.Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject("Test Subject");
			message.setContent(report, "text/html; charset=utf-8");
			Transport.send(message);

			System.out.println("Mail Sent");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		catch (MessagingException e) {
        	throw new RuntimeException(e);
		}
    }
}
