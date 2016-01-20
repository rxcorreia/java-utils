package pt.rxc.commmon.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * This class provides methods to send e-mails.
 * 
 * @author ruben.correia
 */
public class MailUtils {

	/**
	 * Send an e-mail.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the success of the operation
	 * @throws MessagingException
	 */
	public static boolean sendMail(String from, String to, String subject, String text, String host, String username,
			String password) {
		List<String> toEmails = new ArrayList<String>(1);
		toEmails.add(to);
		return sendMail(from, toEmails, subject, text, host, username, password);
	}

	/**
	 * Send an e-mail.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the success of the operation
	 */
	public static boolean sendMail(String from, List<String> to, String subject, String text, String host,
			String username, String password) {
		return sendMail(from, to, null, null, subject, text, host, username, password);
	}

	/**
	 * Send and email as text.
	 * 
	 * @param from
	 *            the from address
	 * @param to
	 *            the to address
	 * @param cc
	 *            the cc address
	 * @param bcc
	 *            the bcc
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the success of the operation
	 */
	public static boolean sendMail(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
			String text, String host, String username, String password) {
		return sendMail(from, to, cc, bcc, subject, text, host, username, password, false);
	}

	/**
	 * Send an email.
	 * 
	 * @param from
	 *            the from address
	 * @param to
	 *            the to address
	 * @param cc
	 *            the cc address
	 * @param bcc
	 *            the bcc
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param isHtml
	 *            indicates if it should be sent as html
	 * @return the success of the operation
	 */
	public static boolean sendMail(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
			String text, String host, final String username, final String password, boolean isHtml) {

		return sendMail(from, to, cc, bcc, subject, text, host, null, username, password, isHtml, false, null);
	}

	/**
	 * Send mail.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param cc
	 *            the cc
	 * @param bcc
	 *            the bcc
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param isHtml
	 *            the is html
	 * @param isSecure
	 *            the is secure
	 * @param signatureSrcPath
	 *            the signature src path
	 * @return true, if successful
	 */
	public static boolean sendMail(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
			String text, String host, String port, final String username, final String password, boolean isHtml,
			boolean isSecure, String signatureSrcPath) {
		return sendMail(from, to, cc, bcc, subject, text, host, port, username, password, isHtml, isSecure,
				signatureSrcPath, null);
	}

	/**
	 * Send mail.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param cc
	 *            the cc
	 * @param bcc
	 *            the bcc
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param isHtml
	 *            the is html
	 * @param isSecure
	 *            the is secure
	 * @param signatureSrcPath
	 *            the signature src path
	 * @param attachList
	 *            a list of attachments to be sent
	 * @return true, if successful
	 * 
	 * @see {@link MailAttachment}
	 */

	public static boolean sendMail(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
			String text, String host, String port, final String username, final String password, boolean isHtml,
			boolean isSecure, String signatureSrcPath, List<MailAttachment> attachList) {
		return sendMail(from, to, cc, bcc, subject, text, host, port, username, password, isHtml, isSecure, false,
				signatureSrcPath, attachList);
	}

	public static boolean sendMail(String from, List<String> to, List<String> cc, List<String> bcc, String subject,
			String text, String host, String port, final String username, final String password, boolean isHtml,
			boolean isSecure, boolean isSSL, String signatureSrcPath, List<MailAttachment> attachList) {
		boolean retObj = true;
		if (StringUtils.isBlank(host)) {
			throw new IllegalArgumentException("The host should not be null or empty");
		}

		if (CollectionUtils.isEmpty(to)) {
			throw new IllegalArgumentException("The contacts list to send the email is null or empty");
		}

		Properties props = new Properties();
		props.put("mail.smtp.host", host);

		Session session = null;

		if (StringUtils.isNotBlank(port)) {
			props.put("mail.smtp.port", port);
			// props.put("mail.smtp.socketFactory.port", port);
		}

		if (isSecure) {
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.ssl.checkserveridentity", "true");

			if (isSSL)
				props.put("mail.smtp.starttls.enable", "true");

			session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		} else {
			if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
				props.setProperty("mail.user", username);
				props.setProperty("mail.password", password);
			}
			session = Session.getInstance(props);
		}

		// Session

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			if (CollectionUtils.isNotEmpty(to)) {
				for (String email : to) {
					if (StringUtils.isNotBlank(email)) {
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					}
				}
			}

			if (CollectionUtils.isNotEmpty(cc)) {
				for (String email : cc) {
					if (StringUtils.isNotBlank(email)) {
						message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
					}
				}
			}

			if (CollectionUtils.isNotEmpty(bcc)) {
				for (String email : bcc) {
					if (StringUtils.isNotBlank(email)) {
						message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
					}
				}
			}

			message.setSubject(subject, "utf-8");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDisposition(MimeBodyPart.INLINE);
			MimeBodyPart signatureBodyPart = null;
			Multipart multipart = new MimeMultipart("mixed");

			if (isHtml) {
				StringBuffer content = new StringBuffer();
				content.append(text);

				if (StringUtils.isNotBlank(signatureSrcPath)) {
					content.append("<br/><img src=\"cid:signature\"/>");
					signatureBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(new File(signatureSrcPath));
					signatureBodyPart.setDataHandler(new DataHandler(source));
					signatureBodyPart.setFileName("signature");
					signatureBodyPart.setHeader("Content-ID", "<signature>");
					signatureBodyPart.setDisposition(MimeBodyPart.INLINE);
				}
				messageBodyPart.setContent(content.toString(), "text/html; charset=utf-8");
			} else {
				messageBodyPart.setContent(text, "text/html; charset=utf-8");
			}

			multipart.addBodyPart(messageBodyPart);
			if (signatureBodyPart != null) {
				multipart.addBodyPart(signatureBodyPart);
			}

			if (CollectionUtils.isNotEmpty(attachList)) {
				for (MailAttachment attach : attachList) {
					MimeBodyPart attachPart = new MimeBodyPart();
					ByteArrayDataSource byteSource = new ByteArrayDataSource(attach.getByteArray(),
							attach.getMimeType());
					attachPart.setDataHandler(new DataHandler(byteSource));

					if (attach.getFilename() != null && !attach.getFilename().isEmpty()) {
						attachPart.setFileName(attach.getFilename());
					}
					multipart.addBodyPart(attachPart);
				}
			}

			message.setContent(multipart);
			if (isSSL) {
				Transport transport = session.getTransport("smtps");
				transport.connect(host, username, password);
				transport.sendMessage(message, message.getAllRecipients());
			} else {
				Transport.send(message);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error occured when attempting to send an email: ", e);
		}
		return retObj;

	}
}