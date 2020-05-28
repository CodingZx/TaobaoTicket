package lol.cicco.tbunion.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuItemView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lol.cicco.tbunion.R;
import lol.cicco.tbunion.ui.fragment.HomeFragment;
import lol.cicco.tbunion.ui.fragment.PacketFragment;
import lol.cicco.tbunion.ui.fragment.SearchFragment;
import lol.cicco.tbunion.ui.fragment.SelectFragment;

public class MainActivity extends AppCompatActivity {

    private static final Map<Integer, Fragment> navigationMap = new HashMap<>(4);

    static {
        navigationMap.put(R.id.navigationBarHome, new HomeFragment());
        navigationMap.put(R.id.navigationBarPacket, new PacketFragment());
        navigationMap.put(R.id.navigationBarSelect, new SelectFragment());
        navigationMap.put(R.id.navigationBarSearch, new SearchFragment());
    }

    private int nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        BottomNavigationView view = findViewById(R.id.mainNavigationBar);
        view.setOnNavigationItemSelectedListener(item -> {
            switchFragment(item.getItemId());
            return true;
        });

        switchFragment(R.id.navigationBarHome);
    }

    private void switchFragment(int itemId) {
        if(itemId != nowFragment) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction tx = manager.beginTransaction();
            tx.replace(R.id.mainFrameLayout, Objects.requireNonNull(navigationMap.get(itemId)));
            tx.commit();

            this.nowFragment = itemId;
        }
    }
}
