package hu.pe.yummykart.yummykart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderDetails_ListAdapter extends ArrayAdapter<OrderDetailsDataModel>
{
    private LayoutInflater mInflater;
    private ArrayList<OrderDetailsDataModel> orderDetailsDataModelArrayList;
    private int mViewResourceId;

    public OrderDetails_ListAdapter(Context context, int textViewResourceId, ArrayList<OrderDetailsDataModel> orderDetailsDataModelArrayList)
    {
        super(context, textViewResourceId, orderDetailsDataModelArrayList);
        this.orderDetailsDataModelArrayList = orderDetailsDataModelArrayList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mInflater.inflate(mViewResourceId, null);

        OrderDetailsDataModel orderDetailsDataModel = orderDetailsDataModelArrayList.get(position);

        if (orderDetailsDataModel != null) {
            TextView item = (TextView) convertView.findViewById(R.id.textItem);
            TextView quantity = (TextView) convertView.findViewById(R.id.textQuantity);
            TextView amount = (TextView) convertView.findViewById(R.id.textAmount);
            if (item != null) {
                item.setText(orderDetailsDataModel.getItem());
            }
            if (quantity != null) {
                quantity.setText((orderDetailsDataModel.getQuantity()));
            }
            if (amount != null) {
                amount.setText((orderDetailsDataModel.getAmount()));
            }
        }

        return convertView;
    }
}
