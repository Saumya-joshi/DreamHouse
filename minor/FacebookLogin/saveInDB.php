<?php
	include 'dbConfig.php';	
	session_start();
	$AccessToken = $_SESSION['facebook_access_token'];
	$UserId = $_SESSION['fb_id'];
	$Name = $_SESSION['fullname'];
	$emailId = $_SESSION['email'];
	$gender = $_SESSION['gender'];
	$birthday = $_SESSION['birthday'];
	echo $UserId.'<br>';
	echo $Name.'<br>';
	echo $emailId.'<br>';
	echo $gender.'<br>';
	echo $birthday.'<br>';
	/*try
	{
		$conn = new PDO($dsn, $user_name, $pwd_connect);
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$checkExistingUser = "Select count(*) from testAndroid.FacebookUsers where emailId = ?";
		$checkExistingUserPrepare = $conn->prepare($checkExistingUser);
		$checkExistingUserPrepare->bindParam(1, $emailId, PDO::PARAM_STR, 50);
		$checkExistingUserPrepare->execute();
		$no_of_users = $checkExistingUserPrepare->fetchColumn();
		if($no_of_users == 0)
		{
			$insertFBRecord = $conn->prepare("Insert into testAndroid.FacebookUsers (AccessToken, user_id, name, emailId, gender) values(?, ?, ?, ?, ?)";
			$insertFBRecord->bindParam(1, $AccessToken, PDO::PARAM_STR, 1000);
			$insertFBRecord->bindParam(2, $UserId, PDO::PARAM_STR, 256);
			$insertFBRecord->bindParam(3, $Name, PDO::PARAM_STR, 256);
			$insertFBRecord->bindParam(4, $emailId, PDO::PARAM_STR, 256);
			$insertFBRecord->bindParam(5, $gender, PDO::PARAM_STR, 10);
			if($insertFBRecord->execute())
			{
				$_SESSION['REGISTERED_FB_USER'] = true;
				echo 'Successfully saved record in database.';
			} 
			else
			{
				echo 'Error while executing.';
			}
		} 
		else
		{
			echo 'Alreday Registered.';
		}
	}
	catch(PDOException $ex)
        {
                echo "Connection failed.." . $ex->getMessage();
        }*/
?>	
