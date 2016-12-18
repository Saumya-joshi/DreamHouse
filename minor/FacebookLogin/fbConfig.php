<?php
	require_once __DIR__ . '/Facebook/autoload.php';
	$App_Id       = "1819483158302528";        // APP ID
	$App_Secret   = "ca95c204a6dcd7a53b3b6209c8dd9037";    // APP SECRET
	$Redirect_Url = "http://192.168.43.220/minor/FacebookLogin";    // APP DOMAIN --> It has to be match with app domain on your facebook app
 
 	$fb = new Facebook\Facebook([
  		'app_id' => $App_Id,
  		'app_secret' => $App_Secret,
  		'default_graph_version' => 'v2.4', // API GRAPH VERSION
  		]);
?>
