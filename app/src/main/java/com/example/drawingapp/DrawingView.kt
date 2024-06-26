package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var canvas: Canvas? = null
    private var color: Int = Color.DKGRAY
    private var mBrushSize: Float = 0.toFloat()
    private var mCanvasBitmap: Bitmap? = null
    private var mCanvasPaint: Paint? = null
    private var mDrawPaint: Paint? = null
    private var mDrawPath: CustomPath? = null
    private var mPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for (path in mPaths) {
            mDrawPaint!!.color = path.color
            mDrawPaint!!.strokeWidth = path.brushThickness
            canvas.drawPath(path, mDrawPaint!!)
        }

        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.color = mDrawPath!!.color
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX = event?.x
        var touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()

                if (touchX != null) {
                    if (touchY != null) mDrawPath!!.moveTo(touchX, touchY)
                }
            }

            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) mDrawPath!!.lineTo(touchX, touchY)
                }
            }

            else -> return false
        }

        invalidate()

        return true
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)

        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    fun clearCanvas() {
        mPaths.clear()
        setUpDrawing()
        invalidate()
    }

    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    fun setBrushSize(newSize: Float) {
        mBrushSize =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                newSize,
                resources.displayMetrics
            )

        mDrawPaint!!.strokeWidth = mBrushSize
    }

    inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }
}