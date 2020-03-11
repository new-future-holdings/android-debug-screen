package com.pandulapeter.beagle.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext

internal class BeagleDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    drawer: View? = null,
    @Dimension drawerWidth: Int? = null
) : DrawerLayout(context, attrs, defStyleAttr) {

    private val container = OverlayFrameLayout(context)
    var keylineOverlay
        get() = container.keylineOverlayToggle
        set(value) {
            container.keylineOverlayToggle = value
        }
    var viewBoundsOverlay
        get() = container.viewBoundsOverlayToggle
        set(value) {
            container.viewBoundsOverlayToggle = value
        }
    private var currentJob: CoroutineContext? = null
    private val listener = object : DrawerListener {

        override fun onDrawerOpened(drawerView: View) = Beagle.notifyListenersOnOpened()

        override fun onDrawerClosed(drawerView: View) = Beagle.notifyListenersOnClosed()

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_DRAGGING && drawer?.let { isDrawerOpen(it) } != true) {
                Beagle.notifyListenersOnDragStarted()
            }
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
    }

    fun takeAndShareScreenshot() = shareImage(getScreenshot())

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDrawerLockMode(if (Beagle.isEnabled) LOCK_MODE_UNDEFINED else LOCK_MODE_LOCKED_CLOSED)
        addDrawerListener(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeDrawerListener(listener)
    }

    private fun shareImage(image: Bitmap) {
        currentJob?.cancel()
        currentJob = GlobalScope.launch(Dispatchers.Default) {
            val imagesFolder = File(context.cacheDir, "beagleScreenshots")
            val uri: Uri?
            try {
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "screenshot_${System.currentTimeMillis()}.png")
                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".beagle.fileProvider", file)
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_STREAM, uri)
                }, "Share"))
            } catch (_: IOException) {
            }
            currentJob = null
        }
    }

    //TODO: System bars should be included in the screenshot
    private fun getScreenshot(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = container.rootView.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                canvas.drawColor(typedValue.data)
            } else {
                context.drawable(typedValue.resourceId)?.draw(canvas)
            }
        }
        container.draw(canvas)
        return bitmap
    }

    init {
        addView(container, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(drawer, LayoutParams(drawerWidth ?: context.dimension(R.dimen.beagle_default_drawer_width), LayoutParams.MATCH_PARENT, GravityCompat.END))
    }
}
