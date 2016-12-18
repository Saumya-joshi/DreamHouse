<?php
	ini_set('display_errors', 'On');
        error_reporting(E_ALL);

	session_start();
	$fb_access_token = $_SESSION['facebook_access_token'];
	$user_id = $_SESSION['USER_UNIQUE_ID'];
	include 'fbConfig.php';
	include 'facebookClass.php';
	$save = new saveFB();
	try 
	{
    		$response = $fb->get('/me?fields=id,name,first_name,last_name,email,gender,age_range, picture', $fb_access_token);
    		$response_freinds = $fb->get('/me/friends?fields=id,name,first_name,last_name,email,gender,context, picture', $fb_access_token);
	}
	catch(Facebook\Exceptions\FacebookResponseException $e)
	{
  		echo 'Graph returned an error: ' . $e->getMessage();
  		exit;
	}
	catch(Facebook\Exceptions\FacebookSDKException $e) 
	{
  		echo 'Facebook SDK returned an error: ' . $e->getMessage();
	  	exit;
	}

	// Get the response typed as a GraphUser
	$user = $response->getGraphUser();
	$user_freinds = $response_freinds->getGraphEdge()->asArray();
	$fb_id = $user->getId();
	$fullname = $user->getName();
	$firstname = $user->getFirstName();
	$lastname = $user->getLastName();
	$email = $user->getEmail();
	$gender = $user->getGender();
	$imageUrl = "https://graph.facebook.com/".$fb_id."/picture";
	if($save -> checkFBExixts($user_id, $fb_id))
	{
		$save -> saveFacebookData($user_id, $fb_id, $fullname, $email, $gender, $imageUrl);
		/*echo $fb_id.'<br>';
		echo $fullname.'<br>';
		echo $firstname.'<br>';
		echo $lastname.'<br>';
		echo $email.'<br>';
		echo $gender.'<br>';
		echo $imageUrl.'<br>';*/
	}
	//echo 'Freinds Data : '.'<br>';
	foreach($user_freinds as $freinds)
        {
		if($save -> doesFreindExists($fb_id, $freinds["id"]))
		{
			$image = "https://graph.facebook.com/".$freinds["id"]."/picture";
			$save -> saveFacebookFreinds($fb_id, $freinds["name"], $freinds["id"], $image);
                	//echo $freinds["name"].'<br>';
                	//echo $freinds["id"].'<br>';
                	//echo "https://graph.facebook.com/".$freinds["id"]."/picture".'<br>';
		}
        }
	echo 'Thanx for registering with facebook.';

?>
