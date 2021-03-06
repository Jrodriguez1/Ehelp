package com.ehelp.user.contactlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.R;
import com.ehelp.map.sendhelp_map;
import com.ehelp.send.CountNum;
import com.ehelp.send.SendQuestion;
import com.ehelp.user.lovebank.TransferActivity;
import com.ehelp.utils.RequestHandler;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.androidinject.annotation.present.AIActionBarActivity;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@AILayout(R.layout.activity_add_friend)
public class AddFriendActivity extends AIActionBarActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    @AIView(R.id.label_list_sample_rfal)
    private RapidFloatingActionLayout rfaLayout;
    @AIView(R.id.label_list_sample_rfab)
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    //toolbar
    private Toolbar mToolbar;
    //用于后台数据交互
    private String message;
    private String jsonStrng;
    //用于获取当前登录id
    private SharedPreferences SharedPref;

    private int idd;//查询的用户ID
    private String name;//查询用户名
    String phone="";//要查询的手机号码

    //0为添加好友，1为转账
    private int type, coin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("QueryPerson",-1);
        coin = getIntent().getIntExtra("coin", 0);
        //set toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView tvv =(TextView) findViewById(R.id.titlefortoolbar);
        if (type == 0) {
            tvv.setText("添加好友");
        } else {
            tvv.setText("查询");
        }
        //toolbar设置结束

        //获取当前登录用户id
        SharedPref = this.getSharedPreferences("user_id", MODE_PRIVATE);

        //set FAB
        fab();

        clickbutton();
    }
    private void fab(){
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("求救")
                        .setResId(R.mipmap.ic_launcher)
                        .setIconNormalColor(0xffd84315)
                        .setIconPressedColor(0xffbf360c)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("求助")
//                        .setResId(R.mipmap.ico_test_c)
                        .setDrawable(getResources().getDrawable(R.mipmap.ic_launcher))
                        .setIconNormalColor(0xff4e342e)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(context, 4)))
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("提问")
                        .setResId(R.mipmap.ic_launcher)
                        .setIconNormalColor(0xff056f00)
                        .setIconPressedColor(0xff0d5302)
                        .setLabelColor(0xff056f00)
                        .setWrapper(2)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(context, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(context, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if (position == 0) {
            Intent intent = new Intent(this, CountNum.class);
            startActivity(intent);
        } else
        if (position == 1) {
            Intent intent = new Intent(this, sendhelp_map.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SendQuestion.class);
            startActivity(intent);
        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position == 0) {
            Intent intent = new Intent(this, CountNum.class);
            startActivity(intent);
        } else
        if (position == 1) {
            Intent intent = new Intent(this, sendhelp_map.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SendQuestion.class);
            startActivity(intent);
        }
        rfabHelper.toggleContent();
    }


    /*
    查询按钮监听事件：从后台获取用户信息放至该页面，
     */
    public void chaxun(View v){
        EditText editText1 =(EditText)findViewById(R.id.editText_comment);
        phone=editText1.getText().toString();

        if(!phone.isEmpty()) {//当输入的手机号不为空
            jsonStrng = "{" +
                    "\"phone\":\"" + phone + "\"}";
            message = RequestHandler.sendPostRequest(
                    "http://120.24.208.130:1501/user/get_information", jsonStrng);
            Log.v("addfatest", message);
            if (message == "false") {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "连接失败，请检查网络是否连接并重试",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            try{
                JSONObject jO = new JSONObject(message);
                if (jO.getInt("status") == 500) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "该用户未注册",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }else {
                    Toast.makeText(getApplicationContext(), "查询成功",
                            Toast.LENGTH_SHORT).show();
                    name = jO.getString("name");//获取用户名
                    //使隐藏的页卡显示
                    RelativeLayout rlll = (RelativeLayout) findViewById(R.id.rll);
                    rlll.setVisibility(View.VISIBLE);

                    //修改页卡中显示的名字
                    TextView tv =(TextView) findViewById(R.id.name);
                    if(jO.getString("nickname") !="") {
                        tv.setText(jO.getString("nickname"));
                    }else {
                        tv.setText(jO.getString("name"));
                    }
                    //修改显示的年龄
                    TextView tv1 =(TextView) findViewById(R.id.age);
                    tv1.setText("年龄："+jO.getInt("age"));
                    //修改显示的性别
                    TextView tv2 =(TextView) findViewById(R.id.gender);
                    if(jO.getInt("gender")==1) {
                        tv2.setText("性别：男");
                    }
                    //获取查询的用户的id
                    idd = jO.getInt("id");
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "手机号不能为空",
                    Toast.LENGTH_SHORT).show();
        }


    }

    /*
    *
    * */
    public void clickbutton() {
        Button button_ = (Button)findViewById(R.id.clicutton);
        if (type == 0) {
            button_.setText("添加");
            button_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    addfriend();
                }
            });
        } else {
            button_.setText("转账");
            button_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(AddFriendActivity.this, TransferActivity.class);
                    intent.putExtra("id", idd);
                    intent.putExtra("name", name);
                    intent.putExtra("coin", coin);
                    startActivity(intent);
                }
            });
        }

    }

    public void addfriend(){
        //这个id等于当前登录用户的id
        int id = SharedPref.getInt("user_id", -1);

        jsonStrng = "{" +
                "\"id\":" + id + "," +
                "\"user_id\":" + idd + "," +
                "\"operation\":" + 1 + "," +
                "\"type\":" + 2 + "}";
        message = RequestHandler.sendPostRequest(
                "http://120.24.208.130:1501/user/relation_manage", jsonStrng);
        if (message == "false") {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "连接失败，请检查网络是否连接并重试",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        try{
            JSONObject jO = new JSONObject(message);
            if (jO.getInt("status") == 500) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //返回500
                        Toast.makeText(getApplicationContext(), "添加失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }else {
                //返回200
                Toast.makeText(getApplicationContext(), "添加成功",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void detail(View v){
        Intent intent = new Intent(this, messageActivity.class);
        intent.putExtra("type",0);//0表示非好友1表示好友2表示紧急联系人
        intent.putExtra("id",idd);
        startActivity(intent);
    }
}
