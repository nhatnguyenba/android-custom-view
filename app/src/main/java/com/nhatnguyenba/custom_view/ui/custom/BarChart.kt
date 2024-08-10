package com.nhatnguyenba.custom_view.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.nhatnguyenba.android_custom_view.R
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class BarChart : View {
    class Item(var name: String?, var value: Float)

    private var items: List<Item> = ArrayList()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 30f
    }
    private var spacing = 10f
    private var itemColor: ColorStateList =
        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_200))
    private var selectedItem: Item? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initChartView(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initChartView(attrs)
    }

    private fun initChartView(attrs: AttributeSet?) {
        if (attrs == null) return

        val a = context.obtainStyledAttributes(attrs, R.styleable.ChartView)

        setItemSpacing(a.getDimension(R.styleable.ChartView_guide_itemSpacing, spacing))
        setItemColor(a.getColorStateList(R.styleable.ChartView_guide_itemColor) ?: itemColor)

        a.recycle()
    }

    fun setItems(items: List<Item>) {
        this.items = items;
    }

    private fun setItemSpacing(spacing: Float) {
        this.spacing = spacing
    }

    private fun setItemColor(itemColor: ColorStateList) {
        this.itemColor = itemColor
    }

    fun setItemColor(itemColor: Int) {
        this.itemColor = ColorStateList.valueOf(itemColor)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val viewportWidth = (width - paddingLeft - paddingRight).toFloat()
        val itemWidth = viewportWidth / items.size

        selectedItem = items[max(
            0.0,
            min(
                floor(((event.x - paddingLeft) / itemWidth).toDouble()),
                (items.size - 1).toDouble()
            )
        ).toInt()]
        postInvalidate()

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewportWidth = (width - paddingLeft - paddingRight).toFloat()
        val viewportHeight = (height - paddingTop - paddingBottom).toFloat()
        val itemWidth = viewportWidth / items.size
        var maxItemHeight = 0f
        for (item in items) maxItemHeight = max(maxItemHeight.toDouble(), item.value.toDouble())
            .toFloat()

        for (i in items.indices) {
            val item = items[i]

            paint.color = itemColor.getColorForState(
                getDrawableStateSelected(selectedItem === item),
                itemColor.defaultColor
            )

            val itemLeft = paddingLeft + i * itemWidth + spacing / 2
            val itemRight = paddingLeft + (i + 1) * itemWidth - spacing / 2
            val itemTop = height - paddingBottom - viewportHeight / maxItemHeight * item.value
            val itemBottom = height - paddingBottom
            canvas.drawRect(
                itemLeft,
                itemTop,
                itemRight,
                itemBottom.toFloat(),
                paint
            )

            val textX = itemLeft
            val textY = itemBottom.toFloat() - ((itemBottom.toFloat() - itemTop) / 2)
            canvas.drawText("${item.name} (${item.value})", textX, textY, textPaint)
        }
    }

    private fun getDrawableStateSelected(selected: Boolean): IntArray {
        if (!selected) {
            return drawableState
        } else {
            val newState = drawableState.copyOf(drawableState.size + 1)
            newState[newState.size - 1] = android.R.attr.state_selected
            return newState
        }
    }
}