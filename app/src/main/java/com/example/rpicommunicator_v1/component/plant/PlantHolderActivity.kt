package com.example.rpicommunicator_v1.component.plant

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.rpicommunicator_v1.R
import com.example.rpicommunicator_v1.component.camera.CameraContract
import com.example.rpicommunicator_v1.component.camera.CameraPresenter

class PlantHolderActivity : AppCompatActivity(R.layout.activity_plant_holder), CameraContract.View  {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<PlantOverviewFragment>(R.id.fragment_container_view_plant)
                }
            }
        }

    override fun openCamera(intent: Intent) {
        startActivityForResult(intent, CameraPresenter.REQUEST_TAKE_PHOTO)
    }

    override fun openGallery(intent: Intent) {
        startActivityForResult(intent, CameraPresenter.REQUEST_GALLERY)
    }


    override fun onImageAdded(path: String) {
        //cameraUtils.onImageAdded(path)
    }

    override fun onImageRemoved(position: Int) {
        //cameraUtils.onImageRemoved(position)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun showPermissionRequestDialog(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

}