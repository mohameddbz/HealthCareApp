package com.example.projecttdm.ui.common.navigationBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.drawscope.Fill

/*
import android.animation.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.projecttdm.R
import com.example.projecttdm.R
import kotlin.math.abs

/**
 * Created by suson on 9/28/20
 */

class CurvedBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "CurvedBottomNavigation"
        private const val PROPERTY_OFFSET = "OFFSET"
        private const val PROPERTY_CENTER_Y = "CENTER_Y"
        private const val PROPERTY_CENTER_X = "CENTER_X"
    }


    private var dotRadius : Float
    private var textSizeInSdp : Float


    private val dotPaint : Paint
    private val textPaint : Paint

    // first bezier curve
    private val firstCurveStart = PointF()
    private val firstCurveEnd = PointF()
    private val firstCurveControlPoint1 = PointF()
    private val firstCurveControlPoint2 = PointF()

    // second bezier curve
    private val secondCurveStart = PointF()
    private val secondCurveEnd = PointF()
    private val secondCurveControlPoint1 = PointF()
    private val secondCurveControlPoint2 = PointF()

    private var textColor = Color.WHITE
        set(value) {
            field = value
            textPaint.color = value
            if(isMenuInitialized){
                invalidate()
            }
        }


    private var dotSize = 5f.getScaledSize(context)
        set(value) {
            field = value
            dotRadius = dotSize
            if(isMenuInitialized){
                invalidate()
            }
        }


    private var fontTextSize =  10f.getScaledSize(context)
        set(value) {
            field = value
            textPaint.textSize = value
            if(isMenuInitialized){
                invalidate()
            }
        }


    private var dotColor = Color.WHITE
        set(value) {
            field = value
            dotPaint.color = value
            if(isMenuInitialized){
                invalidate()
            }
        }

    private var showDot = true
        set(value){
            field = value
            if(isMenuInitialized){
                invalidate()
            }
        }



    private var selectedColor = Color.parseColor("#000000")
        set(value) {
            field = value
            if (isMenuInitialized) {
                updateMenuAVDsTint()
                invalidate()
            }
        }

    private var unSelectedColor = Color.parseColor("#8F8F8F")
        set(value) {
            field = value
            if (isMenuInitialized) {
                updateMenuIconsTint()
                invalidate()
            }
        }

    private val shadowColor: Int = Color.parseColor("#75000000")

    private var animDuration: Long = 300L

    private var fabElevation = 4.toPx(context).toFloat()
        set(value) {
            field = value
            fabPaint.setShadowLayer(fabElevation, 0f, 6f, shadowColor)
            if (isMenuInitialized) {
                invalidate()
            }
        }

    var navElevation = 6.toPx(context).toFloat()
        set(value) {
            field = value
            navPaint.setShadowLayer(navElevation, 0f, 6f, shadowColor)
            if (isMenuInitialized) {
                invalidate()
            }
        }

    var fabBackgroundColor = Color.WHITE
        set(value) {
            field = value
            fabPaint.color = value
            if (isMenuInitialized) {
                invalidate()
            }
        }


    var navBackgroundColor = Color.WHITE
        set(value) {
            field = value
            navPaint.color = value
            if (isMenuInitialized) {
                invalidate()
            }
        }

    // path to represent the curved background
    private val path: Path = Path()

    // paints the BottomNavigation background
    private val navPaint: Paint

    // paints the FAB background
    private val fabPaint: Paint


    // initialize empty array so that we don't have to check if it's initialized or not
    private var cbnMenuItems: Array<CbnMenuItem> = arrayOf()
    private lateinit var bottomNavItemViews: Array<BottomNavItemView>
    private lateinit var menuIcons: Array<Drawable>
    private lateinit var menuAVDs: Array<AnimatedVectorDrawableCompat>

    // width of the cell, computed in onSizeChanged()
    private var menuCellWidth: Int = 0

    // x-offset of the current selected cell with respect to left side
    private var cellOffsetX: Int = 0

    // current active index
    private var selectedIndex: Int = -1

    // index of AVD to be animated
    private var fabIconIndex: Int = -1

    private var prevSelectedIndex: Int = -1


    private val fabSize = resources.getDimensionPixelSize(R.dimen.cbn_fab_size)

    // total height of this layout
    private val layoutHeight = resources.getDimension(R.dimen.cbn_layout_height)

    fun getTotalHeight(): Float {
        return layoutHeight
    }

    // top offset for the BottomNavigation
    private val bottomNavOffsetY =
        layoutHeight - resources.getDimensionPixelSize(
            R.dimen.cbn_height
        )

    // offset of the curve lowest point from the bottom of the BottomNavigation
    private val curveBottomOffset =
        resources.getDimensionPixelSize(R.dimen.cbn_bottom_curve_offset)

    // radius of the FAB
    private val fabRadius = resources.getDimension(R.dimen.cbn_fab_size) / 2

    // offset of the FAB from top of the layout (control how much deep the fab embeds in BottomNavigation)
    // if set to 0, then half of the FAB will embed in BottomNavigation
    private val fabTopOffset = resources.getDimension(R.dimen.cbn_fab_top_offset)

    // spacing between the fab and the curve (computed using other parameters)
    private val fabMargin = layoutHeight - fabSize - fabTopOffset - curveBottomOffset

    // offset of the top control point (independent of curves)
    private val topControlX = fabRadius + fabRadius / 2
    private val topControlY = bottomNavOffsetY + fabRadius / 6

    // offset of the bottom control point (independent of curves)
    private val bottomControlX = fabRadius + (fabRadius / 2)
    private val bottomControlY = fabRadius / 4

    // total width of the curve
    private val curveHalfWidth = fabRadius * 2 + fabMargin

    // center Y
    private val centerY = fabSize / 2f + fabTopOffset
    private var centerX = -1f
    private var curCenterY = centerY

    // flag to indicate the animation in progress (to control whether to handle the click or not)
    private var isAnimating = false

    // listener for the menuItemClick
    private var menuItemClickListener: ((CbnMenuItem, Int) -> Unit)? = null

    // control the rendering of the menu when the menu is empty
    private var isMenuInitialized = false

    private var animatorSet = AnimatorSet()

    // callback to synchronize the animation of AVD and this canvas when software canvas is used
    private val avdUpdateCallback = object : Drawable.Callback {
        override fun invalidateDrawable(who: Drawable) {
            this@CurvedBottomNavigationView.invalidate()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            /* no-op */
        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            /* no-op */
        }
    }

    init {
        // remove the bg as will do our own drawing
        setBackgroundColor(Color.TRANSPARENT)

        dotRadius = dotSize
        textSizeInSdp = fontTextSize

        // initialize the paint here with defaults, we will update paint colors latter from property setters
        navPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = navBackgroundColor
            setShadowLayer(
                navElevation,
                0f,
                6f,
                shadowColor
            )
        }


        textPaint = Paint().apply {
            color = textColor
            textSize = fontTextSize
        }



        dotPaint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }

        fabPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = fabBackgroundColor
            setShadowLayer(
                fabElevation,
                0f,
                6f,
                shadowColor
            )
        }

        // read the attributes values
        context.theme.obtainStyledAttributes(attrs, R.styleable.CurvedBottomNavigationView, 0, 0)
            .apply {
                try {
                    selectedColor = getColor(
                        R.styleable.CurvedBottomNavigationView_cbn_selectedColor,
                        selectedColor
                    )

                    showDot = getBoolean(R.CurvedBottomNavigationView_cbn_showDot, true)


                    textColor = getColor(
                        R.styleable.CurvedBottomNavigationView_cbn_textColor,
                        textColor
                    )

                    dotColor = getColor(
                        R.styleable.CurvedBottomNavigationView_cbn_dotColor,
                        dotColor
                    )

                    dotSize = getDimension(
                        R.styleable.CurvedBottomNavigationView_cbn_dotSize,
                        dotSize
                    )

                    fontTextSize = getDimension(
                        R.styleable.CurvedBottomNavigationView_cbn_textSize,
                        fontTextSize
                    )

                    unSelectedColor = getColor(
                        R.styleable.CurvedBottomNavigationView_cbn_unSelectedColor,
                        unSelectedColor
                    )

                    animDuration = getInteger(
                        R.styleable.CurvedBottomNavigationView_cbn_animDuration,
                        animDuration.toInt()
                    ).toLong()

                    fabBackgroundColor = getColor(
                        R.styleable.CurvedBottomNavigationView_cbn_fabBg,
                        fabBackgroundColor
                    )

                    navBackgroundColor = getColor(R.styleable.CurvedBottomNavigationView_cbn_bg, navBackgroundColor)
                    fabElevation = getDimension(
                        R.styleable.CurvedBottomNavigationView_cbn_fabElevation,
                        fabElevation
                    )
                    navElevation = getDimension(
                        R.styleable.CurvedBottomNavigationView_cbn_elevation,
                        navElevation
                    )
                } finally {
                    recycle()
                }
            }

        // use software rendering instead of hardware acceleration as hardware acceleration doesn't
        // support shadowLayer below API 28
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
    }

    fun isAnimating(): Boolean {
        return isAnimating
    }

    fun setMenuItems(cbnMenuItems: Array<CbnMenuItem>, activeIndex: Int = 0) {
        if (cbnMenuItems.isEmpty()) {
            isMenuInitialized = false
            return
        }
        this.cbnMenuItems = cbnMenuItems
        // initialize the index
        fabIconIndex = activeIndex
        selectedIndex = activeIndex
        bottomNavItemViews = Array(cbnMenuItems.size) {
            BottomNavItemView(context)
        }
        initializeMenuIcons()
        initializeMenuAVDs()
        initializeCurve(activeIndex)
        initializeBottomItems(activeIndex)
        isMenuInitialized = true

        // setup the initial AVD
        setupInitialAVD(activeIndex)
    }

    private fun setupInitialAVD(activeIndex: Int) {
        // set the initial callback to the active item, so that we can animate AVD during app startup
        menuAVDs[activeIndex].callback = avdUpdateCallback
        menuAVDs[selectedIndex].start()
    }

    private fun initializeCurve(index: Int) {
        // only run when this layout has been laid out
        doOnLayout {
            // compute the cell width and centerX for the fab
            menuCellWidth = width / cbnMenuItems.size
            val offsetX = menuCellWidth * index
            centerX = offsetX + menuCellWidth / 2f
            computeCurve(offsetX, menuCellWidth)
        }
    }


    private fun initializeMenuAVDs() {
        val activeColorFilter = PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
        menuAVDs = Array(cbnMenuItems.size) {
            val avd = AnimatedVectorDrawableCompat.create(context, cbnMenuItems[it].avdIcon)!!
            avd.colorFilter = activeColorFilter
            avd
        }
    }

    private fun initializeMenuIcons() {
        menuIcons = Array(cbnMenuItems.size) {
            val drawable =
                ResourcesCompat.getDrawable(resources, cbnMenuItems[it].icon, context.theme)!!
            DrawableCompat.setTint(drawable, unSelectedColor)
            drawable
        }
    }

    private fun updateMenuAVDsTint() {
        val activeColorFilter = PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
        menuAVDs.forEach {
            it.colorFilter = activeColorFilter
        }
    }

    private fun updateMenuIconsTint() {
        menuIcons.forEach {
            DrawableCompat.setTint(it, unSelectedColor)
        }
    }

    fun getMenuItems(): Array<CbnMenuItem> {
        return cbnMenuItems
    }

    // set the click listener for menu items
    fun setOnMenuItemClickListener(menuItemClickListener: (CbnMenuItem, Int) -> Unit) {
        this.menuItemClickListener = menuItemClickListener
    }

    // function to setup with navigation controller just like in BottomNavigationView
    fun setupWithNavController(navController: NavController) {
        // check for menu initialization
        if (!isMenuInitialized) {
            throw RuntimeException("initialize menu by calling setMenuItems() before setting up with NavController")
        }

        // initialize the menu
        setOnMenuItemClickListener { item, _ ->
            navigateToDestination(navController, item)
        }
        // setup destination change listener to properly sync the back button press
        navController.addOnDestinationChangedListener { _, destination, _ ->
            for (i in cbnMenuItems.indices) {
                if (matchDestination(destination, cbnMenuItems[i].destinationId)) {
                    if (selectedIndex != i && isAnimating) {
                        // this is triggered internally, even if the animations looks kinda funky (if duration is long)
                        // but we will sync with the destination
                        animatorSet.cancel()
                        isAnimating = false
                    }
                    onMenuItemClick(i)
                }
            }
        }
    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun navigateToDestination(navController: NavController, itemCbn: CbnMenuItem) {
        if (itemCbn.destinationId == -1) {
            throw RuntimeException("please set a valid id, unable the navigation!")
        }
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//         pop to the navigation graph's start  destination
        builder.setPopUpTo(findStartDestination(navController.graph).id, false)
        val options = builder.build()
        try {
            navController.navigate(itemCbn.destinationId, null, options)
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "unable to navigate!", e)
        }
    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun matchDestination(destination: NavDestination, @IdRes destinationId: Int): Boolean {
        var currentDestination = destination
        while (currentDestination.id != destinationId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent!!
        }

        return currentDestination.id == destinationId
    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun findStartDestination(graph: NavGraph): NavDestination {
        var startDestination: NavDestination = graph
        while (startDestination is NavGraph) {
            startDestination = graph.findNode(graph.startDestinationId)!!
        }

        return startDestination
    }

    private fun initializeBottomItems(activeItem: Int) {
        // clear layout
        removeAllViews()
        val bottomNavLayout = LinearLayout(context)
        // get the ripple from the theme
        val typedValue = TypedValue()
        context.theme.resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, typedValue, true)
        menuIcons.forEachIndexed { index, icon ->
            val menuItem = bottomNavItemViews[index]
            menuItem.setMenuIcon(icon)
            menuItem.setOnClickListener {
                onMenuItemClick(index)
            }
            if (index == activeItem) {
                // render the icon in fab instead of image view, but still allocate the space
                menuItem.visibility = View.INVISIBLE
            }
            val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams.weight = 1f
            bottomNavLayout.addView(menuItem, layoutParams)
        }
        val bottomNavLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.getDimension(R.dimen.cbn_height).toInt(),
            Gravity.BOTTOM
        )
        addView(bottomNavLayout, bottomNavLayoutParams)
    }

    fun onMenuItemClick(index: Int) {
        if (selectedIndex == index) {
            Log.i(TAG, "same icon multiple clicked, skipping animation!")
            return
        }
        if (isAnimating) {
            Log.i(TAG, "animation is in progress, skipping navigation")
            return
        }

        fabIconIndex = selectedIndex
        menuAVDs[index].stop()
        prevSelectedIndex = selectedIndex
        selectedIndex = index
        // make all item except current item invisible
        bottomNavItemViews.forEachIndexed { i, imageView ->
            if (prevSelectedIndex == i) {
                // show the previous selected view with alpha 0
                imageView.visibility = VISIBLE
                imageView.alpha = 0f
            }
        }
        val newOffsetX = menuCellWidth * index
        isAnimating = true
        animateItemSelection(newOffsetX, menuCellWidth, index)
        // notify the listener
        menuItemClickListener?.invoke(cbnMenuItems[index], index)
    }

    private fun animateItemSelection(offset: Int, width: Int, index: Int) {
        val finalCenterX = menuCellWidth * index + (menuCellWidth / 2f)
        val propertyOffset = PropertyValuesHolder.ofInt(PROPERTY_OFFSET, cellOffsetX, offset)
        val propertyCenterX = PropertyValuesHolder.ofFloat(PROPERTY_CENTER_X, centerX, finalCenterX)

        // watch the direction and compute the diff
        val isLTR = (prevSelectedIndex - index) < 0
        val diff = abs(prevSelectedIndex - index)
        // time allocated for each icon in the bottom nav
        val iconAnimSlot = animDuration / diff
        // compute the time it will take to move from start to bottom of the curve
        val curveBottomOffset = ((curveHalfWidth * animDuration) / this.width).toLong()

        val offsetAnimator = getBezierCurveAnimation(
            animDuration,
            width,
            iconAnimSlot,
            isLTR,
            index,
            curveBottomOffset,
            diff,
            propertyOffset,
            propertyCenterX
        )

        val fabYOffset = firstCurveEnd.y + fabRadius
        val halfAnimDuration = animDuration / 2
        // hide the FAB
        val centerYAnimatorHide = hideFAB(fabYOffset)
        centerYAnimatorHide.duration = halfAnimDuration

        // show the FAB with delay
        val centerYAnimatorShow = showFAB(fabYOffset, index)
        centerYAnimatorShow.startDelay = halfAnimDuration
        centerYAnimatorShow.duration = halfAnimDuration

        animatorSet = AnimatorSet()
        animatorSet.playTogether(centerYAnimatorHide, offsetAnimator, centerYAnimatorShow)
        animatorSet.interpolator = FastOutSlowInInterpolator()
        animatorSet.start()
    }

    private fun getBezierCurveAnimation(
        slideAnimDuration: Long,
        width: Int,
        iconAnimSlot: Long,
        isLTR: Boolean,
        index: Int,
        curveBottomOffset: Long,
        diff: Int,
        vararg propertyOffset: PropertyValuesHolder,
    ): ValueAnimator {
        return ValueAnimator().apply {
            setValues(*propertyOffset)
            duration = slideAnimDuration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // reset all the status of icon animations
                    bottomNavItemViews.forEach {
                        it.resetAnimation()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    // reset all the status of icon animations
                    bottomNavItemViews.forEach {
                        it.resetAnimation()
                    }
                }
            })
            addUpdateListener { animator ->
                val newOffset = getAnimatedValue(PROPERTY_OFFSET) as Int
                // the curve will animate no matter what
                computeCurve(newOffset, width)
                invalidate()

                // change the centerX of the FAB
                centerX = getAnimatedValue(PROPERTY_CENTER_X) as Float
                val currentTime = animator.animatedFraction * slideAnimDuration
                // compute the index above which this curve is moving
                // this is the relative index with respect to the previousSelectedIndex
                var overIconIndex = ((currentTime + (iconAnimSlot)) / iconAnimSlot).toInt()
                if (isLTR) {
                    // add the offset
                    overIconIndex += prevSelectedIndex
                    // prevent animation when animatedFraction is 0
                    if (overIconIndex > index) {
                        return@addUpdateListener
                    }
                } else {
                    // add the offset
                    overIconIndex = prevSelectedIndex - overIconIndex
                    // prevent animation when animatedFraction is 0
                    if (overIconIndex < index) {
                        return@addUpdateListener
                    }
                }

                when {
                    overIconIndex == index -> {
                        // we are within the destination
                        // animate the destination icon within the time the curve reaches it's boundary
                        bottomNavItemViews[index].startDestinationAnimation(curveBottomOffset)
                        if (diff == 1) {
                            // also animate the source icon as this is the adjacent click event
                            bottomNavItemViews[prevSelectedIndex].startSourceAnimation(
                                slideAnimDuration
                            )
                        }
                    }
                    abs(overIconIndex - prevSelectedIndex) == 1 -> {
                        // we currently in the adjacent icon of the current source icon, show source animations
                        bottomNavItemViews[prevSelectedIndex].startSourceAnimation(slideAnimDuration)
                        // also initialize the intermediate animations
                        bottomNavItemViews[overIconIndex].startIntermediateAnimation(
                            slideAnimDuration,
                            curveBottomOffset
                        )
                    }
                    else -> {
                        // we over intermediate icons, show the intermediate animations
                        bottomNavItemViews[overIconIndex].startIntermediateAnimation(
                            slideAnimDuration,
                            curveBottomOffset
                        )
                    }
                }
            }
        }
    }

    private fun showFAB(
        fabYOffset: Float,
        index: Int
    ): ValueAnimator {
        val propertyCenterYReverse =
            PropertyValuesHolder.ofFloat(PROPERTY_CENTER_Y, fabYOffset, centerY)
        return ValueAnimator().apply {
            setValues(propertyCenterYReverse)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    // set the callback before starting the animation as the Drawable class
                    // internally uses WeakReference. So settings the callback only during initialization
                    // will result in callback being cleared after certain time. This is a good place
                    // to set the callback so that we can sync the drawable animation with our canvas
                    menuAVDs[index].callback = avdUpdateCallback
                    menuAVDs[index].start()
                }

                override fun onAnimationEnd(animation: Animator) {
                    // disable the clicks in the target view
                    bottomNavItemViews[index].visibility = INVISIBLE
                }
            })
            addUpdateListener { animator ->
                val newCenterY = animator.getAnimatedValue(PROPERTY_CENTER_Y) as Float
                curCenterY = newCenterY
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                }
            })
        }
    }

    private fun hideFAB(fabYOffset: Float): ValueAnimator {
        val propertyCenterY =
            PropertyValuesHolder.ofFloat(PROPERTY_CENTER_Y, centerY, fabYOffset)
        return ValueAnimator().apply {
            setValues(propertyCenterY)
            addUpdateListener { animator ->
                val newCenterY = animator.getAnimatedValue(PROPERTY_CENTER_Y) as Float
                curCenterY = newCenterY
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    fabIconIndex = selectedIndex
                }
            })
        }
    }


    private fun computeCurve(offsetX: Int, w: Int) {
        // store the current offset (useful when animating)
        this.cellOffsetX = offsetX
        // first curve
        firstCurveStart.apply {
            x = offsetX + (w / 2) - curveHalfWidth
            y = bottomNavOffsetY
        }
        firstCurveEnd.apply {
            x = offsetX + (w / 2f)
            y = layoutHeight - curveBottomOffset
        }
        firstCurveControlPoint1.apply {
            x = firstCurveStart.x + topControlX
            y = topControlY
        }
        firstCurveControlPoint2.apply {
            x = firstCurveEnd.x - bottomControlX
            y = firstCurveEnd.y - bottomControlY
        }

        // second curve
        secondCurveStart.set(firstCurveEnd.x, firstCurveEnd.y)
        secondCurveEnd.apply {
            x = offsetX + (w / 2) + curveHalfWidth
            y = bottomNavOffsetY
        }
        secondCurveControlPoint1.apply {
            x = secondCurveStart.x + bottomControlX
            y = secondCurveStart.y - bottomControlY
        }
        secondCurveControlPoint2.apply {
            x = secondCurveEnd.x - topControlX
            y = topControlY
        }

        // generate the path
        path.reset()
        path.moveTo(0f, bottomNavOffsetY)
        // horizontal line from left to the start of first curve
        path.lineTo(firstCurveStart.x, firstCurveStart.y)
        // add the first curve
        path.cubicTo(
            firstCurveControlPoint1.x,
            firstCurveControlPoint1.y,
            firstCurveControlPoint2.x,
            firstCurveControlPoint2.y,
            firstCurveEnd.x,
            firstCurveEnd.y
        )
        // add the second curve
        path.cubicTo(
            secondCurveControlPoint1.x,
            secondCurveControlPoint1.y,
            secondCurveControlPoint2.x,
            secondCurveControlPoint2.y,
            secondCurveEnd.x,
            secondCurveEnd.y
        )
        // continue to draw the remaining portion of the bottom navigation
        path.lineTo(width.toFloat(), bottomNavOffsetY)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // our minimum height is defined in R.dimen.cbn_layout_height
        // currently we don't support custom height and use defaults suggested by Material Design Specs
        val h: Int =
            paddingTop + paddingBottom + resources.getDimensionPixelSize(R.dimen.cbn_layout_height)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {

        if (!isMenuInitialized) {
            return
        }




        // Draw circle for FAB (Selected item indicator)
        canvas.drawCircle(centerX, curCenterY, fabSize / 2f, fabPaint)

        // Draw AVD (Animated Vector Drawable) within the FAB circle
        menuAVDs[fabIconIndex].setBounds(
            (centerX - menuIcons[fabIconIndex].intrinsicWidth / 2).toInt(),
            (curCenterY - menuIcons[fabIconIndex].intrinsicHeight / 2).toInt(),
            (centerX + menuIcons[fabIconIndex].intrinsicWidth / 2).toInt(),
            (curCenterY + menuIcons[fabIconIndex].intrinsicHeight / 2).toInt()
        )

        menuAVDs[fabIconIndex].draw(canvas)

        // Draw the path for the bottom navigation
        canvas.drawPath(path, navPaint)

        // Loop through each bottom navigation item
        for (i in bottomNavItemViews.indices) {
            val itemCenterX = menuCellWidth * i + (menuCellWidth / 2f)
            val textY = layoutHeight - 2 // Position the text

            // If the item is selected, show a dot instead of text
            if (i == selectedIndex && showDot) {
                canvas.drawCircle(itemCenterX, layoutHeight -12, dotRadius, dotPaint)
            } else {
                // Draw text for non-selected items
                textPaint.textAlign = Paint.Align.CENTER
                canvas.drawText(cbnMenuItems[i].title, itemCenterX, textY, textPaint)
            }
        }
    }

    private fun handleItemClick(index: Int) {
        selectedIndex = index
        // Trigger a redraw to update the text and dot
        invalidate()
    }
}




 */



import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch


@Composable
fun CurvedBottomNavigationView(
    navController: NavController,
    menuItems: List<CbnMenuItem>,
    modifier: Modifier = Modifier,
    dotColor: Color = Color.White,
    textColor: Color = Color.White,
    showDot: Boolean = true,
    selectedColor: Color = Color.Black,
    unSelectedColor: Color = Color.Gray,
    navBackgroundColor: Color = Color.White,
    fabBackgroundColor: Color = Color.White,
    navElevation: Dp = 6.dp,
    fabElevation: Dp = 4.dp,
    animDuration: Int = 300
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentDestination = navBackStackEntry?.destination

    // Trouver l'index de l'élément sélectionné
    var currentIndex = remember(currentDestination) {
        menuItems.indexOfFirst { item ->
            currentDestination?.hierarchy?.any { it.route == item.route } ?: false
        }.let { if (it == -1) 0 else it }
    }

    // Stocker l'index précédemment sélectionné
    var prevSelectedIndex by remember { mutableStateOf(currentIndex) }

    // État pour l'animation
    var selectedIndex by remember { mutableStateOf(currentIndex) }
    var fabIconIndex by remember { mutableStateOf(currentIndex) }
    var isAnimating by remember { mutableStateOf(false) }

    // Animation de transition
    val animationScope = rememberCoroutineScope()

    // États pour l'animation
    val animatedCenterX = remember { Animatable(0f) }
    val animatedCenterY = remember { Animatable(0f) }
    val animatedCurveOffset = remember { Animatable(0f) }

    // Pour mesurer et dessiner le texte
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // Dimensions constantes
    val layoutHeight = 80.dp
    val bottomNavOffsetY = 16.dp
    val fabSize = 56.dp
    val fabRadius = fabSize / 2
    val curveBottomOffset = 4.dp
    val fabTopOffset = 4.dp
    val curveHalfWidth = fabRadius * 2

    // Points de contrôle pour les courbes de Bézier
    var bezierCurveState = remember { BezierCurveState() }

    // Fonction pour calculer les points de la courbe
    fun computeCurve(
        width: Float,
        offsetX: Float,
        menuItemWidth: Float,
        bezierCurve: BezierCurveState,
        layoutHeightPx: Float,
        bottomNavOffsetYPx: Float,
        curveBottomOffsetPx: Float,
        curveHalfWidthPx: Float
    ) {
        var centerX = offsetX + menuItemWidth / 2

        // Premier point de courbe
     /*   bezierCurve.firstCurveStart.x = centerX - curveHalfWidthPx
        bezierCurve.firstCurveStart.y = bottomNavOffsetYPx

        // Point final de la première courbe
        bezierCurve.firstCurveEnd.x = centerX
        bezierCurve.firstCurveEnd.y = layoutHeightPx - curveBottomOffsetPx

        // Points de contrôle pour première courbe
        bezierCurve.firstCurveControlPoint1.x = bezierCurve.firstCurveStart.x + curveHalfWidthPx / 2
        bezierCurve.firstCurveControlPoint1.y = bottomNavOffsetYPx + curveHalfWidthPx / 6

        bezierCurve.firstCurveControlPoint2.x = bezierCurve.firstCurveEnd.x - curveHalfWidthPx / 2
        bezierCurve.firstCurveControlPoint2.y = bezierCurve.firstCurveEnd.y - curveHalfWidthPx / 4

        // Deuxième courbe
        bezierCurve.secondCurveStart.x = bezierCurve.firstCurveEnd.x
        bezierCurve.secondCurveStart.y = bezierCurve.firstCurveEnd.y

        // Point final de la deuxième courbe
        bezierCurve.secondCurveEnd.x = centerX + curveHalfWidthPx
        bezierCurve.secondCurveEnd.y = bottomNavOffsetYPx

        // Points de contrôle pour deuxième courbe
        bezierCurve.secondCurveControlPoint1.x = bezierCurve.secondCurveStart.x + curveHalfWidthPx / 2
        bezierCurve.secondCurveControlPoint1.y = bezierCurve.secondCurveStart.y - curveHalfWidthPx / 4

        bezierCurve.secondCurveControlPoint2.x = bezierCurve.secondCurveEnd.x - curveHalfWidthPx / 2
        bezierCurve.secondCurveControlPoint2.y = bottomNavOffsetYPx + curveHalfWidthPx / 6

      */
        // Point de départ de la première courbe
        bezierCurve.firstCurveStart = Offset(
            x = centerX - curveHalfWidthPx,
            y = bottomNavOffsetYPx
        )

// Point final de la première courbe
        bezierCurve.firstCurveEnd = Offset(
            x = centerX,
            y = layoutHeightPx - curveBottomOffsetPx
        )

// Points de contrôle pour la première courbe
        bezierCurve.firstCurveControlPoint1 = Offset(
            x = bezierCurve.firstCurveStart.x + curveHalfWidthPx / 2,
            y = bottomNavOffsetYPx + curveHalfWidthPx / 6
        )

        bezierCurve.firstCurveControlPoint2 = Offset(
            x = bezierCurve.firstCurveEnd.x - curveHalfWidthPx / 2,
            y = bezierCurve.firstCurveEnd.y - curveHalfWidthPx / 4
        )

// Début de la deuxième courbe
        bezierCurve.secondCurveStart = Offset(
            x = bezierCurve.firstCurveEnd.x,
            y = bezierCurve.firstCurveEnd.y
        )

// Fin de la deuxième courbe
        bezierCurve.secondCurveEnd = Offset(
            x = centerX + curveHalfWidthPx,
            y = bottomNavOffsetYPx
        )

    }

    // Gérer le clic sur un élément du menu
    fun handleItemClick(index: Int) {
        if (selectedIndex == index || isAnimating) return

        // Mettre à jour les états
        isAnimating = true
        fabIconIndex = selectedIndex
        prevSelectedIndex = selectedIndex
        selectedIndex = index

        // Calculer les nouvelles positions
        val menuItemWidth = 1f / menuItems.size
        val newOffsetX = index * menuItemWidth
        val centerX = newOffsetX + menuItemWidth / 2

        // Démarrer les animations
        animationScope.launch {
            // Animation pour le déplacement horizontal
            launch {
                animatedCurveOffset.animateTo(
                    targetValue = newOffsetX,
                    animationSpec = tween(
                        durationMillis = animDuration,
                        easing = FastOutSlowInInterpolator
                    )
                )
            }

            // Animation pour le centre X du FAB
            launch {
                animatedCenterX.animateTo(
                    targetValue = centerX,
                    animationSpec = tween(
                        durationMillis = animDuration,
                        easing = FastOutSlowInInterpolator
                    )
                )
            }

            // Animation pour masquer puis afficher le FAB (déplacement vertical)
            val fabYOffset = with(density) { (layoutHeight - curveBottomOffset + fabRadius).toPx() }
            val centerY = with(density) { (fabTopOffset + fabRadius).toPx() }

            // Cacher le FAB
            launch {
                animatedCenterY.animateTo(
                    targetValue = fabYOffset,
                    animationSpec = tween(
                        durationMillis = animDuration / 2,
                        easing = FastOutSlowInInterpolator
                    )
                )

                // Changer l'icône et montrer le FAB
                fabIconIndex = selectedIndex
                animatedCenterY.animateTo(
                    targetValue = centerY,
                    animationSpec = tween(
                        durationMillis = animDuration / 2,
                        easing = FastOutSlowInInterpolator
                    )
                )

                // Animation terminée
                isAnimating = false
            }
        }

        // Naviguer vers la destination
        navController.navigate(menuItems[index].route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Effet pour initialiser les animations
    LaunchedEffect(menuItems, currentIndex) {
        val menuItemWidth = 1f / menuItems.size
        val initialOffsetX = currentIndex * menuItemWidth
        val initialCenterX = initialOffsetX + menuItemWidth / 2
        val centerY = with(density) { (fabTopOffset + fabRadius).toPx() }

        animatedCenterX.snapTo(initialCenterX)
        animatedCenterY.snapTo(centerY)
        animatedCurveOffset.snapTo(initialOffsetX)

        selectedIndex = currentIndex
        fabIconIndex = currentIndex
    }

    // Effet pour synchroniser avec la navigation
    LaunchedEffect(currentDestination) {
        val newIndex = menuItems.indexOfFirst { item ->
            currentDestination?.hierarchy?.any { it.route == item.route } ?: false
        }.let { if (it == -1) selectedIndex else it }

        if (newIndex != selectedIndex && !isAnimating) {
            handleItemClick(newIndex)
        }
    }

    // Layout principal
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(layoutHeight + fabSize / 2)
    ) {
        // Canvas pour dessiner la navigation et ses courbes
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
        ) {
            var width = size.width
            var height = size.height
            var menuItemWidth = width / menuItems.size
            var layoutHeightPx = with(density) { layoutHeight.toPx() }
            var bottomNavOffsetYPx = with(density) { bottomNavOffsetY.toPx() }
            var curveBottomOffsetPx = with(density) { curveBottomOffset.toPx() }
            var curveHalfWidthPx = with(density) { curveHalfWidth.toPx() }
            var fabRadiusPx = with(density) { fabRadius.toPx() }

            // Calculer les points de la courbe
            var offsetX = animatedCurveOffset.value * width
            computeCurve(
                width,
                offsetX,
                menuItemWidth,
                bezierCurveState,
                layoutHeightPx,
                bottomNavOffsetYPx,
                curveBottomOffsetPx,
                curveHalfWidthPx
            )

            // Dessiner le fond de la navigation
            drawNavBackground(
                bezierCurveState,
                width,
                height,
                bottomNavOffsetYPx,
                navBackgroundColor
            )

            // Dessiner le cercle FAB
            drawCircle(
                color = fabBackgroundColor,
                radius = fabRadiusPx,
                center = Offset(
                    animatedCenterX.value * width,
                    animatedCenterY.value
                ),
                style = Fill
            )

            // Dessiner les textes et points des éléments du menu
            menuItems.forEachIndexed { index, item ->
                val itemCenterX = (index + 0.5f) * menuItemWidth
                val textY = height - 12.dp.toPx()

                if (index == selectedIndex && showDot) {
                    // Dessiner le point pour l'élément sélectionné
                    drawCircle(
                        color = dotColor,
                        radius = 4.dp.toPx(),
                        center = Offset(itemCenterX, height - 12.dp.toPx())
                    )
                } else {
                    // Dessiner le texte pour les éléments non sélectionnés
                    val textLayoutResult = textMeasurer.measure(
                        text = item.title,
                        style = TextStyle(
                            color = textColor,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            itemCenterX - textLayoutResult.size.width / 2f,
                            textY - textLayoutResult.size.height
                        )
                    )
                }
            }
        }

        // Contenu cliquable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(layoutHeight)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            menuItems.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            handleItemClick(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Afficher les icônes (sauf pour l'élément sélectionné qui est affiché dans le FAB)
                    if (index != selectedIndex || isAnimating) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = unSelectedColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        var parentSize by remember { mutableStateOf(IntSize.Zero) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    parentSize = coordinates.size
                }
        ) {
            Box(
                modifier = Modifier
                    .size(fabSize)
                    .align(Alignment.TopCenter)
                    .offset(
                        x = with(density) { (animatedCenterX.value * parentSize.width - parentSize.width / 2).toDp() },
                        y = with(density) { (animatedCenterY.value - fabRadius.toPx()).toDp() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = menuItems[fabIconIndex].selectedIcon,
                    contentDescription = menuItems[fabIconIndex].title,
                    tint = selectedColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        // FAB Icon
       /*
        Box(
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.TopCenter)
                .offset(
                    x = with(density) { (animatedCenterX.value * size.width - size.width / 2).toDp() },
                    y = with(density) { (animatedCenterY.value - fabRadius.toPx()).toDp() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = menuItems[fabIconIndex].selectedIcon,
                contentDescription = menuItems[fabIconIndex].title,
                tint = selectedColor,
                modifier = Modifier.size(24.dp)
            )
        } */
    }
}

// Fonction d'extension pour le FastOutSlowInInterpolator
public  var FastOutSlowInInterpolator = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

// Classe pour stocker les points de la courbe de Bézier

class BezierCurveState {
    var firstCurveStart by mutableStateOf(Offset(0f, 0f))
    var firstCurveEnd by mutableStateOf(Offset(0f, 0f))
    var firstCurveControlPoint1 by mutableStateOf(Offset(0f, 0f))
    var firstCurveControlPoint2 by mutableStateOf(Offset(0f, 0f))

    var secondCurveStart by mutableStateOf(Offset(0f, 0f))
    var secondCurveEnd by mutableStateOf(Offset(0f, 0f))
    var secondCurveControlPoint1 by mutableStateOf(Offset(0f, 0f))
    var secondCurveControlPoint2 by mutableStateOf(Offset(0f, 0f))
}

// Fonction pour dessiner le fond de la navigation
public fun DrawScope.drawNavBackground(
    bezierCurve: BezierCurveState,
    width: Float,
    height: Float,
    bottomNavOffsetY: Float,
    color: Color
) {
    val path = Path().apply {
        // Commencer par le coin supérieur gauche
        moveTo(0f, bottomNavOffsetY)

        // Ligne horizontale jusqu'au début de la première courbe
        lineTo(bezierCurve.firstCurveStart.x, bezierCurve.firstCurveStart.y)

        // Première courbe de Bézier
        cubicTo(
            bezierCurve.firstCurveControlPoint1.x, bezierCurve.firstCurveControlPoint1.y,
            bezierCurve.firstCurveControlPoint2.x, bezierCurve.firstCurveControlPoint2.y,
            bezierCurve.firstCurveEnd.x, bezierCurve.firstCurveEnd.y
        )

        // Deuxième courbe de Bézier
        cubicTo(
            bezierCurve.secondCurveControlPoint1.x, bezierCurve.secondCurveControlPoint1.y,
            bezierCurve.secondCurveControlPoint2.x, bezierCurve.secondCurveControlPoint2.y,
            bezierCurve.secondCurveEnd.x, bezierCurve.secondCurveEnd.y
        )

        // Compléter le reste du fond
        lineTo(width, bottomNavOffsetY)
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }

    // Dessiner l'ombre (on pourrait utiliser un Modifier.shadow à la place)
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.1f),
        style = Stroke(width = 3f,
          //  blur = BlurEffect(8f, BlurEffect.TileMode.Decal)
        )
    )

    // Dessiner le fond
    drawPath(
        path = path,
        color = color
    )
}

// Exemple d'utilisation
@Composable
fun BottomNavigationBarExample(navController: NavController) {
    // Exemple de liste d'éléments
    val menuItems = listOf(
        CbnMenuItem(
            route = "home",
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            title = "Accueil"
        ),
        CbnMenuItem(
            route = "appointments",
            icon = Icons.Outlined.Favorite,
            selectedIcon = Icons.Filled.Favorite,
            title = "Favoris"
        ),
        CbnMenuItem(
            route = "qr_scanner",
            icon = Icons.Outlined.Dashboard,
            selectedIcon = Icons.Filled.Dashboard,
            title = "Dashboard"
        ),
        CbnMenuItem(
            route = "profile",
            icon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person,
            title = "Profil"
        ),
        CbnMenuItem(
            route = "settings",
            icon = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings,
            title = "Paramètres"
        )
    )

    CurvedBottomNavigationView(
        navController = navController,
        menuItems = menuItems,
        modifier = Modifier.fillMaxWidth(),
        fabBackgroundColor = Color(0xFFFDB813),  // Jaune comme dans l'image
        selectedColor = Color.White,
        dotColor = Color(0xFFFDB813)
    )
}
