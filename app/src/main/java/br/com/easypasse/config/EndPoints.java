package br.com.easypasse.config;

/**
 * Created by Lincoln on 13/01/18.
 */
public class EndPoints {

    public static final String URL_SITE = "http://easypasse.com.br/gestao/";
    public static final String URL_SITE_GPS = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/";
    public static final String URL_LOGIN = URL_SITE + "wsLogin.php";
    public static final String URL_CADASTRAR = URL_SITE + "wsCadastrar.php";
    public static final String URL_GENERO = URL_SITE + "wsGenero.php";
    public static final String URL_BLOQUEIO_CONTA = URL_SITE + "wsBloqueio.php";
    public static final String URL_FORMA_PAGAMENTO = URL_SITE + "wsFormaPagamento.php";
    public static final String URL_BANDEIRA_CARTAO = URL_SITE + "wsBandeiras.php";
    public static final String URL_BANCOS = URL_SITE + "wsBancos.php";
    public static final String URL_OBTER_POSICOES_ONIBUS = URL_SITE_GPS + "onibus";
}