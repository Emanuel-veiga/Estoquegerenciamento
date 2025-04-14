package Emanuelveiga.mobile.com.Estoquegerenciamento.ui.item;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Emanuelveiga.mobile.com.Estoquegerenciamento.R;
import Emanuelveiga.mobile.com.Estoquegerenciamento.model.Produto;

/**
 * A fragment representing a list of Items.
 */
public class ConProdutoFragment extends Fragment implements
        Response.ErrorListener, Response.Listener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    //atributo com lista de usuarios
    private ArrayList<Produto> produtos;
    //volley
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayReq;
    //passar a view como atributo da classe e não do método
    private View view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConProdutoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConProdutoFragment newInstance(int columnCount) {
        ConProdutoFragment fragment = new ConProdutoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_con_produto_list, container, false);

        //instanciando a fila de requests - caso o objeto seja o view
        this.requestQueue = Volley.newRequestQueue(view.getContext());
        //inicializando a fila de requests do SO
        this.requestQueue.start();
        // Set the adapter
        //array parâmetro de envio para o serviço
        JSONArray jsonArray = new JSONArray();
        //objeto com informações de filtro da consulta
        Produto produto = new Produto();
        produto.setNumerodeserie(0);
        //incluindo objeto no array de envio
        jsonArray.put(produto.toJsonObject());
        //requisição para o Rest Server SEMPRE POST
        jsonArrayReq = new JsonArrayRequest(Request.Method.POST,
                "http://10.0.2.2:8080/seg/conusuario.php",
                jsonArray, this, this);
        //mando executar a requisção na fila do sistema
        requestQueue.add(jsonArrayReq);
        return this.view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //mostrar mensagem que veio do servidor
        Snackbar mensagem = Snackbar.make(view,
                "Ops! Houve um problema ao realizar a consulta: " +
                        error.toString(), Snackbar.LENGTH_LONG);
        mensagem.show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            //array Json para receber a resposta do webservice
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(response.toString());
            //se a consulta não veio vazia passar para array list
            if (jsonArray != null) {
                Produto produto = null;
                //array list para receber a resposta
                this.produtos = new ArrayList<Produto>();
                //preenchendo ArrayList com JSONArray recebido
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    produto = new Produto(jo);
                    this.produtos.add(produto);
                }
                    /*
                    O código abaixo já estava no método onCreateView().
                    Mas foi movido para cá, porque só pode ser
                    executado se o array list não estiver vazio.
                    Ou seja, se NÃO retornar dados da consulta,
                    ele não deve ser executado;
                    */
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView =
                            (RecyclerView) view;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(
                                new GridLayoutManager(context,
                                        mColumnCount));
                    }
                    recyclerView.setAdapter(
                            new ProdutoRecyclerViewAdapter(this.produtos));
                }
            }else {
                Snackbar mensagem = Snackbar.make(view,
                        "A consulta não retornou nenhum registro!",
                        Snackbar.LENGTH_LONG);
                mensagem.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}