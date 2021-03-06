package com.example.milleca.mydailyplanner;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;


public class TaskPriorityAdapter extends RecyclerView.Adapter<TaskPriorityAdapter.ViewHolder>{
    private List<Reminder> mReminderList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView personNameTxtV,priority1;
        public SmoothCheckBox checkbox1;
        public TextView alert1;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personNameTxtV = (TextView) v.findViewById(R.id.name);
            checkbox1=v.findViewById(R.id.scb);
            alert1=v.findViewById(R.id.alert);
            priority1=v.findViewById(R.id.priority);
        }
    }

    public void add(int position,Reminder reminder) {
        mReminderList.add(position, reminder);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mReminderList.remove(position);
        notifyItemRemoved(position);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskPriorityAdapter(List<Reminder> myDataset, Context context, RecyclerView recyclerView) {
        mReminderList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskPriorityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.all_priority_task, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Reminder reminder = mReminderList.get(position);
        holder.personNameTxtV.setText(reminder.getTitle());
        holder.checkbox1.setOnCheckedChangeListener(null);
        holder.priority1.setText(reminder.getPriotity());
        holder.alert1.setText("");

        //if true, your checkbox will be selected, else unselected
        holder.checkbox1.setChecked(reminder.getCompletedTask()==1);
        if(holder.checkbox1.isChecked()){
            holder.personNameTxtV.setPaintFlags(holder.personNameTxtV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.personNameTxtV.setTextColor(Color.parseColor("#808080"));
            holder.alert1.setText("Completed");
        }
        else{
            holder.personNameTxtV.setPaintFlags(holder.personNameTxtV.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.personNameTxtV.setTextColor(Color.parseColor("#333333"));
            holder.alert1.setText("");
        }

        holder.checkbox1.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(checkBox.isChecked()){
                    Log.e("ndsjd","true");
                    holder.personNameTxtV.setPaintFlags(holder.personNameTxtV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    EverydayDBHelper db=new EverydayDBHelper(mContext);
                    holder.personNameTxtV.setTextColor(Color.parseColor("#808080"));
                    db.updateCompletedTask(reminder.getID(),1);
                    holder.alert1.setText("Completed");
                }
                if(!checkBox.isChecked()) {
                    Log.e("ndsjd","false");
                    holder.personNameTxtV.setPaintFlags(holder.personNameTxtV.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.personNameTxtV.setTextColor(Color.parseColor("#333333"));
                    EverydayDBHelper db=new EverydayDBHelper(mContext);
                    db.updateCompletedTask(reminder.getID(),0);
                    holder.alert1.setText("");
                }
            }
        });

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateActivity(reminder.getID());
            }
        });


    }



    private void goToUpdateActivity(int reminderId) {
        String mStringClickID = Integer.toString(reminderId);
        Intent goToUpdate = new Intent(mContext, ReminderEditActivity.class);
        goToUpdate.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, mStringClickID);
        mContext.startActivity(goToUpdate);

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mReminderList.size();
    }
}
