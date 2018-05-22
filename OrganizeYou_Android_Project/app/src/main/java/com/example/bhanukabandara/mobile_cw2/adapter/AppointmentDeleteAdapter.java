package com.example.bhanukabandara.mobile_cw2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.activity.DeleteAppointmentActivity;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.List;

public class AppointmentDeleteAdapter extends RecyclerView.Adapter<AppointmentDeleteAdapter.DeleteViewHolder> {

    private List<Appointment> appointmentList;
    AppointmentDbHandler appointmentDbHandler;

    public class DeleteViewHolder extends RecyclerView.ViewHolder {
        public TextView appointmentTitle, appointmentCreatedDate, appointmentCreatedTime;
        public RelativeLayout viewBackground, viewForeground;

        public DeleteViewHolder(View view) {
            super(view);
            appointmentTitle = view.findViewById(R.id.appointmentTitle);
            appointmentCreatedDate =  view.findViewById(R.id.appointmentCreatedDate);
            appointmentCreatedTime = view.findViewById(R.id.appointmentCreatedTime);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public AppointmentDeleteAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public DeleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delete_appointment_list_item, parent, false);

        return new DeleteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeleteViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.appointmentTitle.setText(appointment.getAppointmentTitle());
        holder.appointmentCreatedDate.setText(appointment.getAppointmentCreatedDate());
        holder.appointmentCreatedTime.setText(appointment.getAppointmentCreatedTime());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void removeItem(int position,Context context) {
        appointmentDbHandler = new AppointmentDbHandler(context);
        appointmentDbHandler.open();
        Appointment appointment = appointmentList.get(position);
        String appointmentId = appointment.getAppointmentId();
        appointmentDbHandler.deleteAppointment(appointmentId);
        appointmentDbHandler.close();
        appointmentList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

}