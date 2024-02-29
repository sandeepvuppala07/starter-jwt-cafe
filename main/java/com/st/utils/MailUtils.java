package com.st.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailUtils {

	@Autowired
	private JavaMailSender mailsender;
	
	public void sendSimpleMail(String to, String subject, String text, List<String> adminsCCList) {
		
		SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
			simpleMailMessage.setFrom("testmailsender010101@gmail.com");
		    simpleMailMessage.setSubject(subject);
		    simpleMailMessage.setTo(to);
		    simpleMailMessage.setText(text);
		    if(adminsCCList!=null && adminsCCList.size()>0)
		       simpleMailMessage.setCc(getAdminCCString(adminsCCList));
		    
		    mailsender.send(simpleMailMessage); 
		
	}
	
	private String[] getAdminCCString(List<String> adminsCCList) {
		
		String[] adminCCArray = new String[adminsCCList.size()];
		for(int i=0;i<adminsCCList.size();i++) {
			
			adminCCArray[i]=adminsCCList.get(i);
		}
		return adminCCArray;
	}
	
	public void forgotMail(String to,String subject, String password) throws MessagingException {
		
		MimeMessage mimemessage = mailsender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimemessage,true);
		helper.setFrom("testmailsender010101@gmail.com");
		helper.setSubject(subject);
		helper.setTo(to);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:6069/user/login/\">Click here to login</a></p>";
		mimemessage.setContent(htmlMsg,"text/html");
		mailsender.send(mimemessage);

	}
	
}


