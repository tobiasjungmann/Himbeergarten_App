package com.example.rpicommunicator_v1.service

import android.util.Log
import com.example.rpicommunicator_v1.Communication
import com.example.rpicommunicator_v1.CommunicatorGrpc

class GrpcCommunicatorService(private var blockingStub: CommunicatorGrpc.CommunicatorBlockingStub) {


    fun setOutletState(id: Int, state: Boolean): Boolean {
        val response =
            blockingStub.outletOn(
                Communication.GPIORequest.newBuilder().setOn(state).setOutletId(id).build()
            )
        Log.d("testing", "helloWorldGrpc: "+response.on)
        return response.on
    }

    fun getStatus(): Communication.StatusReply {
        return blockingStub.getStatus(Communication.EmptyMsg.newBuilder().build())
    }
}