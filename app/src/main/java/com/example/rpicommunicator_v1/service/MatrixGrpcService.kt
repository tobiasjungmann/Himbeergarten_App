package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.MatrixGrpc
import com.example.rpicommunicator_v1.MatrixOuterClass
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import io.grpc.stub.StreamObserver

class MatrixGrpcService(
    private var grpcStub: MatrixGrpc.MatrixStub,
) {


    fun setOutletState(
        id: MatrixOuterClass.GPIOInstances,
        state: Boolean,
        mainActivityViewModel: MainActivityViewModel
    ) {
        grpcStub.outletOn(
            MatrixOuterClass.GPIORequest.newBuilder().setOn(state).setId(id).build(),
            object : StreamObserver<MatrixOuterClass.GPIOReply> {
                override fun onNext(response: MatrixOuterClass.GPIOReply?) {
                    //consume response
                    if (response != null) {
                        mainActivityViewModel.setGpioStates(response.statusListList)
                    }
                    mainActivityViewModel.setServerAvailable(true);
                }

                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: setOutletState " + throwable.toString())
                    mainActivityViewModel.setServerAvailable(false);
                }

                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: setOutletState")
                }
            })
    }

    fun getStatus(mainActivityViewModel: MainActivityViewModel) {
        grpcStub.getStatus(MatrixOuterClass.EmptyMsg.newBuilder().build(),
            object : StreamObserver<MatrixOuterClass.StatusReply> {
                override fun onNext(response: MatrixOuterClass.StatusReply?) {
                    mainActivityViewModel.setGpioStates(
                        response?.gpiosList ?: listOf(
                            false,
                            false,
                            false, false, false
                        )
                    )
                    mainActivityViewModel.setCurrentMatrixMode(
                        response?.matrixState ?: MatrixOuterClass.MatrixState.MATRIX_NONE
                    )
                    mainActivityViewModel.setServerAvailable(true);
                }

                override fun onError(throwable: Throwable?) {
                    Log.d("GRPC", "onError: getStatus " + throwable.toString())
                    mainActivityViewModel.setServerAvailable(false);
                }

                override fun onCompleted() {
                    Log.d("GRPC", "onCompleted: getStatus")
                }
            })
    }

    fun matrixChangeMode(
        matrixMode: MatrixOuterClass.MatrixState,
        mainActivityViewModel: MainActivityViewModel
    ) {
        executeMatrixChangeMode(
            MatrixOuterClass.MatrixChangeModeRequest.newBuilder().setState(matrixMode).build(),
            mainActivityViewModel
        )
    }

    fun matrixChangeToMVV(
        start: String,
        destination: String,
        mainActivityViewModel: MainActivityViewModel
    ) {
        executeMatrixChangeMode(
            MatrixOuterClass.MatrixChangeModeRequest.newBuilder()
                .setState(MatrixOuterClass.MatrixState.MATRIX_MVV).setStart(start)
                .setDestination(destination).build(), mainActivityViewModel
        )
    }

    private fun executeMatrixChangeMode(
        build: MatrixOuterClass.MatrixChangeModeRequest,
        mainActivityViewModel: MainActivityViewModel
    ) {
        grpcStub.matrixSetMode(
            build,
            object : StreamObserver<MatrixOuterClass.MatrixChangeModeReply> {
                override fun onNext(response: MatrixOuterClass.MatrixChangeModeReply?) {
                    response?.let { mainActivityViewModel.setCurrentMatrixMode(it.state) }
                    mainActivityViewModel.setServerAvailable(true);
                }

                override fun onError(throwable: Throwable?) {
                    mainActivityViewModel.setServerAvailable(false);
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
    }

    fun setMatrixBrightness(currentProgress: Int, mainActivityViewModel: MainActivityViewModel) {

        grpcStub.matrixSetBrightness(
            MatrixOuterClass.MatrixBrightnessRequest.newBuilder()
                .setBrightness(currentProgress).build(),
            object : StreamObserver<MatrixOuterClass.EmptyMsg> {
                override fun onNext(response: MatrixOuterClass.EmptyMsg?) {
                    mainActivityViewModel.setServerAvailable(true);
                }

                override fun onError(throwable: Throwable?) {
                    mainActivityViewModel.setServerAvailable(false);
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
    }
}

