package com.mrmeng.gitlab.teacher;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmeng.gitlab.Tools.DividerItemDecoration;
import com.mrmeng.gitlab.TeacherMainActivity;
import com.mrmeng.gitlab.VO.TeacherListVO;
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
public class MyListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<TeacherListVO> teacherList = new ArrayList<>();
    private classAdapter classAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<String> data_list;
    private ArrayList<String> data_list_english;
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
        setContentView(R.layout.teacher_my_list);

        btButton1 = (Button) findViewById(R.id.button1);
        btButton2 = (Button) findViewById(R.id.button2);
        btButton3 = (Button) findViewById(R.id.button3);

        btButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(MyListActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyMain();
            }
        });

        btButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(MyListActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyList();
            }
        });

        btButton3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(MyListActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyWork();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.teacherChooseWork);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("考试");
        data_list.add("作业");
        data_list.add("练习");

        data_list_english = new ArrayList<String>();
        data_list_english.add("exam");
        data_list_english.add("homework");
        data_list_english.add("exercise");

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


        mRecyclerView = (RecyclerView) findViewById(R.id.classDetailView);
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
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        String requestUrl ="";
        switch (position){
            case 0:
                requestUrl="http://115.29.184.56:8090/api/course/2/exam";
                break;
            case 1:
                requestUrl="http://115.29.184.56:8090/api/course/2/homework";
                break;
            case 2:
                requestUrl="http://115.29.184.56:8090/api/course/2/exercise";
                break;
        }
        Log.i("request Url is !!!!!!!",requestUrl);
        Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url(requestUrl)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        teacherList = objectMapper.readValue(responseString,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,TeacherListVO.class));
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
                    MyListActivity.this).inflate(R.layout.teacher_mylist_listview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position)
        {
            holder.name.setText("名称: "+teacherList.get(position).getTitle());
            holder.desc.setText("描述: "+teacherList.get(position).getDescription());
            holder.start.setText("开始时间: "+teacherList.get(position).getStartAt());
            holder.end.setText("结束时间: "+teacherList.get(position).getEndAt());
            holder.status.setText("状态: "+teacherList.get(position).getStatus());

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(MyListActivity.this, position + "click the item", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public int getItemCount()
        {
            return teacherList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView name;
            TextView desc;
            TextView start;
            TextView end;
            TextView status;

            public MyViewHolder(View view)
            {
                super(view);
                name = (TextView) view.findViewById(R.id.courseName);
                desc = (TextView) view.findViewById(R.id.courseDescription);
                start = (TextView) view.findViewById(R.id.startTime);
                end = (TextView) view.findViewById(R.id.endTime);
                status = (TextView) view.findViewById(R.id.courseStatus);
            }
        }


    }

    public void goMyMain (){
        Intent intent = new Intent(MyListActivity.this, TeacherMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Identify", "teacher");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void goMyList (){
        Intent intent = new Intent(MyListActivity.this, MyListActivity.class);
        startActivity(intent);
    }
    public void goMyWork(){
        Intent intent = new Intent(MyListActivity.this, TeacherHomeworkActivity.class);
        startActivity(intent);
    }
}
