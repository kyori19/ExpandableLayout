package com.github.aakira.expandablelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ExpandableConstraintLayout extends ConstraintLayout implements ExpandableLayout {

    private int duration;
    private boolean defaultExpanded;
    private TimeInterpolator interpolator;
    private int orientation;

    private ExpandableLayoutListener listener;
    private ExpandableSavedState savedState;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    private boolean isExpanded;
    private int layoutSize = 0;
    private int closePosition = 0;
    private boolean isCalculatedSize = false;
    private boolean isArranged = false;
    private boolean isAnimating = false;

    public ExpandableConstraintLayout(final Context context) {
        this(context, null);
    }

    public ExpandableConstraintLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableConstraintLayout(final Context context, final AttributeSet attrs,
                                      final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        @SuppressLint("CustomViewStyleable") final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.expandableLayout, defStyleAttr, 0);
        duration = a.getInteger(R.styleable.expandableLayout_ael_duration, DEFAULT_DURATION);
        defaultExpanded = a.getBoolean(R.styleable.expandableLayout_ael_expanded, DEFAULT_EXPANDED);
        orientation = a.getInteger(R.styleable.expandableLayout_ael_orientation, VERTICAL);
        final int interpolatorType = a.getInteger(R.styleable.expandableLayout_ael_interpolator,
                Utils.LINEAR_INTERPOLATOR);
        a.recycle();
        interpolator = Utils.createInterpolator(interpolatorType);
        isExpanded = defaultExpanded;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isCalculatedSize) {
            final int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            if (isVertical()) {
                final int measuredHeight = getMeasuredHeight();

                super.onMeasure(widthMeasureSpec, measureSpec);
                layoutSize = getMeasuredHeight();

                setMeasuredDimension(getMeasuredWidth(), measuredHeight);
            } else {
                final int measuredWidth = getMeasuredWidth();

                super.onMeasure(measureSpec, heightMeasureSpec);
                layoutSize = getMeasuredWidth();

                setMeasuredDimension(measuredWidth, getMeasuredHeight());
            }

            isCalculatedSize = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (isArranged) {
            return;
        }

        if (!defaultExpanded) {
            setLayoutSize(closePosition);
        }
        if (savedState != null) {
            setLayoutSize(savedState.getSize());
        }
        isArranged = true;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        final ExpandableSavedState ss = new ExpandableSavedState(parcelable);
        ss.setSize(getCurrentPosition());
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof ExpandableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final ExpandableSavedState ss = (ExpandableSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        savedState = ss;
    }

    /**
     * Starts animation the state of the view to the inverse of its current state.
     */
    @Override
    public void toggle() {
        toggle(duration, interpolator);
    }

    /**
     * Starts animation the state of the view to the inverse of its current state.
     *
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @Override
    public void toggle(final long duration, @Nullable final TimeInterpolator interpolator) {
        if (closePosition < getCurrentPosition()) {
            collapse(duration, interpolator);
        } else {
            expand(duration, interpolator);
        }
    }

    /**
     * Starts expand animation.
     */
    @Override
    public void expand() {
        expand(duration, interpolator);
    }

    /**
     * Starts expand animation.
     *
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @Override
    public void expand(final long duration, @Nullable final TimeInterpolator interpolator) {
        if (isAnimating) {
            return;
        }

        if (duration <= 0) {
            move(layoutSize, duration, interpolator);
            return;
        }
        createExpandAnimator(getCurrentPosition(), layoutSize, duration, interpolator).start();
    }

    /**
     * Starts collapse animation.
     */
    @Override
    public void collapse() {
        collapse(duration, interpolator);
    }

    /**
     * Starts collapse animation.
     *
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @Override
    public void collapse(final long duration, @Nullable final TimeInterpolator interpolator) {
        if (isAnimating) {
            return;
        }

        if (duration <= 0) {
            move(closePosition, duration, interpolator);
            return;
        }
        createExpandAnimator(getCurrentPosition(), closePosition, duration, interpolator).start();
    }

    /**
     * Sets the expandable layout listener.
     *
     * @param listener ExpandableLayoutListener
     */
    @Override
    public void setListener(@NonNull final ExpandableLayoutListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the length of the animation.
     * The default duration is 300 milliseconds.
     *
     * @param duration
     */
    @Override
    public void setDuration(final int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " +
                    duration);
        }
        this.duration = duration;
    }

    private boolean isVertical() {
        return orientation == VERTICAL;
    }

    public void setOrientation(@Orientation final int orientation) {
        this.orientation = orientation;
    }

    /**
     * Sets state of expanse.
     *
     * @param expanded The layout is visible if expanded is true
     */
    @Override
    public void setExpanded(final boolean expanded) {
        final int currentPosition = getCurrentPosition();
        if ((expanded && (currentPosition == layoutSize))
                || (!expanded && currentPosition == closePosition)) {
            return;
        }

        isExpanded = expanded;
        setLayoutSize(expanded ? layoutSize : closePosition);
        requestLayout();
    }

    /**
     * Gets state of expanse.
     *
     * @return true if the layout is visible
     */
    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * The time interpolator used in calculating the elapsed fraction of this animation. The
     * interpolator determines whether the animation runs with linear or non-linear motion,
     * such as acceleration and deceleration.
     * The default value is  {@link AccelerateDecelerateInterpolator}
     *
     * @param interpolator
     */
    @Override
    public void setInterpolator(@NonNull final TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void move(final int position) {
        move(position, duration, interpolator);
    }

    public void move(final int position, final long duration, @Nullable TimeInterpolator interpolator) {
        if (isAnimating || 0 > position || layoutSize < position) {
            return;
        }

        if (duration <= 0) {
            isExpanded = position > closePosition;
            setLayoutSize(position);
            requestLayout();
            notifyListeners();
            return;
        }
        createExpandAnimator(getCurrentPosition(), position, duration,
                interpolator == null ? this.interpolator : interpolator).start();
    }

    public int getCurrentPosition() {
        if (!isExpanded) {
            return 0;
        }
        return isVertical() ? getMeasuredHeight() : getMeasuredWidth();
    }

    private void setLayoutSize(final int size) {
        if (size == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        if (isVertical()) {
            getLayoutParams().height = size;
        } else {
            getLayoutParams().width = size;
        }
    }

    private ValueAnimator createExpandAnimator(
            final int from, final int to, final long duration, final TimeInterpolator interpolator) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                setLayoutSize((int) animator.getAnimatedValue());
                requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimating = true;
                if (listener == null) {
                    return;
                }

                listener.onAnimationStart();
                if (layoutSize == to) {
                    listener.onPreOpen();
                } else if (closePosition == to) {
                    listener.onPreClose();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
                isExpanded = to > closePosition;

                if (listener == null) {
                    return;
                }

                listener.onAnimationEnd();
                if (to == layoutSize) {
                    listener.onOpened();
                } else if (to == closePosition) {
                    listener.onClosed();
                }
            }
        });
        return valueAnimator;
    }

    private void notifyListeners() {
        if (listener == null) {
            return;
        }

        listener.onAnimationStart();
        if (isExpanded) {
            listener.onPreOpen();
        } else {
            listener.onPreClose();
        }
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);

                listener.onAnimationEnd();
                if (isExpanded) {
                    listener.onOpened();
                } else {
                    listener.onClosed();
                }
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }
}
