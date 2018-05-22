package com.example.bhanukabandara.mobile_cw2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.activity.EditItemActivity;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;

import java.util.List;

public class AppointmentEditAdapter extends RecyclerView.Adapter<AppointmentEditAdapter.EditViewHolder>{

    private List<Appointment> appointmentList;

    public class EditViewHolder extends RecyclerView.ViewHolder {
        public TextView appointmentTitle, appointmentCreatedDate, appointmentCreatedTime;
        public RelativeLayout viewBackground, viewForeground;

        public EditViewHolder(View view) {
            super(view);
            appointmentTitle = view.findViewById(R.id.appointmentTitle);
            appointmentCreatedDate =  view.findViewById(R.id.appointmentCreatedDate);
            appointmentCreatedTime = view.findViewById(R.id.appointmentCreatedTime);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public AppointmentEditAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public AppointmentEditAdapter.EditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_appointment_list_item, parent, false);

        return new AppointmentEditAdapter.EditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppointmentEditAdapter.EditViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.appointmentTitle.setText(appointment.getAppointmentTitle());
        holder.appointmentCreatedDate.setText(appointment.getAppointmentCreatedDate());
        holder.appointmentCreatedTime.setText(appointment.getAppointmentCreatedTime());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

}
