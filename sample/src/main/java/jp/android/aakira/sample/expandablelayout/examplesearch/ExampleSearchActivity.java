package jp.android.aakira.sample.expandablelayout.examplesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Objects;

import jp.android.aakira.sample.expandablelayout.R;
import jp.android.aakira.sample.expandablelayout.util.DividerItemDecoration;

public class ExampleSearchActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExampleSearchActivity.class));
    }

    private ExpandableRelativeLayout mExpandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_search);

        Objects.requireNonNull(getSupportActionBar()).setTitle(ExampleSearchActivity.class.getSimpleName());

        Button mExpandButton = findViewById(R.id.expandButton);
        mExpandLayout = findViewById(R.id.expandableLayout);
        mExpandButton.setOnClickListener(this);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<String> data = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            data.add("Result");
        }
        recyclerView.setAdapter(new ExampleSearchRecyclerAdapter(data));
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.expandButton) {
            mExpandLayout.expand();
        }
    }
}
