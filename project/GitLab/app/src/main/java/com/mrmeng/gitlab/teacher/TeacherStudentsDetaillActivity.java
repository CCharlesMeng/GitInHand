package com.mrmeng.gitlab.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.mrmeng.gitlab.TeacherMainActivity;
import com.mrmeng.gitlab.Tools.DividerItemDecoration;
import com.mrmeng.gitlab.VO.ContentVO;
import com.mrmeng.gitlab.VO.StudentAnalysis.AnalysisVO;
import com.mrmeng.gitlab.VO.TeacherScore.Students;
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
public class TeacherStudentsDetaillActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private classAdapter classAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private AnalysisVO analysisVO;
    private ArrayAdapter<String> arr_adapter;
    private String assignmentId;
    private ContentVO readmeStr = new ContentVO();
    private int studentId;

    Button btButton1;
    Button btButton2;
    Button btButton3;

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    initView();
                    classAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private Handler uiHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the activity_login form.
        setContentView(R.layout.teacher_my_score_detail_detail);
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String assignmentId = bundle.getString("assignmentId");
        String studentId = bundle.getString("studentId");

        btButton1 = (Button) findViewById(R.id.button1);
        btButton2 = (Button) findViewById(R.id.button2);
        btButton3 = (Button) findViewById(R.id.button3);

        btButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherStudentsDetaillActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyMain();
            }
        });

        btButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherStudentsDetaillActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
                goMyList();
            }
        });

        btButton3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(TeacherStudentsDetaillActivity.this,"click the button!",Toast.LENGTH_SHORT ).show();
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

    }

    public void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.myScoreDetailDetailListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new classAdapter();
        mRecyclerView.setAdapter(classAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    protected void initData() throws  IOException, JSONException {//调用接口，获取数据
        Bundle bundle = this.getIntent().getExtras();
        assignmentId= bundle.getString("assignmentId");
        studentId= Integer.parseInt(bundle.getString("studentId"));
        JSONObject jsonOb = new JSONObject();
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");
        String authStr="liuqin:123";
        System.out.println("assignment id is "+assignmentId);
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
        Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url("http://115.29.184.56:8090/api/assignment/"+assignmentId+"/student/"+studentId+"/analysis")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        //  System.out.println("size is "+teacherScore.getQuestions().size());
        analysisVO = objectMapper.readValue(responseString, AnalysisVO.class);


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
                    TeacherStudentsDetaillActivity.this).inflate(R.layout.teacher_myscore_detail_detail_listview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position)
        {
            System.out.println("position now is +++"+position);

            holder.homeworkNameTitle.setText("考试名称: "+analysisVO.getQuestionResults().get(position).getQuestionTitle());
            holder.marks.setText("成绩: "+analysisVO.getQuestionResults().get(position).getScoreResult());
//            holder.marks.setText("成绩: "+analysisVO.getQuestionResults().get(position).getQuestionId());
            holder.totalCount.setText("总计: "+analysisVO.getQuestionResults().get(position).getMetricData().getTotal_line_count());
            holder.commentCount.setText("注释: "+analysisVO.getQuestionResults().get(position).getMetricData().getComment_line_count());
            holder.fieldCount.setText("区域: "+analysisVO.getQuestionResults().get(position).getMetricData().getField_count());
            holder.methodCount.setText("方法: "+analysisVO.getQuestionResults().get(position).getMetricData().getMethod_count());
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                getReadme(position);   //调用网络接口
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentsDetaillActivity.this);
//                        builder.setIcon(R.drawable.ic_launcher);//设置图标
                    builder.setTitle("README 内容");//设置对话框的标题
                    builder.setMessage(readmeStr.getContent());//设置对话框的内容
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(TeacherStudentsDetaillActivity.this, "ok那好吧", Toast.LENGTH_SHORT).show();

                        }
                    });
                    AlertDialog b = builder.create();
                    b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
                  //  Toast.makeText(TeacherStudentsDetaillActivity.this, position + "click the item", Toast.LENGTH_SHORT).show();
                }



            });
        }

        @Override
        public int getItemCount()
        {
            return analysisVO.getQuestionResults().size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView homeworkNameTitle;
            TextView marks;
            TextView totalCount;
            TextView commentCount;
            TextView fieldCount;
            TextView methodCount;

            public MyViewHolder(View view)
            {
                super(view);
                homeworkNameTitle = (TextView) view.findViewById(R.id.examTitleDetail);
                marks = (TextView) view.findViewById(R.id.examScoreDetail);
                totalCount = (TextView) view.findViewById(R.id.totalLineCount);
                commentCount = (TextView) view.findViewById(R.id.commentLineCount);
                fieldCount = (TextView) view.findViewById(R.id.fieldCount);
                methodCount = (TextView) view.findViewById(R.id.methodCount);
            }
        }
    }


    public void goMyMain (){
        Intent intent = new Intent(TeacherStudentsDetaillActivity.this, TeacherMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Identify", "teacher");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void goMyList (){
        Intent intent = new Intent(TeacherStudentsDetaillActivity.this, MyListActivity.class);
        startActivity(intent);
    }
    public void goMyWork(){
        Intent intent = new Intent(TeacherStudentsDetaillActivity.this, TeacherStudentsDetaillActivity.class);
        startActivity(intent);
    }


    public void getReadme(int position)throws  IOException, JSONException {
        Bundle bundle = this.getIntent().getExtras();
        assignmentId= bundle.getString("assignmentId");
        studentId= Integer.parseInt(bundle.getString("studentId"));
        String sid= studentId+"";
        String qid = analysisVO.getQuestionResults().get(position).getQuestionId()+"";
        JSONObject jsonOb = new JSONObject();
        MediaType loginJSON = MediaType.parse("application/json; charset=utf-8");
        String authStr="nanguangtailang:123";
        System.out.println("requestUrl is "+"http://115.29.184.56:8090/api/assignment/"+assignmentId+"/student/"+sid+"/question/"+qid);
        String token = new String(Base64.encode(authStr.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
        RequestBody body = RequestBody.create(loginJSON, String.valueOf(jsonOb));
            Request request = new Request.Builder().addHeader("Authorization","Basic "+token)
                .url("http://115.29.184.56:8090/api/assignment/"+assignmentId+"/student/"+sid+"/question/"+qid)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("response string is "+responseString);
        readmeStr = objectMapper.readValue(responseString, ContentVO.class);
        if(responseString==null){
            readmeStr.setContent("好像没有readme");
        }

        Message msg = new Message();
        msg.what = 1;
        uiHandler2.sendMessage(msg);

    }
}
