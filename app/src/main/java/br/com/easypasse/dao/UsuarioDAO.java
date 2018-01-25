package br.com.easypasse.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.easypasse.model.UsuarioModelo;


/**
 * Created by Robson Dias on 19/01/2018.
 */
public class UsuarioDAO {
    public static final String NOME_TABELA = "USUARIO";

    private static final String[] ALL_FIELDS = new String[]{"ID", "CPF", "NOME", "EMAIL", "SENHA", "TELEFONE", "CIDADE", "SEXO", "LOGADO"};

    public UsuarioDAO() {
    }

    public void insert(UsuarioModelo usuario) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("ID", usuario.getId());
            values.put("CPF", usuario.getCpf());
            values.put("NOME", usuario.getNome());
            values.put("EMAIL", usuario.getEmail());
            values.put("SENHA", usuario.getSenha());
            values.put("CIDADE", usuario.getCidade());
            values.put("TELEFONE", usuario.getTelefone());
            values.put("SEXO", usuario.getSexo());
            values.put("LOGADO", usuario.getLogado());
            bd.insert(NOME_TABELA, null, values);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void update(UsuarioModelo usuario) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("ID", usuario.getId());
            values.put("CPF", usuario.getCpf());
            values.put("NOME", usuario.getNome());
            values.put("EMAIL", usuario.getEmail());
            values.put("SENHA", usuario.getSenha());
            values.put("CIDADE", usuario.getCidade());
            values.put("TELEFONE", usuario.getTelefone());
            values.put("SEXO", usuario.getSexo());
            values.put("LOGADO", usuario.getLogado());
            bd.update(NOME_TABELA, values, "ID = ?", new String[]{String.valueOf(usuario.getId())});
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public UsuarioModelo pesquisarUsuarioPeloId(int id) {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        UsuarioModelo usuario = null;
        try {
            bd.beginTransaction();
            Cursor cursor = bd.query(NOME_TABELA, ALL_FIELDS, "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                usuario = cursorParaUsuario(cursor);
                cursor.close();
                bd.setTransactionSuccessful();
            }
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public UsuarioModelo pesquisarUsuarioLogado() {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        UsuarioModelo usuario = null;
        try {
            bd.beginTransaction();
            Cursor cursor = bd.query(NOME_TABELA, ALL_FIELDS, "LOGADO = ?", new String[]{String.valueOf("1")}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                usuario = cursorParaUsuario(cursor);
                cursor.close();
                bd.setTransactionSuccessful();
            }
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void deletarUsuario(Integer id) throws Exception {
        SQLiteDatabase bd = DatabaseManager.getInstance().openDatabase();
        try {
            bd.beginTransaction();
            bd.delete(NOME_TABELA, "ID = " + id, null);
            bd.setTransactionSuccessful();
        } catch (Exception e) {
            throw new Exception();
        } finally {
            bd.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private UsuarioModelo cursorParaUsuario(Cursor cursor) {
        UsuarioModelo usuario = new UsuarioModelo();
        usuario.setId(cursor.getInt(0));
        usuario.setCpf(cursor.getString(1));
        usuario.setNome(cursor.getString(2));
        usuario.setEmail(cursor.getString(3));
        usuario.setSenha(cursor.getString(4));
        usuario.setCidade(cursor.getString(5));
        usuario.setTelefone(cursor.getString(6));
        usuario.setSexo(cursor.getString(7));
        usuario.setLogado(cursor.getString(8));
        return usuario;
    }
}
