package lol.cicco.tbunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lol.cicco.tbunion.R;
import lol.cicco.tbunion.common.RetrofitConfiguration;
import lol.cicco.tbunion.common.api.HomeApi;
import lol.cicco.tbunion.common.entity.CategoryEntity;
import lol.cicco.tbunion.common.util.LogUtils;
import lol.cicco.tbunion.ui.adapter.HomePagerAdapter;

public class HomeFragment extends HomeBaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    @NonNull
    View loadSuccessView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.fragment_home, viewGroup, false);

        this.tabLayout = view.findViewById(R.id.home_top_tab_layout);
        this.viewPager = view.findViewById(R.id.homeViewPager);

        initTabSelectedListener();
        return view;
    }

    @Override
    void doOnInit(@NonNull View view) {
        initCategories();
    }

    private void initTabSelectedListener() {
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initCategories() {
        RetrofitConfiguration.retrofit.create(HomeApi.class).getHomeCategory()
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(categoryEntities -> {
                    if(categoryEntities.isEmpty()) {
                        setupState(State.Empty);
                    } else {
                        for (CategoryEntity entity : categoryEntities) {
                            TabLayout.Tab tab = tabLayout.newTab();
                            tab.setText(entity.title);
                            tabLayout.addTab(tab);
                        }

                        this.tabLayout.setupWithViewPager(viewPager);
                        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), categoryEntities);
                        this.viewPager.setAdapter(homePagerAdapter);

                        this.tabLayout.setVisibility(View.VISIBLE);
                        setupState(State.Success);
                    }
                }, throwable -> {
                    this.tabLayout.setVisibility(View.GONE);
                    setupState(State.Error);
                    LogUtils.error(HomeFragment.class, throwable.getMessage());
                });
    }

    @Override
    void networkTry() {
        setupState(State.Loading);
        initCategories();
    }
}
