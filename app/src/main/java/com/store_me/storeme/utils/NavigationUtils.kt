package com.store_me.storeme.utils

import androidx.navigation.NavController
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.enums.StoreProfileItems
import com.store_me.storeme.ui.main.CUSTOMER_BOTTOM_ITEM_LIST
import com.store_me.storeme.ui.main.FAVORITE
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.main.NEAR_PLACE
import com.store_me.storeme.ui.main.MY_MENU
import com.store_me.storeme.ui.main.OWNER_BOTTOM_ITEM_LIST
import com.store_me.storeme.ui.main.STORE_TALK
import com.store_me.storeme.ui.main.USER_HOME

class NavigationUtils {
    /**
     * ScreenRoute 생성함수
     * @param bottomItem 활성화 할 Bottom Item
     * @param screenName 이동 할 화면 이름
     * @param additionalData 추가로 전송할 데이터
     */
    private fun createScreenRoute(bottomItem: String, screenName: String, additionalData: String? = null): String{
        return if(additionalData.isNullOrEmpty()){
            "$bottomItem$screenName"
        } else {
            "$bottomItem$screenName/$additionalData"
        }
    }

    /**
     * Bottom Nav의 Item 선택을 유지한 채로 이동하는 함수 (손님)
     * @param navController Bottom Navigation Controller
     * @param screenName 이동 화면 이름
     * @param additionalData 추가 데이터
     */
    fun navigateCustomerNav(navController: NavController, screenName: MainActivity.CustomerNavItem, additionalData: String? = null) {
        val index = getCurrentBottomNavIndex(navController)

        navController.navigate(NavigationUtils().createScreenRoute(CUSTOMER_BOTTOM_ITEM_LIST[index], screenName.name, additionalData))
    }

    /**
     * Bottom Nav의 Item 선택을 유지한 채로 이동하는 함수 (사장님)
     * @param navController Bottom Navigation Controller
     * @param screenName 이동 화면 이름
     * @param additionalData 추가 데이터
     */
    fun navigateOwnerNav(navController: NavController, screenName: MainActivity.OwnerNavItem, additionalData: String? = null) {
        val index = getCurrentBottomNavIndex(navController)

        navController.navigate(NavigationUtils().createScreenRoute(OWNER_BOTTOM_ITEM_LIST[index], screenName.name, additionalData))
    }

    fun navigateOwnerNav(navController: NavController, screenName: StoreProfileItems, additionalData: String? = null) {
        val index = getCurrentBottomNavIndex(navController)

        navController.navigate(NavigationUtils().createScreenRoute(OWNER_BOTTOM_ITEM_LIST[index], screenName.name, additionalData))
    }

    fun navigateOwnerNav(navController: NavController, screenName: StoreHomeItem, additionalData: String? = null) {
        val index = getCurrentBottomNavIndex(navController)

        navController.navigate(NavigationUtils().createScreenRoute(OWNER_BOTTOM_ITEM_LIST[index], screenName.name, additionalData))
    }

    /**
     * 현재 선택된 Bottom Nav Index 를 반환하는 함수
     */
    private fun getCurrentBottomNavIndex(navController: NavController): Int {
        val currentRoute = navController.currentDestination?.route ?: return 0

        return when {
            currentRoute.startsWith(USER_HOME) -> 0
            currentRoute.startsWith(FAVORITE) -> 1
            currentRoute.startsWith(NEAR_PLACE) -> 2
            currentRoute.startsWith(STORE_TALK) -> 3
            currentRoute.startsWith(MY_MENU) -> 4
            else -> 0
        }
    }

}