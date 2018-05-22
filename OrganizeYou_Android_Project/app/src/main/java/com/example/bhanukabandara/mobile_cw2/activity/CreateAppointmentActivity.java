package com.example.bhanukabandara.mobile_cw2.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bhanukabandara.mobile_cw2.R;
import com.example.bhanukabandara.mobile_cw2.dbhandler.AppointmentDbHandler;
import com.example.bhanukabandara.mobile_cw2.model.Appointment;
import com.example.bhanukabandara.mobile_cw2.other.ServerConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAppointmentActivity extends AppCompatActivity {

    Button createAppointmentBtn, saveAppointmentBtn, thesaurusBtn;
    EditText appointmentTitleEditText, appointmentSelectTimeEditText, appointmentDetailsEditText, thesaurusWordEditText;
    String format;
    public static List<Appointment> appointmentList = new ArrayList<>();
    Appointment appointment;
    String appointmentTitle, appointmentDate, appointmentTime, appointmentDetails, date;
    Context context = CreateAppointmentActivity.this;
    Intent intent;
    String appointmentId;
    String appointmentTitleSaved;
    String strAppointmentId;
    String[] synonymsArray;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> synonymsArrayList;
    String synonymWordSelected = null;
    String newSynonymForEditText = null;
    static String oldSynonymForEditText = null;
    final int SYNONYMS = 0;
    boolean isFromCreateAppointment = false;
    String output;

    public CreateAppointmentActivity() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.create_Appointment));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createAppointmentBtn = findViewById(R.id.create_button);
        saveAppointmentBtn = findViewById(R.id.save_button);
        thesaurusBtn = findViewById(R.id.thesaurus_button);
        appointmentTitleEditText = findViewById(R.id.appointment_title_editText);
        appointmentSelectTimeEditText = findViewById(R.id.select_time_editText);
        appointmentDetailsEditText = findViewById(R.id.appointment_details_editText);
        thesaurusWordEditText = findViewById(R.id.thesaurus_editText);

        intent = getIntent();
        date = intent.getStringExtra("createdDate");
        synonymWordSelected = intent.getStringExtra("synonymWordSelected");
        appointmentTitle = intent.getStringExtra("appointmentTitle");
        appointmentTime = intent.getStringExtra("appointmentTime");
        appointmentDetails = intent.getStringExtra("appointmentDetails");
        newSynonymForEditText = intent.getStringExtra("synonymWordSelectedFromThesaurusEditText");

        if (synonymWordSelected != null || appointmentTitle != null || appointmentDetails != null || appointmentTime != null ) {
            thesaurusWordEditText.setText(synonymWordSelected);
            appointmentTitleEditText.setText(appointmentTitle);
            appointmentSelectTimeEditText.setText(appointmentTime);
            appointmentDetailsEditText.setText(appointmentDetails);
        }

        if (newSynonymForEditText != null || appointmentDetails != null && newSynonymForEditText == "") {
            Pattern p = Pattern.compile(String.valueOf(oldSynonymForEditText));
            Matcher matcher = p.matcher(appointmentDetails);
            output = synonymWordSelected;
            output = matcher.replaceAll(newSynonymForEditText);
            appointmentDetailsEditText.setText(output);
        }

        UUID uuid = UUID.randomUUID();
        String str = String.valueOf(uuid);
        strAppointmentId = str.substring(0, 4);

        createAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentId = strAppointmentId;
                appointmentTitle = appointmentTitleEditText.getText().toString();
                appointmentDate = date;
                appointmentTime = appointmentSelectTimeEditText.getText().toString();
                appointmentDetails = appointmentDetailsEditText.getText().toString();
                appointment = new Appointment(appointmentId, appointmentTitle, appointmentDate, appointmentTime, appointmentDetails);
                appointmentList.add(appointment);
                Intent intent = new Intent(CreateAppointmentActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        saveAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = false;
                boolean isSaveCompleted = false;
                appointmentId = strAppointmentId;
                appointmentTitle = appointmentTitleEditText.getText().toString();
                appointmentDate = date;
                appointmentTime = appointmentSelectTimeEditText.getText().toString();
                appointmentDetails = appointmentDetailsEditText.getText().toString();
                appointment = new Appointment(appointmentId, appointmentTitle, appointmentDate, appointmentTime, appointmentDetails);
                AppointmentDbHandler dbAdapter = new AppointmentDbHandler(context);
                dbAdapter.open();
                List<Appointment> appointmentList = new ArrayList<>();
                appointmentList = dbAdapter.getAllAppointments();
                for (Appointment appointment : appointmentList) {
                    appointmentTitleSaved = appointment.getAppointmentTitle();
                    if (appointmentTitleSaved.equals(appointmentTitle)) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Title Already Exist");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Appointment meeting with " + appointmentTitleSaved + " already exists, please choose a different event title")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close the dialog
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else if (!isExist) {
                    isSaveCompleted = dbAdapter.addAppointment(appointment);
                }
                dbAdapter.close();
                if (isSaveCompleted) {
                    Toast.makeText(CreateAppointmentActivity.this, "Saved Data Successfully", Toast.LENGTH_SHORT).show();
                } else if (!isSaveCompleted) {
                    Toast.makeText(CreateAppointmentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        thesaurusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateAppointmentActivity.this,"Thesaurus",Toast.LENGTH_LONG).show();
                String thesaurusWord = thesaurusWordEditText.getText().toString().trim();
                String url1 = "http://thesaurus.altervista.org/thesaurus/v1?word=" + thesaurusWord + "&language=en_US&key=aCwALKnqcPlMQT5od6Ai&output=json";
                url1 = url1.replace(" ", "");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = (JSONArray) response.get("response");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject list = (JSONObject) ((JSONObject) array.get(i)).get("list");
                                String synonym = (String) list.get("synonyms");
                                String arr[] = synonym.split("\\|");
                                int splitCount = arr.length;
                                for (int j = 0; j < splitCount; j++) {
                                    arrayList.add(arr[j]);
                                }
                            }

                            synonymsArray = new String[arrayList.size()];
                            synonymsArray = arrayList.toArray(synonymsArray);

                            synonymsArrayList = new ArrayList<>(Arrays.asList(synonymsArray));

                            String appointmentTitle = appointmentTitleEditText.getText().toString();
                            String appointmentDetails = appointmentDetailsEditText.getText().toString();
                            String appointmentTime = appointmentSelectTimeEditText.getText().toString();

                            isFromCreateAppointment = true;

                            Intent intent = new Intent(CreateAppointmentActivity.this, ThesaurusActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("synonymsArray", synonymsArrayList);
                            intent.putExtra("createdDate",date);
                            intent.putExtra("appointmentTitle", appointmentTitle);
                            intent.putExtra("appointmentDetails", appointmentDetails);
                            intent.putExtra("appointmentTime", appointmentTime);
                            intent.putExtra("isFromCreateAppointment", isFromCreateAppointment);
                            startActivity(intent);

                            arrayList.clear();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateAppointmentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                ServerConnection.getInstance(CreateAppointmentActivity.this).addToRequestque(request);

            }

        });

        appointmentSelectTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateAppointmentActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }
                        appointmentSelectTimeEditText.setText(hourOfDay + ":" + minutes + " " + format);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        appointmentDetailsEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Remove the "select all" option
                menu.removeItem(android.R.id.selectAll);
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.shareText);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode

                // Here is an example MenuItem
                menu.add(0, SYNONYMS, 0, "Synonyms").setIcon(R.drawable.icon_move);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Called when an action mode is about to be exited and
                // destroyed
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case SYNONYMS:
                        int min = 0;
                        int max = appointmentDetailsEditText.getText().length();
                        if (appointmentDetailsEditText.isFocused()) {
                            final int selStart = appointmentDetailsEditText.getSelectionStart();
                            final int selEnd = appointmentDetailsEditText.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }

                        final CharSequence selectedText = appointmentDetailsEditText.getText().subSequence(min, max);
                        //Toast.makeText(CreateAppointmentActivity.this, selectedText, Toast.LENGTH_LONG).show();
                        CharSequence thesaurusWord = selectedText;
                        oldSynonymForEditText = String.valueOf(thesaurusWord);
                        String url1 = "http://thesaurus.altervista.org/thesaurus/v1?word=" + thesaurusWord + "&language=en_US&key=aCwALKnqcPlMQT5od6Ai&output=json";
                        url1 = url1.replace(" ", "");
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray array = (JSONArray) response.get("response");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject list = (JSONObject) ((JSONObject) array.get(i)).get("list");
                                        String synonym = (String) list.get("synonyms");
                                        String arr[] = synonym.split("\\|");
                                        int splitCount = arr.length;
                                        for (int j = 0; j < splitCount; j++) {
                                            arrayList.add(arr[j]);
                                        }
                                    }

                                    synonymsArray = new String[arrayList.size()];
                                    synonymsArray = arrayList.toArray(synonymsArray);

                                    synonymsArrayList = new ArrayList<>(Arrays.asList(synonymsArray));

                                    String appointmentTitle = appointmentTitleEditText.getText().toString();
                                    String appointmentDetails = appointmentDetailsEditText.getText().toString();
                                    String appointmentTime = appointmentSelectTimeEditText.getText().toString();

                                    isFromCreateAppointment = true;

                                    Intent intent = new Intent(CreateAppointmentActivity.this, ThesaurusEditTextActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("synonymsArray", synonymsArrayList);
                                    intent.putExtra("createdDate",date);
                                    intent.putExtra("appointmentTitle", appointmentTitle);
                                    intent.putExtra("appointmentDetails", appointmentDetails);
                                    intent.putExtra("appointmentTime", appointmentTime);
                                    intent.putExtra("isFromCreateAppointment", isFromCreateAppointment);
                                    startActivity(intent);

                                    arrayList.clear();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CreateAppointmentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        ServerConnection.getInstance(CreateAppointmentActivity.this).addToRequestque(request);
                        // Finish and close the ActionMode
                        mode.finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(CreateAppointmentActivity.this, MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        Intent intent = new Intent(CreateAppointmentActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
