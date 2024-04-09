package io.github.coolishbee.auth

import android.os.Parcelable
import io.github.coolishbee.ApiError
import io.github.coolishbee.ApiErrorCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class UniversalLoginResult(
    var socialProfile: UniversalProfile?,
    var errorData: ApiError
): Parcelable {

    companion object {

        private fun error(errorData: ApiError
        ): UniversalLoginResult {
            return Builder()
                .errData(errorData)
                .build()
        }

        fun internalError(errorMsg: String): UniversalLoginResult {
            return error(ApiError(ApiErrorCode.INTERNAL_ERROR.code, errorMsg))
        }

        fun canceledError(): UniversalLoginResult {
            return error(ApiError.createApiCancel())
        }

        fun authenticationAgentError(errorMsg: String): UniversalLoginResult {
            return error(ApiError(ApiErrorCode.AUTHENTICATION_AGENT_ERROR.code, errorMsg))
        }
    }

    data class Builder(
        var socialProfile: UniversalProfile? = null,
        var errorData: ApiError = ApiError(ApiErrorCode.NOT_DEFINED.code, "")
    ) {
        fun socialProfile(socialProfile: UniversalProfile?) = apply {
            this.socialProfile = socialProfile
        }
        fun errData(errorData: ApiError) = apply {
            this.errorData = errorData
        }
        fun build() = UniversalLoginResult(socialProfile, errorData)
    }
}