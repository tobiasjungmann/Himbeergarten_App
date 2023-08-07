package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.PlantStorageGrpc
import com.example.rpicommunicator_v1.PlantStorageOuterClass
import com.example.rpicommunicator_v1.database.plant.PlantRepository
import com.example.rpicommunicator_v1.database.plant.models.Plant
import io.grpc.stub.StreamObserver

class PlantGrpcServer(
    private var grpcStub: PlantStorageGrpc.PlantStorageStub,
) {
    fun reloadPlantsFromServer(plantRepository: PlantRepository) {
        grpcStub.getOverviewAllPlants(
            PlantStorageOuterClass.GetAllPlantsRequest.newBuilder().build(),
            object : StreamObserver<PlantStorageOuterClass.AllPlantsReply> {
                override fun onNext(response: PlantStorageOuterClass.AllPlantsReply) {
                    Log.i("add plant", "On Next Humidity Request ")

                    for (plant: PlantStorageOuterClass.PlantOverviewMsg in response.plantsList) {
                        // todo check if plant exists and update if necessary
                        plantRepository.insert(
                            Plant(
                                plant.name, plant.info, plant.gpio.sensorId
                            )
                        )
                    }

                }

                override fun onError(throwable: Throwable?) {
                    Log.i("add Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                }
            })
    }

    fun getAdditionalPlantData(plantRepository: PlantRepository, plantId: Int) {
        grpcStub.getAdditionalDataPlant(
            PlantStorageOuterClass.GetAdditionalDataPlantRequest.newBuilder().setPlantId(plantId).build(),
            object : StreamObserver<PlantStorageOuterClass.GetAdditionalDataPlantReply> {
                override fun onNext(response: PlantStorageOuterClass.GetAdditionalDataPlantReply) {
                    Log.i("GetAdditionalPlantData", "On Next Humidity Request ")
                    // todo update with values + store images with image utils if they do not yet exist

                }

                override fun onError(throwable: Throwable?) {
                    Log.i("add Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                }
            })
    }

    fun getConnectedDevicesFromServer(plantRepository: PlantRepository) {
        grpcStub.getConnectedSensorOverview(
            PlantStorageOuterClass.GetSensorOverviewRequest.newBuilder().build(),
            object : StreamObserver<PlantStorageOuterClass.GetSensorOverviewResponse> {
                override fun onNext(response: PlantStorageOuterClass.GetSensorOverviewResponse) {
                    // todo direkt hier die deviceobjekte anfragen
                    //plantRepository.getDevic
                    plantRepository.updateConnectedDevices()

                }

                override fun onError(throwable: Throwable?) {
                    Log.i("getDevices", "Loading connected deices failed.")
                }

                override fun onCompleted() {
                }
            })
    }

    fun addUpdatePlant(plant: Plant, plantRepository: PlantRepository) {
        grpcStub.addNewPlant(
            PlantStorageOuterClass.AddPlantRequest.newBuilder()
                .setName(plant.name).setInfo(plant.info).setPlantId(plant.plant).build(),
            object : StreamObserver<PlantStorageOuterClass.PlantOverviewMsg> {
                override fun onNext(response: PlantStorageOuterClass.PlantOverviewMsg) {
                    Log.i("add plant", "On Next Humidity Request ")
                }

                override fun onError(throwable: Throwable?) {
                    throwable?.stackTrace
                    Log.i("add Plant", "Plant not stored in server.")
                }

                override fun onCompleted() {
                    Log.i("add Plant", "Stored plant successfully in server.")
                    plant.syncedWithServer = true
                    plantRepository.update(plant, listOf())
                }
            })
    }

    fun removePlant(plant: Plant) {
        grpcStub.deletePlant(
            PlantStorageOuterClass.PlantRequest.newBuilder()
                .setPlant(plant.plant).build(),
            object : StreamObserver<PlantStorageOuterClass.DeletePlantReply> {
                override fun onNext(response: PlantStorageOuterClass.DeletePlantReply) {
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

