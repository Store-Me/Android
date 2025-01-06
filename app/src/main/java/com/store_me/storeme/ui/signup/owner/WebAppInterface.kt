package com.store_me.storeme.ui.signup.owner

import android.webkit.JavascriptInterface
import com.store_me.storeme.data.DaumPostcodeResponse
import org.json.JSONObject

class WebAppInterface(
    private val onAddressSelected: (DaumPostcodeResponse) -> Unit
) {
    @JavascriptInterface
    fun processDATA(data: String) {
        val jsonObject = JSONObject(data)

        val roadAddress = jsonObject.getString("roadAddress") //도로명 주소
        val legalDong = jsonObject.getString("legalDong")     //법정동
        val administrativeDong = jsonObject.getString("administrativeDong")     //행정동
        val sigunguCode = jsonObject.getString("sigunguCode") //시군구 코드

        onAddressSelected(DaumPostcodeResponse(roadAddress = roadAddress, legalDong = legalDong, administrativeDong= administrativeDong, sigunguCode = sigunguCode))
    }
}