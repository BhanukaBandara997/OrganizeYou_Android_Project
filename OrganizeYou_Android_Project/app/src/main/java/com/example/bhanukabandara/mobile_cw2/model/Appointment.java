package com.example.bhanukabandara.mobile_cw2.model;

public class Appointment {

    private String appointmentId;
    private String appointmentTitle;
    private String appointmentCreatedDate;
    private String appointmentCreatedTime;
    private String appointmentDetails;

    public Appointment() {
    }

    public Appointment(String appointmentId, String appointmentTitle, String appointmentCreatedDate, String appointmentCreatedTime, String appointmentDetails) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentCreatedDate = appointmentCreatedDate;
        this.appointmentCreatedTime = appointmentCreatedTime;
        this.appointmentDetails = appointmentDetails;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }


    public String getAppointmentCreatedTime() {
        return appointmentCreatedTime;
    }

    public void setAppointmentCreatedTime(String appointmentCreatedTime) {
        this.appointmentCreatedTime = appointmentCreatedTime;
    }

    public String getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(String appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentCreatedDate() {
        return appointmentCreatedDate;
    }

    public void setAppointmentCreatedDate(String appointmentCreatedDate) {
        this.appointmentCreatedDate = appointmentCreatedDate;
    }

}
