package com.example.rpicommunicator_v1.service

import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.general.MainActivityViewModel
import io.grpc.stub.StreamObserver

class GrpcCommunicatorService(
    private var futureStub: CommunicatorGrpc.CommunicatorStub,
) {//}, private val context: Context) {


    fun setOutletState(id: Int, state: Boolean): Boolean {
        futureStub.outletOn(
            Communication.GPIORequest.newBuilder().setOn(state).setOutletId(id).build(),
            object : StreamObserver<Communication.GPIOReply> {
                override fun onNext(response: Communication.GPIOReply?) {
                    //consume response
                }

                override fun onError(throwable: Throwable?) {
                    //handle error
                }

                override fun onCompleted() {
                    //on complete
                }
            })
        return true
    }

    fun getStatus(_score: MutableLiveData<Int>, mainActivityViewModel: MainActivityViewModel): Communication.StatusReply {
        futureStub.getStatus(Communication.EmptyMsg.newBuilder().build(),
            object : StreamObserver<Communication.StatusReply> {
                override fun onNext(response: Communication.StatusReply?) {
                    mainActivityViewModel.setScoreValue(100)
                }

                override fun onError(throwable: Throwable?) {
                    mainActivityViewModel.setScoreValue(200)

                }

                override fun onCompleted() {
                    mainActivityViewModel.setScoreValue(200)
                }
            })
        return Communication.StatusReply.getDefaultInstance()
    }

    fun matrixChangeMode(matrixMode: Communication.MatrixState) {
        futureStub.matrixSetMode(
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

