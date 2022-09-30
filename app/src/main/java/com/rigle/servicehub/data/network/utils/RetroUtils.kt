package com.rigle.servicehub.data.network.utils

import android.util.Log
import com.rigle.servicehub.data.model.helper.Resource
import com.rigle.servicehub.data.model.helper.UploadBean
import io.reactivex.FlowableEmitter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RetroUtils {
    companion object {

        fun createMultipartBody(
            filePath: String?,
            keyname: String,
            emitter: FlowableEmitter<Resource<UploadBean?>>
        ): MultipartBody.Part? {
            if (filePath == null) return null
            val file = File(filePath)
            return MultipartBody.Part.createFormData(
                keyname, file.name, createCountingRequestBody(file, emitter)
            )
        }

        private fun createCountingRequestBody(
            file: File,
            emitter: FlowableEmitter<Resource<UploadBean?>>
        ): RequestBody {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            return ProgressRequestBody(requestBody
            ) { progress, total ->
                Log.e("bytes", "==$progress==$total")
                val uploadBean = UploadBean();
                uploadBean.bytesWritten = progress
                uploadBean.contentLength = total
                emitter.onNext(Resource.loading(uploadBean))
            }
        }

    }
}