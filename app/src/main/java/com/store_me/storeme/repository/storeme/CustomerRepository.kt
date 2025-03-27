package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.network.storeme.CustomerApiService
import javax.inject.Inject

interface CustomerRepository {
    suspend fun getCustomerInfo(): Result<CustomerInfoResponse>
}

class CustomerRepositoryImpl @Inject constructor(
    private val customerApiService: CustomerApiService
): CustomerRepository {
    override suspend fun getCustomerInfo(): Result<CustomerInfoResponse> {
        return Result.success(customerApiService.getCustomerInfo().body()!!)
    }
}