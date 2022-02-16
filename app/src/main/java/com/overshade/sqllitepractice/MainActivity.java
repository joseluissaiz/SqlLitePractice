package com.overshade.sqllitepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;

    private ArrayList<HashMap<String, String>> commentList;

    private EditText titleTextField;
    private EditText contentTextField;
    private Button postButton;

    private Spinner titleSpinner;
    private Button showContentButton;

    private LinearLayout displayBox;
    private TextView displayTitleTextView;
    private TextView displayContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViewsToVariables();
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.setCommentListener(this::updateTitleSpinner);
        updateTitleSpinner();
        postButton.setOnClickListener(e -> createComment());
        showContentButton.setOnClickListener(
                e -> setDisplayComment(titleSpinner.getSelectedItemPosition())
        );
    }

    private void assignViewsToVariables() {
        titleTextField         = findViewById(R.id.title_edittext);
        contentTextField       = findViewById(R.id.content_edittext);
        postButton             = findViewById(R.id.post_button);
        titleSpinner           = findViewById(R.id.title_spinner);
        showContentButton      = findViewById(R.id.show_content_button);
        displayBox             = findViewById(R.id.display_box);
        displayTitleTextView   = findViewById(R.id.display_comment_title);
        displayContentTextView = findViewById(R.id.display_comment_content);
    }

    private void createComment() {
        String title = titleTextField.getText().toString();
        String content = contentTextField.getText().toString();
        if (checkValidComment(title, content)) {
            titleTextField.setText("");
            contentTextField.setText("");
            databaseHandler.insertComment(title, content);
            Toast.makeText(
                    this, "Comment created successfully", Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(
                    this, "Not a valid comment", Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean checkValidComment(String title, String content) {
        boolean valid = true;
        if (title.equals("")    || content.equals("")    )  { valid = false; }
        if (title.length() > 20 || content.length() > 150)  { valid = false; }
        if (title.length() < 5  || content.length() < 5  )  { valid = false; }
        return valid;
    }

    private void updateTitleSpinner() {
        commentList = databaseHandler.getCommentList();
        ArrayList<String> titleList = new ArrayList<>();
        for (HashMap<String, String> comment : commentList) {
            titleList.add(comment.get("title"));
        }
        titleSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, titleList
        ));
    }

    private void setDisplayComment(int position) {
        String title = commentList.get(position).get("title");
        String content = commentList.get(position).get("content");
        displayBox.setVisibility(View.VISIBLE);
        displayTitleTextView.setText(title);
        displayContentTextView.setText(content);
    }
}