<?php
ini_set('display_errors', true);
error_reporting(E_ALL);


function conectar(){

	try {
		$opcoes = array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION);
		$pdo = new PDO("mysql:host=localhost;dbname=easyp957_easypassemobile;", "easyp957_easypas", "MIwQRVXj6C7faT38", $opcoes);
	} catch (Exception $e) {
		echo $e->getMessage();
	}

	return $pdo;

}

conectar();

?>
