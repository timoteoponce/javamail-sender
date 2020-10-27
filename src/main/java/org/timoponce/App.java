package org.timoponce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Hello world!
 *
 */
public class App {

	@Option(name = "--host", aliases="-h",usage = "SMTP server host(localhost)")
	private String host;
	@Option(name = "--port", aliases="-p",usage = "SMTP server port(25)")
	private Integer port;
	@Option(name = "--protocol", aliases="-pt",usage = "SMTP server protocol(smtp)")
	private String protocol;
	@Option(name = "--user", aliases="-u",usage = "SMTP server username")
	private String userName;
	@Option(name = "--password", aliases="-pw",usage = "SMTP server password")
	private String password;
	@Option(name = "--auth-mech", aliases="-aum",usage = "SMTP authentication mechanisms")
	private String[] authMechanisms = { "LOGIN", "PLAIN", "DIGEST-MD5" };
	@Option(name = "--use-auth", aliases="-ua",usage = "Use SMTP authentication")
	private Boolean useAuth;
	@Option(name = "--start-tls", aliases="-st",usage = "Use TLS")
	private Boolean startTls;
	@Option(name = "--debug", aliases="-d",usage = "Debug mail-sending-processes")
	private Boolean debugEnabled;
	//
	@Option(name = "--subject", aliases="-s",usage = "Mail subject")
	private String subject;
	@Option(name = "--content", aliases="-c",usage = "Mail content")
	private String content;
	@Option(name = "--from", aliases="-f",usage = "Mail address")
	private String from;
	@Option(name = "--recipients", aliases="-rcs",usage = "Mail recipients, separated with commas")
	private String[] recipients;

	public void sendMail() {
		log("Sending message [from=" + from + ",to="
				+ Arrays.toString(recipients) + "]");
		Properties props = new Properties();
		props.put("mail.transport.protocol", defaultVal(protocol,"smtp"));
		props.put("mail.smtp.auth", defaultVal(useAuth, "false"));
		props.put("mail.smtp.starttls.enable", defaultVal(startTls, "false"));
		props.put("mail.smtp.host", defaultVal(host, "localhost"));
		props.put("mail.smtp.port", defaultVal(port, "25"));
		props.put("mail.debug", defaultVal(debugEnabled, "false"));

		if (authMechanisms != null && authMechanisms.length > 0)
			props.put("mail.smtp.auth.mechanisms", join(authMechanisms, " "));

		Session session = null;
		if ("".equals(defaultVal(userName, "")))
			session = Session.getInstance(props);
		else {
			props.put("mail.smtp.auth", "true");
			session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});
		}
		log("Using properties " + join(props));
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(join(recipients, ",")));
			message.setSubject(defaultVal(subject, "Timo's test mail"));
			message.setText(defaultVal(content, "Timo's test content"));

			Transport.send(message);
			log("message sent");
		} catch (MessagingException e) {
			log("Error sending mail", e);
		}
	}

	private String join(Properties props) {
		StringBuilder buffer = new StringBuilder("{\n");
		for (Entry entry : props.entrySet())
			buffer.append("\t").append(entry.getKey()).append(" : ")
					.append(entry.getValue()).append("\n");
		return buffer.append("}").toString();
	}

	private String join(String[] values, String sep) {
		StringBuilder buffer = new StringBuilder();
		for (String str : values)
			buffer.append(str).append(sep);
		return buffer.toString();
	}

	private String defaultVal(Object val, String defaultVal) {
		return val == null ? defaultVal : val.toString();
	}

	public static void main(String[] args) throws CmdLineException {
		new App().doMain(args);
	}

	void doMain(String[] args) throws CmdLineException {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			if (args == null || args.length == 0) {
				parser.printUsage(System.out);
				return;
			}
			parser.parseArgument(args);
			sendMail();
		} catch (Exception e) {
			log(e.toString(), e);
		}
	}

	static final DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

	private static void log(String msg) {
		System.out.println(df.format(new Date()) + " INFO - " + msg);
	}

	private static void log(String msg, Exception e) {
		System.out.println(df.format(new Date()) + " ERROR - " + msg
				+ (e == null ? "" : " > " + e.getMessage()));
	}
}
