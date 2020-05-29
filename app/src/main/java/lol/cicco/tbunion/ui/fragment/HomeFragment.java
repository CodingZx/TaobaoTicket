package lol.cicco.tbunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
import lol.cicco.tbunion.ui.adapter.HomePagerAdapter;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomePagerAdapter homePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.tabLayout = view.findViewById(R.id.home_top_tab_layout);
        this.viewPager = view.findViewById(R.id.homeViewPager);

        initCategories();
        initTabSelectedListener();
        return view;
    }

    private void initTabSelectedListener() {
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.info(HomeFragment.class, "selected -> "+tab.getText());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtils.info(HomeFragment.class, "unselected -> "+tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogUtils.info(HomeFragment.class, "reselect -> "+tab.getText());
            }
        });
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initCategories() {
        RetrofitConfiguration.retrofit.create(HomeApi.class).getHomeCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(categoryEntities -> {
                    for(CategoryEntity entity : categoryEntities) {
                        TabLayout.Tab tab = tabLayout.newTab();
                        tab.setText(entity.title);
                        tabLayout.addTab(tab);
                    }

                    this.tabLayout.setupWithViewPager(viewPager);
                    this.homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), categoryEntities);
                    this.viewPager.setAdapter(homePagerAdapter);
                }, throwable -> {

                });
    }
}
