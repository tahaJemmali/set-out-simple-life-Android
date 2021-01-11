package tn.esprit.setoutlife.Activities.Forum;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allyants.notifyme.NotifyMe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Adapters.CommentListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.PostRepository;
import tn.esprit.setoutlife.entities.Comment;

public class CommentsActivity extends AppCompatActivity implements TextWatcher, IRepository {

    Toolbar toolbar;

    private String name;
    private WebSocket webSocket;
    //private String SERVER_PATH = "ws://10.0.2.2:3001";
    private String SERVER_PATH = "wss://setoutfahd.herokuapp.com";
    //private String SERVER_PATH = "ws://echo.websocket.org";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private CommentListAdapter commentListAdapter;
    private String post_id;
    private String user_email_post;

    ArrayList<Comment> comments;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        PostRepository.getInstance().setiRepository(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setStatusBarColor(Color.BLACK);
        }

        post_id = getIntent().getStringExtra("post_id");
        user_email_post = getIntent().getStringExtra("user_email_post");
        //comments = (ArrayList<Comment>) getIntent().getSerializableExtra("comments");

        initiateSocketConnection();
    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String string = editable.toString().trim();
        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        for (Comment comment:comments){
            JSONObject jsonObject = new JSONObject();
        try {

            //jsonObject.put("current_user_email", HomeActivity.getCurrentLoggedInUser().getEmail());
            jsonObject.put("message", comment.getText());
            jsonObject.put("firstName", comment.getUser().getFirstName());
            jsonObject.put("lastName", comment.getUser().getLastName());
            jsonObject.put("user_photo", comment.getUser().getPhoto());
            jsonObject.put("comment_date", comment.getComment_date().getTime());

            //jsonObject.put("post_id", post_id);
            //webSocket.send(jsonObject.toString());

            if (comment.getUser().getEmail().equals(HomeActivity.getCurrentLoggedInUser().getEmail()))
                jsonObject.put("isSent", true);
            else
                jsonObject.put("isSent", false);

            commentListAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(commentListAdapter.getItemCount() - 1);
            resetMessageEdit();

        } catch (JSONException e) {
            e.printStackTrace();
            }
        }
    }

    @Override
    public void doAction2() {
        //
    }


    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CommentsActivity.this,
                            "Socket Connection Successful!",
                            Toast.LENGTH_SHORT).show();

                    initializeView();
                }
            });
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            System.out.println("closed WebSocket");
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        jsonObject.put("isSent", false);

                        /*jsonObject.put("message", text);

                        jsonObject.put("firstName", HomeActivity.getCurrentLoggedInUser().getFirstName());
                        jsonObject.put("lastName", HomeActivity.getCurrentLoggedInUser().getLastName());
                        jsonObject.put("user_photo", HomeActivity.getCurrentLoggedInUser().getPhoto());
                        jsonObject.put("comment_date", new Date().getTime());*/

                        /*jsonObject.put("firstName", HomeActivity.getCurrentLoggedInUser().getFirstName()+" 2");
                        jsonObject.put("lastName", HomeActivity.getCurrentLoggedInUser().getLastName());
                        jsonObject.put("user_photo", HomeActivity.getCurrentLoggedInUser().getPhoto());*/
                        jsonObject.put("comment_date", 1000 );

                        /*if (jsonObject.getString("user_email_post").equals(HomeActivity.getCurrentLoggedInUser().getEmail())){
                            System.out.println("NOTIFICATION");

                            NotifyMe.Builder notifyMe = new NotifyMe.Builder(getApplicationContext());

                            notifyMe.content(jsonObject.getString("firstName")+" "+jsonObject.getString("lastName")+" commented on your post!");
                            notifyMe.title("Set out - Life!");
                            notifyMe.build();
                        }*/

                        commentListAdapter.addItem(jsonObject);

                        recyclerView.smoothScrollToPosition(commentListAdapter.getItemCount() - 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webSocket.close(1000,"left comments");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.recyclerView);

        commentListAdapter = new CommentListAdapter(getLayoutInflater());
        recyclerView.setAdapter(commentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadComments();

        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("current_user_email", HomeActivity.getCurrentLoggedInUser().getEmail());
                    jsonObject.put("message", messageEdit.getText().toString());
                    jsonObject.put("post_id", post_id);

                        jsonObject.put("user_email_post",user_email_post);

                        jsonObject.put("firstName", HomeActivity.getCurrentLoggedInUser().getFirstName());
                        jsonObject.put("lastName", HomeActivity.getCurrentLoggedInUser().getLastName());
                        jsonObject.put("user_photo", HomeActivity.getCurrentLoggedInUser().getPhoto());
                        jsonObject.put("comment_date", 1000);

                    webSocket.send(jsonObject.toString());

                    jsonObject.put("isSent", true);

                    commentListAdapter.addItem(jsonObject);

                    recyclerView.smoothScrollToPosition(commentListAdapter.getItemCount() - 1);

                    resetMessageEdit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadComments() {
        comments = PostRepository.getInstance().getComments(post_id,this);
    }

}
