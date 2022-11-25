package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.StorageServerGrpc
import com.example.rpicommunicator_v1.StorageServerOuterClass
import com.example.rpicommunicator_v1.database.plant.Plant
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import io.grpc.stub.StreamObserver

class GrpcStorageServerService(
    private var grpcStub: StorageServerGrpc.StorageServerStub,
) {
    fun setHumidityTest() {
        grpcStub.storeHumidityEntry(
            StorageServerOuterClass.StoreHumidityRequest.newBuilder()
                .setHumidity(123).setPlantId(1).build(),
            object : StreamObserver<StorageServerOuterClass.StoreHumidityReply> {
                override fun onNext(response: StorageServerOuterClass.StoreHumidityReply) {
                    Log.i("buttonClick", "On Next Humidity Request ")
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                    Log.i("buttonClick", "On Error humidity request")
                }

                override fun onCompleted() {
                    Log.i("buttonClick", "on completed Humidity")
                    //on complete
                }
            })
    }

    fun addPlant(name: String, type: String, info: String) {
        grpcStub.addNewPlant(
            StorageServerOuterClass.AddPlantRequest.newBuilder()
                .setName(name).setInfo(info).setType(type).build(),
            object : StreamObserver<StorageServerOuterClass.PlantOverviewMsg> {
                override fun onNext(response: StorageServerOuterClass.PlantOverviewMsg) {
                    Log.i("buttonClick", "On Next Humidity Request ")
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                    Log.i("buttonClick", "On Error humidity request")
                }

                override fun onCompleted() {
                    Log.i("buttonClick", "on completed Humidity")
                    //on complete
                }
            })
    }

    fun reloadPlantsFromServer(plantRepository: PlantRepository) {
        TODO("Not yet implemented")
    }

    fun removePlant(plant: Plant) {

    }

    fun updatePlant(plant: Plant) {
        TODO("Not yet implemented")
    }
}

