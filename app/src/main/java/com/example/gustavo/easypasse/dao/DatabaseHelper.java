package com.example.gustavo.easypasse.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EASYPASSE";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(Tabelas.CRIAR_USUARIO);
            Log.w(DatabaseHelper.class.getName(), "TABELAS CRIADAS COM SUCESSO");
        } catch (Exception e) {
            Log.w(DatabaseHelper.class.getName(), e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try {
            database.execSQL(Tabelas.DELETAR_USUARIO);
            Log.w(DatabaseHelper.class.getName(), "TABELAS DELETADAS COM SUCESSO");
            onCreate(database);

        } catch (Exception e) {
            Log.w(DatabaseHelper.class.getName(), e.getMessage());
        }
    }

    private void carregarDadosIniciais(SQLiteDatabase bd) {
        try {

        } catch (Exception e) {
            Log.w(DatabaseHelper.class.getName(), e.getMessage());
        }
    }
}
