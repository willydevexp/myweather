package com.example.myweather.data.datasource

import com.example.myweather.domain.DomainLocation


interface LocationServiceDataSource {
    suspend fun findLastLocation(): DomainLocation?

    suspend fun findLocation(locationName: String) : DomainLocation?
}


