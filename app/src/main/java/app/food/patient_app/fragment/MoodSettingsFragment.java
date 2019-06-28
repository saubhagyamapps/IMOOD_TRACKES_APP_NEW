package app.food.patient_app.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.adapter.NotesAdapter;
import app.food.patient_app.database.AlarmReceiver;
import app.food.patient_app.database.DatabaseHelper;
import app.food.patient_app.interfacea.DeletItem;
import app.food.patient_app.model.Note;

public class MoodSettingsFragment extends Fragment {

    private static final String TAG = "MoodSettingsFragment";
    View mView;
    Button buttonstartSetDialog;
    TextView textAlarmPrompt;
    TimePickerDialog timePickerDialog;
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DatabaseHelper db;
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            Log.e(TAG, "onTimeSet: " + calSet.getTime().getDay() + "--->" +
                    calSet.getTime().getMinutes());
            calSet.getTime().getDay();
            calSet.getTime().getMinutes();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
                Log.e(TAG, "onTimeSet: ");
            }
            if (calSet.before(calNow)) {//if its in the past increment
                calSet.add(Calendar.DATE, 1);
                Log.e(TAG, "next time: ");
            }

            setAlarm(calSet);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mood_settings, container, false);
        initialization();
        return mView;
    }

    private void initialization() {
        textAlarmPrompt = mView.findViewById(R.id.alarmprompt);

        buttonstartSetDialog = mView.findViewById(R.id.startAlaram);
        buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openTimePickerDialog(false);

            }
        });
        getAllNoteList();

    }

    private void getAllNoteList() {


        recyclerView = mView.findViewById(R.id.recycler_view);


        db = new DatabaseHelper(getActivity());

        notesList.addAll(db.getAllNotes());


        mAdapter = new NotesAdapter(getActivity(), notesList, new DeletItem() {
            @Override
            public void deletItem(int position) {
                deleteNote(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }


    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

    }

    private void createNote(String note,int time) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note,time);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

        }
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(getActivity(),
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    private void setAlarm(Calendar targetCal) {
        final int _id = (int) System.currentTimeMillis();
        createNote(targetCal.getTime().getHours() + ":" + targetCal.getTime().getMinutes(),_id);

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), _id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }


}
