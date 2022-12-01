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


    fun reloadPlantsFromServer(plantRepository: PlantRepository) {
        grpcStub.getOverviewAllPlants(
            StorageServerOuterClass.GetAllPlantsRequest.newBuilder().build(),
            object : StreamObserver<StorageServerOuterClass.AllPlantsReply> {
                override fun onNext(response: StorageServerOuterClass.AllPlantsReply) {
                    Log.i("add plant", "On Next Humidity Request ")
                    // todo update with values
                }

                override fun onError(throwable: Throwable?) {
                    Log.i("add Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                }
            })
    }

    fun addUpdatePlant(plant: Plant, plantRepository: PlantRepository) {
        grpcStub.addNewPlant(
            StorageServerOuterClass.AddPlantRequest.newBuilder()
                .setName(plant.name).setInfo(plant.info).setType(plant.lastWatered).build(),
            object : StreamObserver<StorageServerOuterClass.PlantOverviewMsg> {
                override fun onNext(response: StorageServerOuterClass.PlantOverviewMsg) {
                    Log.i("add plant", "On Next Humidity Request ")
                }

                override fun onError(throwable: Throwable?) {
                     throwable?.stackTrace
                    Log.i("add Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                    Log.i("add Plant", "Stored plant successfully in server.")
                    plant.syncedWithServer=true
                    plantRepository.updatePlant(plant)
                }
            })
    }

    fun removePlant(plant: Plant) {
        grpcStub.deletePlant(
            StorageServerOuterClass.DeletePlantRequest.newBuilder()
                .setPlant(plant.id).build(),
            object : StreamObserver<StorageServerOuterClass.DeletePlantReply> {
                override fun onNext(response: StorageServerOuterClass.DeletePlantReply) {
                    Log.i("delete plant", "On Next Humidity Request ")
                }

                override fun onError(throwable: Throwable?) {
                    Log.i("delete Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                    Log.i("delete Plant", "Stored plant successfully in server.")
                }
            })
    }
}

