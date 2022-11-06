package com.example.rpicommunicator_v1.component.general

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.Constants
import com.example.rpicommunicator_v1.service.GrpcCommunicatorService
import io.grpc.ManagedChannelBuilder


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var outlets = booleanArrayOf(false, false, false)
    private var grpcCommunicationInterface: GrpcCommunicatorService

    //val items = MutableLiveData<List<Boolean>>()
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    var test=0;

    // Start GRPC Implementation
    fun outletClicked(outletId: Int): Boolean {
        outlets[outletId] = grpcCommunicationInterface.setOutletState(outletId, !outlets[outletId])
        return outlets[outletId]
    }

    fun loadStatus() {

        val status = grpcCommunicationInterface.getStatus(_score, this)
        /*for (i in 0..outlets.size - 1) {
            outlets[i] = status.getOutlets(i)
        }*/
    }

    fun getOutletState(outletId: Int): Boolean {
        return outlets[outletId]
    }

    fun matrixChangeMode(matrixMode: Communication.MatrixState) {
        Log.i("buttonClick", "matrix mode change request to: " + matrixMode.name)
        grpcCommunicationInterface.matrixChangeMode(matrixMode)
    }

    fun setScoreValue(i: Int) {
        _score.postValue(i)
        test=12
    }


    init {
        //    items.value.put;
        val mChannel =
            ManagedChannelBuilder.forAddress(Constants.IP, 8010).usePlaintext().build()
        grpcCommunicationInterface =
            GrpcCommunicatorService(CommunicatorGrpc.newStub(mChannel))
    }
}