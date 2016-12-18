<?php
	require_once 'sendMail.php';
	require_once 'Users.php';

	class Functions
	{
 		private $db;
		private $mailI;
		public function __construct() 
		{
       			$this -> db = new Users();
			$this -> mailI = new SendMail();
		}
	 
		public function registerUser($name, $email, $password) 
		{
    			$db = $this -> db;
			$mail = $this -> mailI;
    			if (!empty($name) && !empty($email) && !empty($password)) 
			{
       				if ($db -> checkIfUserExists($email)) 
				{
          				$response["result"] = "failure";
         				$response["message"] = "User Already Registered !";
  	     				return json_encode($response);
      				}
				else 
				{
 			        	$result = $db -> getUserRegistered($name, $email, $password);
          				if ($result) 
					{	
						
						$hash = $db -> getHashCode($email);
						$OTP = $db -> getOTP($email);
						$subject = 'Sucessful Registration.';
						$link = 'http://192.168.43.220/minor/verify.php?email_id='.$email.'&hashCode='.$hash.'';
						$message = 'Hi '.$name."\n".'You have successfully registered.'."\n".'Please Click on the given link:'."\n".$link."\n".'to verify your email id'."\n".'Or enter the OTP :'.$OTP.'';
						$mailResponse = $mail->sendVerificationMail('pksinha217@gmail.com', '1210199521011997', $email, $message, $subject);				
						if($mailResponse)
						{
               						$response["result"] = "success";
            						$response["message"] = "User Registered Successfully ! Verification mail Sent.";
							$response["user"] = $result;
            						return json_encode($response);
						}
						else
						{
							$response["result"] = "failure";
                                                        $response["message"] = "Mail Failed";
                                                        return json_encode($response);
						}
 				        }
					else 
					{
 				        	$response["result"] = "failure";
            					$response["message"] = "Registration Failure";
            					return json_encode($response);
 					}
      				}
   			} 
			else 
			{
       				return $this -> getMsgParamNotEmpty();
    			}
		}
 
		public function loginUser($email, $password) 
		{
			$db = $this -> db;
   			if (!empty($email) && !empty($password)) 
			{
     				if ($db -> checkIfUserExists($email)) 
				{
        				$result =  $db -> getUserLoggedIn($email, $password);
 				        if(!$result) 
					{
        					$response["result"] = "failure";
        					$response["message"] = "Either login credentials are invalid ou user is not verified.";
        					return json_encode($response);
       					} 
					else 
					{
 		        			$response["result"] = "success";
        					$response["message"] = "Login Sucessful";
        					$response["user"] = $result;
        					return json_encode($response);
					}
    				} 
				else 
				{
       					$response["result"] = "failure";
      					$response["message"] = "Invaild Login Credentials";
      					return json_encode($response);
     				}
  			} 
			else 
			{
      				return $this -> getMsgParamNotEmpty();
    			}	
		}


		public function verify_OTP($emailId, $OTP)
		{
			$db = $this -> db;
			if (!empty($emailId) && !empty($OTP))
			{
				if($db -> checkIfUserExists($emailId))
				{
					$result = $db -> verifyOTP($emailId, $OTP);
					if(!$result)
					{
						$response["result"] = "failure";
                                                $response["message"] = "OTP expired. Please resend OTP.";
                                                return json_encode($response);
					}
					else
					{
						$response["result"] = "success";
                                                $response["message"] = "Successfully Verified!!!";
                                                return json_encode($response);
					}
				}
				else
				{
					$response["result"] = "failure";
                                        $response["message"] = "User with this email id does not exixts.";
                                        return json_encode($response);
				}
			}
		}

		public function verify_hash($emailId, $hashValue)
                {
                        $db = $this -> db;
                        if (!empty($emailId) && !empty($hashValue))
                        {
                                if($db -> checkIfUserExists($emailId))
                                {
                                        $result = $db -> getHashVerified($emailId, $hashValue);
                                        if(!$result)
                                        {
                                                $response["result"] = "failure";
                                                $response["message"] = "Either your hash expired or is invalid. Please try with OTP.";
                                                return json_encode($response);
                                        }
                                        else
                                        {
                                                $response["result"] = "success";
                                                $response["message"] = "Successfully Verified!!!";
                                                return json_encode($response);
                                        }
                                }
                                else
                                {
                                        $response["result"] = "failure";
                                        $response["message"] = "User with this email id does not exixts.";
                                        return json_encode($response);
                                }
                        }
                }

		public function savePersonalInfo($user_email, $user_uniqueId, $user_fname, $user_mname, $user_lname, $user_sex, $user_dob, $user_mob)
		{
			$db = $this -> db;
			if(!empty($user_email) && !empty($user_uniqueId) && !empty($user_fname) && !empty($user_mname) && !empty($user_lname) && !empty($user_sex) && !empty($user_dob) && !empty($user_mob))
			{
				if($db -> checkIfUserExists($user_email))
				{
					$result = $db -> getPersonalProfileSaved($user_uniqueId, $user_fname, $user_mname, $user_lname, $user_sex, $user_dob, $user_mob);
					if(!$result)
					{
						$response["result"] = "failure";
                                                $response["message"] = "Some Error Occured.";
                                                return json_encode($response);
					}
					else
					{
						$response["result"] = "success";
                                                $response["message"] = "Successfully Saved Personal Profile.";
                                                return json_encode($response);
					}
				}
				else
				{
					$response["result"] = "failure";
                                        $response["message"] = "User with this email id does not exixts.";
                                        return json_encode($response);
				}
			}
		}

 		public function changePassword($email, $old_password, $new_password) 
		{
   			$db = $this -> db;
   			if (!empty($email) && !empty($old_password) && !empty($new_password)) 
			{
     				if(!$db -> checkLogin($email, $old_password))
				{
      					$response["result"] = "failure";
      					$response["message"] = 'Invalid Old Password';
					return json_encode($response);
 				}
				else 
				{
     					$result = $db -> changePassword($email, $new_password);
       					if($result) 
					{
         					$response["result"] = "success";
        					$response["message"] = "Password Changed Successfully";
        					return json_encode($response);
       					} 
					else 
					{
         					$response["result"] = "failure";
        					$response["message"] = 'Error Updating Password';
        					return json_encode($response);
       					}
    				}
  			}
			else 
			{
       				return $this -> getMsgParamNotEmpty();
  			}
		}
		
		public function isEmailValid($email)
		{
   			return filter_var($email, FILTER_VALIDATE_EMAIL);
		}
 
		public function getMsgParamNotEmpty()
		{
  			$response["result"] = "failure";
  			$response["message"] = "Parameters should not be empty !";
  			return json_encode($response);
		}
 
		public function	getMsgInvalidParam()
		{
   			$response["result"] = "failure";
  			$response["message"] = "Invalid Parameters";
  			return json_encode($response);
		}
 
		public function getMsgInvalidEmail()
		{
  			$response["result"] = "failure";
  			$response["message"] = "Invalid Email";
  			return json_encode($response);
		}
	}
?>
