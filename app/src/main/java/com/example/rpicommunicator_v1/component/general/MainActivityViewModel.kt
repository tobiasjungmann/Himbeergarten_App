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

    private var grpcCommunicationInterface: GrpcCommunicatorService

    private val _outletStatus = MutableLiveData(listOf(false, false, false, false, false))
    val outletStatus: LiveData<List<Boolean>> get() = _outletStatus

    fun outletClicked(outletId: Communication.GPIOInstances) {
        Log.i("TAG", "outletClicked: reached")
        outletStatus.value?.let {
            grpcCommunicationInterface.setOutletState(outletId, !it[outletId.number], this)
        }
    }

    fun loadStatus() {
        grpcCommunicationInterface.getStatus(this)
    }


    fun matrixChangeMode(matrixMode: Communication.MatrixState) {
        Log.i("buttonClick", "matrix mode change request to: " + matrixMode.name)
        grpcCommunicationInterface.matrixChangeMode(matrixMode)
    }

    fun setOutletList(list: List<Boolean>) {
        _outletStatus.postValue(list)
    }

    init {
        val mChannel =
            ManagedChannelBuilder.forAddress(Constants.IP, 8010).usePlaintext().build()
        grpcCommunicationInterface =
            GrpcCommunicatorService(CommunicatorGrpc.newStub(mChannel))
    }
}