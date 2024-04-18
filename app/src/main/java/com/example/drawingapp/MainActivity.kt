package com.example.drawingapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var colorPickerDialog: Dialog? = null
    private var imgBtnCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setBrushSize(10.toFloat())

        val brushPickerBtn: ImageButton = findViewById(R.id.ib_brush_picker)
        brushPickerBtn.setOnClickListener { showBrushSizePickerDialog() }

        val colorPickerBtn: ImageButton = findViewById(R.id.ib_color_picker)
        colorPickerBtn.setOnClickListener { showColorPickerDialog() }

        val clearBtn: ImageButton = findViewById(R.id.ib_clear)
        clearBtn.setOnClickListener {
            drawingView?.clearCanvas()
        }
    }

    private fun showBrushSizePickerDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.brush_size_dialog)
        brushDialog.setTitle("Select a brush size:")

        val smallBtn = brushDialog.findViewById<ImageButton?>(R.id.ib_small_brush)
        val mediumBtn = brushDialog.findViewById<ImageButton?>(R.id.ib_medium_brush)
        val largeBtn = brushDialog.findViewById<ImageButton?>(R.id.ib_large_brush)

        smallBtn?.setOnClickListener {
            drawingView?.setBrushSize(10.toFloat())
            brushDialog.dismiss()
        }

        mediumBtn?.setOnClickListener {
            drawingView?.setBrushSize(20.toFloat())
            brushDialog.dismiss()
        }

        largeBtn?.setOnClickListener {
            drawingView?.setBrushSize(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    private fun showColorPickerDialog() {
        colorPickerDialog = Dialog(this)
        colorPickerDialog!!.setContentView(R.layout.color_picker_dialog)
        colorPickerDialog!!.setTitle("Select a color:")
        colorPickerDialog!!.show()

        val paintColors: LinearLayout? = colorPickerDialog!!.findViewById(R.id.ll_paint_colors)

        imgBtnCurrentPaint = paintColors?.get(0) as ImageButton?
        imgBtnCurrentPaint?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pallette_selected
            )
        )
    }

    fun colorClicked(view: View) {
        if (view != imgBtnCurrentPaint) {
            val imgBtn = view as ImageButton
            val colorTag = imgBtn.tag.toString()

            drawingView?.setColor(colorTag)

            imgBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallette_selected
                )
            )

            imgBtnCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallette
                )
            )

            imgBtnCurrentPaint = view

            colorPickerDialog!!.dismiss()
        }
    }
}