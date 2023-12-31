package com.bhaskar.aa45.coderevision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.aa45.coderevision.Firebase.DataHolder;
import com.bhaskar.aa45.coderevision.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.Stack;

public class AddProblemActivity extends AppCompatActivity {

    private Button datePickerButton;
    private DatePickerDialog datePickerDialog;
    private String selectedDate;
    private int selectedDifficulty= -1;
    private final String[] diffItems = {"Basic", "Easy" , "Medium" , "Hard"};
    private final String[] branch = {"Solved","Tried","Wishlist"};
    String sl = "0";
    int selectedTab;
    private String questionLink ="",enteredCode = "",questionTitle = "",questionTag="" , date = "",summary="";
    private int diff;
    String uniqueId ="";

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        String[] topic_list = getResources().getStringArray(R.array.topic_list);

        EditText code = findViewById(R.id.code);
        TextView textView1 = findViewById(R.id.text_headline1);
        TextView textView2 = findViewById(R.id.text_headline2);
        EditText title = findViewById(R.id.title);
        EditText link = findViewById(R.id.link);
        LinearLayout addTopic = findViewById(R.id.add_tag);
        Button addButton = findViewById(R.id.add_problem);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText summ = findViewById(R.id.summary_add);
        TextView topic = findViewById(R.id.topic);
        ImageView back = findViewById(R.id.back_to_home);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_items, android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedDifficulty=pos-1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //back
        back.setOnClickListener(v -> {
            finish();
        });


        //select date
        initDatePicker();
        datePickerButton = findViewById(R.id.date_picker);
        selectedDate = getTodaysDate();
        datePickerButton.setText(selectedDate);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


        Stack<Integer> tab = HomeFragment.tabNo;
        selectedTab = tab.pop();
        String name = MainActivity.userDetails.get(0);
        String[] splitName;
        String firstName;
        if(name!=null) {
            splitName = name.split(" ");
            firstName = splitName[0] ;
        }else{
            firstName = "";
        }

        switch (selectedTab){
            case 0 :
                textView1.setText("🎉 Wow! Solved a new problem");
                textView2.setText("Keep it up " + firstName +"!");
                break;
            case 1:
                textView1.setText("\uD83D\uDC4F Wow, You Tried to solve one");
                textView2.setText("question, Now Save it and try again later");
                break;
            case 2:
                textView1.setText("Add Problem to wishlist");
                textView2.setText("To solve it later \uD83D\uDE4C");
                break;
        }

        //if wishlist selected then code & summary is not necessary
        if(selectedTab==2){
            code.setHint("Code (Optional)");
            summ.setHint("Write Summary of the Code (Optional)");
        }

        //click on add tag
        boolean[] listCheck = new boolean[topic_list.length];
        Arrays.fill(listCheck,false);
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> l = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProblemActivity.this);
                builder.setTitle("Select Tags/Topics :")
                        .setMultiChoiceItems(topic_list, listCheck, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    listCheck[which] = true;
                                }
                            }
                        });
                builder.setCancelable(false);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        questionTag ="";
                        for(int i=0 ;i<listCheck.length;i++){
                            if(listCheck[i]){
                                questionTag += topic_list[i] + "    ";
                            }
                        }
                        topic.setText(questionTag);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTitle += title.getText().toString();
                questionLink += link.getText().toString();
                enteredCode += code.getText().toString();
                summary += summ.getText().toString();
                date = selectedDate;
                diff = selectedDifficulty;



                if(!Patterns.WEB_URL.matcher(questionLink).matches()){
                    Toast.makeText(AddProblemActivity.this, "Please Enter valid Link", Toast.LENGTH_SHORT).show();
                }
                else if (diff <0 || questionTag.equals("") ) {
                    Toast.makeText(AddProblemActivity.this, "Please fill the form appropriately", Toast.LENGTH_SHORT).show();
                }
                else if(selectedTab==0 && enteredCode.equals("")){
                    Toast.makeText(AddProblemActivity.this, "Please fill the form appropriately", Toast.LENGTH_SHORT).show();
                }
                else {
                    setData();
                    Toast.makeText(AddProblemActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        ImageView calendar_icon = findViewById(R.id.calendar_icon);
        ImageView link_icon = findViewById(R.id.link_icon);
        ImageView tag_icon = findViewById(R.id.tag_icon);
        ImageView code_icon = findViewById(R.id.code_icon);
        ImageView summary_icon = findViewById(R.id.summary_icon);
        ImageView diff_icon = findViewById(R.id.diff_icon);

        RelativeLayout diff_layout = findViewById(R.id.spinner_layout);
        RelativeLayout date_layout = findViewById(R.id.date_layout);
        RelativeLayout title_layout = findViewById(R.id.enter_title);
        RelativeLayout link_layout = findViewById(R.id.enter_link);
        RelativeLayout tag_layout = findViewById(R.id.enter_tag);
        RelativeLayout code_layout = findViewById(R.id.enter_code);
        RelativeLayout summary_layout = findViewById(R.id.enter_summary);
        
        //if light mode
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO){
            calendar_icon.setImageDrawable(getResources().getDrawable(R.drawable.edit_cal_dark));
            link_icon.setImageDrawable(getResources().getDrawable(R.drawable.link_dark));
            tag_icon.setImageDrawable(getResources().getDrawable(R.drawable.tag_dark));
            code_icon.setImageDrawable(getResources().getDrawable(R.drawable.code_dark));
            summary_icon.setImageDrawable(getResources().getDrawable(R.drawable.summary_dark));
            diff_icon.setImageDrawable(getResources().getDrawable(R.drawable.difficulty_dark));
            back.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow_black));
            datePickerButton.setTextColor(getResources().getColor(R.color.black));
            summ.setTextColor(getResources().getColor(R.color.black));
            code.setTextColor(getResources().getColor(R.color.black));
            link.setTextColor(getResources().getColor(R.color.black));
            title.setTextColor(getResources().getColor(R.color.black));
            topic.setTextColor(getResources().getColor(R.color.black));
            ImageView img = findViewById(R.id.image_add_circle);
            img.setImageDrawable(getResources().getDrawable(R.drawable.add_circle_dark));



            diff_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            date_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            title_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            link_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            tag_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            code_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
            summary_layout.setBackground(getResources().getDrawable(R.drawable.edittet_shape));
        }
    }


    @SuppressLint("SimpleDateFormat")
    public String getTodaysDate(){
        Calendar calendar = Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month + 1,year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                selectedDate = makeDateString(day,month +1 , year);
                datePickerButton.setText(selectedDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return (day + "-" + month + "-" + year);
    }
    @SuppressLint("CommitPrefEdits")
    private void setData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://code-revision-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = db.getReference(); //root
        String uid = FirebaseAuth.getInstance().getUid();

        //root->user->uid->branch(tab)
        DatabaseReference branchRef = myRef.child("user").child(uid).child(branch[selectedTab]);
        uniqueId = branchRef.push().getKey();
        DatabaseReference finalRef = myRef.child("user").child(uid).child(branch[selectedTab]).child(uniqueId);

        DataHolder obj = new DataHolder(questionTitle, questionLink, date, diffItems[diff], questionTag,enteredCode,uniqueId,branch[selectedTab],summary);

        finalRef.setValue(obj);

    }
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(AddProblemActivity.this.getSupportActionBar()).hide();
    }

}