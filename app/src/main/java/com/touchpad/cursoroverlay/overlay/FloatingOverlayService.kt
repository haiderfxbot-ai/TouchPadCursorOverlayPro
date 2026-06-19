package com.touchpad.cursoroverlay.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.touchpad.cursoroverlay.R
import com.touchpad.cursoroverlay.data.PreferencesManager
import com.touchpad.cursoroverlay.data.models.OverlaySettings
import com.touchpad.cursoroverlay.service.TouchPadAccessibilityService
import com.touchpad.cursoroverlay.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FloatingOverlayService : Service() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private lateinit var windowManager: WindowManager
    private lateinit var touchpadView: View
    private lateinit var cursorView: CursorView

    private var touchpadParams: WindowManager.LayoutParams? = null
    private var cursorParams: WindowManager.LayoutParams? = null

    private var touchpadSurface: TouchpadSurface? = null

    private var cursorX = 0f
    private var cursorY = 0f
    private var cursorSpeed = 1.0f
    private var cursorSize = 20
    private var overlayWidth = 260
    private var overlayHeight = 380
    private var overlayOpacity = 0.85f

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isMinimized = false

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createNotificationChannel()
        startForeground(Constants.NOTIFICATION_ID, createNotification())
        loadSettingsAndCreateOverlay()
    }

    private fun loadSettingsAndCreateOverlay() {
        scope.launch {
            val settings = preferencesManager.settingsFlow.first()
            applySettings(settings)
            createTouchpadOverlay(settings)
            createCursorOverlay(settings)
            cursorX = 500f
            cursorY = 500f
            updateCursorPosition()
        }
    }

    private fun applySettings(settings: OverlaySettings) {
        cursorSpeed = settings.cursorSpeed
        cursorSize = settings.cursorSize
        overlayWidth = settings.overlayWidth
        overlayHeight = settings.overlayHeight
        overlayOpacity = settings.overlayOpacity
    }

    private fun createTouchpadOverlay(settings: OverlaySettings) {
        touchpadParams = WindowManager.LayoutParams(
            dpToPx(overlayWidth.coerceIn(Constants.MIN_OVERLAY_WIDTH, Constants.MAX_OVERLAY_WIDTH)),
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = settings.overlayX
            y = settings.overlayY
            alpha = overlayOpacity
        }

        touchpadView = LayoutInflater.from(this).inflate(R.layout.overlay_touchpad, null)
        setupTouchpadControls(touchpadView, settings)
        windowManager.addView(touchpadView, touchpadParams)
    }

    private fun setupTouchpadControls(view: View, settings: OverlaySettings) {
        touchpadSurface = view.findViewById(R.id.touchpadSurface)
        touchpadSurface?.onCursorMove = { dx, dy ->
            val speedFactor = cursorSpeed * 1.5f
            cursorX = (cursorX + dx * speedFactor).coerceIn(0f, getScreenWidth().toFloat())
            cursorY = (cursorY + dy * speedFactor).coerceIn(0f, getScreenHeight().toFloat())
            updateCursorPosition()
        }

        val leftBtn = view.findViewById<TextView>(R.id.btnLeftClick)
        val rightBtn = view.findViewById<TextView>(R.id.btnRightClick)

        leftBtn?.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    TouchPadAccessibilityService.instance?.startDrag(cursorX, cursorY)
                    true
                }
                android.view.MotionEvent.ACTION_UP -> {
                    TouchPadAccessibilityService.instance?.performLeftClick(cursorX, cursorY)
                    TouchPadAccessibilityService.instance?.stopDrag()
                    vibrate()
                    true
                }
                else -> false
            }
        }

        rightBtn?.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    TouchPadAccessibilityService.instance?.startDrag(cursorX, cursorY)
                    true
                }
                android.view.MotionEvent.ACTION_UP -> {
                    TouchPadAccessibilityService.instance?.performRightClick(cursorX, cursorY)
                    TouchPadAccessibilityService.instance?.stopDrag()
                    vibrate()
                    true
                }
                else -> false
            }
        }

        val minimizeBtn = view.findViewById<ImageButton>(R.id.btnMinimize)
        minimizeBtn?.setOnClickListener { toggleMinimize() }

        val closeBtn = view.findViewById<ImageButton>(R.id.btnClose)
        closeBtn?.setOnClickListener { stopSelf() }

        val settingsBtn = view.findViewById<ImageButton>(R.id.btnSettings)
        settingsBtn?.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        setupSliders(view, settings)

        val overlayRoot = view.findViewById<OverlayTouchpadLayout>(R.id.overlayRoot)
        overlayRoot?.onPositionChanged = { x, y ->
            scope.launch { preferencesManager.updateOverlayPosition(x, y) }
        }
    }

    private fun setupSliders(view: View, settings: OverlaySettings) {
        val widthSlider = view.findViewById<SeekBar>(R.id.widthSlider)
        widthSlider?.apply {
            progress = ((settings.overlayWidth - Constants.MIN_OVERLAY_WIDTH).toFloat() /
                    (Constants.MAX_OVERLAY_WIDTH - Constants.MIN_OVERLAY_WIDTH) * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val width = Constants.MIN_OVERLAY_WIDTH +
                                (progress * (Constants.MAX_OVERLAY_WIDTH - Constants.MIN_OVERLAY_WIDTH) / 100)
                        updateOverlayWidth(width)
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }

        val heightSlider = view.findViewById<SeekBar>(R.id.heightSlider)
        heightSlider?.apply {
            progress = ((settings.overlayHeight - Constants.MIN_OVERLAY_HEIGHT).toFloat() /
                    (Constants.MAX_OVERLAY_HEIGHT - Constants.MIN_OVERLAY_HEIGHT) * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val height = Constants.MIN_OVERLAY_HEIGHT +
                                (progress * (Constants.MAX_OVERLAY_HEIGHT - Constants.MIN_OVERLAY_HEIGHT) / 100)
                        updateOverlayHeight(height)
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }

        val speedSlider = view.findViewById<SeekBar>(R.id.speedSlider)
        speedSlider?.apply {
            progress = ((settings.cursorSpeed - Constants.MIN_CURSOR_SPEED) /
                    (Constants.MAX_CURSOR_SPEED - Constants.MIN_CURSOR_SPEED) * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        cursorSpeed = Constants.MIN_CURSOR_SPEED +
                                (progress * (Constants.MAX_CURSOR_SPEED - Constants.MIN_CURSOR_SPEED) / 100f)
                        scope.launch { preferencesManager.updateCursorSpeed(cursorSpeed) }
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }

        val opacitySlider = view.findViewById<SeekBar>(R.id.opacitySlider)
        opacitySlider?.apply {
            progress = (settings.overlayOpacity * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val opacity = progress / 100f
                        updateOverlayOpacity(opacity)
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }
    }

    private fun createCursorOverlay(settings: OverlaySettings) {
        val size = dpToPx(settings.cursorSize + 4)
        cursorParams = WindowManager.LayoutParams(
            size,
            size,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 500
            y = 500
            alpha = settings.cursorOpacity
        }

        cursorView = CursorView(this).apply {
            updateSize(settings.cursorSize)
            updateOpacity(settings.cursorOpacity)
        }
        windowManager.addView(cursorView, cursorParams)
    }

    private fun updateCursorPosition() {
        val params = cursorParams ?: return
        params.x = cursorX.toInt() - dpToPx(cursorSize) / 2
        params.y = cursorY.toInt() - dpToPx(cursorSize) / 2
        try {
            windowManager.updateViewLayout(cursorView, params)
        } catch (e: Exception) {
            // View may not be attached
        }
    }

    private fun updateOverlayWidth(width: Int) {
        val params = touchpadParams ?: return
        val clamped = width.coerceIn(Constants.MIN_OVERLAY_WIDTH, Constants.MAX_OVERLAY_WIDTH)
        params.width = dpToPx(clamped)
        overlayWidth = clamped
        try {
            windowManager.updateViewLayout(touchpadView, params)
        } catch (e: Exception) {}
        scope.launch { preferencesManager.updateOverlaySize(clamped, overlayHeight) }
    }

    private fun updateOverlayHeight(height: Int) {
        overlayHeight = height.coerceIn(Constants.MIN_OVERLAY_HEIGHT, Constants.MAX_OVERLAY_HEIGHT)
        scope.launch { preferencesManager.updateOverlaySize(overlayWidth, overlayHeight) }
    }

    private fun updateOverlayOpacity(opacity: Float) {
        val params = touchpadParams ?: return
        params.alpha = opacity.coerceIn(0.1f, 1.0f)
        overlayOpacity = params.alpha
        try {
            windowManager.updateViewLayout(touchpadView, params)
        } catch (e: Exception) {}
        scope.launch { preferencesManager.updateOverlayOpacity(params.alpha) }
    }

    private fun toggleMinimize() {
        val params = touchpadParams ?: return
        isMinimized = !isMinimized
        if (isMinimized) {
            params.width = dpToPx(48)
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        } else {
            params.width = dpToPx(overlayWidth.coerceIn(Constants.MIN_OVERLAY_WIDTH, Constants.MAX_OVERLAY_WIDTH))
            params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
        }
        try {
            windowManager.updateViewLayout(touchpadView, params)
        } catch (e: Exception) {}
    }

    private fun vibrate() {
        val vm = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        if (vm.hasVibrator()) {
            vm.vibrate(android.os.VibrationEffect.createOneShot(20, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun getScreenWidth() = resources.displayMetrics.widthPixels
    private fun getScreenHeight() = resources.displayMetrics.heightPixels

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        try {
            windowManager.removeView(cursorView)
        } catch (e: Exception) {}
        try {
            windowManager.removeView(touchpadView)
        } catch (e: Exception) {}
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "TouchPad overlay is running"
                setShowBadge(false)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("TouchPad Overlay")
            .setContentText("Overlay is running")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}
