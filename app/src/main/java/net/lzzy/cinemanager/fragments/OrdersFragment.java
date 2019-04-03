package net.lzzy.cinemanager.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {
    public static final String ARG_NEW_ORDER = "order";
    private static final int MIN_DISTANCE=100;
    private List<Order> orders;
    private GenericAdapter<Order> adapter;
    private OrderFactory factory=OrderFactory.getInstance();
    private Order order;
    private float touchX1;
    private boolean isDelete=false;
    private ListView lv;

    public static OrdersFragment newInstance(Order order){
        OrdersFragment fragment=new OrdersFragment();
        Bundle args=new Bundle();
        args.putParcelable(ARG_NEW_ORDER,order);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            order=getArguments().getParcelable(ARG_NEW_ORDER);
        }
    }

    @Override
    protected void populate() {
        lv = find(R.id.activity_order_lv);
        View t=find(R.id.activity_order_tv_none);
        lv.setEmptyView(t);

        orders=factory.get();
          adapter=new GenericAdapter<Order>(getActivity(),R.layout.order_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                String location= CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()).toString();

                holder.setTextView(R.id.order_item_movie,order.getMovie())
                        .setTextView(R.id.order_item_location,location);
                Button btn=holder.getView(R.id.order_item_btn);
                btn.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                        .setTitle("删除确认")
                        .setMessage("要删除订单吗？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确认",(dialog,which)->{
                            isDelete=false;
                            adapter.remove(order);
                        })
                        .show());
                int visible=isDelete?View.VISIBLE:View.GONE;
                btn.setVisibility(visible);
                holder.getConvertView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        slideToDelete(event,order,btn);
                        return true;
                    }
                });
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }

        };
        lv.setAdapter(adapter);
        if (order!=null){
            save(order);
        }
    }
    private void slideToDelete(MotionEvent event, Order order, Button btn) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float touchX2=event.getX();
                if (touchX1-touchX2>MIN_DISTANCE){
                    if(!isDelete){
                        btn.setVisibility(View.VISIBLE);
                        isDelete=true;
                    }
                }else {
                    if(btn.isShown()){
                        btn.setVisibility(View.GONE);
                        isDelete=false;
                    }else {
                        clickOrder(order);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void clickOrder(Order order) {
        Cinema cinema= CinemaFactory.getInstance().getById(order.getCinemaId().toString());
        String content = "[" + order.getMovie() + "]" + order.getMovieTime() + "\n"
                + cinema.toString() + "票价" + order.getPrice() + "元";
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_qrcode,null);
        ImageView img=view.findViewById(R.id.dialog_grcode_img);
        img.setImageBitmap(AppUtils.createQRCodeBitmap(content,300,300));
        new AlertDialog.Builder(getActivity())
                .setView(view).show();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    @Override
    public void search(String kw) {
        orders.clear();
        if (TextUtils.isEmpty(kw)){
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void save(Order order) {
        adapter.add(order);

    }
}
