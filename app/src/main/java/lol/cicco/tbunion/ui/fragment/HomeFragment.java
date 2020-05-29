package lol.cicco.tbunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lol.cicco.tbunion.R;
import lol.cicco.tbunion.common.GsonConfiguration;
import lol.cicco.tbunion.common.RetrofitConfiguration;
import lol.cicco.tbunion.common.api.HomeApi;
import lol.cicco.tbunion.common.entity.CategoryEntity;
import lol.cicco.tbunion.common.exception.HttpRequestException;
import lol.cicco.tbunion.common.util.LogUtils;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.home_top_tab_layout);

        ViewPager viewPager = view.findViewById(R.id.homeViewPager);

        RetrofitConfiguration.retrofit.create(HomeApi.class).getHomeCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(categoryEntities -> {
                    for(CategoryEntity entity : categoryEntities) {
                        TabLayout.Tab tab = tabLayout.newTab();
                        tab.setText(entity.title);
                        tab.setTag(entity);
                        tabLayout.addTab(tab);
                    }
                }, throwable -> {
                    if(throwable instanceof HttpRequestException) {
                        Toast.makeText(view.getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(view.getContext(), "系统错误", Toast.LENGTH_LONG).show();
                    }
                });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.info(HomeFragment.class, GsonConfiguration.gson.toJson(tab.getTag()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
