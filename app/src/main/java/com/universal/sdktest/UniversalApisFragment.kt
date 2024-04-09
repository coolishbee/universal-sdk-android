package com.universal.sdktest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.universal.sdktest.databinding.FragmentApiBinding
import io.github.coolishbee.api.UniversalApiClient
import io.github.coolishbee.api.UniversalApiClientBuilder
import io.github.coolishbee.auth.UniversalLoginApi
import io.github.coolishbee.utils.UniversalUtils

class UniversalApisFragment : Fragment() {
    private var _binding: FragmentApiBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var apiClient: UniversalApiClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_api, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //event listener
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "result code: ${it.resultCode}")
            when(it.resultCode){
                0 -> {
                    val result = UniversalLoginApi.getGoogleLoginResultFromIntent(it.data)
                    addLog(result.toString())
                }
                1 -> {
                    val result = UniversalLoginApi.getFacebookLoginResultFromIntent(it.data)
                    addLog(result.toString())
                }
                2 -> {
                    val result = UniversalLoginApi.getAppleLoginResultFromIntent(it.data)
                    addLog(result.toString())
                }
            }
        }

        apiClient = activity?.let { UniversalApiClientBuilder(it).build() }!!

        apiClient.setupSDK()

        binding.googleLoginBtn.setOnClickListener {
            addLog("google login success")

            val loginIntent = context?.let {
                UniversalLoginApi.getGoogleLoginIntent(it)
            }
            //startActivityForResult(loginIntent, REQUEST_CODE)

            resultLauncher.launch(loginIntent)
        }

        binding.facebookLoginBtn.setOnClickListener {
            addLog("fb login success")

            val loginIntent = context?.let {
                UniversalLoginApi.getFacebookLoginIntent(it)
            }
            resultLauncher.launch(loginIntent)
        }

        binding.appleLoginBtn.setOnClickListener {
            val loginIntent = context?.let {
                UniversalLoginApi.getAppleLoginIntent(it)
            }
            resultLauncher.launch(loginIntent)
        }

        binding.logoutBtn.setOnClickListener {
            apiClient.logout(requireActivity())
        }

//        binding.initBillingBtn.setOnClickListener {
//            val list = "boxer_1000 boxer_2000 boxer_3000 boxer_4000 boxer_5000"
//            val strList = list.split(" ")
//            for (item in strList)
//            {
//                addLog(item)
//            }
//        }
//
//        binding.inappPurchaseBtn1.setOnClickListener {
//            addLog("purchase")
//        }

        binding.customTabViewBtn.setOnClickListener {
            apiClient.openCustomTabView(requireActivity(), "https://www.naver.com")
        }

        binding.popupDialogBtn.setOnClickListener {
            addLog("popup open")
            addLog(UniversalUtils.getOSVersion())
        }

        binding.imgBannerBtn.setOnClickListener {

        }

        binding.clearLogBtn.setOnClickListener {
            binding.log.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addLog(logText: String) {
        binding.log.text = logText.plus("\n" + binding.log.text)

        Log.d(TAG, "log: $logText")
        //println(logText)
    }

    companion object {
        private const val TAG = "UniversalApisFragment"

        private const val REQUEST_CODE = 1
    }
}