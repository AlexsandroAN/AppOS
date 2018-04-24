package br.com.dae.appos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.dae.appos.dao.ClienteDAO;
import br.com.dae.appos.entidade.Cliente;

public class CadastroClienteActivity extends AppCompatActivity {

    private EditText edtNome, edtDescricao, edtEndereco, edtEmail, edtTelefone;
    private Button btnPoliform;
    private Cliente editarCliente, cliente;
    private ClienteDAO clienteBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Cadastrar Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cliente = new Cliente();
        clienteBO = new ClienteDAO(CadastroClienteActivity.this);

        Intent intent = getIntent();
        editarCliente = (Cliente) intent.getSerializableExtra("cliente-escolhido");

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);

        btnPoliform = (Button) findViewById(R.id.btn_Poliform);


        if (editarCliente != null){
            btnPoliform.setText("Modificar Produto");
        } else {
            btnPoliform.setText("Cadastrar Novo Cliente");
        }

        btnPoliform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.setNome(edtNome.getText().toString());
                cliente.setDescricao(edtDescricao.getText().toString());
                cliente.setEndereco(edtEndereco.getText().toString());
                cliente.setEmail(edtEmail.getText().toString());
                cliente.setTelefone(edtTelefone.getText().toString());

                if(btnPoliform.getText().toString().equals("Cadastrar Novo Cliente")){
                    clienteBO.salvarCliente(cliente);
                    clienteBO.close();
                } else {
                    clienteBO.close();
                    clienteBO.alterarCliente(cliente);
                }
            }
        });
    }
}
