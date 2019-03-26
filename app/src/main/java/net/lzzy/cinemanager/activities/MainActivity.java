package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.LinearLayout;

import android.widget.SearchView;

import android.widget.TextView;

import net.lzzy.cinemanager.R;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private LinearLayout layoutAddOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 去掉标题栏 **/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();

    }
    /** 标题栏 **/
    private void setTitleMenu() {
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v->{
            int visible=layoutMenu.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v->System.exit(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_title_img_menu:
                break;
            case R.id.bar_title_tv_exit:
                break;

            case R.id.bar_title_tv_add_cinema:
                tvTitle.setText(R.string.bar_title_menu_add_cinema);
                break;

            case R.id.bar_title_tv_view_cinema:
                tvTitle.setText(R.string.bar_title_menu_cinema);
                finish();
                break;
            case R.id.bar_title_tv_add_order:
                tvTitle.setText(R.string.bar_title_menu_add_orders);
                layoutMenu.setVisibility(View.GONE);
                layoutAddOrder.setVisibility(View.VISIBLE);
                break;
            case R.id.bar_title_tv_view_order:
                tvTitle.setText(R.string.bar_title_menu_orders);
                layoutMenu.setVisibility(View.GONE);
                layoutAddOrder.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }
}
