package br.com.easypasse.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.easypasse.model.FormaPagamentoModelo;


/**
 * Created by Robson Dias on 19/01/2018.
 */
public class FormaPagamentoDAO {
    public static final String NOME_TABELA = "FORMA_PAGAMENTO";

    private static final String[] ALL_FIELDS = new String[]{"ID", "DESCRICAO", "ATIVO", "USUARIO"};

    public FormaPagamentoDAO() {
    }

    public void insert(FormaPagamentoModelo formaPagamentoModelo) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("ID", formaPagamentoModelo.getId());
            values.put("DESCRICAO", formaPagamentoModelo.getDescricao());
            values.put("ATIVO", formaPagamentoModelo.getAtivo());
            values.put("USUARIO", formaPagamentoModelo.getUsuario());
            bd.insert(NOME_TABELA, null, values);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public FormaPagamentoModelo pesquisarFormaPagamentoPeloUsuario(int usuario) {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        FormaPagamentoModelo formaPagamentoModelo = null;
        try {
            bd.beginTransaction();
            Cursor cursor = bd.query(NOME_TABELA, ALL_FIELDS, "USUARIO = ?", new String[]{String.valueOf(usuario)}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                formaPagamentoModelo = cursorParaFormaPagamento(cursor);
                cursor.close();
                bd.setTransactionSuccessful();
            }
            return formaPagamentoModelo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void deletarFormaPagamento(Integer usuario) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            bd.delete(NOME_TABELA, "USUARIO = " + usuario, null);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception();
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private FormaPagamentoModelo cursorParaFormaPagamento(Cursor cursor) {
        FormaPagamentoModelo formaPagamentoModelo = new FormaPagamentoModelo();
        formaPagamentoModelo.setId(cursor.getInt(0));
        formaPagamentoModelo.setDescricao(cursor.getString(1));
        formaPagamentoModelo.setAtivo(cursor.getString(2));
        formaPagamentoModelo.setUsuario(cursor.getInt(3));
        return formaPagamentoModelo;
    }
}
