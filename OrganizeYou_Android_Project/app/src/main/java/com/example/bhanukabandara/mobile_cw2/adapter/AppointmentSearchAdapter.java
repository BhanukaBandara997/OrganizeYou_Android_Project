package com.example.bhanukabandara.mobile_cw2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import java.util.ArrayList;
import com.example.bhanukabandara.mobile_cw2.R;
import java.util.List;

public class AppointmentSearchAdapter extends RecyclerView.Adapter<AppointmentSearchAdapter.SearchViewHolder> implements Filterable {

    Context context;
    private List<Appointment> appointmentList;
    private List<Appointment> appointmentListFiltered;
    private AppointmentAdapterListener listener;

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView appointmentTitle,appointmentCreatedDate, appointmentCreatedTime;

        public SearchViewHolder(View view) {
            super(view);
            appointmentTitle = view.findViewById(R.id.appointmentTitle);
            appointmentCreatedDate =  view.findViewById(R.id.appointmentCreatedDate);
            appointmentCreatedTime = view.findViewById(R.id.appointmentCreatedTime);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onAppointmentSelected(appointmentListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public AppointmentSearchAdapter(Context context, List<Appointment> appointmentList, AppointmentAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.appointmentList = appointmentList;
        this.appointmentListFiltered = appointmentList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_appointment_list_item, parent, false);

        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        final Appointment appointment = appointmentListFiltered.get(position);
        holder.appointmentTitle.setText(appointment.getAppointmentTitle());
        holder.appointmentCreatedDate.setText(appointment.getAppointmentCreatedDate());
        holder.appointmentCreatedTime.setText(appointment.getAppointmentCreatedTime());
    }

    @Override
    public int getItemCount() {
        return appointmentListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    appointmentListFiltered = appointmentList;
                } else {
                    List<Appointment> filteredList = new ArrayList<>();
                    for (Appointment row : appointmentList) {

                        // appointmentTitle match condition. this might differ depending on your requirement
                        // here we are looking for appointmentTitle or appointmentDetails match
                        if (row.getAppointmentTitle().toLowerCase().contains(charString.toLowerCase()) || row.getAppointmentDetails().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    appointmentListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appointmentListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appointmentListFiltered = (ArrayList<Appointment>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AppointmentAdapterListener {
        void onAppointmentSelected(Appointment appointment);
    }
}
