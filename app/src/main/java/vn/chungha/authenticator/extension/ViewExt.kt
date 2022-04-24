package vn.chungha.authenticator.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import vn.chungha.authenticator.utils.SafeOnClickListener
import kotlin.math.roundToInt

fun View.onClick(safe: Boolean = true, action: (View) -> Unit) = setOnClickListener(
    SafeOnClickListener(safe, action)
)


fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

inline fun <reified T : View> ViewGroup.inflate(@LayoutRes resId: Int) =
    LayoutInflater.from(context).inflate(resId, this, false) as T

val RecyclerView.hasItems: Boolean
    get() = (adapter?.itemCount ?: 0) > 0

fun RecyclerView.clearItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

var RecyclerView.firstItem: Int
    get() = (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        ?: RecyclerView.NO_POSITION
    set(value) {
        if (value != RecyclerView.NO_POSITION) {
            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(value, 0)
        }
    }

inline fun View.showPopupMenu(
    @MenuRes menuRes: Int,
    onPrepare: (Menu) -> Unit = {},
    onItemClick: PopupMenu.OnMenuItemClickListener,
) {
    val menu = PopupMenu(context, this)
    menu.inflate(menuRes)
    menu.setOnMenuItemClickListener(onItemClick)
    onPrepare(menu.menu)
    menu.show()
}

fun ViewGroup.hitTest(x: Int, y: Int): Set<View> {
    val result = HashSet<View>(4)
    val rect = Rect()
    for (child in children) {
        if (child.isVisible && child.getGlobalVisibleRect(rect)) {
            if (rect.contains(x, y)) {
                if (child is ViewGroup) {
                    result += child.hitTest(x, y)
                } else {
                    result += child
                }
            }
        }
    }
    return result
}

fun View.hasGlobalPoint(x: Int, y: Int): Boolean {
    if (visibility != View.VISIBLE) {
        return false
    }
    val rect = Rect()
    getGlobalVisibleRect(rect)
    return rect.contains(x, y)
}

fun DrawerLayout.toggleDrawer(gravity: Int) {
    if (isDrawerOpen(gravity)) {
        closeDrawer(gravity)
    } else {
        openDrawer(gravity)
    }
}

fun View.measureHeight(): Int {
    val vh = height
    return if (vh == 0) {
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        measuredHeight
    } else vh
}

fun View.measureWidth(): Int {
    val vw = width
    return if (vw == 0) {
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        measuredWidth
    } else vw
}

inline fun ViewPager2.doOnPageChanged(crossinline callback: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            callback(position)
        }
    })
}

val ViewPager2.recyclerView: RecyclerView?
    inline get() = children.find { it is RecyclerView } as? RecyclerView

fun View.resetTransformations() {
    alpha = 1f
    translationX = 0f
    translationY = 0f
    translationZ = 0f
    scaleX = 1f
    scaleY = 1f
    rotationX = 0f
    rotationY = 0f
}

inline fun RecyclerView.doOnCurrentItemChanged(crossinline callback: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private var lastItem = -1

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val item = recyclerView.findCenterViewPosition()
            if (item != RecyclerView.NO_POSITION && item != lastItem) {
                lastItem = item
                callback(item)
            }
        }
    })
}

fun RecyclerView.findCenterViewPosition(): Int {
    val centerX = width / 2f
    val centerY = height / 2f
    val view = findChildViewUnder(centerX, centerY) ?: return RecyclerView.NO_POSITION
    return getChildAdapterPosition(view)
}

fun BaseProgressIndicator<*>.setIndeterminateCompat(indeterminate: Boolean) {
    if (isIndeterminate != indeterminate) {
        if (indeterminate && visibility == View.VISIBLE) {
            visibility = View.INVISIBLE
            isIndeterminate = indeterminate
            visibility = View.VISIBLE
        } else {
            isIndeterminate = indeterminate
        }
    }
}

fun resolveAdjustedSize(
    desiredSize: Int,
    maxSize: Int,
    measureSpec: Int,
): Int {
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)
    return when (specMode) {
        View.MeasureSpec.UNSPECIFIED ->
            // Parent says we can be as big as we want. Just don't be larger
            // than max size imposed on ourselves.
            desiredSize.coerceAtMost(maxSize)
        View.MeasureSpec.AT_MOST ->
            // Parent says we can be as big as we want, up to specSize.
            // Don't be larger than specSize, and don't be larger than
            // the max size imposed on ourselves.
            desiredSize.coerceAtMost(specSize).coerceAtMost(maxSize)
        View.MeasureSpec.EXACTLY ->
            // No choice. Do what we are told.
            specSize
        else ->
            // This should not happen
            desiredSize
    }
}

fun Slider.setValueRounded(newValue: Float) {
    val step = stepSize
    value = (newValue / step).roundToInt() * step
}

fun TextView.setTextAndVisibility(string: String?) {
    isVisible = !string.isNullOrBlank()
    text = string
}

fun View.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    anchor: View? = null
) {
    Snackbar.make(this, message, duration)
        .setAnchorView(anchor)
        .show()
}

fun View.showSnackBar(
    @StringRes textId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    anchor: View? = null
) {
    Snackbar.make(this, textId, duration)
        .setAnchorView(anchor)
        .show()
}

fun View.showSnackBar(
    @StringRes textId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    @IdRes anchor: Int
) {
    Snackbar.make(this, textId, duration)
        .setAnchorView(anchor)
        .show()
}
fun Context.showSnackBar(
    view: View,
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view, message, duration).show()
}
fun Context.openBrowser(
    url: String
) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    this.startActivity(browserIntent)
}
fun Context.openWebUrl(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    return try {
        startActivity(i)
    } catch (error: ActivityNotFoundException) {
    }
}
@ExperimentalCoroutinesApi
val EditText.textFlow: Flow<String>
    get() = callbackFlow {
        val textWatcher = doAfterTextChanged { trySend(it.toString()) }
        awaitClose { removeTextChangedListener(textWatcher) }
    }

fun Fragment.openWebUrl(url: String) = requireContext().openWebUrl(url)

fun View.openWebUrl(url: String) = context.openWebUrl(url)

fun View.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.let { leftMargin = it }
        top?.let { topMargin = it }
        right?.let { rightMargin = it }
        bottom?.let { bottomMargin = it }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.focusAndShowKeyboard() {
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun View.setVisiable(visiable: Boolean, invisiable: Int = View.GONE) {
    visibility = if (visiable) View.VISIBLE else invisiable
}

fun View.onLongClick(action: (View) -> Unit) = setOnLongClickListener {
    action(it)
    true
}

fun ImageView.loadImageFromUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadImageFromDrawable(image : Int) {
    Glide.with(context)
        .load(image)
        .into(this)
}
fun View.expandTouch(amount: Int = 50) {
    val rect = Rect()
    this.getHitRect(rect)
    rect.top -= amount
    rect.right += amount
    rect.bottom += amount
    rect.left -= amount
    (this.parent as View).touchDelegate = TouchDelegate(rect, this)
}

inline val ViewGroup.inflater: LayoutInflater get() = LayoutInflater.from(context)

fun View.visibleIf(condition: Boolean, gone: Boolean = true) =
    if (condition) {
        visible()
    } else {
        if (gone) gone() else invisible()
    }

fun View.setPaddingHorizontal(padding: Int) {
    setPadding(padding, paddingTop, padding, paddingBottom)
}

fun View.setPaddingVertical(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, padding)
}

fun getColor(context: Context, idColor: Int): Int {
    return ContextCompat.getColor(context, idColor)
}

fun Context.dimenToPx(@DimenRes dimenResId: Int) = resources.getDimensionPixelSize(dimenResId)