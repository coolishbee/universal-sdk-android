package io.github.coolishbee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ApiError(
    var errCode: Int,
    var message: String?
) : Parcelable {

    companion object {

        fun createApiError(errCode: Int,
                           errString: String?): ApiError
        {
            return ApiError(errCode = errCode, message = errString)
        }

        fun createApiCancel(): ApiError
        {
            return ApiError(ApiErrorCode.CANCEL.code, "User Canceled")
        }
    }
}
