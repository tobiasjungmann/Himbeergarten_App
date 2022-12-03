package com.example.rpicommunicator_v1.component.plant

import com.example.rpicommunicator_v1.database.plant.models.GpioElement

class GpioElementPair(
    val left: GpioElement,
    val right: GpioElement

) {
    fun gpioInUse(leftUsed: Boolean): Boolean{
        return if (leftUsed){
            left.userId>-1
        }else{
            right.userId>-1
        }
    }

    fun getLabel(leftUsed: Boolean): String{
        return if (leftUsed){
            left.label
        }else{
            right.label
        }
    }

    fun getAccentColor(leftUsed: Boolean): Int{
        return if (leftUsed){
            left.accentColor
        }else{
            right.accentColor
        }
    }
}