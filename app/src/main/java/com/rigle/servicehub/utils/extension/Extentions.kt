package com.rigle.servicehub.utils.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.MalformedJsonException
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.ViewDataHomeBinding
import com.rigle.servicehub.databinding.ViewEmptyBinding
import com.rigle.servicehub.databinding.ViewLoaderBinding
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.rxutils.EventBus
import com.rigle.servicehub.utils.view.MyToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.blurry.Blurry
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

/** Network Extensions */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun SharedPreferences.saveValue(key: String, value: Any?) {
    when (value) {
        is String? -> editNdCommit { it.putString(key, value) }
        is Int -> editNdCommit { it.putInt(key, value) }
        is Boolean -> editNdCommit { it.putBoolean(key, value) }
        is Float -> editNdCommit { it.putFloat(key, value) }
        is Long -> editNdCommit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

fun <T> SharedPreferences.getValue(key: String, defaultValue: Any? = null): T? {
    return when (defaultValue) {
        is String? -> {
            getString(key, defaultValue as? String) as? T
        }
        is Int -> {
            getInt(key, defaultValue as? Int ?: -1) as? T
        }
        is Boolean -> getBoolean(key, defaultValue as? Boolean ?: false) as? T
        is Float -> getFloat(key, defaultValue as? Float ?: -1f) as? T
        is Long -> getLong(key, defaultValue as? Long ?: -1) as? T
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

inline fun SharedPreferences.editNdCommit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}


fun Activity.hideKeyboard() {
    val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    try {
        val manager =
            this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    } catch (e: Exception) {

    }
}


fun Fragment.showSuccessToast(message: String) {
    MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showSuccessToast(message: String) {
    MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showInfoToast(message: String) {
    MyToast.info(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.showErrorToast(message: String) {
    MyToast.error(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showErrorToast(message: String) {
    MyToast.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.showInfoToast(message: String) {
    MyToast.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this.requireContext(), message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.successToast(message: String) {
    if (message.isNotEmpty())
        MyToast.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also {
        it.view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
        it.show()
    }
}


fun BaseViewModel.parseException(it: Throwable?): String? {
    when (it) {
        is HttpException -> {
            when (it.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    val message = getErrorText(it)
                    if (message.contains("Unauth")) {
                        EventBus.post(UnAuthUser())
                        return ""
                    }
                    return message
                }
                HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                    return it.message()
                }
                else -> {
                    return getErrorText(it)
                }
            }
        }
        is MalformedJsonException -> {
            return it.message
        }
        is IOException -> {
            return "Slow or No Internet Access"
        }
        else -> {
            return it?.message.toString()
        }
    }


}

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Resources.dptoPx(dp: Int): Float {
    return dp * this.displayMetrics.density
}

fun Activity.startNewActivity(dest: Class<*>) {
    val intent = Intent(this, dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
}

fun Activity.startNewActivity(dest: Class<*>, finish: Boolean) {
    val intent = Intent(this, dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    if (finish)
        finish()
}

fun Fragment.startNewActivity(dest: Class<*>, finish: Boolean) {
    val intent = Intent(this.requireContext(), dest)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    if (finish)
        this.activity?.finish()
}

fun EditText.focusMode(mode: Boolean) {
    this.isFocusableInTouchMode = mode
    this.isFocusable = mode
}

fun ViewLoaderBinding.showLoading() {
    this.shimmer.startShimmer()
    this.main.visibility = View.VISIBLE
}

fun ViewEmptyBinding.showEmptyView(message: String?) {
    this.main.visibility = View.VISIBLE
    this.tvMessage.text = message
}

fun ViewEmptyBinding.hideEmptyView() {
    this.main.visibility = View.GONE
    this.tvMessage.text = ""
}

fun ViewDataHomeBinding.showEmptyView(message: String?) {
    this.main.visibility = View.VISIBLE
    this.tvMessage.text = message
}

fun ViewDataHomeBinding.hideEmptyView() {
    this.main.visibility = View.GONE
    this.tvMessage.text = ""
}

fun ViewLoaderBinding.hideLoading() {
    this.shimmer.stopShimmer()
    this.main.visibility = View.GONE
}

fun RecyclerView.setLinearLayoutManger() {
    this.layoutManager = LinearLayoutManager(this.context)
}

fun EditText.borderEnable() {
    this.setBackgroundResource(R.drawable.edit_back_view_stroke)
}

fun EditText.borderDisable() {
    this.setBackgroundResource(R.drawable.edit_back_view)
}


fun Fragment.showSheet(sheet: BottomSheetDialogFragment?) {
    sheet?.show(this.childFragmentManager, sheet.tag)
}

fun FragmentActivity.showSheet(sheet: BottomSheetDialogFragment) {
    sheet.show(this.supportFragmentManager, sheet.tag)
}


fun Fragment.openBrowser(link: String?) {
    try {
        val url = Uri.parse(link)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = url
        startActivity(i)
    } catch (e: Exception) {
        showErrorToast("Url not valid")
    }
}
fun FragmentActivity.openBrowser(link: String?) {
    try {
        val url = Uri.parse(link)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = url
        startActivity(i)
    } catch (e: Exception) {
        showErrorToast("Url not valid")
    }
}

fun Fragment.showSheetBlur(
    mainView: View?,
    blurView: ImageView,
    sheet: BaseCloseSheet<*>?
) {
    Blurry.with(context)
        .color(ContextCompat.getColor(requireContext(), R.color.sheet_back_color))
        .capture(mainView)
        .getAsync { bitmap: Bitmap? ->
            blurView.alpha = 0f
            blurView.visibility = View.VISIBLE
            blurView.setImageBitmap(bitmap)
            sheet?.show(this.childFragmentManager, sheet.tag)
            blurView.animate().alpha(1f)
        }
}

fun AppCompatActivity.showSheetBlur(
    mainView: View?,
    blurView: ImageView,
    sheet: BaseCloseSheet<*>?
) {
    Blurry.with(this)
        .color(ContextCompat.getColor(this, R.color.sheet_back_color))
        .capture(mainView)
        .getAsync { bitmap: Bitmap? ->
            blurView.alpha = 0f
            blurView.visibility = View.VISIBLE
            blurView.setImageBitmap(bitmap)
            sheet?.show(supportFragmentManager, sheet.tag)
            blurView.animate().alpha(1f)
        }
}

fun EditText.attachValidator(validator: Validator) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            var text = ""
            s?.let {
                text = it.toString()
            }
            if (validator.isValidField(text)) {
                this@attachValidator.borderEnable()
            } else {
                this@attachValidator.borderDisable()
            }

        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}