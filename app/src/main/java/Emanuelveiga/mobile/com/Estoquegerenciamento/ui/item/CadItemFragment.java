package Emanuelveiga.mobile.com.Estoquegerenciamento.ui.item;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import Emanuelveiga.mobile.com.Estoquegerenciamento.model.Produto;
import Emanuelveiga.mobile.com.Estoquegerenciamento.R; // Assuming R class exists in your project

/**
 * A simple {@link Fragment} subclass for adding a new product.
 * Use the {@link CadItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadItemFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View view;

    private EditText etNomeProduto;
    private EditText etFabricanteProduto;
    private EditText etNumeroSerieProduto;
    private EditText etQuantidadeProduto;
    private EditText etCategoriaProduto; // Changed to EditText
    private Button btCadastrarProduto;
    private CheckBox cbFragilProduto;
    //volley
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectReq;

    public CadItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadProdutoFragment.
     */
    public static CadItemFragment newInstance(String param1, String param2) {
        CadItemFragment fragment = new CadItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_cad_item, container, false); // Assuming you have a layout named fragment_cad_produto
        // Binding
        this.etNomeProduto = view.findViewById(R.id.etNome); // Replace with your actual IDs
        this.etFabricanteProduto = view.findViewById(R.id.etFabricante);
        this.etNumeroSerieProduto = view.findViewById(R.id.etNumero_serie);
        this.etQuantidadeProduto = view.findViewById(R.id.etQuantidade);
        this.etCategoriaProduto = view.findViewById(R.id.etCategoria); // Now an EditText
        this.btCadastrarProduto = view.findViewById(R.id.btCadastrar);
        this.btCadastrarProduto.setOnClickListener(this);
        this.cbFragilProduto = view.findViewById(R.id.btfragil);

        //instanciando a fila de requests - caso o objeto seja o view
        this.requestQueue = Volley.newRequestQueue(view.getContext());
        //inicializando a fila de requests do SO
        this.requestQueue.start();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btCadastrar) {
            try {
                // Instanciar o objeto de negócio (Produto neste caso)
                Produto produto = new Produto();

                // Pegar dados da tela e definir nos atributos do objeto Produto
                produto.setNome(this.etNomeProduto.getText().toString());
                produto.setFabricante(this.etFabricanteProduto.getText().toString());

                String numeroSerieStr = this.etNumeroSerieProduto.getText().toString();
                if (!numeroSerieStr.isEmpty()) {
                    produto.setNumerodeserie(Integer.parseInt(numeroSerieStr));
                } else {
                    produto.setNumerodeserie(0); // Ou defina um valor padrão
                }

                String quantidadeStr = this.etQuantidadeProduto.getText().toString();
                if (!quantidadeStr.isEmpty()) {
                    produto.setQuantidade(Integer.parseInt(quantidadeStr));
                } else {
                    produto.setQuantidade(0); // Ou defina um valor padrão
                }
                produto.setFragil(this.cbFragilProduto.isChecked());
                produto.setCategoria(this.etCategoriaProduto.getText().toString()); // Get text from EditText
                produto.setFragil(false); // Você pode precisar de uma UI para isso

                // Aqui você faria a lógica para salvar o produto (ex: banco de dados, API)
                // Exemplo de mensagem de sucesso:

                //REQUEST VOLLEY AQUI !!!!!!!
                JSONObject jsonParams = produto.toJsonObject(); // Assuming toJsonObject() is correctly implemented in Produto
                jsonObjectReq = new JsonObjectRequest(
                        Request.Method.POST,
                        "http://10.0.2.2/estoquegerenciamento/cadproduto.php", // Check your API endpoint
                        jsonParams, this, this);
                requestQueue.add(jsonObjectReq);

                Toast.makeText(view.getContext(), "Produto cadastrado com sucesso!", Toast.LENGTH_LONG).show();

            } catch (NumberFormatException e) {
                Toast.makeText(view.getContext(), "Erro: Verifique os campos numéricos.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(view.getContext(), "Erro ao cadastrar o produto.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Snackbar mensagem = Snackbar.make(view,
                "Ops! Houve um problema ao realizar o cadastro: " +
                        error.toString(),Snackbar.LENGTH_LONG);
        mensagem.show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            //instanciando objeto para manejar o JSON que recebemos
            JSONObject json = new JSONObject(response.toString());
            //context e text são para a mensagem na tela o Toast
            Context context = view.getContext();
            //pegando mensagem que veio do json
            CharSequence mensagem = json.getString("message");
            //duração da mensagem na tela
            int duration = Toast.LENGTH_SHORT;
            //verificando se salvou sem erro para limpar campos da tela
            if (json.getBoolean("success")){
                //limpar campos da tela
                this.etNomeProduto.setText("");
                this.etFabricanteProduto.setText("");
                this.etQuantidadeProduto.setText("");
                this.etNumeroSerieProduto.setText("");
                this.etCategoriaProduto.setText(""); // Clear the EditText
                cbFragilProduto.setChecked(false);
                //selecionando primeiro item dos spinners (no longer applicable)
            }
            //mostrando a mensagem que veio do JSON
            Toast toast = Toast.makeText (context, mensagem, duration);
            toast.show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}