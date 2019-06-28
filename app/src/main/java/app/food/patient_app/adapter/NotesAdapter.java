package app.food.patient_app.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.database.AlarmReceiver;
import app.food.patient_app.interfacea.DeletItem;
import app.food.patient_app.model.Note;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    DeletItem deletItem;
    public NotesAdapter(Context context, List<Note> notesList, DeletItem deletItem) {
        this.context = context;
        this.notesList = notesList;
        this.deletItem = deletItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        holder.note.setText(note.getNote());
        holder.imgDeletAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, note.getTimestamp(), alarmIntent, 0);
                alarmManager.cancel(pendingIntent);
                deletItem.deletItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public ImageView imgDeletAlarm;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            imgDeletAlarm = view.findViewById(R.id.imgDeletAlarm);


        }
    }
}