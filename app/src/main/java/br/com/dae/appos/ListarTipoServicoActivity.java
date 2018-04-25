package br.com.dae.appos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import br.com.dae.appos.Util.TipoMsg;
import br.com.dae.appos.Util.Util;
import br.com.dae.appos.dao.TipoServicoDAO;
import br.com.dae.appos.entidade.TipoServico;

public class ListarTipoServicoActivity extends AppCompatActivity {

    private Button btnCadastrarTipoServico;
    private ListView listaViewTipoServico;
    private List<TipoServico> listaTipoServicos;
    private TipoServicoDAO tipoServicoDAO;
    private ArrayList<TipoServico> listViewTipoServico;
    private TipoServico tipoServico;
    private ArrayAdapter adapter;
    private int posicaoSelecionada;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_tipo_servico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.search_view);

        getSupportActionBar().setTitle("Lista Tipo Serviço");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tipoServicoDAO = new TipoServicoDAO(this);

        listaViewTipoServico = (ListView) findViewById(R.id.listViewTipoServico);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        setArrayAdapterTipoServico();

        listaViewTipoServico.setOnItemClickListener(clickListenerTipoServico);
        listaViewTipoServico.setOnCreateContextMenuListener(contextMenuListener);
        listaViewTipoServico.setOnItemLongClickListener(longClickListener);

        btnCadastrarTipoServico = (Button) findViewById(R.id.btnCadastrarTipoServico);

        btnCadastrarTipoServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarTipoServicoActivity.this, CadastroTipoServicoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTipoServico();
    }

    public void carregarTipoServico() {
        tipoServicoDAO = new TipoServicoDAO(ListarTipoServicoActivity.this);
        listViewTipoServico = tipoServicoDAO.getLista();
        tipoServicoDAO.close();

        if (listViewTipoServico != null) {
            adapter = new ArrayAdapter<TipoServico>(ListarTipoServicoActivity.this, android.R.layout.simple_list_item_1, listViewTipoServico);
            listaViewTipoServico.setAdapter(adapter);
        }
    }

    private void setArrayAdapterTipoServico() {
        listaTipoServicos = tipoServicoDAO.getLista();

        List<String> valores = new ArrayList<String>();
        for (TipoServico ts : listaTipoServicos) {
            valores.add(ts.getNome());
        }
        adapter.clear();
        adapter.addAll(valores);
        listaViewTipoServico.setAdapter(adapter);
    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            posicaoSelecionada = position;
            return false;
        }
    };

    private View.OnCreateContextMenuListener contextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opções").setHeaderIcon(R.drawable.edit).add(1, 10, 1, "Editar");
            menu.add(1, 20, 2, "Deletar");
        }
    };

    private AdapterView.OnItemClickListener clickListenerTipoServico = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TipoServico tipoServico = tipoServicoDAO.consultarTipoServicoPorId(listaTipoServicos.get(position).getId());
            StringBuilder info = new StringBuilder();
            info.append("Nome: " + tipoServico.getNome());
            info.append("\nDescrição: " + tipoServico.getDescricao());
            Util.showMsgAlertOK(ListarTipoServicoActivity.this, "Info", info.toString(), TipoMsg.INFO);
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 10:
                TipoServico tipoServico = tipoServicoDAO.consultarTipoServicoPorId(listaTipoServicos.get(posicaoSelecionada).getId());
                Intent i = new Intent(this, CadastroTipoServicoActivity.class);
                i.putExtra("tipo_servico-escolhido", tipoServico);
                startActivity(i);
                finish();
                break;
            case 20:
                Util.showMsgConfirm(ListarTipoServicoActivity.this, "Remover Tipo Serviço", "Deseja remover realmente o(a) " + listaTipoServicos.get(posicaoSelecionada).getNome() + "?",
                        TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // int id = listaTipoServicos.get(posicaoSelecionada).getId();
                                tipoServicoDAO.deletarTipoServico(listaTipoServicos.get(posicaoSelecionada));
                                setArrayAdapterTipoServico();
                                adapter.notifyDataSetChanged();
                                Util.showMsgToast(ListarTipoServicoActivity.this, "Tipo Serviço deletada com sucesso!");
                            }
                        });
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                //Do some magic
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuPesquisa) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuSair:
                finish();
                break;
        }
        return true;
    }
}
