package com.mrmeng.gitlab.teacher;

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
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmeng.gitlab.Tools.DividerItemDecoration;
import com.mrmeng.gitlab.VO.StudentVO;
import com.mrmeng.mrmeng.gitlab.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mr.meng on 17/6/14.
 */
public class ClassDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<StudentVO> students = new ArrayList<>();
    private classAdapter classAdapter;
    private final OkHttpClient client = new OkHttpClient();
    JSONArray reponseJSON;
    String groupId;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    System.out.println("msg.arg1:" + msg.arg1);
                    System.out.println("msg.arg2:" + msg.arg2);
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
        setContentView(R.layout.teacher_class_detail);
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        groupId= bundle.getString("groupId");
        Log.i("groupId is !!!!!!!!",groupId);
        Toast.makeText(ClassDetailActivity.this, "groupId is "+groupId, Toast.LENGTH_SHORT).show();

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

        mRecyclerView = (RecyclerView) findViewById(R.id.classDetailView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new classAdapter();
        mRecyclerView.setAdapter(classAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    protected void initData() throws  IOException, JSONException {//调用接口，获取数据
        int groupID = Integer.parseInt(groupId);
        JSONObject jsonOb = new JSONObject();
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");
        String authStr="liuqin:123";
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url("http://115.29.184.56:8090/api/group/"+groupID+"/students")
                .get()
                .build();

//        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        students = objectMapper.readValue(responseString,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,StudentVO.class));
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
                    ClassDetailActivity.this).inflate(R.layout.teacher_classdetail_listview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position)
        {
            holder.name.setText("姓名: "+students.get(position).getName());
            holder.id.setText("ID: "+students.get(position).getId());
            holder.studentNum.setText("ID: "+students.get(position).getNumber());
            holder.sex.setText("性别: "+students.get(position).getGender());
            holder.email.setText("email: "+students.get(position).getEmail());
            holder.gitAccount.setText("github账户: "+students.get(position).getGitUsername());


            //add Listener
//            holder.tv.setText(mDatas.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(ClassDetailActivity.this, position + "click the item", Toast.LENGTH_SHORT).show();
                }
            });
        }




        @Override
        public int getItemCount()
        {
            return students.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView name;
            TextView id;
            TextView sex;
            TextView email;
            TextView gitAccount;
            TextView studentNum;

            public MyViewHolder(View view)
            {
                super(view);
                name = (TextView) view.findViewById(R.id.studentName);
                id = (TextView) view.findViewById(R.id.studentId);
                sex = (TextView) view.findViewById(R.id.detailStudentSex);
                email = (TextView) view.findViewById(R.id.studentEmailAddress);
                gitAccount = (TextView) view.findViewById(R.id.gitUserName);
                studentNum = (TextView) view.findViewById(R.id.studentNum);
            }
        }


    }


}
