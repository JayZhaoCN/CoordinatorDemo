package com.jay.coordinatordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private ImageView tabImg1;
    private ImageView tabImg2;
    private ImageView tabImg3;
    private ImageView tabImg4;
    private ImageView tabImg5;

    private MaskFrameLayout mMaskLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MyRecyclerAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if(!mMaskLayout.isStarted()) {
                    mMaskLayout.setVisibility(View.VISIBLE);
                    mMaskLayout.initAnim();
                }
            }

            @Override
            public void onMenuCollapsed() {
                mMaskLayout.hideMask();
            }
        });

        mMaskLayout = (MaskFrameLayout) findViewById(R.id.mask_layout);
        mMaskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                mMaskLayout.hideMask();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton button1 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        com.getbase.floatingactionbutton.FloatingActionButton button2 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
        com.getbase.floatingactionbutton.FloatingActionButton button3 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_c);

        Drawable drawable1 = ContextCompat.getDrawable(this, R.mipmap.ic_share);
        DrawableCompat.setTint(drawable1, ContextCompat.getColor(this, R.color.black_semi_transparent));

        Drawable drawable2 = ContextCompat.getDrawable(this, R.mipmap.ic_answer);
        DrawableCompat.setTint(drawable2, ContextCompat.getColor(this, R.color.black_semi_transparent));

        Drawable drawable3 = ContextCompat.getDrawable(this, R.mipmap.ic_question);
        DrawableCompat.setTint(drawable3, ContextCompat.getColor(this, R.color.black_semi_transparent));

        button1.setIconDrawable(drawable1);
        button2.setIconDrawable(drawable2);
        button3.setIconDrawable(drawable3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                fabMenu.collapse();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
                fabMenu.collapse();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        tabImg1 = (ImageView) findViewById(R.id.tab_1);
        tabImg2 = (ImageView) findViewById(R.id.tab_2);
        tabImg3 = (ImageView) findViewById(R.id.tab_3);
        tabImg4 = (ImageView) findViewById(R.id.tab_4);
        tabImg5 = (ImageView) findViewById(R.id.tab_5);

        tabImg1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setImgTint(1);
            }
        });
        tabImg2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setImgTint(2);
            }
        });
        tabImg3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setImgTint(3);
            }
        });
        tabImg4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setImgTint(4);
            }
        });
        tabImg5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setImgTint(5);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setImgTint(int position) {
        tabImg1.getDrawable().setTint(position == 1 ? ContextCompat.getColor(MainActivity.this, R.color.colorPrimary) : 0xff999999);
        tabImg2.getDrawable().setTint(position == 2 ? ContextCompat.getColor(MainActivity.this, R.color.colorPrimary) : 0xff999999);
        tabImg3.getDrawable().setTint(position == 3 ? ContextCompat.getColor(MainActivity.this, R.color.colorPrimary) : 0xff999999);
        tabImg4.getDrawable().setTint(position == 4 ? ContextCompat.getColor(MainActivity.this, R.color.colorPrimary) : 0xff999999);
        tabImg5.getDrawable().setTint(position == 5 ? ContextCompat.getColor(MainActivity.this, R.color.colorPrimary) : 0xff999999);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //去除菜单按钮
        menu.findItem(R.id.action_settings).setVisible(false);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
        private Context mContext;

        class MyViewHolder extends RecyclerView.ViewHolder {
            MyViewHolder(View itemView) {
                super(itemView);
            }
        }

        MyRecyclerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.layout_recycler_item, null);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //do nothing
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    public static class MyFragment extends Fragment {
        private Context context;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_view_pager, container, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setAdapter(new MyRecyclerAdapter(context));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            return view;
        }
    }
}
