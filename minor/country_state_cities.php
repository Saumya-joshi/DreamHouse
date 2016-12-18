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
		$query_country = 'SELECT *FROM dreamHouse.countries';
		$fetch_country = $conn->prepare($query_country);
		$fetch_country -> execute();
		$fetch_country -> setFetchMode(PDO::FETCH_ASSOC);
		while($country = $fetch_country->fetch())
		{
			$country_id = $country['id'];
			$country_sort_name = $country['sortname'];
			$country_name = $country['name'];
			//echo $country_id.'<br>';
			$query_state = 'SELECT *FROM dreamHouse.states where country_id = ?';
			$fetch_state = $conn->prepare($query_state);
			$fetch_state -> bindParam(1, $country_id, PDO::PARAM_INT);
			$fetch_state -> execute();
			$fetch_state -> setFetchMode(PDO::FETCH_ASSOC);
			$json_state = array();
			while($states = $fetch_state->fetch())
			{
				$state_id = $states['state_id'];
				$state_name = $states['name'];
				$state_country_id = $states['country_id'];
				//echo $state_id.'<br>'.$state_name.'<br>';
				$query_city = 'SELECT *FROM dreamHouse.cities where state_id = ?';
				$fetch_city = $conn->prepare($query_city);
				$fetch_city -> bindParam(1, $state_id, PDO::PARAM_INT);
				$fetch_city -> execute();
				$fetch_city -> setFetchMode(PDO::FETCH_ASSOC);
				$json_city = array();
				while($cities = $fetch_city->fetch())
				{
					$city_id = $cities['city_id'];
					$city_name = $cities['name'];
					//echo $city_id.'<br>'.$city_name.'<br>';
					/*$data = array(
						'country_id' => $country_id,
						'country_sort_name' => $country_sort_name,
						'country_name' => $country_name,
						'states' => array(
							'state_id' => $state_id,
							'state_name' => $state_name,
							'cities' => array(
								'city_id' => $city_id,
								'city_name' => $city_name)));*/
					$data = array('city_id' => $city_id,
                                                      'city_name' => $city_name);
					array_push($json_city, $data);
				}
				//var_dump($json_city);
				$data_state = array(
						'state_id' => $state_id,
						'state_name' => $state_name,
						'cities' => $json_city
						);
				array_push($json_state, $data_state);
			}
			$data_country = array(
					'country_id' => $country_id,
					'country_sort_name' => $country_sort_name,
					'country_name' => $country_name,
					'states' => $json_state);

			//var_dump($json_state);
			array_push($json_result, $data_country);
		}
		//var_dump($json_result);
	 	$json = json_encode($json_result);
		var_dump($json);	
		/*echo '<br>'.'Dumping json data to file...'.'<br>';
		$file_pointer = fopen('country_state_city.json', 'w');
		if(fwrite($file_pointer, $json))
			echo 'Successfully dumped data to file'.'<br>';
		else
			echo 'Error'.'<br>';
		fclose($file_pointer);*/
	}
	catch(PDOException $ex)
	{
		echo 'Connection Failed'.$ex->getMessage();
	}
?>
