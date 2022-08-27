package com.example.rpicommunicator_v1.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc
import io.grpc.ManagedChannelBuilder

class GrpcCommunicatorService (application: Application) : AndroidViewModel(application) {
    private var blockingStub: CommunicatorGrpc.CommunicatorBlockingStub
    init {
         val mChannel =
             ManagedChannelBuilder.forAddress("192.168.0.8", 8000).usePlaintext().build();
        blockingStub = CommunicatorGrpc.newBlockingStub(mChannel)
    }

    fun helloWorldGrpc() {
       blockingStub.sayHello(Communication.Request.newBuilder().setMsg("Hello World").build())
    }
}