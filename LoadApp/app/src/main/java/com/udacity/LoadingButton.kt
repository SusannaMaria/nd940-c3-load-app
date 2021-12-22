package com.udacity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private var angle: Float = 0f
        set(value) {
            field = value
        }
    private lateinit var animator: ObjectAnimator
    private var loadingBackgroundColor: Int = 0
    private var loadingTextColor: Int = 0
    private var loadingPrimaryDarkColor: Int = 0
    private var loadingCircleColor: Int = 0
    private var downloadString: String
    private var weAreLoadingString: String
    var isLoading: Boolean = false
    private var heightSize = 0
    private var value = 0.0f
    private lateinit var mpaint: Paint
    private val textWidth: Float by lazy { mpaint.measureText("We are loading") }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading  -> {
                val widthAnimation = PropertyValuesHolder.ofInt("widthSize", 0, width)
                val circleAnimation = PropertyValuesHolder.ofFloat("angle", 0f, 360f)
                animator = ObjectAnimator.ofPropertyValuesHolder(this,widthAnimation,circleAnimation)
                animator.duration = 2000

                animator.repeatCount = INFINITE
                animator.start()
                isLoading = true
            }
            ButtonState.Completed ->{
                animator.end()
                animator.cancel()
                isLoading = false
                widthSize=width
            }
        }
    }


    init {
        mpaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = resources.getDimension(R.dimen.default_text_size)
            style = Paint.Style.FILL
        }
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingBackgroundColor = getColor(R.styleable.LoadingButton_loadingBackgroundColor, 0)
            loadingTextColor = getColor(R.styleable.LoadingButton_loadingTextColor, 0)
        }
        loadingPrimaryDarkColor = context.getColor(R.color.colorPrimaryDark)
        loadingCircleColor = context.getColor(R.color.colorCircle)
        downloadString = context.getString(R.string.download)
        weAreLoadingString = context.getString(R.string.we_are_loading)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isLoading) {
            mpaint.color = loadingPrimaryDarkColor
            canvas?.drawRect(0.0f, 0.0f, widthSize.toFloat(), heightSize.toFloat(), mpaint)

            mpaint.color = loadingBackgroundColor
            canvas?.drawRect(widthSize.toFloat(), 0f, (width).toFloat(), (height).toFloat(), mpaint)

            mpaint.color = loadingTextColor
            canvas?.drawText(
                weAreLoadingString,
                width.toFloat() / 2,
                height.toFloat() / 2 + mpaint.textSize / 2,
                mpaint
            )
            mpaint.color = loadingCircleColor

            canvas?.drawArc(
                width / 2 + textWidth / 2.toFloat(),
                (height - mpaint.textSize) / 2,
                width / 2 + textWidth / 2 + mpaint.textSize,
                (height - mpaint.textSize) / 2 + (mpaint.textSize),
                0f,
                angle,
                true,
                mpaint
            )

        } else {
            mpaint.color = loadingBackgroundColor

            canvas?.drawRect(0f, 0f, (width).toFloat(), (height).toFloat(), mpaint)
            mpaint.color = loadingTextColor

            canvas?.drawText(
                downloadString,
                width.toFloat() / 2,
                height.toFloat() / 2 + mpaint.textSize / 2,
                mpaint
            )

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}