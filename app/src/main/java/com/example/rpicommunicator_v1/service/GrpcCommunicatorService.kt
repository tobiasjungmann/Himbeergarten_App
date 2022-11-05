package com.example.rpicommunicator_v1.service

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import com.example.rpicommunicator_v1.component.Constants.IP
import io.grpc.ManagedChannelBuilder

class GrpcCommunicatorService (application: Application) : AndroidViewModel(application) {
    private var blockingStub: CommunicatorGrpc.CommunicatorBlockingStub
    init {
         val mChannel =
             ManagedChannelBuilder.forAddress(IP, 8010).usePlaintext().build();
        blockingStub = CommunicatorGrpc.newBlockingStub(mChannel)
    }

    fun helloWorldGrpc() {
        var response =
            blockingStub.outletOn(
                Communication.GPIORequest.newBuilder().setOn(false).setOutletId(0).build()
            )
        Log.d("testing", "helloWorldGrpc: "+response.on)
    }
}