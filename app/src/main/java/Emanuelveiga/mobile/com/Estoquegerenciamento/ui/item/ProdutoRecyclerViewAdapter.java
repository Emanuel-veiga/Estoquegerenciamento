package Emanuelveiga.mobile.com.Estoquegerenciamento.ui.item;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import Emanuelveiga.mobile.com.Estoquegerenciamento.databinding.FragmentConProdutoBinding;
import Emanuelveiga.mobile.com.Estoquegerenciamento.model.Produto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Produto}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProdutoRecyclerViewAdapter extends RecyclerView.Adapter<ProdutoRecyclerViewAdapter.ViewHolder> {

    private final List<Produto> mValues;

    public ProdutoRecyclerViewAdapter(List<Produto> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentConProdutoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int qtd = mValues.get(position).getQuantidade();
        holder.mIdView.setText(String.valueOf(qtd));
        holder.mContentView.setText(mValues.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Produto mItem;

        public ViewHolder(FragmentConProdutoBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}