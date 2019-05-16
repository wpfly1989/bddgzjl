package com.example.lenovo.baodingdongguzhangjilu;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private com.bin.david.form.core.SmartTable<CommonItem> table;
    private List<CommonItem> commonItems = new ArrayList<>();
    private List<CommonItem> commonItems1 = new ArrayList<>();
    private BoomMenuButton boomMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        table = (com.bin.david.form.core.SmartTable<CommonItem>)findViewById(R.id.smarttable);
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boommenubutton);

        boomMenuButton.setButtonEnum(ButtonEnum.TextOutsideCircle);
        boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
        boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);

        boomMenuButton.addBuilder(new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.ts).normalText("搜索"));
        boomMenuButton.addBuilder(new TextOutsideCircleButton.Builder().normalImageRes(R.drawable.tss).normalText("取消搜索"));

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
                                @Override
                                public void onClicked(int index, BoomButton boomButton) {
                                    switch (index) {
                                        case 0:
                                            AlertDialog.Builder alertDialogBuilderChaXun = new AlertDialog.Builder(Main2Activity.this);
                                            View viewForChaXun = LayoutInflater.from(Main2Activity.this).inflate(R.layout.chaxun,null);
                                            final EditText editTextForChaXun = (EditText)viewForChaXun.findViewById(R.id.guanjianzichaxun);
                                            alertDialogBuilderChaXun.setView(viewForChaXun);
                                            alertDialogBuilderChaXun.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    commonItems1.clear();
                                                    String msg = editTextForChaXun.getText().toString();
                                                    for (CommonItem commonItem : commonItems) {
                                                        if (commonItem.getName1().contains(msg) ||
                                                                commonItem.getName2().contains(msg) ||
                                                                commonItem.getName3().contains(msg)){
                                                            commonItems1.add(commonItem);
                                                        }
                                                    }
                                                    table.notifyDataChanged();

                                                }
                                            });
                                            alertDialogBuilderChaXun.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });


                                            AlertDialog alertDialog = alertDialogBuilderChaXun.create();
                                            alertDialog.setTitle("提示：");
                                            alertDialog.show();
                                            break;
                                        case 1:
                                            commonItems1.clear();
                                            commonItems1.addAll(commonItems);
                                            table.notifyDataChanged();
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
