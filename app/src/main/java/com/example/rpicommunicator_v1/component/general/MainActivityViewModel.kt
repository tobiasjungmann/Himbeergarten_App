package com.example.rpicommunicator_v1.component.general

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rpicommunicator_v1.MatrixGrpc
import com.example.rpicommunicator_v1.MatrixOuterClass
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.Constants.DEFAULT_STATION_PORT
import com.example.rpicommunicator_v1.service.MatrixGrpcService
import io.grpc.ManagedChannelBuilder
import java.util.Collections


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {


    private var backPressedInLastSecond: Boolean = false
    private val _gpioStates = MutableLiveData(listOf(false, false, false, false, false))
    val gpioStates: LiveData<List<Boolean>> get() = _gpioStates

    private var grpcCommunicationInterface: MatrixGrpcService = initGrpcStub()

    private val _currentMatrixMode = MutableLiveData(MatrixOuterClass.MatrixState.MATRIX_NONE)
    val currentMatrixMode: LiveData<MatrixOuterClass.MatrixState> get() = _currentMatrixMode

    private val _serverAvailable = MutableLiveData(true)
    val serverAvailable: LiveData<Boolean> get() = _serverAvailable

    fun gpioButtonClicked(outletId: MatrixOuterClass.GPIOInstances) {
        Log.i("TAG", "outletClicked: reached")
        gpioStates.value?.let {
            grpcCommunicationInterface.setOutletState(outletId, !it[outletId.number], this)
        }
    }

    fun loadStatus() {
        grpcCommunicationInterface.getStatus(this)
    }

    fun matrixChangeMode(matrixMode: MatrixOuterClass.MatrixState) {
        grpcCommunicationInterface.matrixChangeMode(matrixMode, this)
    }

    fun matrixChangeToMVV(start: String, destination: String) {
        grpcCommunicationInterface.matrixChangeToMVV(start, destination, this)
    }

    fun setGpioStates(list: List<Boolean>) {
        _gpioStates.postValue(list)
    }

    fun setCurrentMatrixMode(matrixMode: MatrixOuterClass.MatrixState) {
        _currentMatrixMode.postValue(matrixMode)
    }

    fun setServerAvailable(available: Boolean) {
        _serverAvailable.postValue(available)
    }

    fun setMatrixBrightness(currentProgress: Int) {
        grpcCommunicationInterface.setMatrixBrightness(currentProgress, this)
    }

    private fun initGrpcStub(): MatrixGrpcService {       // todo code dublicate -> use static fucntion
        val mPref: SharedPreferences = this.getApplication<Application>().getSharedPreferences(
            this.getApplication<Application>().resources.getString(R.string.SHARED_PREF_KEY),
            Context.MODE_PRIVATE
        )
        val ipStation = mPref.getString(
            this.getApplication<Application>().resources.getString(R.string.ADDRESS_STATION_PREF),
            "192.168.0.8"
        )
        var portStation = mPref.getInt(
            this.getApplication<Application>().resources.getString(R.string.PORT_STATION_PREF),
            DEFAULT_STATION_PORT
        )
        if (portStation > 65535 || portStation < 1000) {
            portStation = DEFAULT_STATION_PORT
        }
        val wildcardConfig: MutableMap<String, Any> = HashMap()
        wildcardConfig["name"] = listOf(emptyMap<Any, Any>())
        wildcardConfig["timeout"] = "7s"
        val mChannel =
            ManagedChannelBuilder.forAddress(ipStation, portStation).usePlaintext()
                .defaultServiceConfig(
                    Collections.singletonMap(
                        "methodConfig", listOf(
                            wildcardConfig
                        )
                    )
                ).build()
        return MatrixGrpcService(MatrixGrpc.newStub(mChannel))
    }

    fun resetBackPressed() {
        this.backPressedInLastSecond = false
    }

    fun backPressed(): Boolean {
        backPressedInLastSecond = !backPressedInLastSecond
        return !backPressedInLastSecond
    }
}