<?php
	require './PHPMailer-master/PHPMailerAutoload.php';
	class SendMail
	{
		private $phpMailer;
		public function __construct()
		{
			$this->phpMailer = new PHPMailer;
		}
		
		public function sendVerificationMail($gmailId, $gmailPwd, $to, $msg, $subject)
		{
			$mail = $this -> phpMailer;
			$mail->isSMTP();
			$mail->SMTPDebug = 0;
			$mail->Debugoutput = 'html';
			$mail->Host = 'smtp.gmail.com';
			$mail->Port = 587;
			$mail->SMTPSecure = 'tls';
			$mail->SMTPAuth = true;
			$mail->Username = $gmailId;
			$mail->Password = $gmailPwd;
			$mail->setFrom($gmailId, 'Dream House');
			$mail->addAddress($to);
			$mail->Subject = $subject;
			$mail->Body = $msg;
			if (!$mail->send()) 
			{
				echo "Mailer Error: " . $mail->ErrorInfo;
			}
			else 
			{
		    		return true;					
			}
		}
	}
	/*$mailSedn = new SendMail();
	if($mailSedn -> sendVerificationMail('pksinha217@gmail.com', '1210199521011997', $hash, 'pksinha217@gmail.com', 'Hi everyone'))
		echo 'Success';
	else
		echo 'Error';*/
?>
