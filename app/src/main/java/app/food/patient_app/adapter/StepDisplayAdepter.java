package app.food.patient_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.StepDisplayModel;

public class StepDisplayAdepter extends RecyclerView.Adapter<StepDisplayAdepter.MyViewHolder> {
    Context context;
    List<StepDisplayModel.FootStepDataBean> stepDataBeans;

    public StepDisplayAdepter(Context context, List<StepDisplayModel.FootStepDataBean> stepDataBeans) {
        this.context = context;
        this.stepDataBeans = stepDataBeans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_display_rowlist, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txtDate.setText(stepDataBeans.get(i).getDate());
        myViewHolder.txtStepCount.setText(String.valueOf(stepDataBeans.get(i).getFoot_step())+" steps");
        myViewHolder.txtMoveMunites.setText(String.valueOf(stepDataBeans.get(i).getFoot_move_minute())+" minutes");
    }

    @Override
    public int getItemCount() {
        return stepDataBeans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStepCount, txtDate,txtMoveMunites;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStepCount = itemView.findViewById(R.id.txtStepCount);
            txtMoveMunites = itemView.findViewById(R.id.txtMoveMunites);
        }
    }
}
