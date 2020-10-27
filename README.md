# Java-mail sender

Simple javamail based application that lets you send mails using a specified STMP server.
## Building

- Clone the repository
- From the shell execute
```
	$> mvn clean package
``` 
- With that you'll have the application generated at target/mailsender.jar


## Usage

- mailsender.jar is a command-line application that works with the following parameters

```
	$> java -jar mailsender.jar -p 1025 -f timoteo.ponce@sbmail.ch -rcs "a1@mail.ch,a2@mail.cg" 
	11-12-14 14:48:04 INFO - Sending message [from=timoteo.ponce@swissbytes.ch,to=[a1@mail.ch,a2@mail.cg]]
	11-12-14 14:48:04 INFO - Using properties {
		mail.smtp.starttls.enable : false
		mail.debug : false
		mail.smtp.port : 1025
		mail.transport.protocol : smtp
		mail.smtp.host : localhost
		mail.smtp.auth : false
		mail.smtp.auth.mechanisms : LOGIN PLAIN DIGEST-MD5 
	}
	11-12-14 14:48:04 INFO - message sent	
```

### Available options
```
 --auth-mech (-aum) VAL  : SMTP authentication mechanisms
 --content (-c) VAL      : Mail content
 --debug (-d)            : Debug mail-sending-processes
 --from (-f) VAL         : Mail address
 --host (-h) VAL         : SMTP server host(localhost)
 --password (-pw) VAL    : SMTP server password
 --port (-p) N           : SMTP server port(25)
 --protocol (-pt) VAL    : SMTP server protocol(smtp)
 --recipients (-rcs) VAL : Mail recipients, separated with commas
 --start-tls (-st)       : Use TLS
 --subject (-s) VAL      : Mail subject
 --use-auth (-ua)        : Use SMTP authentication
```