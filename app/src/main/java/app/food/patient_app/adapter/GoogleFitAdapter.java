package app.food.patient_app.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.GetGooGleFitActivityModel;
import app.food.patient_app.util.AppUtil;

public class GoogleFitAdapter extends RecyclerView.Adapter<GoogleFitAdapter.MyViewHolder> {

    Context mContext;
    private List<GetGooGleFitActivityModel.DataBean> resultBeanList;

    public GoogleFitAdapter(Context mContext, List<GetGooGleFitActivityModel.DataBean> resultBeanList) {
        this.mContext = mContext;
        this.resultBeanList = resultBeanList;
    }

    @NonNull
    @Override
    public GoogleFitAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.google_fit_list,viewGroup,false);
        return new GoogleFitAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleFitAdapter.MyViewHolder myViewHolder, int i) {
      /*//  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);*/
        GetGooGleFitActivityModel.DataBean result = resultBeanList.get(i);
        myViewHolder.txt_Date.setText(result.getDate());
        if (result.getActivity().equals("0")) {
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"On Vehicle");
        }else if (result.getActivity().equals("1")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Biking");
        }else if (result.getActivity().equals("2")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"On Foot");
        }else if (result.getActivity().equals("3")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Still (not moving)");
        }else if (result.getActivity().equals("4")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Unknown");
        }else if (result.getActivity().equals("5")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Unknown");
        }else if (result.getActivity().equals("7")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Walking");
        }else if (result.getActivity().equals("9")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Aerobics");
        }else if (result.getActivity().equals("72")){
            myViewHolder.txt_Activity_Name.setText("Activity:       "+"Sleeping");
        }
        myViewHolder.txt_Duration.setText("Duration:     "+String.format(AppUtil.formatMilliSeconds(Long.parseLong(result.getDuration()))));
    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Date, txt_Activity_Name, txt_Duration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Date = itemView.findViewById(R.id.txt_fit_date);
            txt_Activity_Name = itemView.findViewById(R.id.txt_fit_activity_name);
            txt_Duration = itemView.findViewById(R.id.txt_fit_duration);
        }
    }
}
