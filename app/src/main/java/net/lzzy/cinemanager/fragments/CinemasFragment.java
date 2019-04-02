package net.lzzy.cinemanager.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import net.lzzy.cinemanager.R;
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
    private ListView lv;
    private List<Cinema> cinemas;
    private CinemaFactory factory=CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;

    public CinemasFragment(){}
    public CinemasFragment(Cinema cinema){
        this.cinema=cinema;
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
}
