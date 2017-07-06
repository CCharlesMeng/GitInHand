package com.mrmeng.gitlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mrmeng.gitlab.student.StudentMyListActivity;
import com.mrmeng.mrmeng.gitlab.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A activity_login screen that offers activity_login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    /**
     * Keep track of the activity_login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText username;
    private EditText password;
    JSONObject reponseJSON;
    private boolean ifLogin = false;

    private Handler handler = new Handler();
    //private GoogleApiClient ;

   private  GoogleApiClient googleClient;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    System.out.println("msg.arg1:" + msg.arg1);
                    System.out.println("msg.arg2:" + msg.arg2);

                    try {
                        System.out.println("name is "+reponseJSON.get("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    login();
                    break;
                default:
                    System.out.println("login failed!");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // Set up the activity_login form.
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, username.getText().toString(), Toast.LENGTH_SHORT).show();
                //   login();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            attemptLogin();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //   googleClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            }
        });
    }

            private final OkHttpClient client = new OkHttpClient();

            private void login() {

                final String usernameStr = username.getText().toString().trim();

                if (usernameStr.equals("s")) {
                    Intent intent = new Intent(LoginActivity.this, StudentMyListActivity.class);
                    startActivity(intent);
                }
                if (usernameStr.equals("liuqin")) {
                    Intent intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Identify", "teacher");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }


    private void attemptLogin() throws IOException, JSONException {
        String usernameStr = username.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("username","nanguangtailang");
        jsonOb.put("password","123");
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        Request request = new Request.Builder()
                .url("http://115.29.184.56:8090/api/user/auth")
                .post(body)
                .build();

//        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        reponseJSON = new JSONObject(responseString.toString());

        Log.i("response",responseString);

        ifLogin = true;
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = 123;
        msg.arg2 = 321;
        uiHandler.sendMessage(msg);
    }

}

