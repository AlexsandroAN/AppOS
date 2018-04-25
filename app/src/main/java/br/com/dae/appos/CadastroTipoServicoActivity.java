package br.com.dae.appos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.dae.appos.Util.Util;
import br.com.dae.appos.dao.TipoServicoDAO;
import br.com.dae.appos.entidade.TipoServico;

public class CadastroTipoServicoActivity extends AppCompatActivity {

    private EditText edtNome, edtDescricao;
    private Button btnPoliform;
    private TipoServico editarTipoServico, tipoServico;
    private TipoServicoDAO tipoServicoDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo_servico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setTitle("Cadastrar Tipo Serviço");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tipoServico = new TipoServico();
        tipoServicoDAO = new TipoServicoDAO(CadastroTipoServicoActivity.this);

        Intent intent = getIntent();
        editarTipoServico = (TipoServico) intent.getSerializableExtra("tipo_servico-escolhido");

        edtNome = (EditText) findViewById(R.id.edtNomeTipoServico);
        edtDescricao = (EditText) findViewById(R.id.edtDescricaoTipoServico);

        btnPoliform = (Button) findViewById(R.id.btn_Poliform_TipoServico);


        if (editarTipoServico != null) {
            getSupportActionBar().setTitle("Alterar Tipo Serviço");
            btnPoliform.setText("Alterar Tipo Serviço");

            edtNome.setText(editarTipoServico.getNome());
            edtDescricao.setText(editarTipoServico.getDescricao());

            tipoServico.setId(editarTipoServico.getId());
        } else {
            getSupportActionBar().setTitle("Cadastrar Tipo Serviço");
            btnPoliform.setText("Cadastrar novo Tipo Serviço");
        }

        btnPoliform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoServico.setNome(edtNome.getText().toString());
                tipoServico.setDescricao(edtDescricao.getText().toString());

                if (!validarTipoServico(tipoServico)) {
                    if (btnPoliform.getText().toString().equals("Cadastrar novo Tipo Serviço")) {
                        tipoServicoDAO.salvarTipoServico(tipoServico);
                        tipoServicoDAO.close();
                        Util.showMsgToast(CadastroTipoServicoActivity.this, "Tipo de Serviço salvo com sucesso!");
                    } else {
                        tipoServicoDAO.alterarTipoServico(tipoServico);
                        tipoServicoDAO.close();
                        Util.showMsgToast(CadastroTipoServicoActivity.this, "Tipo de Serviço modificado com sucesso!");
                    }
                    Intent i = new Intent(CadastroTipoServicoActivity.this, ListarTipoServicoActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private boolean validarTipoServico(TipoServico tipoServico) {
        boolean erro = false;
        if (tipoServico.getNome() == null || "".equals(tipoServico.getNome())) {
            erro = true;
            edtNome.setError("Campo Nome é obrigatório!");
        }
//        if (tipoServico.getDescricao() == null || "".equals(tipoServico.getDescricao())) {
//            erro = true;
//            edtDescricao.setError("Campo Descrição é obrigatório!");
//        }
        return erro;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(CadastroTipoServicoActivity.this, ListarTipoServicoActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
