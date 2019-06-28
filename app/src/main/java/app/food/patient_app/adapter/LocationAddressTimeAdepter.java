package app.food.patient_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.AddressTimeModel;

public class LocationAddressTimeAdepter extends RecyclerView.Adapter<LocationAddressTimeAdepter.ViewHolder> {
    private List<AddressTimeModel.ResultBean> dataBeans;
    private Context context;


    public LocationAddressTimeAdepter(List<AddressTimeModel.ResultBean> dataBeans, Context context) {
        this.dataBeans = dataBeans;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresstimelist, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txtTime.setText(dataBeans.get(position).getTime_difference());
            holder.txtAddress.setText(dataBeans.get(position).getAddress());
    }


    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTime, txtAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtAddress = itemView.findViewById(R.id.txtAddress);

        }
    }
}
