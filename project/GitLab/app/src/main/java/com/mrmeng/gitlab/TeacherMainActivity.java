package com.mrmeng.gitlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmeng.gitlab.Tools.DividerItemDecoration;
import com.mrmeng.gitlab.VO.ClassVO;
import com.mrmeng.gitlab.teacher.ClassDetailActivity;
import com.mrmeng.gitlab.teacher.MyListActivity;
import com.mrmeng.gitlab.teacher.TeacherHomeworkActivity;
import com.mrmeng.mrmeng.gitlab.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mr.meng on 17/6/14.
 */
public class TeacherMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<ClassVO> classList = new ArrayList<>();
    private classAdapter classAdapter;
    private final OkHttpClient client = new OkHttpClient();
    Button btButton1;
    Button btButton2;
    Button btButton3;


    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    System.out.println("msg.arg1:++++++" + msg.arg1);
                    System.out.println("msg.arg2:++++++" + msg.arg2);
                    mRecyclerView.setAdapter(classAdapter);
                    classAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the activity_login form.
        setContentView(R.layout.teacher_main);
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String name = bundle.getString("Identify");
        //Log.i("name",name);
        //Toast.makeText(TeacherMainActivity.this, "name is !"+name, Toast.LENGTH_SHORT).show();

        btButton1 = (Button) findViewById(R.id.button1);
        btButton2 = (Button) findViewById(R.id.button2);
        btButton3 = (Button) findViewById(R.id.button3);

        btButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherMainActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyMain();
            }
        });

        btButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherMainActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyList();
            }
        });

        btButton3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherMainActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyWork();
            }
        });


        new Thread() {
            @Override
            public void run() {
                try {
                    initData();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        mRecyclerView = (RecyclerView) findViewById(R.id.classRView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new classAdapter();
        mRecyclerView.setAdapter(classAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    protected void initData() throws  IOException, JSONException {//调用接口，获取数据
        JSONObject jsonOb = new JSONObject();
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");
        String authStr="liuqin:123";
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url("http://115.29.184.56:8090/api/group")
                .get()
                .build();

//        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        classList = objectMapper.readValue(responseString,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,ClassVO.class));

        Log.i("response",classList.get(0).getName());

        Message msg = new Message();
        msg.what = 1;

        uiHandler.sendMessage(msg);

    }

    class classAdapter extends RecyclerView.Adapter<classAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    TeacherMainActivity.this).inflate(R.layout.class_listview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position)
        {
            holder.name.setText("班级名称： "+classList.get(position).getName());
            holder.id.setText("ID： "+classList.get(position).getId());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    goClassDetail(position);
                    //Toast.makeText(TeacherMainActivity.this, position + "click the item", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return classList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView name;
            TextView id;
            public MyViewHolder(View view)
            {
                super(view);
                name = (TextView) view.findViewById(R.id.className);
                id = (TextView) view.findViewById(R.id.classId);
            }
        }


    }


    public void goClassDetail(int position){
        String groupId = classList.get(position).getId()+"";
        Intent intent = new Intent(TeacherMainActivity.this, ClassDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goMyMain (){
        Intent intent = new Intent(TeacherMainActivity.this, TeacherMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Identify", "teacher");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void goMyList (){
        Intent intent = new Intent(TeacherMainActivity.this, MyListActivity.class);
        startActivity(intent);
    }
    public void goMyWork(){
        Intent intent = new Intent(TeacherMainActivity.this, TeacherHomeworkActivity.class);
        startActivity(intent);
    }

}
