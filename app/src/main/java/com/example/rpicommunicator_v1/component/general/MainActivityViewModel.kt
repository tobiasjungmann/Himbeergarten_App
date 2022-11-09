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


    private val _gpioStates = MutableLiveData(listOf(false, false, false, false, false))
    val gpioStates: LiveData<List<Boolean>> get() = _gpioStates
    private val rpiIpAddress = Constants.IP
    private val rpiPort = 8010
    private var grpcCommunicationInterface: GrpcCommunicatorService=initGrpcStub()

    private val _currentMatrixMode = MutableLiveData(Communication.MatrixState.MATRIX_TIME)
    val currentMatrixMode: LiveData<Communication.MatrixState> get() = _currentMatrixMode

    fun gpioButtonClicked(outletId: Communication.GPIOInstances) {
        Log.i("TAG", "outletClicked: reached")
        gpioStates.value?.let {
            grpcCommunicationInterface.setOutletState(outletId, !it[outletId.number], this)
        }
    }

    fun loadStatus() {
        grpcCommunicationInterface.getStatus(this)
    }

    fun matrixChangeMode(matrixMode: Communication.MatrixState) {
        Log.i("buttonClick", "matrix mode change request to: " + matrixMode.name)
        grpcCommunicationInterface.matrixChangeMode(matrixMode, this)
    }

    fun matrixChangeToMVV(start: String, destination: String) {
        Log.i(
            "buttonClick",
            "matrix mode change request to MATRIX_MVV (from " + start + " to " + destination + ")"
        )
        grpcCommunicationInterface.matrixChangeToMVV(start, destination, this)
    }

    fun setGpioStates(list: List<Boolean>) {
        _gpioStates.postValue(list)
    }

    fun setCurrentMatrixMode(matrixMode: Communication.MatrixState) {
        _currentMatrixMode.postValue(matrixMode)
    }

    fun setMatrixBrightness(currentProgress: Int) {
        grpcCommunicationInterface.setMatrixBrightness(currentProgress, this)
    }

    fun setCommunicationCredentials(ipAddress: String, port: Int) {
        if (!(ipAddress == rpiIpAddress && port == rpiPort)) {
            grpcCommunicationInterface=initGrpcStub()
            // todo save in preferences and load for the next time
        }
    }

    private fun initGrpcStub() : GrpcCommunicatorService{
        val mChannel =
            ManagedChannelBuilder.forAddress(rpiIpAddress, rpiPort).usePlaintext().build()
        return GrpcCommunicatorService(CommunicatorGrpc.newStub(mChannel))
    }
}