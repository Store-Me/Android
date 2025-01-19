package com.store_me.storeme.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class KeyboardHeightObserver(
    activity: Activity,
    onKeyboardHeightChanged: (Int) -> Unit
) {
    private val rootView: View = activity.window.decorView
    private val navigationBarHeight: Int = getNavigationBarHeight(rootView)
    private var lastKeyboardHeight = 0
    private var lastUpdateTime = 0L
    private val handler = Handler(Looper.getMainLooper())

    /**
     * 키보드 높이를 감지하고 안정화된 값 콜백 호출
     */
    fun startObserving() {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     * 키보드 감지를 중단하고 리스너 제거
     */
    fun stopObserving() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keyboardHeight = screenHeight - rect.bottom - navigationBarHeight

        if (keyboardHeight > 100) { // 키보드가 열렸을 때
            if(lastKeyboardHeight < keyboardHeight)
                lastKeyboardHeight = keyboardHeight

            lastUpdateTime = System.currentTimeMillis()
        } else { // 키보드가 닫혔을 때
            lastKeyboardHeight = 0
        }

        // 안정화된 높이 감지
        handler.postDelayed({
            if (System.currentTimeMillis() - lastUpdateTime > 300 && lastKeyboardHeight > 100) {
                onKeyboardHeightChanged(lastKeyboardHeight)
            }
        }, 300)
    }

    private fun getNavigationBarHeight(view: View): Int {
        val insets = ViewCompat.getRootWindowInsets(view)
        return insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0
    }
}