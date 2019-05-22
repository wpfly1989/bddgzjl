package com.example.lenovo.baodingdongguzhangjilu;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText didian;
    private EditText shijian;
    private Button queding;
    private EditText jilu;
    private TextView chakan;
    private long exitTime = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        didian = (EditText)findViewById(R.id.didianedittext);
        shijian = (EditText)findViewById(R.id.shijianedittext);
        queding = (Button)findViewById(R.id.queding);
        jilu = (EditText)findViewById(R.id.jiluedittext);
        chakan = (TextView)findViewById(R.id.chakantextview);

        testNetwork();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date;
        date = new Date(System.currentTimeMillis());
        shijian.setText(simpleDateFormat.format(date));


        final String[] didianlist = {"ZZD-GBDD07","方官AT所","高碑店东站","高碑店10KV配电所","GBDD-XSD01","GBDD-XSD02","GBDD-XSD03","臧官营AT所","GBDD-XSD04","GBDD-XSD05(中继）","GBDD-XSD06","姚家庄牵引变电所","GBDD-XSD07","GBDD-XSD08","GBDD-XSD09","徐水东站","崔庄AT所","XSD-BDD01","XSD-BDD02","XSD-BDD03","XSD-BDD04","XSD-BDD05（中继）","西小营AT所","XSD-BDD06","XSD-BDD07","XSD-BDD08","保养点","保定东10KV配电所","保定东站","孙村AT所","BDD-DZD01","BDD-DZD02","BDD-DZD03","BDD-DZD04（中继）","BDD-DZD05","保定东牵引变电所","BDD-DZD06","BDD-DZD07","BDD-DZD08（中继）","BDD-DZD09","靳庄AT所","BDD-DZD10","BDD-DZD11"};

        didian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didian.setFocusable(true);
                didian.setFocusableInTouchMode(true);
                didian.requestFocus();

                final ListPopupWindow listPopupWindow;
                listPopupWindow = new ListPopupWindow(MainActivity.this);
                listPopupWindow.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, didianlist));//用android内置布局，或设计自己的样式
                listPopupWindow.setAnchorView(didian);//以哪个控件为基准，在该处以mEditText为基准
                listPopupWindow.setModal(true);


                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        didian.setText(didianlist[i]);//把选择的选项内容展示在EditText上
                        listPopupWindow.dismiss();//如果已经选择了，隐藏起来
                    }
                });
                listPopupWindow.show();//把ListPopWindow展示出来
            }
        });

        didian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                didian.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String s1 = "'" + didian.getText().toString() + "'";
                final String s2 = "'" + shijian.getText().toString() + "'";
                final String s3 = "'" + jilu.getText().toString() + "'";

                if (didian.getText().toString().trim().equals("") || jilu.getText().toString().trim().equals("") || shijian.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"不能有空值！",Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setMessage("数据添加中......");
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    final int[] i = new int[1];
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               Class.forName("com.mysql.jdbc.Driver").newInstance();
                               Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://43.226.43.155:3306/mysql190312606e_db", "mysql190312606e", "60S6e49405");
                               PreparedStatement ps = (PreparedStatement) con.prepareStatement("insert into bddgzjl(adds,date,what)values(" + s1 + "," + s2 + "," + s3 + ")");

                               i[0] = ps.executeUpdate();

                               ps.close();
                               con.close();
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {

                                       if (i[0] > 0) {
                                           alertDialog.dismiss();

                                           AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(MainActivity.this);

                                           alertDialogBuilder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   didian.setText("");
                                                   jilu.setText("");

                                               }
                                           });

                                           AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                           alertDialog1.setTitle("提示：");
                                           alertDialog1.setMessage("数据添加成功！");
                                           alertDialog1.setCancelable(false);
                                           alertDialog1.show();
                                       } else {
                                           alertDialog.dismiss();
                                           Toast.makeText(MainActivity.this, "数据库故障，请及时联系作者", Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               });
                           }catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                   }).start();
                }
            }
        });

        chakan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

    }

    public void testNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(MainActivity.this);

                            alertDialogBuilder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });

                            AlertDialog alertDialog1 = alertDialogBuilder1.create();
                            alertDialog1.setTitle("提示：");
                            alertDialog1.setMessage("手机没有网络！！");
                            alertDialog1.setCancelable(false);
                            alertDialog1.show();
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {

                Toast.makeText(MainActivity.this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
