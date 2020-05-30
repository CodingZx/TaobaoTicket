package lol.cicco.tbunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import lol.cicco.tbunion.R;
import lol.cicco.tbunion.common.util.ToastUtils;

public abstract class BaseFragment extends Fragment {

    private FrameLayout container;

    protected enum  State {
        Loading,
        Success,
        Empty,
        Error,
    }

    protected View networkErrorView;
    protected View loadingView;
    protected View successView;
    protected View emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = loadRootView(inflater, container);
        this.networkErrorView = loadNetWorkErrorView(inflater, container);
        this.loadingView = loadLoadingView(inflater, container);
        this.successView = loadSuccessView(inflater, container);
        this.emptyView = loadEmptyView(inflater, container);

        this.container = getBaseContainer(view);
        setupState(initState());
        // 子Fragment进行初始化
        doOnInit(successView);
        return view;
    }

    protected void setupState(State state) {
        container.removeAllViewsInLayout();
        switch (state) {
            case Loading:
                container.addView(loadingView);
                break;
            case Error:
                container.addView(networkErrorView);
                break;
            case Empty:
                container.addView(emptyView);
                break;
            case Success:
                container.addView(successView);
                break;
        }
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    protected View loadNetWorkErrorView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.fragment_network_error, viewGroup, false);

        RelativeLayout layout = view.findViewById(R.id.networkErrorLayout);
        layout.setOnClickListener(v -> networkTry());
        return view;
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_loading, viewGroup, false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.fragment_empty, viewGroup, false);
    }

    protected FrameLayout getBaseContainer(View view) {
        return view.findViewById(R.id.baseContainer);
    }

    abstract @NonNull View loadSuccessView(LayoutInflater inflater, ViewGroup viewGroup);

    abstract @NonNull State initState();

    abstract void doOnInit(@NonNull View view);

    abstract void networkTry();

}
