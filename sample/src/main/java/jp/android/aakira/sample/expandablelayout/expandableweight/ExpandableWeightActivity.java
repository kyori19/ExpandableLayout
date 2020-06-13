package jp.android.aakira.sample.expandablelayout.expandableweight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import java.util.Objects;

import jp.android.aakira.sample.expandablelayout.R;

public class ExpandableWeightActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpandableWeightActivity.class));
    }

    private ExpandableWeightLayout mExpandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_weight);

        Objects.requireNonNull(getSupportActionBar()).setTitle(ExpandableWeightActivity.class.getSimpleName());

        Button mExpandButton = findViewById(R.id.expandButton);
        mExpandLayout = findViewById(R.id.expandableLayout);
        mExpandButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.expandButton) {
            mExpandLayout.toggle();
        }
    }
}
