package br.com.dae.appos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.dae.appos.dao.ClienteDAO;
import br.com.dae.appos.entidade.Cliente;

public class ListarClientesActivity extends AppCompatActivity {

    private Button btnCadastrarCliente;
    private ListView lista;
    private ClienteDAO clienteBO;
    private ArrayList<Cliente> listViewClientes;
    private Cliente cliente;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lista Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lista = (ListView) findViewById(R.id.listViewClientes);


        btnCadastrarCliente = (Button) findViewById(R.id.btnCadastrarCliente);

        btnCadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarClientesActivity.this, CadastroClienteActivity.class);
                startActivity(intent);
                // finish();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        carregarClientes();
    }

    public void carregarClientes() {
        clienteBO = new ClienteDAO(ListarClientesActivity.this);
        listViewClientes = clienteBO.getLista();
        clienteBO.close();

        if (listViewClientes != null) {
            adapter = new ArrayAdapter<Cliente>(ListarClientesActivity.this, android.R.layout.simple_list_item_1, listViewClientes);
            lista.setAdapter(adapter);
        }
    }

}
