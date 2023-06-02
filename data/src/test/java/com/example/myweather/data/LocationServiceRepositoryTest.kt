package com.example.myweather.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class LocationServiceRepositoryTest {

    @Test
    fun `Returns default location when coarse permission not granted`() : Unit = runBlocking {
        val locationServiceRepository = LocationServiceRepository(
            locationServiceDataSource = mock(),
            permissionChecker = mock {
                on {check(PermissionChecker.Permission.COARSE_LOCATION)} doReturn false
            }
        )

        val location = locationServiceRepository.findLastLocation()

        assertEquals(LocationServiceRepository.DEFAULT_LOCATION, location)
    }

    @Test
    fun `Returns location when coarse permission is granted`() : Unit = runBlocking {
        val locationServiceRepository = LocationServiceRepository(
            locationServiceDataSource = mock{
                onBlocking {findLastLocation()} doReturn LocationServiceRepository.MADRID_LOCATION
            },
            permissionChecker = mock {
                on {check(PermissionChecker.Permission.COARSE_LOCATION)} doReturn true
            }
        )

        val location = locationServiceRepository.findLastLocation()

        assertEquals(LocationServiceRepository.MADRID_LOCATION, location)
    }

}