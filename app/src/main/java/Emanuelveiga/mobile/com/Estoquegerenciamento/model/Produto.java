package Emanuelveiga.mobile.com.Estoquegerenciamento.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Produto {
    //atributos
    private String nome;
    private String fabricante;
    private String categoria;
    private int numerodeserie;
    private int quantidade ;
    private boolean fragil;
    //métodos
    public String getNome(){
        return this.nome;
    }

    public void setNome(String nm){
        if(!nm.isEmpty())  {
            this.nome = nm;
        }else {
            this.nome = "nome";
        }
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getNumerodeserie() {
        return numerodeserie;
    }

    public void setNumerodeserie(int numerodeserie) {
        if(numerodeserie > 0) {
            this.numerodeserie = numerodeserie;
        } else{
            this.numerodeserie = 000000000000;
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isFragil() {
        return fragil;
    }

    public void setFragil(boolean fragil) {
        this.fragil = fragil;
    }





    public Produto (JSONObject jp) {
        try {
            this.setCategoria(jp.getString("categoria"));
            this.setNome(jp.getString("nome"));
            this.setFabricante(jp.getString("fabricante"));
            this.setNumerodeserie(jp.getInt(String.valueOf(000000000000)));
            this.setFragil(jp.getBoolean("fragil"));
            this.setQuantidade(jp.getInt(String.valueOf(0)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //CONSTRUTOR - Inicializa os atributos para gerar Objeto Json
    public Produto() {
        this.setCategoria("categoria");
        this.setNome("nome");
        this.setNumerodeserie(0);
        this.setFabricante("fabricante");
        this.setFragil(false);
        this.setQuantidade(0);
    }
    //Metodo retorna o objeto com dados no formato JSON
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("quantidade", this.quantidade);
            json.put("nome", this.nome);
            json.put("fabricante", this.fabricante);
            json.put("fragil", this.fragil);
            json.put("numero de serie", this.numerodeserie);
            json.put("categoria", this.categoria);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
