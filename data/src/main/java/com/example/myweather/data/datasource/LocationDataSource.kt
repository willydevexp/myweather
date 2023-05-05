package com.example.myweather.data.datasource

import com.example.myweather.domain.DomainLocation


interface LocationDataSource {
    suspend fun findLastLocation(): DomainLocation?
}


