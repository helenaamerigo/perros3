package edu.ub.pis2324.xoping.presentation.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ub.pis2324.xoping.R;
import edu.ub.pis2324.xoping.domain.model.valueobjects.PricedLineItem;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartLineItemViewHolder> {

  private List<PricedLineItem<ProductPO>> pricedLineItems;
  private OnRemoveItemListener onRemoveItemListener;

  public interface OnRemoveItemListener {
    void onRemoveItem(PricedLineItem<ProductPO> position);
  }

  public CartRecyclerViewAdapter(
      OnRemoveItemListener onRemoveItemListener
  ) {
    super();
    this.onRemoveItemListener = onRemoveItemListener;
  }

  @SuppressLint("NotifyDataSetChanged")
  public void setData(List<PricedLineItem<ProductPO>> lineItemModels) {
    this.pricedLineItems = lineItemModels;
    notifyDataSetChanged();
  }

  private void removeItem(int position) {
    PricedLineItem<ProductPO> cartLine = pricedLineItems.remove(position);
    notifyItemRemoved(position);
    onRemoveItemListener.onRemoveItem(cartLine);
  }

  @NonNull
  @Override
  public CartLineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_rv_cartlineitem, parent, false);

    return new CartLineItemViewHolder(view, position -> removeItem(position));
  }

  @Override
  public void onBindViewHolder(@NonNull CartLineItemViewHolder holder, int position) {
    PricedLineItem<ProductPO> pricedLineItem = pricedLineItems.get(position);
    ProductPO productPO = pricedLineItem.getItem();
    Integer quantity = pricedLineItem.getQuantity();
    String subtotalPrice = pricedLineItem.getSubtotalPrice().toString();

    holder.tvCartLineProductName.setText(productPO.getName());
    holder.tvCartLineProductQuantity.setText(quantity.toString());
    holder.tvCartLineSubtotalPrice.setText(subtotalPrice);
    Picasso.get().load(productPO.getImageUrl()).into(holder.ivCartLineProductImage);
  }

  @Override
  public int getItemCount() {
    return (pricedLineItems == null) ? 0 : pricedLineItems.size();
  }

  public static class CartLineItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvCartLineProductName;
    private final TextView tvCartLineProductQuantity;
    private final TextView tvCartLineSubtotalPrice;
    private final ImageView ivCartLineProductImage;
    private final Button btnRemoveFromCart;

    public interface OnRemoveClickPositionListener {
      void onRemoveClickPosition(int position);
    }

    public CartLineItemViewHolder(
        @NonNull View itemView,
        OnRemoveClickPositionListener listener
    ) {
      super(itemView);
      tvCartLineProductName = itemView.findViewById(R.id.tvCartLineProductName);
      tvCartLineProductQuantity = itemView.findViewById(R.id.tvCartLineProductQuantity);
      tvCartLineSubtotalPrice = itemView.findViewById(R.id.tvCartLineSubtotalPrice);
      ivCartLineProductImage = itemView.findViewById(R.id.ivCartProduct);
      btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);

      btnRemoveFromCart.setOnClickListener(v -> listener.onRemoveClickPosition(getAdapterPosition()));
    }
  }
}
