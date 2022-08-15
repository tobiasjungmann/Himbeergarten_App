package com.example.rpicommunicator_v1.component

import android.Manifest

object Constants {
    const val PORT = 15439
    const val IP = "192.168.0.23"

    //Open PlantView Activity
    const val EXTRA_HUMIDITY = "EXTRA_HUMIDITY"
    const val EXTRA_NAME = "EXTRA_NAME"
    const val EXTRA_WATERED = "EXTRA_WATERED"
    const val EXTRA_NEEDS_WATER = "EXTRA_NEEDS_WATER"
    const val EXTRA_IMAGE = "EXTRA_IMAGE"
    const val EXTRA_ICON = "EXTRA_ICON"
    const val EXTRA_INFO = "EXTRA_INFO"
    const val EXTRA_ID = "EXTRA_ID"
    const val EXTRA_GRAPH_STRING = "EXTRA_GRAPH_STRING"

    const val EXTRA_TITLE = "EXTRA_TITLE"
    const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
    const val EXTRA_PRIORITY = "EXTRA_PRIORITY"
    const val EXTRA_IMAGE_PATH = "EXTRA_image_path"
    const val MODE = "mode"
    const val ADD_NOTE_REQUEST = "ADD"
    const val EDIT_NOTE_REQUEST = "EDIT"

    const val TAG_CMAERA="cameraX"
    const val FILE_NAME_FORMAT= "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSION=123
    val REQUIRED_PERMISSIONS= arrayOf(Manifest.permission.CAMERA)
}