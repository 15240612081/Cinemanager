package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.Order;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemaFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州市";
    private String area="鱼峰区";
    private OnFragmentInteractionListener listener;
    private OnCinemaCreateListener cinemaListener;

    @Override
    protected void populate() {
        listener.hideSearch();
        TextView tvArea=find(R.id.dialog_add_tv_area);
        EditText edtName=find(R.id.dialog_add_cinema_edt_name);
        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v -> {
            JDCityPicker cityPicker = new JDCityPicker();
            cityPicker.init(getActivity());
            cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    AddCinemaFragment.this.province=province.getName();
                    AddCinemaFragment.this.city=city.getName();
                    AddCinemaFragment.this.area=district.getName();
                    String loc=province.getName()+city.getName()+district.getName();
                    tvArea.setText(loc);
                }

                @Override
                public void onCancel() {
                }
            });
            cityPicker.showCityPicker();
        });
        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(getActivity(),"要有名称",Toast.LENGTH_SHORT).show();
                return;
            }
            Cinema cinema=new Cinema();
            cinema.setName(name);
            cinema.setArea(area);
            cinema.setCity(city);
            cinema.setProvince(province);
            cinema.setLocation(tvArea.getText().toString());
            edtName.setText("");
            cinemaListener.saveCinema(cinema);
        });
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v ->
                cinemaListener.cancelAddCinema());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cinemas;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void save(Order order) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(OnFragmentInteractionListener) context;
            cinemaListener=(OnCinemaCreateListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    +"必须实现OnFragmentInteractionListener&OnCinemaCreateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        cinemaListener=null;
    }
    public interface OnCinemaCreateListener{
        void cancelAddCinema();
        void saveCinema(Cinema cinema);
    }

}
