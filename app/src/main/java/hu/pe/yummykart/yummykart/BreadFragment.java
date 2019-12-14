package hu.pe.yummykart.yummykart;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BreadFragment extends Fragment
{
    ArrayList<MenuInitialisation> menuInitialisationArrayList = new ArrayList<>();
    ArrayList<MenuInitialisation> categoriseMenuInitialisationArrayList = new ArrayList<>();
    FloatingActionButton fab;
    @SuppressLint("ValidFragment")
    public BreadFragment(FloatingActionButton fab)
    {
        this.fab=fab;
    }

    public BreadFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int i;
        MenuInitialisationArrayList menuInitialisationArray = new MenuInitialisationArrayList();
        menuInitialisationArrayList=menuInitialisationArray.getMenuInitialisationArrayList();

        int n=menuInitialisationArrayList.size();

        for(i=0;i<n;i++)
        {
            MenuInitialisation menuInitialisation;

            menuInitialisation =  menuInitialisationArrayList.get(i);
            if(menuInitialisation.getCategory().toLowerCase().indexOf("BREAD".toLowerCase()) != -1)
            {
                categoriseMenuInitialisationArrayList.add(menuInitialisation);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_bread,container,false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        TextView category_status = (TextView)rootView.findViewById(R.id.category_status);
        ImageView img_not_available = (ImageView)rootView.findViewById(R.id.img_not_available);
        category_status.setVisibility(View.INVISIBLE);
        img_not_available.setVisibility(View.INVISIBLE);
        if(categoriseMenuInitialisationArrayList.size()==0)
        {
            category_status.setVisibility(View.VISIBLE);
            img_not_available.setVisibility(View.VISIBLE);
        }

        MyAdapter adapter = new MyAdapter(this.getContext(),categoriseMenuInitialisationArrayList);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (dy < 0)
                    fab.show();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });
        return rootView;
    }
}
