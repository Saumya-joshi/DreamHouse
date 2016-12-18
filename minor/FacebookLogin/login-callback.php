<?php
	session_start();
	include 'fbConfig.php';
	$helper = $fb->getRedirectLoginHelper();
	try 
	{
  		$accessToken = $helper->getAccessToken();
	}
	catch(Facebook\Exceptions\FacebookResponseException $e)
	{
  		// When Graph returns an error
  		echo 'Graph returned an error: ' . $e->getMessage();
  		exit;
	}
	catch(Facebook\Exceptions\FacebookSDKException $e) 
	{
  		// When validation fails or other local issues
  		echo 'Facebook SDK returned an error: ' . $e->getMessage();
  		exit;
	}
	
	if (isset($accessToken) && isset($_GET['userId'])) 
	{
  		// Logged in!
		$_SESSION['USER_UNIQUE_ID'] = $_GET['userId'];
  		$_SESSION['facebook_access_token'] = (string) $accessToken;
		echo $_SESSION['facebook_access_token'].'<br>';
		echo $_SESSION['USER_UNIQUE_ID'].'<br>';
		echo 'logged In';
		/*$redirect_url = 'getUserDetails.php';
		$redirect = true;*/
		header('Refresh: 3; URL='. $Redirect_Url .'/welcome.php');
	}
?>
