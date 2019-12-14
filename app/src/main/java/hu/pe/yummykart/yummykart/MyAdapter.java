package hu.pe.yummykart.yummykart;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    Context context;
    ArrayList<MenuInitialisation> menuInitialisationArrayList;
    int flag;
    public MyAdapter(Context context, ArrayList<MenuInitialisation> menuInitialisationArrayList)
    {
        this.context=context;
        this.menuInitialisationArrayList=menuInitialisationArrayList;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.buttondec.setVisibility(View.INVISIBLE);
        holder.qty_btw_buttons.setVisibility(View.INVISIBLE);
        holder.tv_total.setVisibility(View.GONE);
        holder.tv_item.setText(menuInitialisationArrayList.get(position).getItem());
        holder.tv_price.setText("₹ "+String.valueOf(menuInitialisationArrayList.get(position).getPrice())+"/-");
        if(menuInitialisationArrayList.get(position).getDetail()!=0)
        {
            holder.item_detail.setVisibility(View.VISIBLE);
            holder.item_detail.setText("@ "+String.valueOf(menuInitialisationArrayList.get(position).getDetail())+" pcs.");
        }
        else if(menuInitialisationArrayList.get(position).getDetail()==0)
        {
            holder.item_detail.setVisibility(View.INVISIBLE);
        }

        int index = findIndex(menuInitialisationArrayList.get(position).getItem());

        if(index!=-1)
        {
            MenuInitialisation menuInitialisation = CartInitialisation.cartInitialisationArrayList.get(index);
           // holder.tv_total.setVisibility(View.VISIBLE);
            holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getAmount())+"/-");
            holder.qty_btw_buttons.setVisibility(View.VISIBLE);
            holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
            holder.buttondec.setVisibility(View.VISIBLE);
        }
        else if(index==-1)
        {
            holder.buttondec.setVisibility(View.INVISIBLE);
            holder.tv_total.setVisibility(View.GONE);
            holder.qty_btw_buttons.setVisibility(View.INVISIBLE);//new
        }

            holder.tv_item.setText(menuInitialisationArrayList.get(position).getItem());
            holder.tv_price.setText("₹ "+String.valueOf(menuInitialisationArrayList.get(position).getPrice())+"/-");
        if(menuInitialisationArrayList.get(position).getType().equals("VEG"))
        {
            holder.typelogo.setImageResource(R.drawable.veglogo);
        }
        else
        {
            holder.typelogo.setImageResource(R.drawable.nonveglogo);
        }

        if(menuInitialisationArrayList.get(position).getDetail()!=0)
        {
            holder.item_detail.setText("@ "+String.valueOf(menuInitialisationArrayList.get(position).getDetail())+" pcs.");
        }

        holder.buttoninc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (view.getId() == R.id.buttoninc)
                {
                    int index = findIndex(menuInitialisationArrayList.get(position).getItem());
                    MenuInitialisation menuInitialisation = new MenuInitialisation();
                    int i = holder.getAdapterPosition();

                    if (index == -1)
                    {
                        holder.buttondec.setVisibility(View.VISIBLE);
                        holder.qty_btw_buttons.setVisibility(View.VISIBLE);
                       // holder.tv_total.setVisibility(View.VISIBLE);
                        menuInitialisation.setItem(menuInitialisationArrayList.get(position).getItem());
                        menuInitialisation.setPrice(menuInitialisationArrayList.get(position).getPrice());
                        menuInitialisation.setCategory(menuInitialisationArrayList.get(position).getCategory());
                        menuInitialisation.setAmount(menuInitialisationArrayList.get(position).getPrice());
                        menuInitialisation.setQuantity(menuInitialisationArrayList.get(position).getQuantity() + 1);
                        menuInitialisation.setType(menuInitialisationArrayList.get(position).getType());
                        menuInitialisation.setDetail(menuInitialisationArrayList.get(position).getDetail());
                        CartInitialisation.cartInitialisationArrayList.add(menuInitialisation);

                        holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getPrice())+"/-");
                        holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
                    }
                    else if (index != -1)
                    {
                        holder.buttondec.setVisibility(View.VISIBLE);
                        holder.qty_btw_buttons.setVisibility(View.VISIBLE);
                      //  holder.tv_total.setVisibility(View.VISIBLE);
                        int quantity = CartInitialisation.cartInitialisationArrayList.get(index).getQuantity();
                        quantity = quantity + 1;

                        menuInitialisation.setItem(menuInitialisationArrayList.get(position).getItem());
                        menuInitialisation.setPrice(menuInitialisationArrayList.get(position).getPrice());
                        menuInitialisation.setCategory(menuInitialisationArrayList.get(position).getCategory());
                        menuInitialisation.setAmount(quantity * menuInitialisationArrayList.get(position).getPrice());
                        menuInitialisation.setQuantity(quantity);
                        menuInitialisation.setType(menuInitialisationArrayList.get(position).getType());
                        menuInitialisation.setDetail(menuInitialisationArrayList.get(position).getDetail());
                        CartInitialisation.cartInitialisationArrayList.set(index, menuInitialisation);

                        holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getAmount())+"/-");
                        holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
                    }
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
                    int index = findIndex(menuInitialisationArrayList.get(position).getItem());
                    MenuInitialisation menuInitialisation = new MenuInitialisation();
                    int i = holder.getAdapterPosition();

                    if(index!=-1)
                    {
                        int quantity = CartInitialisation.cartInitialisationArrayList.get(index).getQuantity();
                        quantity = quantity - 1;

                        if(quantity==0)
                        {
                            CartInitialisation.cartInitialisationArrayList.remove(index);
                            holder.tv_total.setText(null);
                            holder.qty_btw_buttons.setText(null);
                            holder.buttondec.setVisibility(View.INVISIBLE);
                            holder.tv_total.setVisibility(View.GONE);
                            holder.qty_btw_buttons.setVisibility(View.INVISIBLE);//new
                        }
                        else if(quantity > 0)
                        {
                            holder.buttondec.setVisibility(View.VISIBLE);
                            holder.qty_btw_buttons.setVisibility(View.VISIBLE);
                           // holder.tv_total.setVisibility(View.VISIBLE);
                            menuInitialisation.setItem(menuInitialisationArrayList.get(position).getItem());
                            menuInitialisation.setPrice(menuInitialisationArrayList.get(position).getPrice());
                            menuInitialisation.setCategory(menuInitialisationArrayList.get(position).getCategory());
                            menuInitialisation.setAmount(quantity * menuInitialisationArrayList.get(position).getPrice());
                            menuInitialisation.setQuantity(quantity);
                            menuInitialisation.setType(menuInitialisationArrayList.get(position).getType());
                            menuInitialisation.setDetail(menuInitialisationArrayList.get(position).getDetail());
                            CartInitialisation.cartInitialisationArrayList.set(index, menuInitialisation);

                            holder.tv_total.setText("Total: ₹ "+String.valueOf(menuInitialisation.getAmount())+"/-");
                            holder.qty_btw_buttons.setText(String.valueOf(menuInitialisation.getQuantity()));
                        }
                    }
                }
            }
        });
    }

    int findIndex(String item)
    {
        flag=0;
        int i,size = CartInitialisation.cartInitialisationArrayList.size();
        for(i=0;i<size;i++)
        {
            MenuInitialisation menuInitialisation = CartInitialisation.cartInitialisationArrayList.get(i);
            if(menuInitialisation.getItem().toLowerCase().indexOf(item.toLowerCase()) != -1)
            {
                flag=1;
                return i;
            }
            if(flag==1)
            {
                break;
            }
        }
        return -1;
    }
    //CartInitialisation.cartInitialisationArrayList.contains(menuInitialisationArrayList.get(position))
    //Toast.makeText(context,String.valueOf(value),Toast.LENGTH_SHORT).show();
    @Override
    public int getItemCount()
    {
        return menuInitialisationArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_item,tv_total,tv_price,qty_btw_buttons,item_detail;
        private Button buttoninc,buttondec;
        private ImageView typelogo;

        public MyViewHolder(View view)
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
