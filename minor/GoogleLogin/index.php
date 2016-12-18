<?php
	//include the google auth class
	require_once("src/Google/autoload.php");
	//require_once __DIR__ . '/vendor/autoload.php';

	ini_set('display_errors', 'On');
        error_reporting(E_ALL);

	/*Set APP Id, APP Secret AND Redirect Url*/

	const APP_ID = "982662959886-juiep51k26tssshjh2ilb3m2ahu0ivj7.apps.googleusercontent.com";
	const APP_SECRET = "Pxdb2Uv-DckWzVWaQsq5gUU-";
	const REDIRECT_URI = "http://localhost/minor/GoogleLogin";

	session_start();

	/*Initializing Google auth class by setting variables*/
	$googleClient = new Google_Client();
	$googleClient->setClientId(APP_ID);
	$googleClient->setClientSecret(APP_SECRET);
	$googleClient->setRedirectUri(REDIRECT_URI);
	$googleClient->addScope("email");
	$googleClient->addScope('https://www.googleapis.com/auth/plus.login');
	$googleClient->addScope('https://www.googleapis.com/auth/contacts.readonly');

	/*Initializing Google+ service for the client*/
	$googlePlus = new Google_Service_Plus($googleClient);
	#$googleConnection = new Google_Service_People($googleClient);

	/*when user log off his account*/
	if (isset($_REQUEST['logout'])) {
		session_unset();
	}

	/*Handling resposne from google server*/
	if (isset($_GET['code'])) 
	{
		$googleClient->authenticate($_GET['code']);
		$_SESSION["GoogleAccessToken"] = $googleClient->getAccessToken();
		$redirect_url = "http://".$_SERVER['HTTP_HOST'].$_SERVER['PHP_SELF'];
		$redirect = filter_var($redirect_url, FILTER_SANITIZE_URL);
		header("Location: {$redirect}");
	}

	/*Getting User Info*/
	if (isset($_SESSION['GoogleAccessToken']) && $_SESSION['GoogleAccessToken']) 
	{
		$googleClient->setAccessToken($_SESSION['GoogleAccessToken']);
		/*Calling plus Api*/
		$UserInfo = $googlePlus->people->get("me");
		//$connections = $people_service->people->connections->listConnections('people/me');
		$User_Id = $UserInfo['id'];
		$User_Name = $UserInfo['displayName'];
		$User_Email = $UserInfo['emails'];
		$User_Profile_Pic = $UserInfo['image']['url'];
		$User_Profile_Url = $UserInfo['url'];
		var_dump($UserInfo);
		echo 'User'.'<br>';
		echo 'hell'.'<br>';

		//calling people api
		$googleConnection = new Google_Service_People($googleClient);
		$connections = $googleConnection->people_connections->listPeopleConnections('people/me');

		echo 'hell_returns';
		//var_dump($connections);
		echo '<br>';
	//	var_dump($connections['connections']);
		echo json_encode($connections);
	}
	else
	{
		/*Get Oauth URL*/
		$login_url = $googleClient->createAuthUrl();
	}
?>

<?php
	if (isset($login_url)) 
	{
		echo "<a href='{$login_url}'>".'Login with google'."</a>";
	}
	else
	{
		/*$redirectDB = 'saveInfoinDB.php';
		$redirect = true;
		if($redirect == true)
		{
			header('Location: '.$redirectDB);
		}*/
		echo $User_Id.'<br>';
		echo $User_Name.'<br>';
		echo $User_Email[0]['value'].'<br>';
		echo $User_Profile_Url.'<br>';
		//echo $connections.'<br>';
	}
?>

