<?php
	if(isset($_GET['userId']) && !empty($_GET['userId']))
	{
		session_start();
		$_SESSION['USER_UNIQUE_ID'] = $_GET['user_id'];
		include 'fbConfig.php';
		$helper = $fb->getRedirectLoginHelper();
		//$permissions = ['email']; // Optional permissions
		$permissions = array(scope => 'email, public_profile, user_friends');
		$loginUrl = $helper->getLoginUrl($Redirect_Url.'/login-callback.php?userId='.$_GET['userId'], $permissions);
		if ($_SESSION['fb_id']) 
		{
    			header("Location:".$Redirect_Url."/welcome.php");
		}
		else
		{
    			echo '<a href="' . $loginUrl . '">Log in with Facebook!</a>';
		}
	}
	else
	{
		echo 'Invalid Link..';
	}
?>
