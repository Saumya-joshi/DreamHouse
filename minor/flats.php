<?php
	include 'DBConfig.php';
        session_start();
        ini_set('display_errors', 'On');
        error_reporting(E_ALL);
        try
        {
                $json_result = array();
                $conn = new PDO($dsn, $user_name, $pwd_connect);
                $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		echo 'Connection Successful'.'<br>';
                $query_flats = 'SELECT *FROM dreamHouse.flat_data';
                $fetch_flats = $conn->prepare($query_flats);
                $fetch_flats -> execute();
                $fetch_flats -> setFetchMode(PDO::FETCH_ASSOC);
		$fetch_result_set = $fetch_flats->fetchAll();
		$json = json_encode($fetch_result_set);
		var_dump($json);
		echo '<br>'.'Dumping json data to file...'.'<br>';
                $file_pointer = fopen('flats.json', 'w');
                if(fwrite($file_pointer, $json))
                        echo 'Successfully dumped data to file'.'<br>';
                else
                        echo 'Error'.'<br>';
                fclose($file_pointer);
	}
	catch(PDOException $ex)
	{
		echo $ex->getMessage();
	}
?>
