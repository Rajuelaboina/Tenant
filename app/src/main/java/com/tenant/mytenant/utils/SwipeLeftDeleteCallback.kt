package com.tenant.mytenant.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tenant.mytenant.R


abstract class SwipeLeftDeleteCallback (context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")

    private val editIcon = ContextCompat.getDrawable(context, R.drawable.baseline_edit_24)
    private val editIntrinsicWidth = editIcon?.intrinsicWidth
    private val editIntrinsicHeight = editIcon?.intrinsicHeight

    private val editbackgroundColor = Color.parseColor("#187306")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder ): Int {
        //if (viewHolder?.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(  recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw( c: Canvas,recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,dX: Float,dY: Float,actionState: Int,isCurrentlyActive: Boolean) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

        /*if (dX > 0){
            // left Draw the green edit background

            background.color = editbackgroundColor
            background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
            background.draw(c)

            // Calculate position of edit icon
            val editIconTop = itemView.top + (itemHeight - editIntrinsicHeight!!) / 2
            val editIconMargin = (itemHeight - editIntrinsicHeight) / 2
            val editIconLeft = itemView.left + editIconMargin - editIntrinsicWidth!!
            val editIconRight = itemView.left + editIconMargin
            val editIconBottom = editIconTop + editIntrinsicHeight

            // Draw the edit icon
            editIcon?.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
            editIcon?.draw(c)

        }else if (dX < 0){
            // left Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon?.draw(c)
        }*/

        // Draw the red delete background
       /* background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)*/

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

}