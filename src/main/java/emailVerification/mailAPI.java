package emailVerification;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class mailAPI {
	
	public static String USER_NAME = "sbudiscord";
	public static String PASS_WORD = "pokemon227";
	
	public static void sendEmail(String Recipient, int Key) {
		String Subject = "Stony Brook Discord Email Verification";
		String Body = "Hey! Your key is: " + Integer.toString(Key);
		
		Properties Props = System.getProperties();
		String Host = "smtp.gmail.com";
		
		Props.put("mail.smtp.starttls.enable", "true");
        Props.put("mail.smtp.host", Host);
        Props.put("mail.smtp.user", USER_NAME);
        Props.put("mail.smtp.password", PASS_WORD);
        Props.put("mail.smtp.port", "587");
        Props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(Props);
        MimeMessage message = new MimeMessage(session);
        
        try {
        	InternetAddress toAddress = new InternetAddress(Recipient);
        	message.setFrom(new InternetAddress(USER_NAME));
        	message.addRecipient(Message.RecipientType.TO, toAddress);
        	
        	message.setSubject(Subject);
        	message.setText(Body);
        	
        	Transport transport = session.getTransport("smtp");
        	transport.connect(Host, USER_NAME, PASS_WORD);
        	transport.sendMessage(message, message.getAllRecipients());
        	transport.close();
        	
        	System.out.println("Email Sent Successfully!");
        	
        }catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
	}
}
