package com.xiaogang.Mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.BaseActivity;

/**
 * Created by Administrator on 2015/10/10.
 */
public class SelectFapiaoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView select_one;
    private ImageView select_two;
    private int tmpSelect = 0;
    private EditText invoice_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_fapiao);

        select_one = (ImageView) this.findViewById(R.id.select_one);
        select_two = (ImageView) this.findViewById(R.id.select_two);
        invoice_title = (EditText) this.findViewById(R.id.invoice_title);
        invoice_title.setVisibility(View.GONE);
        this.findViewById(R.id.select_one_liner).setOnClickListener(this);
        this.findViewById(R.id.select_two_liner).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_one_liner:
            {
                tmpSelect = 0;
                select_one.setImageDrawable(getResources().getDrawable(R.drawable.select_one));
                select_two.setImageDrawable(getResources().getDrawable(R.drawable.select_two));
                invoice_title.setVisibility(View.GONE);
            }

                break;
            case R.id.select_two_liner:
            {
                tmpSelect = 1;
                select_one.setImageDrawable(getResources().getDrawable(R.drawable.select_two));
                select_two.setImageDrawable(getResources().getDrawable(R.drawable.select_one));
                invoice_title.setVisibility(View.VISIBLE);
            }

                break;

        }
    }
    public void back(View view){
        finish();
    }

    public void sure(View view){
        if(tmpSelect == 0){
            Intent intent = new Intent(SelectFapiaoActivity.this, OrderMakeActivity.class);
            intent.putExtra("community", "0");
            intent.putExtra("invoice_title", "无");
            setResult(001, intent);
        }else {
            Intent intent = new Intent(SelectFapiaoActivity.this, OrderMakeActivity.class);
            intent.putExtra("community", "1");
            intent.putExtra("invoice_title", (invoice_title.getText().toString()==null?"无":invoice_title.getText().toString()));
            setResult(001, intent);
            finish();
        }
    }
}
