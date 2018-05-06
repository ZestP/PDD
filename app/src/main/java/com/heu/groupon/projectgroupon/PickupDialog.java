package com.heu.groupon.projectgroupon;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickupDialog extends Dialog {
    public PickupDialog(Context context)
    {
        super(context);
    }
    public PickupDialog(Context context,int theme)
    {
        super(context,theme);
    }
    public static class Builder {

        private Context mContext;
        private View layout;
        private PickupDialog dialog;
        private int mGoodID;
        public Builder(Context context, int theme, int goodid, final float price, int repcnt, final Bitmap bmp, final boolean isSingle, final String name, final int teamid) {
            mContext=context;
            mGoodID=goodid;
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new PickupDialog(context,theme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_pickup, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.findViewById(R.id.pickup_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ZX","GOTOCONFIRM");
                    Intent myintent = new Intent();
                    myintent.setClass(mContext, ConfirmOrderActivity.class);
                    myintent.putExtra("goodid",mGoodID);
                    myintent.putExtra("price",price);
                    myintent.putExtra("issingle",isSingle);
                    myintent.putExtra("name",name);
                    myintent.putExtra("teamid",teamid);
                    mContext.startActivity(myintent);

                }
            });
            ((TextView)layout.findViewById(R.id.pickup_price)).setText("￥"+price);
            ((TextView)layout.findViewById(R.id.pickup_repcnt)).setText("库存"+repcnt+"件");
            ((ImageView)layout.findViewById(R.id.pickup_image)).setImageBitmap(bmp);
            ((ImageView)layout.findViewById(R.id.pickup_imageback)).setImageBitmap(bmp);
        }



        public PickupDialog createDialog() {

            create();
            return dialog;
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private void create() {
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(true);        //用户不能通过点击对话框之外的地方取消对话框显示
            loadRadio();
            //initViewTwo();

        }
        private void loadRadio()
        {
            Log.i("ZX","loadRadio");
            CustomGridView gv1=layout.findViewById(R.id.pickup_radiogroup1);
            List<String> ls=new ArrayList<String>();
//            for(int i=0;i<100;i++)
//            {
//                ls.add("Zest"+i);
//            }
            ls.add("默认");
            RadioGridAdapter rga=new RadioGridAdapter(mContext,ls);
            gv1.setAdapter(rga);
            CustomGridView gv2=layout.findViewById(R.id.pickup_radiogroup2);
            List<String> ls2=new ArrayList<String>();
//            for(int i=0;i<100;i++)
//            {
//                ls2.add("NotZest"+i);
//            }
            ls2.add("默认");
            RadioGridAdapter rga2=new RadioGridAdapter(mContext,ls2);
            gv2.setAdapter(rga2);
            TextView txtBaseMsg=layout.findViewById(R.id.dialog_color);
            txtBaseMsg.setFocusable(true);
            txtBaseMsg.setFocusableInTouchMode(true);
            txtBaseMsg.requestFocus();
        }



        public void initViewTwo(){
            RadioGroup radiogroup=(RadioGroup)layout.findViewById(R.id.pickup_radiogroup1);


            addview(radiogroup);
            RadioGroup radiogroup2=(RadioGroup)layout.findViewById(R.id.pickup_radiogroup2);


            addview(radiogroup2);
        }

        public List<String> getListSize(){
            List<String>list=new ArrayList<String>();
            list.add("服装33333");
            list.add("玩具44444");
            list.add("饰品5555");
            list.add("饰品6666");
            list.add("文具7777");
            list.add("文具8888");
            list.add("文具9999");
            return list;
        }

        //动态添加视图
        public void addview(RadioGroup radiogroup){

            int index=0;
            for(String ss:getListSize()){

                RadioButton  button=new RadioButton(mContext);
                setRaidBtnAttribute(button,ss,index);

                radiogroup.addView(button);

                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                        .getLayoutParams();
                layoutParams.setMargins(0, 0,  10, 0);//4个参数按顺序分别是左上右下
                button.setLayoutParams(layoutParams);
                index++;
            }


        }


        private void setRaidBtnAttribute( final RadioButton codeBtn, String btnContent, int id ){
            if( null == codeBtn ){
                return;
            }
            codeBtn.setBackgroundResource(R.drawable.radio_group_selector);
            codeBtn.setTextColor(R.drawable.color_radiobutton);
            codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            //codeBtn.setTextSize( ( textSize > 16 )?textSize:24 );
            codeBtn.setId( id );
            codeBtn.setText( btnContent );
            codeBtn.setPadding(10, 10, 10, 10);

            codeBtn.setGravity( Gravity.CENTER );
            codeBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "HEELLO", Toast.LENGTH_SHORT).show();
                }
            });
            //DensityUtilHelps.Dp2Px(this,40)
            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT );

            codeBtn.setLayoutParams( rlp );
        }

    }

}
