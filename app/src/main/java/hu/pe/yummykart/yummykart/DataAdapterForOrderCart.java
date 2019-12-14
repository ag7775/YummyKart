package hu.pe.yummykart.yummykart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;
import static hu.pe.yummykart.yummykart.CartInitialisation.cartInitialisationArrayList;

public class DataAdapterForOrderCart extends RecyclerView.Adapter<DataAdapterForOrderCart.CartViewHolder>
{

    Context context;
    ArrayList<MenuInitialisation> cartInitialisationArrayList;
    RecyclerView recyclerView;
    TextView tv_cart_qty,tv_cart_amt,tv_clear_cart,tv_GST,tv_gst_amount,tv_amount,tv_discount,tv_discount_amount,tv_to_pay;
    int DISCOUNT,GST;
    public DataAdapterForOrderCart(Context context, ArrayList<MenuInitialisation> cartInitialisationArrayList,RecyclerView recyclerView,TextView tv_cart_qty,TextView tv_cart_amt,TextView tv_clear_cart,TextView tv_GST,TextView tv_gst_amount,TextView tv_amount,TextView tv_discount,TextView tv_discount_amount,TextView tv_to_pay,int DISCOUNT, int GST)
    {
        this.cartInitialisationArrayList = cartInitialisationArrayList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.tv_cart_qty = tv_cart_qty;
        this.tv_cart_amt = tv_cart_amt;
        this.tv_clear_cart = tv_clear_cart;
        this.tv_GST = tv_GST;
        this.tv_gst_amount = tv_gst_amount;
        this.tv_amount = tv_amount;

        this.tv_discount = tv_discount;
        this.tv_discount_amount = tv_discount_amount;
        this.tv_to_pay = tv_to_pay;
        this.DISCOUNT = DISCOUNT;
        this.GST = GST;
    }

    @Override
    public DataAdapterForOrderCart.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart,parent,false);
        DataAdapterForOrderCart.CartViewHolder vh = new DataAdapterForOrderCart.CartViewHolder(v);

        tv_clear_cart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cartInitialisationArrayList.clear();
                if(CartInitialisation.cartInitialisationArrayList.size()==0)
                {
                    Toast.makeText(context,"Your Cart is empty!",Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final DataAdapterForOrderCart.CartViewHolder holder, final int position)
    {
        holder.tv_item.setText(CartInitialisation.cartInitialisationArrayList.get(position).getItem());
        holder.tv_price.setText("₹ "+String.valueOf(CartInitialisation.cartInitialisationArrayList.get(position).getPrice())+"/-");
        holder.tv_total.setText("Total: ₹ "+String.valueOf(CartInitialisation.cartInitialisationArrayList.get(position).getAmount())+"/-");
        holder.qty_btw_buttons.setText(String.valueOf(CartInitialisation.cartInitialisationArrayList.get(position).getQuantity()));
        if(CartInitialisation.cartInitialisationArrayList.get(position).getType().equals("VEG"))
        {
            holder.typelogo.setImageResource(R.drawable.veglogo);
        }
        else
        {
            holder.typelogo.setImageResource(R.drawable.nonveglogo);
        }
        if(cartInitialisationArrayList.get(position).getDetail()!=0)
        {
            holder.item_detail.setVisibility(View.VISIBLE);
            holder.item_detail.setText("@ "+String.valueOf(cartInitialisationArrayList.get(position).getDetail())+" pcs.");
        }
        else if(cartInitialisationArrayList.get(position).getDetail()==0)
        {
            holder.item_detail.setVisibility(View.INVISIBLE);
        }


        holder.buttoninc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (view.getId() == R.id.buttoninc)
                {
                    holder.buttondec.setVisibility(View.VISIBLE);
                    MenuInitialisation menuInitialisation = new MenuInitialisation();
                    menuInitialisation.setItem(CartInitialisation.cartInitialisationArrayList.get(position).getItem());
                    menuInitialisation.setPrice(CartInitialisation.cartInitialisationArrayList.get(position).getPrice());
                    menuInitialisation.setType(CartInitialisation.cartInitialisationArrayList.get(position).getType());
                    menuInitialisation.setQuantity(CartInitialisation.cartInitialisationArrayList.get(position).getQuantity()+1);
                    menuInitialisation.setAmount(CartInitialisation.cartInitialisationArrayList.get(position).getAmount()+cartInitialisationArrayList.get(position).getPrice());
                    menuInitialisation.setCategory(CartInitialisation.cartInitialisationArrayList.get(position).getCategory());
                    menuInitialisation.setDetail(CartInitialisation.cartInitialisationArrayList.get(position).getDetail());

                    holder.qty_btw_buttons.setText(String.valueOf(CartInitialisation.cartInitialisationArrayList.get(position).getQuantity()+1));
                    holder.tv_total.setText("Total: ₹ "+String.valueOf(CartInitialisation.cartInitialisationArrayList.get(position).getAmount()+cartInitialisationArrayList.get(position).getPrice())+"/-");
                    CartInitialisation.cartInitialisationArrayList.set(position,menuInitialisation);

                    int i,qty=0,sum=0,discount_amt=0,to_pay=0,gst_amount=0,amount=0;
                    for(i=0;i<cartInitialisationArrayList.size();i++)
                    {
                        qty = qty+cartInitialisationArrayList.get(i).getQuantity();
                        sum = sum+cartInitialisationArrayList.get(i).getAmount();
                    }

                    gst_amount = (sum*GST)/100;
                    tv_gst_amount.setText("₹"+String.valueOf(gst_amount)+".00");
                    amount = sum+gst_amount;

                    discount_amt=(amount*DISCOUNT)/100;
                    tv_discount_amount.setText("- ₹"+String.valueOf(discount_amt)+".00");

                    to_pay=amount-discount_amt;
                    tv_to_pay.setText("To Pay   ₹"+String.valueOf(to_pay)+".00");

                    tv_cart_qty.setText(String.valueOf(qty));
                    tv_cart_amt.setText("₹"+String.valueOf(sum)+".00");
                }
            }
        });
        holder.buttondec.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(view.getId()==R.id.buttondec)
                {
                    MenuInitialisation menuInitialisation = new MenuInitialisation();
                    menuInitialisation.setQuantity(CartInitialisation.cartInitialisationArrayList.get(position).getQuantity()-1);
                    menuInitialisation.setItem(CartInitialisation.cartInitialisationArrayList.get(position).getItem());
                    menuInitialisation.setPrice(CartInitialisation.cartInitialisationArrayList.get(position).getPrice());
                    menuInitialisation.setType(CartInitialisation.cartInitialisationArrayList.get(position).getType());
                    menuInitialisation.setAmount(CartInitialisation.cartInitialisationArrayList.get(position).getAmount()-CartInitialisation.cartInitialisationArrayList.get(position).getPrice());
                    menuInitialisation.setCategory(CartInitialisation.cartInitialisationArrayList.get(position).getCategory());
                    menuInitialisation.setDetail(CartInitialisation.cartInitialisationArrayList.get(position).getDetail());
                    holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
                    holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getAmount())+"/-");
                    CartInitialisation.cartInitialisationArrayList.set(position,menuInitialisation);

                    if(menuInitialisation.getQuantity()==0)
                    {
                        CartInitialisation.cartInitialisationArrayList.remove(position);
                        if(CartInitialisation.cartInitialisationArrayList.size()==0)
                        {
                            Toast.makeText(context,"Your Cart is empty!",Toast.LENGTH_SHORT).show();
                            ((Activity)context).finish();
                        }
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, CartInitialisation.cartInitialisationArrayList.size());

                        int i,qty=0,sum=0,discount_amt=0,to_pay=0,gst_amount=0,amount=0;
                        for(i=0;i<cartInitialisationArrayList.size();i++)
                        {
                            qty = qty+cartInitialisationArrayList.get(i).getQuantity();
                            sum = sum+cartInitialisationArrayList.get(i).getAmount();
                        }

                        gst_amount = (sum*GST)/100;
                        tv_gst_amount.setText("+ ₹"+String.valueOf(gst_amount)+".00");

                        amount = sum+gst_amount;
                        tv_amount.setText("₹"+String.valueOf(amount)+".00");

                        discount_amt=(amount*DISCOUNT)/100;
                        tv_discount_amount.setText("- ₹"+String.valueOf(discount_amt)+".00");

                        to_pay=amount-discount_amt;
                        tv_to_pay.setText("To Pay   ₹"+String.valueOf(to_pay)+".00");

                        tv_cart_qty.setText(String.valueOf(qty));
                        tv_cart_amt.setText("₹"+String.valueOf(sum)+".00");

                    }
                    else
                    {
                        holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
                        holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getAmount())+"/-");
                        CartInitialisation.cartInitialisationArrayList.set(position,menuInitialisation);

                        int i,qty=0,sum=0,discount_amt=0,to_pay=0,gst_amount=0,amount=0;
                        for(i=0;i<cartInitialisationArrayList.size();i++)
                        {
                            qty = qty+cartInitialisationArrayList.get(i).getQuantity();
                            sum = sum+cartInitialisationArrayList.get(i).getAmount();
                        }

                        gst_amount = (sum*GST)/100;
                        tv_gst_amount.setText("+ ₹"+String.valueOf(gst_amount)+".00");

                        amount = sum+gst_amount;
                        tv_amount.setText("₹"+String.valueOf(amount)+".00");

                        discount_amt=(amount*DISCOUNT)/100;
                        tv_discount_amount.setText("- ₹"+String.valueOf(discount_amt)+".00");

                        to_pay=amount-discount_amt;
                        tv_to_pay.setText("To Pay   ₹"+String.valueOf(to_pay)+".00");

                        tv_cart_qty.setText(String.valueOf(qty));
                        tv_cart_amt.setText("₹"+String.valueOf(sum)+".00");

                    }
                }
            }
        });

    }

    public int getItemCount()
    {
        return CartInitialisation.cartInitialisationArrayList.size();
    }



    public static class CartViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_item,tv_total,tv_price,qty_btw_buttons,item_detail;
        private Button buttoninc,buttondec;
        private ImageView typelogo;

        public CartViewHolder(View view)
        {
            super(view);
            tv_item = (TextView)view.findViewById(R.id.tv_item);
            tv_total = (TextView)view.findViewById(R.id.tv_total);
            tv_price = (TextView)view.findViewById(R.id.tv_price);
            buttoninc = (Button)view.findViewById(R.id.buttoninc);
            qty_btw_buttons = (TextView)view.findViewById(R.id.qty_btw_buttons);
            buttondec = (Button)view.findViewById(R.id.buttondec);
            typelogo = (ImageView)view.findViewById(R.id.typelogo);
            item_detail = (TextView)view.findViewById(R.id.item_detail);
        }
    }
}
