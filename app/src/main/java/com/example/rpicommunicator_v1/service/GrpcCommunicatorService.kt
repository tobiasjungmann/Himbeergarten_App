package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import io.grpc.stub.StreamObserver

class GrpcCommunicatorService(
    private var grpcStub: CommunicatorGrpc.CommunicatorStub,
) {


    fun setOutletState(
        id: Communication.GPIOInstances,
        state: Boolean,
        mainActivityViewModel: MainActivityViewModel
    ) {
        grpcStub.outletOn(
            Communication.GPIORequest.newBuilder().setOn(state).setId(id).build(),
            object : StreamObserver<Communication.GPIOReply> {
                override fun onNext(response: Communication.GPIOReply?) {
                    //consume response
                    if (response != null) {
                        mainActivityViewModel.setGpioStates(response.statusListList)
                    }
                }

                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: setOutletState " + throwable.toString())

                }

                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: setOutletState")
                }
            })
    }

    fun getStatus(mainActivityViewModel: MainActivityViewModel) {
        grpcStub.getStatus(Communication.EmptyMsg.newBuilder().build(),
            object : StreamObserver<Communication.StatusReply> {
                override fun onNext(response: Communication.StatusReply?) {
                    mainActivityViewModel.setGpioStates(
                        response?.gpiosList ?: listOf(
                            false,
                            false,
                            false
                        )
                    )
                    mainActivityViewModel.setCurrentMatrixMode(
                        response?.matrixState ?: Communication.MatrixState.MATRIX_TIME
                    )
                }

                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: getStatus " + throwable.toString())
                }

                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: getStatus")
                }
            })
    }

    fun matrixChangeMode(
        matrixMode: Communication.MatrixState,
        mainActivityViewModel: MainActivityViewModel
    ) {
        executeMatrixChangeMode(
            Communication.MatrixChangeModeRequest.newBuilder().setState(matrixMode).build(),
            mainActivityViewModel
        )
    }

    fun matrixChangeToMVV(
        start: String,
        destination: String,
        mainActivityViewModel: MainActivityViewModel
    ) {
        executeMatrixChangeMode(
            Communication.MatrixChangeModeRequest.newBuilder()
                .setState(Communication.MatrixState.MATRIX_MVV).setStart(start)
                .setDestination(destination).build(), mainActivityViewModel
        )
    }

    private fun executeMatrixChangeMode(
        build: Communication.MatrixChangeModeRequest,
        mainActivityViewModel: MainActivityViewModel
    ) {
        grpcStub.matrixSetMode(
            build,
            object : StreamObserver<Communication.MatrixChangeModeReply> {
                override fun onNext(response: Communication.MatrixChangeModeReply?) {
                    response?.let { mainActivityViewModel.setCurrentMatrixMode(it.state) }
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
    }

    fun setMatrixBrightness(currentProgress: Int, mainActivityViewModel: MainActivityViewModel) {

        grpcStub.matrixSetBrightness(
            Communication.MatrixBrightnessRequest.newBuilder()
                .setBrightness(currentProgress).build(),
            object : StreamObserver<Communication.EmptyMsg> {
                override fun onNext(response: Communication.EmptyMsg?) {
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
        TODO("Not yet implemented")
    }
}

