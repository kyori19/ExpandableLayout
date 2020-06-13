package jp.android.aakira.sample.expandablelayout.expandablelayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.Objects;

import jp.android.aakira.sample.expandablelayout.R;

public class ExpandableLayoutActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpandableLayoutActivity.class));
    }

    private ExpandableRelativeLayout mExpandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_layout);

        Objects.requireNonNull(getSupportActionBar()).setTitle(ExpandableLayoutActivity.class.getSimpleName());

        Button mExpandButton = findViewById(R.id.expandButton);
        Button mMoveChildButton = findViewById(R.id.moveChildButton);
        Button mMoveChildButton2 = findViewById(R.id.moveChildButton2);
        Button mMoveTopButton = findViewById(R.id.moveTopButton);
        Button mSetCloseHeightButton = findViewById(R.id.setCloseHeightButton);
        mExpandLayout = findViewById(R.id.expandableLayout);
        mExpandButton.setOnClickListener(this);
        mMoveChildButton.setOnClickListener(this);
        mMoveChildButton2.setOnClickListener(this);
        mMoveTopButton.setOnClickListener(this);
        mSetCloseHeightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.expandButton:
                mExpandLayout.toggle();
                break;
            case R.id.moveChildButton:
                mExpandLayout.moveChild(0);
                break;
            case R.id.moveChildButton2:
                mExpandLayout.moveChild(1);
                break;
            case R.id.moveTopButton:
                mExpandLayout.move(0);
                break;
            case R.id.setCloseHeightButton:
                mExpandLayout.setClosePosition(mExpandLayout.getCurrentPosition());
                break;
        }
    }
}
