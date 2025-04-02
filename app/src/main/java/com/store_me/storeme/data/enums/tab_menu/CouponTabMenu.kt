package com.store_me.storeme.data.enums.tab_menu

enum class CouponTabMenu(val displayName: String){
    Create("만들기"),
    CouponList("쿠폰 목록");

    companion object {
        fun getDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}