package com.xiaogang.Mine.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xiaogang.Mine.R;
import com.xiaogang.Mine.base.ActivityTack;
import com.xiaogang.Mine.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MineAnquanActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_anquan_activity);

        this.findViewById(R.id.updatePwr).setOnClickListener(this);
        this.findViewById(R.id.findPwr).setOnClickListener(this);
        this.findViewById(R.id.tuichu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updatePwr:
                Intent updatePwr = new Intent(MineAnquanActivity.this , MineUpdatePwrActivity.class);
                startActivity(updatePwr);
                break;
            case R.id.findPwr:
                Intent findpwr = new Intent(MineAnquanActivity.this , ForgetActivity.class);
                startActivity(findpwr);
                break;
            case R.id.tuichu:
            {
                //
                AlertDialog dialog = new AlertDialog.Builder(MineAnquanActivity.this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle(getResources().getString(R.string.is_quite))
                        .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog pd = new ProgressDialog(MineAnquanActivity.this);
                                String st = getResources().getString(R.string.Are_logged_out);
                                pd.setMessage(st);
                                pd.setCanceledOnTouchOutside(false);
                                pd.dismiss();
                                save("password", "");
                                ActivityTack.getInstanse().exit(MineAnquanActivity.this);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
            }
                break;

        }
    }

    public void back(View view){finish();}
}
