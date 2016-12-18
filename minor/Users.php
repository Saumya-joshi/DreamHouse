<?php

	//ini_set('display_errors', 'On');
        //`error_reporting(E_ALL);
	class Users
	{
		private $host = '127.0.0.1';
		private $user_name = 'root';
		private $database_name = 'dreamHouse';
		private $password_connect = 'pri2si17';
		private $charset = 'utf8';
		private $conn;

		public function __construct()
		{
			try
			{
				$this->conn = new PDO("mysql:host=".$this -> host.";dbname=".$this -> database_name.";charset = ".$this->charset, $this -> user_name, $this -> password_connect);	
				$this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
				//echo 'Connection Successful'.'<br>';
			}
			catch(PDOException $ex)
			{
				echo 'Connection Failed...'.$ex->getMessage();
			}
		}

		public function getUserRegistered($Name, $emailId, $Password)
		{
			try
			{
				$uniqueID = uniqid('user_', true);
				$userEmail = $emailId;
				$userName = $Name;
				$userPassword = password_hash($Password, PASSWORD_DEFAULT);
				$dateRegistered = date('Y-m-d H:i:s');
				$hashCode = md5(rand(0, 1000));
				$userVerified = 0;
				/*echo $uniqueID.'<br>';
				echo $userEmail.'<br>';
				echo $userName.'<br>';
				echo $userPassword.'<br>';
				echo $dateRegistered.'<br>';
				echo $hashCode.'<br>';
				echo $userVerified.'<br>';*/
				$registerQuery = 'INSERT INTO dreamHouse.registeredUser SET user_id = :user_id, email_id = :userEmail, password = :userPassword, hash_code = :hashCode, date_registered = NOW(), isVerified = 0, user_name = :userName';
				$executableQuery = $this -> conn -> prepare($registerQuery);
				$executableQuery->execute(array(':user_id' => $uniqueID, ':userEmail' => $userEmail, ':userPassword' => $userPassword, ':hashCode' => $hashCode, ':userName' => $userName));
				if($executableQuery)
				{
					//echo 'Success'.'<br>';
					$hashStatus = 'sent';
					$registerHashQuery = 'INSERT INTO dreamHouse.userHashStatus SET user_id = :userID, email_id = :emailID, hash = :userHASH, date_created = NOW(), status = :hashSTATUS';
					$executeHASH = $this -> conn -> prepare($registerHashQuery);
					$executeHASH->execute(array(':userID' => $uniqueID, ':emailID' => $userEmail, ':userHASH' => $hashCode, ':hashSTATUS' => $hashStatus));
					if($executeHASH)
					{
						//echo 'Success'.'<br>';
						$user['email'] = $userEmail;
						$user['unique_id'] = $uniqueID;
						$user['user_hash'] = $hashCode;
						return $user;
					}
					else
					{
						//echo 'Failure'.'<br>';
						return false;
					}
				}
				else
				{
					//echo 'Faliure'.'<br>';
					return false;
				}
			}
			catch(PDOException $ex)
			{
				echo $ex->getMessage();
			}
		}

		public function checkIfUserExists($emailId)
		{
			try
			{
				$chkEmail = $emailId;
				$chkQuery = 'SELECT COUNT(*) from dreamHouse.registeredUser WHERE email_id = :emailId';
				$executeChkQuery = $this -> conn -> prepare($chkQuery);
				$executeChkQuery -> execute(array(':emailId' => $chkEmail));
				if($executeChkQuery)
				{
					$user_count = $executeChkQuery -> fetchColumn();
					if($user_count == 0)
					{
						//echo 'No user Exixts.'.'<br>';
						return false;
					}
					else
					{
						//echo 'User Exists'.'<br>';
						return true;
					}
				}
				else
				{
					//echo 'Error'.'<br>';
					return false;
				}
			}
			catch(PDOException $ex)
			{
				echo $ex->getMessage();
			}
		}

		public function getHashCode($emailId)
		{
			try
			{
				$hashEmail = $emailId;
				$hashQuery = 'SELECT hash_code from dreamHouse.registeredUser WHERE email_id = :emailId';
				$executeHashQuery = $this -> conn -> prepare($hashQuery);
				$executeHashQuery->execute(array(':emailId' => $hashEmail));
				if($executeHashQuery)
				{
					$hashValue = $executeHashQuery->fetch();
					$hashCode = $hashValue['hash_code'];
					//echo $hashCode.'<br>';
					return $hashCode;
				}
				else
				{
					echo 'Error Hash Code'.'<br>';
				}
			}
			catch(PDOException $ex)
			{
				echo $ex->getMessage();
			}
		}

		public function getUserLoggedIn($emailId, $password)
		{
			try
			{
				$loginEmail = $emailId;
				$loginPassword = $password;
				$loginQuery = 'SELECT *FROM dreamHouse.registeredUser where email_id = :emailId';
				$executeLoginQuery = $this -> conn -> prepare($loginQuery);
				$executeLoginQuery -> execute(array(':emailId' => $loginEmail));
				if($executeLoginQuery)
				{
					$userData = $executeLoginQuery->fetch();
					$isVerified = $userData['isVerified'];
					$userPassword = $userData['password'];
					$matchPassword = password_verify($loginPassword, $userPassword);
					if($matchPassword)
					{
						if($isVerified == 1)
						{
							//echo 'Successfully Logged in'.'<br>';
							$user['name'] = $userData['user_name'];
							$user['email'] = $userData['email_id'];
							//$user['USER_SERIAL_NO'] = $userData['serial_no'];
							$user['unique_id'] = $userData['user_id'];
							return $user;
						}
						else
						{
							//echo 'Not a verified user'.'<br>';
							return false;
						}
					}
					else
					{
						//echo 'Invalid password'.'<br>';
						return false;
					}
				}
				else
				{
					//echo 'Invalid User id';
					return false;
				}
			}
			catch(PDOException $ex)
                        {
                                echo $ex->getMessage();
                        }
		}
	
		public function getOTP($emailID)
		{
			try
			{
				$OTPemail = $emailID;
				$OTP = mt_rand(100000, 999999);
				$status = 'sent';
				$select_otp = 'SELECT COUNT(*) from dreamHouse.userOTP where email_id = :emailID';
				$prepare_select = $this -> conn -> prepare($select_otp);
				$prepare_select -> execute(array(':emailID' => $OTPemail));
				if($prepare_select)
				{
					$count_row = $prepare_select->fetchColumn();
					if($count_row == 0)
					{
						$sql_query = 'INSERT INTO dreamHouse.userOTP SET email_id = :emailID, OTP = :otp, date_created = NOW(), status = :status';
						$prepare_query = $this -> conn -> prepare($sql_query);
						$prepare_query -> execute(array(':emailID' => $OTPemail, ':otp' => $OTP, ':status' => $status));
						if($prepare_query)
						{
							//echo 'Successfully generated OTP'.'<br>';
							return $OTP;
						}
						else
						{
							//echo 'ERROR';
							return false;
						}
					}
					else
					{
						$sql_update = 'UPDATE dreamHouse.userOTP SET OTP = :otp, date_created = NOW(), status = :status where email_id = :email';
						$prepare_update = $this -> conn -> prepare($sql_update);
						$prepare_update -> execute(array(':otp' => $OTP, ':status' => $status, ':email' => $OTPemail));
						if($prepare_update)
						{
							return $OTP;
						}
						else
						{
							return false;
						}
					}
				}
			}
			catch(PDOException $ex)
			{
				 echo $ex->getMessage();
			}
		}

		public function verifyOTP($emailID, $OTP)
		{
			try
			{
				$sql_query = 'SELECT *FROM dreamHouse.userOTP where email_id = :emailID';
				$prepare_query = $this -> conn -> prepare($sql_query);
				$prepare_query -> execute(array(':emailID' => $emailID));
				if($prepare_query)
				{
					$OTPdata = $prepare_query->fetch();
					$regOTP = $OTPdata['OTP'];
					$regTime = $OTPdata['date_created'];
					$time = date('Y-m-d H:i:s' , $_SERVER['REQUEST_TIME']);
					//echo $OTP.'<br>';
					//echo $regOTP.'<br>';
					//echo $regTime.'<br>';
					//echo $time.'<br>';
					$date_time_1 = new DateTime($regTime);
					$date_time_2 = new DateTime($time);
					$time_difference = $date_time_1->diff($date_time_2);
					$time_minutes = $time_difference->format('%i');
					//echo $time_minutes.'<br>';
					if($time_minutes <= 2)
					{
						if($regOTP == $OTP)
						{
							$statusUpdate = 'verified';
							$query_update = 'UPDATE dreamHouse.userOTP SET status = :status where email_id = :emailId';
							$prepare_update = $this -> conn -> prepare($query_update);
							$prepare_update -> execute(array(':status' => $statusUpdate, ':emailId' => $emailID));
							if($prepare_update)
							{
								$isVerified = 1;
								$query_update_user_status = 'UPDATE dreamHouse.registeredUser SET isVerified = :isVerified where email_id = :emailId';
								$prepare_update_user_status = $this -> conn -> prepare($query_update_user_status);
								$prepare_update_user_status -> execute(array(':isVerified' => $isVerified, ':emailId' => $emailID));
								if($prepare_update_user_status)
								{
									//echo 'Successfully Verified.';
									return true;
								}
								else
								{
									//echo 'Error';
									return false;
								}
							}
						}
						else
						{
							//echo 'Invalid OTP.'.'<br>';
							return false;
						}
					}
					else
					{
						$statusUpdate = 'expired';
						$query_update = 'UPDATE dreamHouse.userOTP SET status = :status where email_id = :emailId';
                                        	$prepare_update = $this -> conn -> prepare($query_update);
                                        	$prepare_update -> execute(array(':status' => $statusUpdate, ':emailId' => $emailID));
						if($prepare_update)
						{
							//echo 'OTP expired.';
						}
						return false;
					}
				}
			}
			catch(PDOException $ex)
			{
				echo $ex->getMessage();
			}
		}

		public function getHashVerified($emailId, $hash_code)
		{
			try
			{
				$hashEmail = $emailId;
				$hashCode = $hash_code;
				$status = 'sent';
				$select_query = 'SELECT *FROM dreamHouse.userHashStatus where email_id = :emailID and status = :status';
				$prepare_query = $this -> conn-> prepare($select_query);
				$prepare_query -> execute(array(':emailID' => $hashEmail, ':status' => $status));
				$prepare_query -> setFetchMode(PDO::FETCH_ASSOC);
				if($prepare_query)
				{
					$data = $prepare_query->fetch();
					$reg_hash_code = $data['hash'];
					$reg_time = $data['date_created'];
					$current_time = date('Y-m-d H:i:s' , $_SERVER['REQUEST_TIME']);
					$time_1 = new DateTime($reg_time);
					$time_2 = new DateTime($current_time);
					$time_interval = $time_1->diff($time_2);
					$interval_hour = $time_interval->format('%h');
					//echo $reg_hash_code.'<br>';
					//echo $reg_time.'<br>';
					//echo $current_time.'<br>';
					//echo $interval_hour.'<br>';
					if($interval_hour > 24)
					{
						//echo 'Link Expired';
						$status_ex = 'Expired';
						$update_query = 'UPDATE dreamHouse.userHashStatus SET status = :status where email_id = :emailID';
						$prepare_update = $this -> conn -> prepare($update_query);
						$prepare_update -> execute(array(':status' => $status_ex, 'emailID' => $hashEmail));
						if($prepare_update)
						{
							//echo 'Link Expired'.'<br>';
							return false;
						}	 
						else
						{
							//echo 'Error while executing query'.'<br>';
							return false;
						}
					}
					else
					{
						if($reg_hash_code == $hashCode)
						{
							//echo 'matched'.'<br>';
							$status_vr = 'Verified';
							$isVerified = 1;
							$update_query = 'UPDATE dreamHouse.userHashStatus SET status = :status where email_id = :emailID';
                                        		$prepare_update = $this -> conn -> prepare($update_query);
                                        		$prepare_update -> execute(array(':status' => $status_vr, 'emailID' => $hashEmail));
							if($prepare_update)
							{
								$update_user = 'UPDATE dreamHouse.registeredUser SET isVerified = :isVerified where email_id = :emailID';
								$prepare_update_user = $this -> conn -> prepare($update_user);
								$prepare_update_user -> execute(array(':isVerified' => $isVerified, ':emailID' => $hashEmail));
								if($prepare_update_user)
								{
									//echo 'Your email id is Verified. Now you can login.'.'<br>';
									return true;
								}
								else
								{
									//echo 'Error updating user.';
									return false;
								}
							}
							else
							{
								//echo 'User updation failed.'.'<br>';
								return false;
							}
						}
						else
						{
							//echo 'Invalid hash'.'<br>';
							return false;
						}
					}
				}
				else
				{
					//echo 'Link is expired'.'<br>';
					return false;
				}
			}
		
			catch(PDOException $ex)
			{
				echo $ex->getMessage();
			}
		}

		public function getPersonalProfileSaved($user_uniqueId, $user_fname, $user_mname, $user_lname, $user_sex, $user_dob, $user_mob)
		{
			try
			{
				$date_of_birth = date("Y-m-d", strtotime($user_dob));
				//echo $date_of_birth.'<br>';
				$profile_id = uniqid('personal_', true);
				$sql_query = 'INSERT INTO dreamHouse.personal_profile_info SET profile_id = :profile_id, first_name = :fName, middle_name = :mName, last_name = :lName, sex = :gender, mobile_number = :mob, date_of_birth = :dob';
				$prepare_query = $this -> conn -> prepare($sql_query);
				$prepare_query -> execute(array(':profile_id' => $profile_id, ':fName' => $user_fname, ':mName' => $user_mname, ':lName' => $user_lname, ':gender' => $user_sex, ':mob' => $user_mob, ':dob' => $date_of_birth));
				if($prepare_query)
				{
					$sql_link_query = 'INSERT INTO dreamHouse.link_user_personal_info SET user_id = :user_id, profile_id = :profile_id';
					$prepare_link = $this -> conn -> prepare($sql_link_query);
					$prepare_link -> execute(array(':user_id' => $user_uniqueId, ':profile_id' => $profile_id));
					if($prepare_link)
					{
						//echo 'Success'.'<br>';
						return true;
					}
					else
					{
						//echo 'Failure'.'<br>';
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			catch(PDOException $ex)
			{
				$ex -> getMessage();
			}
		}
	}

	//$user = new Users();	
	//$user->getUserRegistered('Priyanshu Sinha', 'pksinha217@gmail.com', 'pri2si17');
	//$user->checkIfUserExists('pksinha217@gmail.com');
	//$user->getHashCode('pksinha217@gmail.com');
	//$user->getUserLoggedIn('pksinha217@yahoo.com', '1334');
	//echo $user->getOTP('pksinha217@gmail.com');
	//echo $user->verifyOTP('pksinha217@gmail.com', '12468');
	//echo $user->getHashVerified('pksinha217@gmail.com', 'cbcb58ac2e496207586df2854b17995f');
	//echo $user -> getPersonalProfileSaved('user_583f0fb56bc482.67276693', 'Priyanshu', 'Kumar', 'Sinha', 'Male', '21-01-1997', '9958150222');
?>
