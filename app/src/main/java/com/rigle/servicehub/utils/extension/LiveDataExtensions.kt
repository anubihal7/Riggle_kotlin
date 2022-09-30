package com.rigle.servicehub.utils.extension

import android.util.Log
import android.util.MalformedJsonException
import com.rigle.servicehub.data.model.helper.UploadBean
import com.rigle.servicehub.data.model.helper.Resource
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.rxutils.EventBus
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

/*
fun <T> BaseViewModel.apiSubscription(
    method: Method<T>,
    liveData: SingleLiveEvent<Resource<T>>
) {
    viewModelScope.launch*//*async(Dispatchers.IO)*//* {
        liveData.postValue(Resource.loading(null))
        try {
            var token: String? = null
            *//*  SharedPrefManager.getToken()?.let { it ->
                  token = "Bearer $it"
              }*//*

            method.getSimpleApiMethod(token).let {
                if (it?.body()?.isStatus == true) {
                    it.body()?.success?.let {
                        liveData.postValue(Resource.success(it.data, it.message))
                    }
                } else {
                    liveData.postValue(setThrowableCode(it))
                }
            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                liveData.postValue(Resource.error("No Internet Connection", null))
            } else {

            }
        }
    }
}*/

fun Flowable<Resource<UploadBean?>>.mediaSubscription(liveData: SingleRequestEvent<UploadBean>): Disposable {
    return this.doOnSubscribe {
        liveData.postValue(Resource.loading<UploadBean>())
    }.subscribeOn(Schedulers.computation()).subscribe({
        liveData.postValue(it)
    }, { it ->
        val error = parseException(it);
        error?.let {
            Log.e("API ERROR",it)
            liveData.postValue(Resource.error(null, it))
        }
    })
}
/*
fun <M> Single<ApiResponse<M>>.apiSubscription(liveData: SingleRequestEvent<M>): Disposable {
    return this.doOnSubscribe {
        liveData.postValue(Resource.loading<M>())
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it.isStatusOK)
                liveData.postValue(Resource.success(it.data, it.message.toString()))
            else liveData.postValue(Resource.warn(null, it.message.toString()))
        }, { it ->
            val error = parseException(it);
            error?.let {
                liveData.postValue(Resource.error(null, it))
            }
        })
}*/
/*
fun <M> Single<PageResponse<M>>.pageSubscription(liveData: SingleRequestEvent<PageResponse<M>>): Disposable {
    return this.doOnSubscribe {
        liveData.postValue(Resource.loading())
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            if (it.isStatusOK)
                liveData.postValue(Resource.success(it, it.message.toString()))
            else liveData.postValue(Resource.warn(null, it.message.toString()))
        }, { it ->
            val error = parseException(it);
            error?.let {
                liveData.postValue(Resource.error(null, it))
            }
        })
}*/


fun <M> Single<M>.apiSubscription(liveData: SingleRequestEvent<M>): Disposable {
    return this.doOnSubscribe {
        liveData.value?.let {
            if (it.status == Status.SUCCESS) {
                it.status = Status.CACHED
                liveData.postValue(it)
            }
        }
        liveData.postValue(Resource.loading<M>())
    }.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it != null)
                liveData.postValue(Resource.success(it, "Successful"))
            else liveData.postValue(Resource.error(null, "Data null"))
        }, { it ->
            val error = parseException(it);
            error?.let {
                Log.e("API ERROR",it)
                liveData.postValue(Resource.error(null, it))
            }
        })
}/*

fun Single<SimpleApiResponse>.simpleSubscription(liveData: SingleRequestEvent<Void>): Disposable {
    return this.doOnSubscribe {
        liveData.postValue(Resource.loading<Void>())
    }.subscribeOn(Schedulers.io()).subscribe({
        if (it.isStatusOK)
            liveData.postValue(Resource.success(null, it.message.toString()))
        else liveData.postValue(Resource.warn(null, it.message.toString()))
    }, { it ->
        val error = parseException(it);
        error?.let {
            liveData.postValue(Resource.error(null, it))
        }

    })
}

fun <M> Single<SimpleApiResponse>.simpleSubscriptionWithTag(
    tag: M,
    liveData: SingleRequestEvent<M>
): Disposable {
    return this.doOnSubscribe {
        liveData.postValue(Resource.loading<M>())
    }.subscribeOn(Schedulers.io()).subscribe({
        if (it.isStatusOK)
            liveData.postValue(Resource.success(tag, it.message.toString()))
        else liveData.postValue(Resource.warn(tag, it.message.toString()))
    }, { it ->
        val error = parseException(it);
        error?.let {
            liveData.postValue(Resource.error(tag, it))
        }

    })
}

fun EditText.setSearchConsumer(liveData: MutableLiveData<String>): Disposable {
    return RxSearchObservable.from(this)
        .debounce(RxSearchObservable.DEFAULT_WAIT, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(Consumer { s ->
            liveData.value = s
        })
}*/

fun parseException(it: Throwable?): String? {
    when (it) {
        is HttpException -> {
            when (it.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    val message = getErrorText(it)
                    if (message.contains("Authentication")) {
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

fun getErrorText(it: HttpException): String {
    val errorBody: ResponseBody? = it.response()?.errorBody()
    val text: String? = errorBody?.string()
    if (!text.isNullOrEmpty()) {
        return try {
            val obj = JSONObject(text)
            obj.getString("detail")
        } catch (e: Exception) {
            return text
        }
    }
    return it.message().toString()
}

class UnAuthUser


