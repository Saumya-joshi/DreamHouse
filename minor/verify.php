<?php
	include 'DBConfig.php';
	session_start();
	ini_set('display_errors', 'On');
        error_reporting(E_ALL);
	try
	{
		if(isset($_GET['email_id']) && isset($_GET['hashCode']))
		{
			$email_id = $_GET['email_id'];
			$hash_value = $_GET['hashCode'];
			//echo $email_id.'<br>';
			//echo $hash_value.'<br>';
			$conn = new PDO($dsn, $user_name, $pwd_connect);
                	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			//$conn->setFetchMode(PDO::FETCH_ASSOC);
                	//echo 'Connection Successful'.'<br>';
			$status = 'sent';
			$select_query = 'SELECT *FROM dreamHouse.userHashStatus where email_id = :emailID and status = :status';
			$prepare_query = $conn->prepare($select_query);
			$prepare_query -> execute(array(':emailID' => $email_id, ':status' => $status));
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
					$prepare_update = $conn->prepare($update_query);
					$prepare_update -> execute(array(':status' => $status_ex, 'emailID' => $email_id));
					if($prepare_update)
					{
						echo 'Link Expired'.'<br>';
					} 
					else
					{
						echo 'Error while executing query'.'<br>';
					}
				}
				else
				{
					if($reg_hash_code == $hash_value)
					{
						//echo 'matched'.'<br>';
						$status_vr = 'Verified';
						$isVerified = 1;
						$update_query = 'UPDATE dreamHouse.userHashStatus SET status = :status where email_id = :emailID';
                                        	$prepare_update = $conn->prepare($update_query);
                                        	$prepare_update -> execute(array(':status' => $status_vr, 'emailID' => $email_id));
						if($prepare_update)
						{
							$update_user = 'UPDATE dreamHouse.registeredUser SET isVerified = :isVerified where email_id = :emailID';
							$prepare_update_user = $conn -> prepare($update_user);
							$prepare_update_user -> execute(array(':isVerified' => $isVerified, ':emailID' => $email_id));
							if($prepare_update_user)
							{
								echo 'Your email id is Verified. Now you can login.'.'<br>';
							}
							else
							{
								echo 'Error updating user.';
							}
						}
						else
						{
							echo 'User updation failed.'.'<br>';
						}

					}
					else
					{
						echo 'Invalid hash'.'<br>';
					}
				}
			}
			else
			{
				echo 'Link is expired'.'<br>';
			}
		}
		else
		{
			echo 'Invalid Link';
		}
	}
	catch(PDOException $ex)
	{
		echo $ex->getMessage();
	}
?>
