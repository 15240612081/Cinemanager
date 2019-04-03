package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.activities.CinemaOrdersActivity;
import net.lzzy.cinemanager.activities.MainActivity;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class CinemasFragment extends BaseFragment {
    public static final String ARG_CINEMA = "cinema";
    private OnCinemaSelectedListener listener;
    private ListView lv;
    private List<Cinema> cinemas;
    private CinemaFactory factory=CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;

    public static CinemasFragment newInstance(Cinema cinema){
        CinemasFragment fragment=new CinemasFragment();
        Bundle args=new Bundle();
        args.putParcelable(ARG_CINEMA,cinema);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            cinema=getArguments().getParcelable(ARG_CINEMA);
        }
    }

    @Override
    protected void populate() {
        lv=find(R.id.activity_cinemas_lv);
        View empty=find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        adapter=new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinemas_item,cinemas) {
            @Override
            public void populate(ViewHolder holder, Cinema cinema) {
                holder.setTextView(R.id.cinema_item_tv_name,cinema.getName())
                        .setTextView(R.id.cinema_item_tv_location,cinema.getLocation());
            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) ->
                listener.onCinemaSelected(adapter.getItem(position).getId().toString()));
        if(cinema!=null){
            save(cinema);
        }
    }
    public void save(Cinema cinema){
        adapter.add(cinema);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    public void search(String kw) {
        cinemas.clear();
        if (TextUtils.isEmpty(kw)){
            cinemas.addAll(factory.get());
        }else {
            cinemas.addAll(factory.searchCinemas(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void save(Order order) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCinemaSelectedListener){
            listener=(OnCinemaSelectedListener) context;
        }else {
            throw new ClassCastException(context.toString()+"必须实现OnCinemaSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface OnCinemaSelectedListener{
        void onCinemaSelected(String cinemaId);
    }
}
