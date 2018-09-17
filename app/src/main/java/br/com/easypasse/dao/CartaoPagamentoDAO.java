package br.com.easypasse.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.easypasse.model.CartaoPagamentoModelo;


/**
 * Created by Robson Dias on 19/01/2018.
 */
public class CartaoPagamentoDAO {
    public static final String NOME_TABELA = "CARTAO_PAGAMENTO";

    private static final String[] ALL_FIELDS = new String[]{"ID", "NOMETITULAR", "NUMERO", "MES", "ANO", "IDFLAG", "IDUSUARIO", "CODIGOBANCO", "ATIVO", "CPFTITULAR"};

    public CartaoPagamentoDAO() {
    }

    public void insert(CartaoPagamentoModelo cartaoPagamentoModelo) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("ID", cartaoPagamentoModelo.getId());
            values.put("NOMETITULAR", cartaoPagamentoModelo.getNomeTitular());
            values.put("NUMERO", cartaoPagamentoModelo.getNumero());
            values.put("MES", cartaoPagamentoModelo.getMes());
            values.put("ANO", cartaoPagamentoModelo.getAno());
            values.put("IDFLAG", cartaoPagamentoModelo.getIdFlag());
            values.put("IDUSUARIO", cartaoPagamentoModelo.getIdUsuario());
            values.put("CODIGOBANCO", cartaoPagamentoModelo.getCodigoBanco());
            values.put("ATIVO", cartaoPagamentoModelo.getAtivo());
            values.put("CPFTITULAR", cartaoPagamentoModelo.getCpf());
            bd.insert(NOME_TABELA, null, values);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public CartaoPagamentoModelo pesquisarCartaoPeloUsuario(int usuario) {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        CartaoPagamentoModelo cartaoPagamentoModelo = null;
        try {
            bd.beginTransaction();
            Cursor cursor = bd.query(NOME_TABELA, ALL_FIELDS, "IDUSUARIO = ?", new String[]{String.valueOf(usuario)}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cartaoPagamentoModelo = cursorParaCartaoPagamento(cursor);
                cursor.close();
                bd.setTransactionSuccessful();
            }
            return cartaoPagamentoModelo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void deletarCartao(Integer usuario) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            bd.delete(NOME_TABELA, "IDUSUARIO = " + usuario, null);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception();
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private CartaoPagamentoModelo cursorParaCartaoPagamento(Cursor cursor) {
        CartaoPagamentoModelo cartaoPagamentoModelo = new CartaoPagamentoModelo();
        cartaoPagamentoModelo.setId(cursor.getInt(0));
        cartaoPagamentoModelo.setNomeTitular(cursor.getString(1));
        cartaoPagamentoModelo.setNumero(cursor.getString(2));
        cartaoPagamentoModelo.setMes(cursor.getString(3));
        cartaoPagamentoModelo.setAno(cursor.getString(4));
        cartaoPagamentoModelo.setIdFlag(cursor.getInt(5));
        cartaoPagamentoModelo.setIdUsuario(cursor.getInt(6));
        cartaoPagamentoModelo.setCodigoBanco(cursor.getInt(7));
        cartaoPagamentoModelo.setAtivo(cursor.getString(8));
        cartaoPagamentoModelo.setCpf(cursor.getString(9));
        return cartaoPagamentoModelo;
    }
}
