-- phpMyAdmin SQL Dump
-- version 4.3.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 08-Dez-2017 às 10:45
-- Versão do servidor: 5.5.51-38.2
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `easyp957_easypassemobile`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `bandeirascartao`
--

CREATE TABLE IF NOT EXISTS `bandeirascartao` (
  `idFlag` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `strImagem` varchar(80) NOT NULL COMMENT 'nome da imagem',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo, 1 ativo. Uso interno.'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `bandeirascartao`
--

INSERT INTO `bandeirascartao` (`idFlag`, `strImagem`, `ativo`) VALUES
(1, 'americanexpress', 1),
(2, 'bb', 1),
(3, 'bradesco', 1),
(4, 'caixa', 1),
(5, 'cielo', 1),
(6, 'dinners', 1),
(7, 'itau', 1),
(8, 'mastercard', 1),
(9, 'santander', 1),
(10, 'unibanco', 1),
(11, 'visa', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `cartaopagamento`
--

CREATE TABLE IF NOT EXISTS `cartaopagamento` (
  `idCartaoPagamento` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `nometitular` varchar(80) NOT NULL COMMENT 'Nome do Titular no cartão. Obrigatório',
  `numero` varchar(16) NOT NULL COMMENT 'Número do cartão. Obrigatório.',
  `mes` char(2) NOT NULL COMMENT 'Nês de validade. Obrigatório.',
  `ano` char(4) NOT NULL COMMENT 'Ano de validade. Obrigatório.',
  `idFlag` bigint(20) NOT NULL COMMENT 'Id da Bandeira da administradora de cartão. Obrigatório.',
  `idUsuario` bigint(20) NOT NULL COMMENT 'Id da conta de usuário vinculada ao sistema. Obrigatório',
  `codigo` varchar(10) DEFAULT '' COMMENT 'Codigo do banco para cartão débito',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo, 1 ativo.\r\n\r\nFeito no cadastro de cartão para 1 como default e podendo ser desativado pelo usuário com perfil SUPER ADMINISTRADOR.'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `cartaopagamento`
--

INSERT INTO `cartaopagamento` (`idCartaoPagamento`, `nometitular`, `numero`, `mes`, `ano`, `idFlag`, `idUsuario`, `codigo`, `ativo`) VALUES
(1, 'Tarcisio Machado dos Reis', '4485063490500729', '12', '2018', 11, 1, '', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `formapagamento`
--

CREATE TABLE IF NOT EXISTS `formapagamento` (
  `idFormaPagamento` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `descricao` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Descrição da Forma de Pagamento.\r\n\r\n\r\nObrigatório.',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo, 1 ativo.\r\n\r\nFeito no cadastro de forma de pagamento para 1 como default e podendo ser desativado pelo usuário com perfil SUPER ADMINISTRADOR.'
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `formapagamento`
--

INSERT INTO `formapagamento` (`idFormaPagamento`, `descricao`, `ativo`) VALUES
(1, 'CARTÃO DE CRÉDITO', 1),
(2, 'CARTÃO DE DÉBITO', 1),
(3, 'BOLETO', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `genero`
--

CREATE TABLE IF NOT EXISTS `genero` (
  `idGenero` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `descricao` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Descrição do genero.\r\n\r\n\r\nObrigatório.'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `genero`
--

INSERT INTO `genero` (`idGenero`, `descricao`) VALUES
(2, 'FEMININO'),
(1, 'MASCULINO'),
(3, 'OUTROS');

-- --------------------------------------------------------

--
-- Estrutura da tabela `perfil`
--

CREATE TABLE IF NOT EXISTS `perfil` (
  `idPerfil` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `descricao` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Descrição do perfil.\r\n\r\n\r\nObrigatório.',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo, 1 ativo.\r\n\r\n\r\nTabela de uso interno, podendo bloquear todos os usuários do aplicativo. CUIDADO!!!!'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `perfil`
--

INSERT INTO `perfil` (`idPerfil`, `descricao`, `ativo`) VALUES
(1, 'SUPER ADMINISTRADOR', 1),
(2, 'ADMINISTRADOR', 1),
(3, 'CLIENTE', 1),
(4, 'USUARIO', 1),
(5, 'GESTOR', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` bigint(20) NOT NULL COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',
  `nome` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Nome completo.\r\n\r\nObrigatório.',
  `username` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Id de acesso do usuário.\r\n\r\nObrigatório.',
  `senha` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Senha do usuário.\r\n\r\nObrigatório.',
  `ddd` char(3) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'DDD.\r\n\r\nObrigatório.',
  `telefone` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Telefone.\r\n\r\nObrigatório.',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'E-mail.\r\n\r\nObrigatório.',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo, 1 ativo.\r\n\r\nFeito no cadastro do usuário para 1 como default e podendo ser desativado pelo usuário com perfil SUPER ADMINISTRADOR.',
  `logado` int(1) DEFAULT '0' COMMENT 'Logado - informa se usuario esta conectado.\r\n\r\n',
  `idPerfil` bigint(20) NOT NULL COMMENT 'Perfil de acesso do usuario.\r\n\r\n',
  `idUsuarioAdm` bigint(20) DEFAULT NULL COMMENT 'Administrador que fez cadastro.\r\n\r\n',
  `cpf` varchar(11) NOT NULL COMMENT 'CPF Obrigatório',
  `dtnascimento` date DEFAULT NULL COMMENT 'Data de nascimento. Não obrigatório.',
  `idGenero` bigint(20) NOT NULL COMMENT 'Chave estrangeira. Tabela Genero'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `nome`, `username`, `senha`, `ddd`, `telefone`, `email`, `ativo`, `logado`, `idPerfil`, `idUsuarioAdm`, `cpf`, `dtnascimento`, `idGenero`) VALUES
(1, 'TARCISIO MACHADO DOS REIS', 'tarcisio', '84cb70ea94171619811f664822aba099', '051', '98490-4355', 'comercial@mobiconsultoria.com', 1, 0, 5, 1, '67590926000', '1973-07-08', 1),
(2, 'SUPER ADMINISTRADOR', 'super', 'e10adc3949ba59abbe56e057f20f883e', '021', '99758-5523', 'ivan.junior@easypasse.com.br', 1, 0, 1, 1, '14845149710', '1991-07-03', 1),
(3, 'ADMINISTRADOR', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '011', '99999-9999', 'tarcisio.reis.ti@gmail.com', 1, 0, 2, 1, '22222222222', NULL, 1),
(4, 'CLIENTE', 'cliente', 'e10adc3949ba59abbe56e057f20f883e', '011', '98888-8888', 'tarcisio.reis.ti@gmail.com', 0, 0, 3, 1, '33333333333', NULL, 3),
(8, 'TESTE', 'teste', 'e10adc3949ba59abbe56e057f20f883e', '21', '995997407', 'ivansdasj@hotmail.com', 1, 0, 4, 2, '26928574615', '0000-00-00', 1),
(9, 'VLADIMIR', 'vladimir', '827ccb0eea8a706c4c34a16891f84e7b', '51', '84286513', 'vladimir.silva@easypasse.com.br', 1, 1, 2, 2, '18246125249', '0000-00-00', 1),
(10, 'RUBINEI', 'rubinei', '827ccb0eea8a706c4c34a16891f84e7b', '21', '98202-5269', 'rubinei.silva@easypasse.com.br', 1, 0, 2, 2, '89563456513', '0000-00-00', 1),
(11, 'GUSTAVO', 'gustavo', 'e10adc3949ba59abbe56e057f20f883e', '55', '8427-7470', 'gustavo.severo@easypasse.com.br', 1, 1, 2, 2, '91315786087', '1976-08-01', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuarioformapagamento`
--

CREATE TABLE IF NOT EXISTS `usuarioformapagamento` (
  `idUsuario` bigint(20) NOT NULL COMMENT 'Id da conta de usuário vinculada ao sistema. Obrigatório',
  `idFormaPagamento` bigint(20) NOT NULL COMMENT 'Id da forma de pagamento de uso do usuário. Obrigatório',
  `ativo` int(1) DEFAULT '1' COMMENT 'Ativo - sendo 0 inativo e 1 ativo.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `usuarioformapagamento`
--

INSERT INTO `usuarioformapagamento` (`idUsuario`, `idFormaPagamento`, `ativo`) VALUES
(1, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bandeirascartao`
--
ALTER TABLE `bandeirascartao`
  ADD PRIMARY KEY (`idFlag`);

--
-- Indexes for table `cartaopagamento`
--
ALTER TABLE `cartaopagamento`
  ADD PRIMARY KEY (`idCartaoPagamento`);

--
-- Indexes for table `formapagamento`
--
ALTER TABLE `formapagamento`
  ADD PRIMARY KEY (`idFormaPagamento`), ADD UNIQUE KEY `IX_FormaPagamento_Descricao` (`descricao`);

--
-- Indexes for table `genero`
--
ALTER TABLE `genero`
  ADD PRIMARY KEY (`idGenero`), ADD UNIQUE KEY `IX_Genero_Descricao` (`descricao`);

--
-- Indexes for table `perfil`
--
ALTER TABLE `perfil`
  ADD PRIMARY KEY (`idPerfil`), ADD KEY `IX_Perfil_Descricao` (`descricao`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`), ADD UNIQUE KEY `IX_Usuario_CPF` (`cpf`), ADD KEY `idPerfil` (`idPerfil`), ADD KEY `IX_Usuario_Nome` (`nome`), ADD KEY `IX_Usuario_UserName` (`username`), ADD KEY `IX_Usuario_Telefone` (`telefone`), ADD KEY `IX_Usuario_Email` (`email`), ADD KEY `Genero_cfk` (`idGenero`);

--
-- Indexes for table `usuarioformapagamento`
--
ALTER TABLE `usuarioformapagamento`
  ADD PRIMARY KEY (`idUsuario`), ADD KEY `FormaPagamentoFormaPagamento_cfk` (`idFormaPagamento`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bandeirascartao`
--
ALTER TABLE `bandeirascartao`
  MODIFY `idFlag` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `cartaopagamento`
--
ALTER TABLE `cartaopagamento`
  MODIFY `idCartaoPagamento` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `formapagamento`
--
ALTER TABLE `formapagamento`
  MODIFY `idFormaPagamento` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `genero`
--
ALTER TABLE `genero`
  MODIFY `idGenero` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `perfil`
--
ALTER TABLE `perfil`
  MODIFY `idPerfil` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Chave primária.\r\n\r\n\r\nObrigatório.',AUTO_INCREMENT=12;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `usuario`
--
ALTER TABLE `usuario`
ADD CONSTRAINT `Genero_cfk` FOREIGN KEY (`idGenero`) REFERENCES `genero` (`idGenero`) ON UPDATE CASCADE,
ADD CONSTRAINT `Usuario_cfk` FOREIGN KEY (`idPerfil`) REFERENCES `perfil` (`idPerfil`) ON UPDATE CASCADE;

--
-- Limitadores para a tabela `usuarioformapagamento`
--
ALTER TABLE `usuarioformapagamento`
ADD CONSTRAINT `FormaPagamentoFormaPagamento_cfk` FOREIGN KEY (`idFormaPagamento`) REFERENCES `formapagamento` (`idFormaPagamento`) ON UPDATE CASCADE,
ADD CONSTRAINT `UsuarioFormaPagamento_cfk` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
