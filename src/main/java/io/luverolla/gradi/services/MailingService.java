package io.luverolla.gradi.services;

import io.luverolla.gradi.entities.Message;
import io.luverolla.gradi.entities.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Set;

/**
 * Service bean dedicated to email dispatching
 */
@Service
public class MailingService
{
    @Value("${gradi.system.email.smtp.name}")
    private String smtpName;

    @Value("${gradi.system.email.smtp.host}")
    private String smtpHost;

    @Value("${gradi.system.email.smtp.port}")
    private int smtpPort;

    @Value("${gradi.system.email.smtp.username}")
    private String smtpUser;

    @Value("${gradi.system.email.smtp.password}")
    private String smtpPass;

    @Value("${gradi.admin.email}")
    private String adminAddr;

    @Value("${gradi.system.domain}")
    private String domain;

    public JavaMailSender mailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);

        mailSender.setUsername(smtpUser);
        mailSender.setPassword(smtpPass);

        // testing purpose fragment (TPF)
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        // end TPF

        return mailSender;
    }

    public void notifyUserAdd(User us)
    {
        String NEW_USER_ADDEDD =
            "<p>Dear %s,</p><p>Your email has been registered on %s</p>" +
            "<p>If you believe this was an error, please <a href='mailto:%s'>contact the administrator</a></p>";

        try {
            MimeMessage mm = mailSender().createMimeMessage();
            MimeMessageHelper hlp = new MimeMessageHelper(mm, true);

            hlp.setFrom(smtpUser, smtpName);
            hlp.setTo(us.getEmail());
            hlp.setBcc(adminAddr); // always admin in BCC
            hlp.setSubject("[WELCOME USER] " + us.getRecipientName());
            hlp.setText("", String.format(NEW_USER_ADDEDD, us.getFullName(), domain, adminAddr));

            mailSender().send(mm);
        }
        catch(MessagingException | UnsupportedEncodingException e) { e.printStackTrace(); }
    }

    public void notifyUserUpdate(String code, String oldEmail, User data)
    {
        String USER_UPDATED =
            "<p>Dear user,</p><p>Your details have been changed</p>" +
            "<p>New details are:" +
            "<ul>" +
            "<li><strong>Full Name:</strong> %s</li>" +
            "<li><strong>Email address:</strong> %s</li>" +
            "<li><strong>Role:</strong> %s</li>" +
            "</ul></p>" +
            "<p>If you didn't expect such update or you don't acknowledge the details above, " +
            "<a href='mailto:%s'>inform the administrator</a> immediately</p>";

        try {
            MimeMessage mm = mailSender().createMimeMessage();
            MimeMessageHelper hlp = new MimeMessageHelper(mm, true);

            hlp.setFrom(smtpUser, smtpName);
            hlp.setTo(new String[]{oldEmail, data.getEmail()});
            hlp.setBcc(adminAddr); // always admin in BCC
            hlp.setSubject("[USER DETAILS UPDATED]");
            hlp.setText("", String.format(USER_UPDATED, data.getFullName(), data.getEmail(), data.getRole().toString(), adminAddr));

            mailSender().send(mm);
        }
        catch(MessagingException | UnsupportedEncodingException e) { e.printStackTrace(); }
    }

    public void notifyPasswordChange(User us, String pswd)
    {
        String NEW_USER_ADDEDD =
            "<p>Dear %s,</p><p>Your have requested a password reset</p>" +
            "<p>Here is your new password: <code>%s</code></p>" +
            "<p>If you have not made this request, <a href='mailto:%s'>inform the administrator</a> immediately</p>";

        try {
            MimeMessage mm = mailSender().createMimeMessage();
            MimeMessageHelper hlp = new MimeMessageHelper(mm, true);

            hlp.setFrom(smtpUser, smtpName);
            hlp.setTo(us.getEmail());
            hlp.setBcc(adminAddr); // always admin in BCC
            hlp.setSubject("[PASSWORD RESET] " + us.getRecipientName());
            hlp.setText("", String.format(NEW_USER_ADDEDD, us.getFullName(), pswd, adminAddr));

            mailSender().send(mm);
        }
        catch(MessagingException | UnsupportedEncodingException e) { e.printStackTrace(); }
    }

    public void notifyMessage(Message msg, Set<User> dst)
    {
        try {
            MimeMessage mm = mailSender().createMimeMessage();
            MimeMessageHelper hlp = new MimeMessageHelper(mm, true);

            hlp.setFrom(smtpUser, smtpName);
            hlp.setTo(adminAddr); // admin sends email to himself
            hlp.setBcc(dst.stream().map(User::getEmail).toArray(String[]::new)); // each recipient in BCC, so nobody can see each other
            hlp.setSubject("[MESSAGE FROM ADMIN] " + msg.getSubject());
            hlp.setText("", msg.getText());

            mailSender().send(mm);
        }
        catch(MessagingException | UnsupportedEncodingException e) { e.printStackTrace(); }
    }
}