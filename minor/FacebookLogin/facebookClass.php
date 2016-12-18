<?php
	class saveFB
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

		public function checkFBExixts($user_id, $facebook_id)
		{
			$sql_check = "SELECT COUNT(*) from user_facebook_link where user_id = :userID and facebook_id = :fbId";
			$prepare_check = $this -> conn -> prepare($sql_check);
			$prepare_check -> execute(array(':userID' => $user_id, ':fbId' => $facebook_id));
			if($prepare_check)
			{
				$count = $prepare_check->fetchColumn();
				if($count == 0)
				{
					//no recored exixts
					return true;
				}
				else
				{
					//record exists
					return false;
				}
			}
		}

		public function saveFacebookData($user_id, $facebookId, $name, $email, $gender, $pictureUrl)
		{
			$sql_insert = "INSERT INTO dreamHouse.facebookInfo SET facebook_id = :facebookId, fbName = :name, fbEmail = :email, gender = :sex, picUrl = :picture";
			$prepare_insert = $this -> conn -> prepare($sql_insert);
			$prepare_insert -> execute(array(':facebookId' => $facebookId, ':name' => $name, ':email' => $email, 'sex' => $gender, ':picture' => $pictureUrl));
			if($prepare_insert)
			{
				//successfully created record. Now create link
				$create_link = "INSERT INTO dreamHouse.user_facebook_link SET user_id = :userId, facebook_id = :facebookId";
				$prepare_link = $this -> conn -> prepare($create_link);
				$prepare_link -> execute(array(':userId' => $user_id, ':facebookId' => $facebookId));
				if($prepare_link)
				{
					//created link
					return true;
				}
				else
				{
					//link not created
					return false;
				}
			}
		}
		
		public function doesFreindExists($facebookId, $freindId)
		{
			$sql_check = "SELECT COUNT(*) FROM dreamHouse.link_user_facebook_freind where facebook_id = :facebookId and freind_id = :freindId";
			$prepare_check = $this -> conn -> prepare($sql_check);
			$prepare_check -> execute(array(':facebookId' => $facebookId, ':freindId' => $freindId));
			if($prepare_check)
			{
				$count = $prepare_check->fetchColumn();
				if($count == 0)
				{
					//no such record
					return true;
				}
				else
				{
					return false;
				}
			}
		}

		public function saveFacebookFreinds($facebookId, $name, $fbId, $picture)
		{
			$sql_insert = "INSERT INTO dreamHouse.facebookFreinds set facebook_id = :fbId, name = :name, picture = :picture";
			$prepare_insert = $this -> conn -> prepare($sql_insert);
			$prepare_insert -> execute(array(':fbId' => $fbId, ':name' => $name, ':picture' => $picture));
			if($prepare_insert)
			{
				//echo 'Success'.'<br>';
				//successfully created record. Now create link
                                $create_link = "INSERT INTO dreamHouse.link_user_facebook_freind SET facebook_id = :facebookId, freind_id = :freindId";
                                $prepare_link = $this -> conn -> prepare($create_link);
                                $prepare_link -> execute(array(':facebookId' => $facebookId, ':freindId' => $fbId));
                                if($prepare_link)
                                {
                                        //created link
                                        return true;
                                }
                                else
                                {
                                        //link not created
                                        return false;
                                }

			}
		}
	}
?>
