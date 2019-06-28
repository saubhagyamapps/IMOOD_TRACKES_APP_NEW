package app.food.patient_app.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.GetMoodNotesModel;

public class MoodCalendarAdapter extends RecyclerView.Adapter<MoodCalendarAdapter.MyViewHolder> {
    private static final String TAG = "MoodCalendarAdapter";
    Context mContext;
    View view;
    private List<GetMoodNotesModel.ResultBean> resultBeanList;

    public MoodCalendarAdapter(Context mContext, List<GetMoodNotesModel.ResultBean> resultBeanList) {
        this.mContext = mContext;
        this.resultBeanList = resultBeanList;
    }


    @NonNull
    @Override
    public MoodCalendarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.moodlist, viewGroup, false);

        return new MoodCalendarAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodCalendarAdapter.MyViewHolder myViewHolder, int position) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        myViewHolder.txt_Notes.setText(resultBeanList.get(position).getNotes());
        List<String> items = Arrays.asList(resultBeanList.get(position).getActivities().split(","));
        Log.e(TAG, "List" + items.toString());
        myViewHolder.txt_Date.setText(resultBeanList.get(position).getDate());
        myViewHolder.txt_Time.setText(resultBeanList.get(position).getTime());
        if (!resultBeanList.get(position).getActivities().equals("nul")) {
            myViewHolder.txt_Activity.setText(resultBeanList.get(position).getActivities());
        }else {
            myViewHolder.txt_Activity.setText("");
        }
        if (resultBeanList.get(position).getMode().equals("rad")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.rad);
        } else if (resultBeanList.get(position).getMode().equals("good")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.good);
        } else if (resultBeanList.get(position).getMode().equals("meh")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.meh);
        } else if (resultBeanList.get(position).getMode().equals("bad")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.bad);
        } else if (resultBeanList.get(position).getMode().equals("awful")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.awful);
        } else if (resultBeanList.get(position).getMode().equals("Mad")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.mad);
        } else if (resultBeanList.get(position).getMode().equals("Sad")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.sad);
        } else if (resultBeanList.get(position).getMode().equals("Smiling Happy")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.smilinghappy);
        } else if (resultBeanList.get(position).getMode().equals("Unhappy")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.unhappy);
        } else if (resultBeanList.get(position).getMode().equals("Very Happy")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.veryhappy);
        }

    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Date, txt_Time, txt_Activity, txt_Notes;
        GridView grid_view;
        CardView card_view;
        ImageView image_Mood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);

            txt_Date = itemView.findViewById(R.id.txt_mood_date);
            txt_Activity = itemView.findViewById(R.id.txt_activity);
            txt_Notes = itemView.findViewById(R.id.txt_notes);
            image_Mood = itemView.findViewById(R.id.mood_image);
            txt_Time = itemView.findViewById(R.id.txt_time);
        }
    }
}
