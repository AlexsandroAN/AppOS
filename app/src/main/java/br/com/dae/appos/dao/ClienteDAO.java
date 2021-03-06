package br.com.dae.appos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.dae.appos.entidade.Cliente;
import br.com.dae.appos.Util.Constantes;

/**
 * Created by 39091 on 23/04/2018.
 */

public class ClienteDAO extends SQLiteOpenHelper {

//    private static final String DATABASE = "bd_appOS";
//    private static final int VERSION = 1;


    public ClienteDAO(Context context) {
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE tb_cliente (");
        query.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        query.append(" nome TEXT(50) NOT NULL,");
        query.append(" descricao TEXT(100),");
        query.append(" endereco TEXT(100),");
        query.append(" email TEXT(20),");
        query.append(" telefone TEXT(20))");

        db.execSQL(query.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String cliente = "DROP TABLE IF EXISTS tb_cliente";
        db.execSQL(cliente);
    }

    // Método para salvar cliente
    public void salvarCliente(Cliente cliente) {
        ContentValues values = new ContentValues();

        values.put("nome", cliente.getNome());
        values.put("descricao", cliente.getDescricao());
        values.put("endereco", cliente.getEndereco());
        values.put("email", cliente.getEmail());
        values.put("telefone", cliente.getTelefone());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tb_cliente", null, values);
    }

    public void alterarCliente(Cliente cliente) {
        ContentValues values = new ContentValues();

        values.put("nome", cliente.getNome());
        values.put("descricao", cliente.getDescricao());
        values.put("endereco", cliente.getEndereco());
        values.put("email", cliente.getEmail());
        values.put("telefone", cliente.getTelefone());

        getWritableDatabase().update("tb_cliente", values, "id = ?", new String[]{String.valueOf(cliente.getId())});
    }

    public void deletarCliente(Cliente cliente) {
        getWritableDatabase().delete("tb_cliente", "id = ?", new String[]{String.valueOf(cliente.getId())});
    }

    // Método para listar todos os clientes
    public ArrayList<Cliente> getLista() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_cliente", null, null, null, null, null, null);

        ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();

        while (cursor.moveToNext()) {
            Cliente cliente = new Cliente();
            setPessoaFromCursor(cursor, cliente);
            listaClientes.add(cliente);
        }

        return listaClientes;
    }

    private void setPessoaFromCursor(Cursor cursor, Cliente cliente) {
        cliente.setId(cursor.getInt(cursor.getColumnIndex("id")));
        cliente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        cliente.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        cliente.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
        cliente.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        cliente.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
    }
}
