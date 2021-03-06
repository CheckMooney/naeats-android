package com.checkmooney.naeats.data

import com.checkmooney.naeats.data.entities.FoodData
import com.checkmooney.naeats.data.entities.RecommendFood
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val menuRemoteDataSource: MenuRemoteDataSource
) {

    suspend fun getAllFoodList(): MutableList<FoodData> {
        val res = menuRemoteDataSource.getAllFood()

        return res?.foods ?: mutableListOf()
    }

    suspend fun getRecommendFoodList(day: Int, isEat: Boolean, order: String, isLike: Boolean, limit: Int): MutableList<RecommendFood> {
        val res = menuRemoteDataSource.getRecommendFoodList(day = day, isEat = isEat, order = order, isLike = isLike, limit = limit)

        return res?.recommends ?: mutableListOf()
    }

    suspend fun getRecommendFoodListByCategories(category: String, day: Int, isEat: Boolean, order: String, isLike: Boolean, limit: Int): MutableList<RecommendFood> {
        val res = menuRemoteDataSource.getRecommendFoodList(category = category, day = day, isEat = isEat, order = order, isLike = isLike, limit = limit)

        return res?.recommends ?: mutableListOf()
    }

    suspend fun getCategories(): List<String> {
        val defaultList = listOf("전체")
        val res = menuRemoteDataSource.getCategoryList()
        println(defaultList + (res?.categories ?: listOf()))
        return defaultList + (res?.categories ?: listOf())
    }

    suspend fun getFoodListByCategory(category: String): List<FoodData> {
        val res = menuRemoteDataSource.getCategorizedFoodList(category)

        return res?.foods ?: listOf()
    }

    suspend fun addTodayEatLog(foodId: String) {
        val currentTime = Calendar.getInstance()
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        date.timeZone = TimeZone.getTimeZone("UTC")

        val res =
            menuRemoteDataSource.addEatFoodLog(foodId, date.format(currentTime.time))

        println(res?.isSuccess())
    }

    suspend fun updateMyFavor(id: String, isDislike: Boolean): Boolean {
        val res =
            menuRemoteDataSource.updatePreference(id, isDislike)

        println(res?.isSuccess())
        return res?.isSuccess() ?: false
    }
}
