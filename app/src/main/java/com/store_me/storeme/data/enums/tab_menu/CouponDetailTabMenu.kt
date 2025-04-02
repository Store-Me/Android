package com.store_me.storeme.data.enums.tab_menu

enum class CouponDetailTabMenu(val displayName: String){
    Received("받은 손님"),
    Used("사용한 손님");

    companion object {
        fun getDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}