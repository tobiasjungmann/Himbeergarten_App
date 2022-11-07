package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import io.grpc.stub.StreamObserver

class GrpcCommunicatorService(
    private var grpcStub: CommunicatorGrpc.CommunicatorStub,
) {


    fun setOutletState(id: Communication.GPIOInstances, state: Boolean, mainActivityViewModel: MainActivityViewModel) {
        grpcStub.outletOn(
            Communication.GPIORequest.newBuilder().setOn(state).setId(id).build(),
            object : StreamObserver<Communication.GPIOReply> {
                override fun onNext(response: Communication.GPIOReply?) {
                    //consume response
                    if (response != null) {
                        mainActivityViewModel.setOutletList(response.statusListList)
                    }
                }
                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: setOutletState "+throwable.toString())

                }
                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: setOutletState")
                }
            })
    }

    fun getStatus( mainActivityViewModel: MainActivityViewModel) {
        grpcStub.getStatus(Communication.EmptyMsg.newBuilder().build(),
            object : StreamObserver<Communication.StatusReply> {
                override fun onNext(response: Communication.StatusReply?) {
                    mainActivityViewModel.setOutletList(response?.outletsList ?: listOf(false,false,false))
                }
                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: getStatus "+throwable.toString())
                }
                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: getStatus")
                }
            })
    }

    // todo adapt the ui accordingly - must also be based on live data
    fun matrixChangeMode(matrixMode: Communication.MatrixState) {
        grpcStub.matrixSetMode(
            Communication.MatrixChangeModeRequest.newBuilder().setState(matrixMode).build(),
            object : StreamObserver<Communication.EmptyMsg> {
                override fun onNext(response: Communication.EmptyMsg?) {
                    //consume response
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
    }
}

