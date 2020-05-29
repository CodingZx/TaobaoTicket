package lol.cicco.tbunion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import lol.cicco.tbunion.common.entity.CategoryEntity;
import lol.cicco.tbunion.ui.fragment.HomePagerFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<CategoryEntity> data;

    public HomePagerAdapter(@NonNull FragmentManager fm, List<CategoryEntity> data) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.data = data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new HomePagerFragment();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(data.size() <= position) {
            return null;
        }
        return data.get(position).title;
    }
}