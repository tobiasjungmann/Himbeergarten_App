package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.StorageServerGrpc
import com.example.rpicommunicator_v1.StorageServerOuterClass
import io.grpc.stub.StreamObserver

class GrpcStorageServerService(
    private var grpcStub: StorageServerGrpc.StorageServerStub,
) {
        fun setHumidityTest() {

        grpcStub.storeHumidityEntry(
            StorageServerOuterClass.StoreHumidityRequest.newBuilder()
                .setHumidity(1).setPlantId(2).build(),
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
}

