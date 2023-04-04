package com.example.spherelink.domain.distance

import com.example.spherelink.data.entities.DeviceEntity
import com.example.spherelink.data.repository.DeviceRepository
import javax.inject.Inject

class DistanceCalculatorImpl: DistanceCalculator {

    @Inject
    lateinit var repository: DeviceRepository

    override fun calculateDistance(deviceAddress: String, rssi: Int) {

        //wrap in coroutine
        //val deviceEntity = repository.getDeviceByAddress(deviceAddress)

        //update deviceEntity with new fields./
        //create the obeject????

        //needs to be inserted after deviceEntity is created
        //TODO
        // add rssi to db

        //uncomment when above is done
        //val distance = deviceEntity.distance


        //Grab distance from db
        //calculate distance

        //update distance in db

        //examples
        //repository.updateDistance(deviceEntity.address, distance)
        //example
        //repository.insertDevice(deviceEntity)
        //
    }
}