<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
ini_set('display_errors', true);
error_reporting(E_ALL);

include_once("con.php");

$pdo = conectar();

$data = file_get_contents("php://input");
$data = json_decode($data);

$cpf = $data->cpf;
$senha = $data->senha;

$login=$pdo->prepare("SELECT * FROM usuario WHERE cpf=:cpf AND senha=:senha");
$login->bindValue(":cpf", $cpf);
$login->bindValue(":senha", $senha);
$login->execute();

$return = array();

while ($linha=$login->fetch(PDO::FETCH_ASSOC)) {

	$idusuario = $linha['idUsuario'];
	$nome = $linha['nome'];

	$return = array(
		'idusuario'	=> $idusuario,
		'nome'	=> $nome
	);

}

echo json_encode($return);

// if($data){
// 	$opcao = $data->opcao;
// }else{
// 	$opcao = $_GET['opcao'];
// }


// switch ($opcao) {
//
// 	case 1: //Busca clientes do aplicativo
//
// 		$getAllCli=$pdo->prepare("SELECT idusuario, nome, sobrenome FROM usuario");
// 		$getAllCli->execute();
//
// 		$return = array();
//
// 		while ($linha=$getAllCli->fetch(PDO::FETCH_ASSOC)) {
//
// 			$idusuario = $linha['idusuario'];
// 			$nome = $linha['nome'];
// 			$sobrenome = $linha['sobrenome'];
//
// 			$return[] = array(
// 				'idusuario'	=> $idusuario,
// 				'nome'	=> $nome,
// 				'sobrenome'	=> $sobrenome
// 			);
//
// 		}
//
// 		echo json_encode($return);
//
// 		break;
//
// 	// case 2: //Busca detalhes do usuário
//   //
// 	// 	$idusuario = $_GET['idusuario'];
//   //
// 	// 	$getCli=$pdo->prepare("SELECT * FROM enderecoUsuario
// 	// 	 						INNER JOIN usuario ON enderecoUsuario.idusuario = usuario.idusuario
// 	// 	 						WHERE enderecoUsuario.idusuario = :idusuario");
//   //
// 	// 	$getCli->bindValue(":idusuario", $idusuario);
// 	// 	$getCli->execute();
//   //
// 	// 	$return = array();
//   //
// 	// 	while ($linha=$getCli->fetch(PDO::FETCH_ASSOC)) {
//   //
// 	// 		$idusuario = $linha['idusuario'];
// 	// 		$nome = $linha['nome'];
// 	// 		$sobrenome = $linha['sobrenome'];
// 	// 		$logradouro = $linha['logradouro'];
// 	// 		$bairro = $linha['bairro'];
// 	// 		$cidade = $linha['cidade'];
// 	// 		$uf = $linha['uf'];
// 	// 		$email = $linha['email'];
//   //
// 	// 		$nomeCompleto = $nome." ".$sobrenome;
//   //
// 	// 		$return[] = array(
// 	// 			'idusuario' => $idusuario,
// 	// 			'nomeCompleto' => $nomeCompleto,
// 	// 			'logradouro' => $logradouro,
// 	// 			'bairro' => $bairro,
// 	// 			'cidade' => $cidade,
// 	// 			'uf' => $uf,
// 	// 			'email' => $email
// 	// 		);
//   //
// 	// 	}
//   //
// 	// 	//print_r($return);
// 	// 	echo json_encode($return);
// 	//
//   //
// 	// 	break;
//
// 	default:
// 		# code...
// 		break;
// }



?>
