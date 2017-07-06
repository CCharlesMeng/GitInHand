package com.mrmeng.gitlab.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmeng.gitlab.Tools.DividerItemDecoration;
import com.mrmeng.gitlab.TeacherMainActivity;
import com.mrmeng.gitlab.VO.TeacherScore.TeacherScoreVO;
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
public class TeacherHomeworkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TeacherScoreVO teacherScore;
    private classAdapter classAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private int selectedPsition;

    Button btButton1;
    Button btButton2;
    Button btButton3;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    System.out.println("msg.arg1:" + msg.arg1);
                    System.out.println("msg.arg2:" + msg.arg2);
                    initView();
                    classAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the activity_login form.
        setContentView(R.layout.teacher_my_score);

        Spinner spinner = (Spinner) findViewById(R.id.teacherChooseWork);

        btButton1 = (Button) findViewById(R.id.button1);
        btButton2 = (Button) findViewById(R.id.button2);
        btButton3 = (Button) findViewById(R.id.button3);

        btButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherHomeworkActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyMain();
            }
        });

        btButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherHomeworkActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyList();
            }
        });

        btButton3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherHomeworkActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyWork();
            }
        });


        //数据
        data_list = new ArrayList<String>();
        data_list.add("38");
        data_list.add("93");


        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器


        new Thread() {
            @Override
            public void run() {
                try {
                    initData(0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedPsition= position;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                initData(selectedPsition);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.myScoreListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new classAdapter();
        mRecyclerView.setAdapter(classAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    protected void initData(int position) throws  IOException, JSONException {//调用接口，获取数据
        JSONObject jsonOb = new JSONObject();
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");
        String authStr="liuqin:123";
        String assignmentId =data_list.get(position).toString();
        System.out.println("assignment id is "+assignmentId);
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url("http://115.29.184.56:8090/api/assignment/"+assignmentId+"/score")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
      //  System.out.println("size is "+teacherScore.getQuestions().size());
        teacherScore = objectMapper.readValue(responseString, TeacherScoreVO.class);
        System.out.println("sizeeee is "+teacherScore.getQuestions().size());

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
                    TeacherHomeworkActivity.this).inflate(R.layout.teacher_myscore_listview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position)
        {
            System.out.println("position now is +++"+position);
            holder.title.setText("名称: "+teacherScore.getQuestions().get(position).getQuestionInfo().getTitle());
            holder.desc.setText("描述: "+teacherScore.getQuestions().get(position).getQuestionInfo().getDescription());
            holder.type.setText("类型: "+teacherScore.getQuestions().get(position).getQuestionInfo().getType());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    goHomeworkDetail(teacherScore.getAssignmentId()+"",position);


                    Toast.makeText(TeacherHomeworkActivity.this, position + "click the item", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public int getItemCount()
        {
            return teacherScore.getQuestions().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView title;
            TextView type;
            TextView desc;

            public MyViewHolder(View view)
            {
                super(view);
                title = (TextView) view.findViewById(R.id.examTitle);
                desc = (TextView) view.findViewById(R.id.examDescription);
                type = (TextView) view.findViewById(R.id.examType);
            }
        }
    }


    public void goHomeworkDetail(String assignmentId,int position){
        Intent intent = new Intent(TeacherHomeworkActivity.this, TeacherHomeworkDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("assignmentId", assignmentId);
        bundle.putString("position", position+"");
       // bundle.putParcelable("students", (Parcelable) students);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goMyMain (){
        Intent intent = new Intent(TeacherHomeworkActivity.this, TeacherMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Identify", "teacher");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void goMyList (){
        Intent intent = new Intent(TeacherHomeworkActivity.this, MyListActivity.class);
        startActivity(intent);
    }
    public void goMyWork(){
        Intent intent = new Intent(TeacherHomeworkActivity.this, TeacherHomeworkActivity.class);
        startActivity(intent);
    }
}
