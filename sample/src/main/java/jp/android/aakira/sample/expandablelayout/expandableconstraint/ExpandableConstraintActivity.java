package jp.android.aakira.sample.expandablelayout.expandableconstraint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableConstraintLayout;

import java.util.Objects;

import jp.android.aakira.sample.expandablelayout.R;

public class ExpandableConstraintActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpandableConstraintActivity.class));
    }

    private ExpandableConstraintLayout expandableLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_constraint);

        Objects.requireNonNull(getSupportActionBar()).setTitle(ExpandableConstraintActivity.class.getSimpleName());

        Button expandButton = findViewById(R.id.expandButton);
        expandableLayout = findViewById(R.id.expandableLayout);
        expandButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        expandableLayout.toggle();
    }
}
