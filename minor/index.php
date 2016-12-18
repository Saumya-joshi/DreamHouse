<?php
	//require_once 'sendMail.php';
	require_once 'Functions.php';
	$fun = new Functions();
	//$sendMail = new SendMail();
	if ($_SERVER['REQUEST_METHOD'] == 'POST')
	{
  		$data = json_decode(file_get_contents("php://input"));
   		if(isset($data -> operation))
		{
   			$operation = $data -> operation;
    			if(!empty($operation))
			{
       				if($operation == 'register')
				{
 			        	if(isset($data -> user ) && !empty($data -> user) && isset($data -> user -> name) && isset($data -> user -> email) && isset($data -> user -> password))
					{
            					$user = $data -> user;
            					$name = $user -> name;
            					$email = $user -> email;
            					$password = $user -> password;
           					if ($fun -> isEmailValid($email)) 
						{
        						echo $fun -> registerUser($name, $email, $password);
           					} 
						else 
						{
             						echo $fun -> getMsgInvalidEmail();
          					}
          				} 
					else 
					{
             					echo $fun -> getMsgInvalidParam();
 					}
 				}
				else if ($operation == 'login') 
				{
         				if(isset($data -> user ) && !empty($data -> user) && isset($data -> user -> email) && isset($data -> user -> password))
					{
			          		$user = $data -> user;
          					$email = $user -> email;
          					$password = $user -> password;
           					echo $fun -> loginUser($email, $password);
 				        } 
					else 
					{
           					echo $fun -> getMsgInvalidParam();
 				        }
      				} 
				else if ($operation == 'chgPass') 
				{
         				if(isset($data -> user ) && !empty($data -> user) && isset($data -> user -> email) && isset($data -> user -> old_password) && isset($data -> user -> new_password))
					{
           					$user = $data -> user;
          					$email = $user -> email;
          					$old_password = $user -> old_password;
          					$new_password = $user -> new_password;
           					echo $fun -> changePassword($email, $old_password, $new_password);
       	 				} 
					else 
					{
           					echo $fun -> getMsgInvalidParam();
 				        }
      				}
				/*else if($operation == 'personal_details')
				{
					if(isset($data -> user) && !empty($data -> user) && isset($data -> user -> fName) && isset($data -> user -> lName) && isset($data -> user -> ))
				}*/
				else if($operation == 'verify_hash')
				{
					if(isset($data -> user) && !empty($data -> user) && isset($data -> user -> email) && isset($data -> user -> user_hash))
					{
						$user = $data -> user;
						$user_email = $user -> email;
						$user_hash_value = $user -> user_hash;
						echo $fun -> verify_hash($user_email, $user_hash_value);
					}
					else
					{
						echo $fun -> getMsgInvalidParam();
					}
				}

				else if($operation == 'verify_otp')
                                {
                                        if(isset($data -> user) && !empty($data -> user) && isset($data -> user -> email) && isset($data -> user -> OTP))
                                        {
                                                $user = $data -> user;
                                                $user_email = $user -> email;
                                                $user_otp_value = $user -> OTP;
                                                echo $fun -> verify_OTP($user_email, $user_otp_value);
                                        }
                                        else
                                        {
                                                echo $fun -> getMsgInvalidParam();
                                        }
                                }

				else if($operation == 'personal_details')
                                {
                                        if(isset($data -> user) && !empty($data -> user) && isset($data -> user -> email) && isset($data -> user -> unique_id) && isset($data -> user -> fName) && isset($data -> user -> lName) && isset($data -> user -> mName) && isset($data -> user -> sex) && isset($data -> user -> dateOfBirth) && isset($data -> user -> mobileNumber))
                                        {
                                                $user = $data -> user;
                                                $user_email = $user -> email;
                                                $user_uniqueId = $user -> unique_id;
						$user_fname = $user -> fName;
						$user_mname = $user -> mName;
						$user_lname = $user -> lName;
						$user_sex = $user -> sex;
						$user_dob = $user -> dateOfBirth;
						$user_mob = $user -> mobileNumber;
                                                echo $fun -> savePersonalInfo($user_email, $user_uniqueId, $user_fname, $user_mname, $user_lname, $user_sex, $user_dob, $user_mob);
                                        }
                                        else
                                        {
                                                echo $fun -> getMsgInvalidParam();
                                        }
                                }
   			}
			else
			{
       				echo $fun -> getMsgParamNotEmpty();
  			}
  		} 
		else 
		{
       			echo $fun -> getMsgInvalidParam();
   		}
	} 
?>
