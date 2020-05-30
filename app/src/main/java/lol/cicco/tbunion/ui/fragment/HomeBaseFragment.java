package lol.cicco.tbunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import lol.cicco.tbunion.R;

public abstract class HomeBaseFragment extends BaseFragment {

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_home_base, viewGroup, false);
    }

    @Override
    protected FrameLayout getBaseContainer(View view) {
        return view.findViewById(R.id.homeBaseContainer);
    }

    @NonNull
    @Override
    State initState() {
        return State.Loading;
    }

}
