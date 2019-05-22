package com.example.lenovo.baodingdongguzhangjilu;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.tip.MultiLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private com.bin.david.form.core.SmartTable<CommonItem> table;
    private List<CommonItem> commonItems = new ArrayList<>();
    private List<CommonItem> commonItems1 = new ArrayList<>();
    private BoomMenuButton boomMenuButton;
    public static int CHAXUN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        table = (com.bin.david.form.core.SmartTable<CommonItem>)findViewById(R.id.smarttable);
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boommenubutton);

        boomMenuButton.setButtonEnum(ButtonEnum.TextOutsideCircle);
        boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);

        boomMenuButton.addBuilder(new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.ts).normalText("搜索"));

        boomMenuButton.setDraggable(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    Connection con = (Connection)DriverManager.getConnection("jdbc:mysql://43.226.43.155:3306/mysql190312606e_db", "mysql190312606e", "60S6e49405");
                    Statement stmt   = (Statement) con.createStatement();

                    ResultSet result = (ResultSet) stmt.executeQuery("select * from bddgzjl");
                    while(result.next()){
                        commonItems.add(new CommonItem(result.getString("adds"),result.getString("date"),result.getString("what")));
                    }
                    result.close();
                    stmt.close();
                    con.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Column<String> name1 = new Column<String>("地点","name1");
                            Column<String> name2 = new Column<String>("日期","name2");
                            final Column<String> name3 = new Column<String>("事件历史记录","name3");

                            commonItems1.addAll(commonItems);

                            TableData<CommonItem> tableData = new TableData<CommonItem>("名称",commonItems1,name1,name2,name3);

                            table.setTableData(tableData);

                            name3.setTextAlign(Paint.Align.LEFT);

                            //progressbar消失
                            progressBar.setVisibility(View.GONE);

                            //不显示表格标题
                            table.getConfig().setShowTableTitle(false);
                            table.getConfig().setFixedYSequence(true);
                            table.getConfig().setFixedXSequence(true);

                            table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {

                                @Override
                                public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                                    if(cellInfo.row%2==0){
                                        paint.setColor(Color.parseColor("#F5F5F5"));
                                        canvas.drawRect(rect,paint);
                                    }
                                }

                                @Override
                                public int getTextColor(CellInfo cellInfo) {
                                    return 0;
                                }
                            });

                            //放大缩小
                            table.setZoom(true);

                            //点击单元格内容后放大显示
                            name1.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(Main2Activity.this);
                                    View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.name3msg,null);
                                    TextView textView = (TextView)view.findViewById(R.id.name3msgtextview);
                                    alertdialog.setView(view);
                                    textView.setText(commonItems1.get(position).getName1());
                                    AlertDialog alertDialog1 = alertdialog.create();
                                    alertDialog1.show();
                                }
                            });
                            name2.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(Main2Activity.this);
                                    View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.name3msg,null);
                                    TextView textView = (TextView)view.findViewById(R.id.name3msgtextview);
                                    alertdialog.setView(view);
                                    textView.setText(commonItems1.get(position).getName2());
                                    AlertDialog alertDialog1 = alertdialog.create();
                                    alertDialog1.show();
                                }
                            });

                            name3.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                                @Override
                                public void onClick(Column<String> column, String value, String s, int position) {
                                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(Main2Activity.this);
                                    View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.name3msg,null);
                                    TextView textView = (TextView)view.findViewById(R.id.name3msgtextview);
                                    alertdialog.setView(view);
                                    textView.setText(commonItems1.get(position).getName3());
                                    AlertDialog alertDialog1 = alertdialog.create();
                                    alertDialog1.show();
                                }
                            });


                            boomMenuButton.setOnBoomListener(new OnBoomListener() {
                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public void onClicked(int index, BoomButton boomButton) {
                                    switch (index) {
                                        case 0:
                                            AlertDialog.Builder alertDialogBuilderChaXun = new AlertDialog.Builder(Main2Activity.this);
                                            View viewForChaXun = LayoutInflater.from(Main2Activity.this).inflate(R.layout.chaxun,null);
                                            final EditText editTextForChaXun = (EditText)viewForChaXun.findViewById(R.id.guanjianzichaxun);
                                            final RadioButton didian = (RadioButton)viewForChaXun.findViewById(R.id.didianradiobutton);
                                            final RadioButton quanbu = (RadioButton)viewForChaXun.findViewById(R.id.quanburadiobutton);
                                            final RadioButton riqi = (RadioButton)viewForChaXun.findViewById(R.id.riqiradiobutton);
                                            final RadioButton guanjianzi = (RadioButton)viewForChaXun.findViewById(R.id.guanjianziradiobutton);

                                            switch (CHAXUN) {
                                                case 0:
                                                    quanbu.setChecked(true);
                                                    editTextForChaXun.setHint("点击 搜索 即可");
                                                    editTextForChaXun.setFocusable(false);
                                                    editTextForChaXun.setFocusableInTouchMode(false);
                                                    editTextForChaXun.setCursorVisible(false);
                                                    break;
                                                case 1:
                                                    didian.setChecked(true);
                                                    editTextForChaXun.setHint("");
                                                    editTextForChaXun.setFocusable(false);
                                                    editTextForChaXun.setFocusableInTouchMode(false);
                                                    editTextForChaXun.setCursorVisible(false);
                                                    break;
                                                case 2:
                                                    riqi.setChecked(true);
                                                    editTextForChaXun.setHint("");
                                                    editTextForChaXun.setFocusable(true);
                                                    editTextForChaXun.setFocusableInTouchMode(true);
                                                    editTextForChaXun.requestFocus();
                                                    editTextForChaXun.setCursorVisible(true);
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//获取当前时间
                                                    Date date;
                                                    date = new Date(System.currentTimeMillis());
                                                    editTextForChaXun.setText(simpleDateFormat.format(date));
                                                    break;
                                                case 3:
                                                    guanjianzi.setChecked(true);
                                                    editTextForChaXun.setHint("");
                                                    editTextForChaXun.setFocusable(true);
                                                    editTextForChaXun.setFocusableInTouchMode(true);
                                                    editTextForChaXun.requestFocus();
                                                    editTextForChaXun.setCursorVisible(true);
                                                    break;
                                                    default:
                                                        break;
                                            }

                                            editTextForChaXun.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    switch (CHAXUN) {
                                                        case 1:
                                                            editTextForChaXun.setFocusable(true);
                                                            editTextForChaXun.setFocusableInTouchMode(true);
                                                            editTextForChaXun.requestFocus();
                                                            final String[] didianlist = {"ZZD-GBDD07","方官AT所","高碑店东站","高碑店10KV配电所","GBDD-XSD01","GBDD-XSD02","GBDD-XSD03","臧官营AT所","GBDD-XSD04","GBDD-XSD05(中继）","GBDD-XSD06","姚家庄牵引变电所","GBDD-XSD07","GBDD-XSD08","GBDD-XSD09","徐水东站","崔庄AT所","XSD-BDD01","XSD-BDD02","XSD-BDD03","XSD-BDD04","XSD-BDD05（中继）","西小营AT所","XSD-BDD06","XSD-BDD07","XSD-BDD08","保养点","保定东10KV配电所","保定东站","孙村AT所","BDD-DZD01","BDD-DZD02","BDD-DZD03","BDD-DZD04（中继）","BDD-DZD05","保定东牵引变电所","BDD-DZD06","BDD-DZD07","BDD-DZD08（中继）","BDD-DZD09","靳庄AT所","BDD-DZD10","BDD-DZD11"};
                                                            final ListPopupWindow listPopupWindow;
                                                            listPopupWindow = new ListPopupWindow(Main2Activity.this);
                                                            listPopupWindow.setAdapter(new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1, didianlist));//用android内置布局，或设计自己的样式
                                                            listPopupWindow.setAnchorView(editTextForChaXun);//以哪个控件为基准，在该处以mEditText为基准
                                                            listPopupWindow.setModal(true);


                                                            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
                                                                @Override
                                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    editTextForChaXun.setText(didianlist[i]);//把选择的选项内容展示在EditText上
                                                                    listPopupWindow.dismiss();//如果已经选择了，隐藏起来
                                                                }
                                                            });
                                                            listPopupWindow.show();//把ListPopWindow展示出来
                                                            break;
                                                            default:
                                                                break;
                                                    }
                                                }
                                            });

                                            editTextForChaXun.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {

                                                    if (CHAXUN == 1 || CHAXUN == 0) {
                                                        editTextForChaXun.setInputType(InputType.TYPE_NULL);
                                                    } else {
                                                        editTextForChaXun.setInputType(InputType.TYPE_CLASS_TEXT);
                                                    }

                                                    return false;
                                                }
                                            });

                                            alertDialogBuilderChaXun.setView(viewForChaXun);

                                            quanbu.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CHAXUN = 0;
                                                    editTextForChaXun.setText("");
                                                    editTextForChaXun.setHint("点击 搜索 即可");
                                                }
                                            });

                                            didian.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CHAXUN = 1;
                                                    editTextForChaXun.setText("");
                                                    editTextForChaXun.setHint("");

                                                }
                                            });

                                            riqi.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CHAXUN = 2;
                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//获取当前时间
                                                    Date date;
                                                    date = new Date(System.currentTimeMillis());
                                                    editTextForChaXun.setFocusable(true);
                                                    editTextForChaXun.setFocusableInTouchMode(true);
                                                    editTextForChaXun.requestFocus();
                                                    editTextForChaXun.setCursorVisible(true);
                                                    editTextForChaXun.setText(simpleDateFormat.format(date));
                                                    editTextForChaXun.setHint("");

                                                }
                                            });

                                            guanjianzi.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    CHAXUN = 3;
                                                    editTextForChaXun.setFocusable(true);
                                                    editTextForChaXun.setFocusableInTouchMode(true);
                                                    editTextForChaXun.requestFocus();
                                                    editTextForChaXun.setCursorVisible(true);
                                                    editTextForChaXun.setText("");
                                                    editTextForChaXun.setHint("");
                                                }
                                            });

                                            alertDialogBuilderChaXun.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    switch (CHAXUN) {

                                                        case 0:
                                                            commonItems1.clear();
                                                            commonItems1.addAll(commonItems);
                                                            table.notifyDataChanged();
                                                            break;

                                                        case 1:
                                                            commonItems1.clear();
                                                            String msg = editTextForChaXun.getText().toString();
                                                            for (CommonItem commonItem : commonItems) {
                                                                if (commonItem.getName1().contains(msg)){
                                                                    commonItems1.add(commonItem);
                                                                }
                                                            }
                                                            table.notifyDataChanged();
                                                            break;
                                                        case 2:
                                                            commonItems1.clear();
                                                            String msg1 = editTextForChaXun.getText().toString();
                                                            for (CommonItem commonItem : commonItems) {
                                                                if (commonItem.getName2().contains(msg1)){
                                                                    commonItems1.add(commonItem);
                                                                }
                                                            }
                                                            table.notifyDataChanged();
                                                            break;
                                                        case 3:
                                                            commonItems1.clear();
                                                            String msg2 = editTextForChaXun.getText().toString();
                                                            for (CommonItem commonItem : commonItems) {
                                                                if (commonItem.getName1().contains(msg2) ||
                                                                        commonItem.getName2().contains(msg2) ||
                                                                        commonItem.getName3().contains(msg2)){
                                                                    commonItems1.add(commonItem);
                                                                }
                                                            }
                                                            table.notifyDataChanged();
                                                            break;
                                                            default:
                                                                break;
                                                    }



                                                }
                                            });
                                            alertDialogBuilderChaXun.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });


                                            AlertDialog alertDialog = alertDialogBuilderChaXun.create();
                                            WindowManager.LayoutParams wlp =alertDialog.getWindow().getAttributes();
                                            wlp.gravity = Gravity.TOP | Gravity.CENTER;
                                            wlp.x=0;
                                            wlp.y=0;

                                            alertDialog.show();
                                            break;
                                            default:
                                                break;
                                    }
                                }

                                @Override
                                public void onBackgroundClick() {

                                }

                                @Override
                                public void onBoomWillHide() {

                                }

                                @Override
                                public void onBoomDidHide() {

                                }

                                @Override
                                public void onBoomWillShow() {

                                }

                                @Override
                                public void onBoomDidShow() {

                                }
                            });


                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
