package com.zoazh.le.ComPract.controller.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.ChatList;
import com.zoazh.le.ComPract.controller.sub.CreateQuestionActivity;
import com.zoazh.le.ComPract.controller.sub.NotificationsActivity;
import com.zoazh.le.ComPract.controller.sub.QuestionActivity;
import com.zoazh.le.ComPract.controller.sub.ViewProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.Question;
import com.zoazh.le.ComPract.model.database.User;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PracticeActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;
    private TextView cTextNoQuestion;

    private String[] learnLanguage;
    private String[] fillterLanguage;
    private String check;
    private ImageView cImageButtonChat;
    private ImageButton cButtonChat;
    private ImageButton cImageButtonNotification;

    private ConstraintLayout cBottomBar;
    private ConstraintLayout cLayoutPractice;
    private ImageView cImageViewPractice;
    private ConstraintLayout cLayoutAdvise;
    private ConstraintLayout cLayoutSearch;
    private ConstraintLayout cLayoutProfile;
    private TextView cTextPractice;
    private ImageView cFilterimage;

    ListPractice adapter;
    private ListView cListView;
    private List<HashMap<String, String>> cListQuestion = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        cListView = (ListView) findViewById(R.id.ListViewPractice);
        cListView.setAdapter(null);

        cButtonChat = (ImageButton) findViewById(R.id.ImageButtonChat);
        cImageButtonNotification = (ImageButton) findViewById(R.id.ImageButtonNotification);


//        cTextNoQuestion = (TextView) findViewById(R.id.textNoQuestion);
//        if(cListView.getVisibility()==View.VISIBLE){
//            cTextNoQuestion.setVisibility(View.INVISIBLE);
//        }else {
//            cTextNoQuestion.setVisibility(View.VISIBLE);
//        }

        cImageButtonChat = (ImageView) findViewById(R.id.ImageButtonChat);
        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
        cLayoutPractice = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutPractice);
        cImageViewPractice = (ImageView) cBottomBar.findViewById(R.id.ImageViewPractice);
        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
        cTextPractice = (TextView) cBottomBar.findViewById(R.id.TextPractice);
        cFilterimage = (ImageView) findViewById(R.id.filterimage);

        cTextPractice.setTextColor(getResources().getInteger(R.color.secondary));
        cImageViewPractice.setColorFilter(getResources().getInteger(R.color.secondary));

        //OnClick
        cFilterimage.setOnClickListener(clickListener);
        cImageButtonChat.setOnClickListener(clickListener);
        cImageButtonNotification.setOnClickListener(clickListener);
        cButtonChat.setOnClickListener(clickListener);
        cLayoutAdvise.setOnClickListener(clickListener);
        cLayoutSearch.setOnClickListener(clickListener);
        cLayoutProfile.setOnClickListener(clickListener);



        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth cAuth = FirebaseAuth.getInstance();
                DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
                User user = dataSnapshot.getValue(User.class);
                fillterLanguage = user.learnFull.toString().split(",");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        check = "1";
        ListPractice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OnlineTimer(true);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (getCurrentFocus() != null) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
            switch (v.getId()) {
                case R.id.ImageButtonChat:
                    startActivity(new Intent(getApplicationContext(), ChatList.class));
                    break;
                case R.id.ImageButtonNotification:
                    startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                    break;
                case R.id.LayoutAdvise:
                    startActivity(new Intent(PracticeActivity.this, AdviseActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(PracticeActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(PracticeActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.filterimage:
                    listLearn();
                    break;
            }
        }
    };


    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }

    private void ListPractice() {
        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth cAuth = FirebaseAuth.getInstance();
                DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
                User user = dataSnapshot.getValue(User.class);
                learnLanguage = user.learnFull.toString().split(",");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        cDatabaseRef.child("question").orderByChild("DESCQuestionTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cListQuestion.clear();
                adapter = new ListPractice(getApplicationContext(), cListQuestion);
                cListView.setAdapter(adapter);

                for (final DataSnapshot questionID : dataSnapshot.getChildren()) {
                    final Question question = questionID.getValue(Question.class);
                    String questionLanguage = question.QuestionLanguage.toString();

                    if (check.matches("1")) {
                        if (!Arrays.asList(learnLanguage).contains(questionLanguage)) {
                            continue; //show only learnLanguage
                        }
                    } else {
                        if (!fillterLanguage[0].matches(questionLanguage)) {
                            continue;
                        }

                        if (!Arrays.asList(learnLanguage).contains(questionLanguage)) {
                            continue; //show only learnLanguage
                        }
                    }

                    String user = question.QuestionAuthor;
                    if (cAuth.getCurrentUser().getUid().equals(user))
                        continue;
                    cDatabaseRef.child("user").child(question.QuestionAuthor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final User user = dataSnapshot.getValue(User.class);
                            final HashMap<String, String> map = new HashMap<String, String>();
                            cDatabaseRef.child("answer").child(question.QuestionAuthor).child(questionID.getKey()).child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        map.put("QuestionID", questionID.getKey());
                                        map.put("AuthorID", question.QuestionAuthor);
                                        map.put("AuthorPicture", user.profilePicture);
                                        map.put("AuthorName", user.fullName);
                                        map.put("QuestionLanguage", question.QuestionLanguage);
                                        map.put("QuestionType", question.QuestionType);
                                        map.put("Question", question.Question);
                                        map.put("QuestionPicture", question.QuestionPicture);
                                        map.put("ChoiceA", question.ChoiceA);
                                        map.put("ChoiceB", question.ChoiceB);
                                        map.put("ChoiceC", question.ChoiceC);
                                        map.put("ChoiceD", question.ChoiceD);
                                        map.put("Answer", question.Answer);

                                        cListQuestion.add(map);

                                        adapter = new ListPractice(getApplicationContext(), cListQuestion);
                                        cListView.setAdapter(adapter);

                                        cListView.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    cTextNoQuestion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(PracticeActivity.this, QuestionActivity.class).putExtra("map", cListQuestion.get(position)));
            }
        });
    }

    private void listLearn() {
        AlertDialog.Builder listLearn = new AlertDialog.Builder(PracticeActivity.this);
        listLearn.setItems(learnLanguage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        fillterLanguage[0] = learnLanguage[0];
                        check = "2";
                        ListPractice();
                        break;
                    case 1:
                        fillterLanguage[0] = learnLanguage[1];
                        check = "2";
                        ListPractice();
                        break;
                    case 2:
                        fillterLanguage[0] = learnLanguage[2];
                        check = "2";
                        ListPractice();
                        break;
                    case 3:
                        fillterLanguage[0] = learnLanguage[3];
                        check = "2";
                        ListPractice();
                        break;
                    case 4:
                        fillterLanguage[0] = learnLanguage[4];
                        check = "2";
                        ListPractice();
                        break;
                    case 5:
                        fillterLanguage[0] = learnLanguage[5];
                        check = "2";
                        ListPractice();
                        break;
                    case 6:
                        fillterLanguage[0] = learnLanguage[6];
                        check = "2";
                        ListPractice();
                        break;
                    case 7:
                        fillterLanguage[0] = learnLanguage[7];
                        check = "2";
                        ListPractice();
                        break;
                    case 8:
                        fillterLanguage[0] = learnLanguage[8];
                        check = "2";
                        ListPractice();
                        break;
                    case 9:
                        fillterLanguage[0] = learnLanguage[9];
                        check = "2";
                        ListPractice();
                        break;
                    case 10:
                        fillterLanguage[0] = learnLanguage[10];
                        check = "2";
                        ListPractice();
                        break;
                    case 11:
                        fillterLanguage[0] = learnLanguage[11];
                        check = "2";
                        ListPractice();
                        break;
                }
            }
        });
        listLearn.show();
    }
}

class ListPractice extends ArrayAdapter {

    List<HashMap<String, String>> cListQuestion;

    public ListPractice(Context context, List<HashMap<String, String>> listUser) {
        super(context, R.layout.listview_practice, R.id.TextQuestion, listUser);
        this.cListQuestion = listUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListQuestion.get(position);

        String vAuthorID = map.get("AuthorID");
        String vAuthorPicture = map.get("AuthorPicture");
        String vQuestionAuthorName = map.get("AuthorName");
        String vQuestionLanguage = map.get("QuestionLanguage");
        String vQuestionType = map.get("QuestionType");
        String vQuestion = map.get("Question");
        String vImage = map.get("QuestionPicture");
//        final String vUID = map.get("UID");
//        final String vEmail = map.get("email");
//        String vCountry = map.get("country");
//        String vNative = map.get("native");
//        String vLearn = map.get("learnAbbreviation");


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_practice, parent, false);

        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        TextView TextQuestionAuthor = (TextView) row.findViewById(R.id.TextQuestionAuthor);
        TextView TextQuestionType = (TextView) row.findViewById(R.id.QuestionType);
        TextView TextQuestion = (TextView) row.findViewById(R.id.TextViewQuestion);
        final ImageView ImageViewQuestion = (ImageView) row.findViewById(R.id.ImageViewQuestion);


        MyClass mc = new MyClass();
        mc.SetImage(getContext(), ImageViewPicture, vAuthorPicture, vAuthorID);
        TextQuestionAuthor.setText(vQuestionAuthorName);
        TextQuestion.setText("\t\t\t\t" + vQuestion + "\n");
        TextQuestionType.setText(vQuestionLanguage + " (" + vQuestionType + ")");
        Picasso.with(getContext()).load(vImage).into(ImageViewQuestion);
//        TextName.setText(vName);
//        TextNative.setText("Native: " + vNative);
//        TextLearn.setText("Learning: " + vLearn.replaceAll(",", ", "));

        return row;
    }
}
