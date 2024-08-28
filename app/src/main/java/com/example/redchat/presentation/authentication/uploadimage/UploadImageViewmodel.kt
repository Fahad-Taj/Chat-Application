package com.example.redchat.presentation.authentication.uploadimage

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redchat.api.RetrofitInstance
import com.example.redchat.api.access_token
import com.example.redchat.models.UploadImageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UploadImageViewmodel: ViewModel() {

    var uri = mutableStateOf<Uri?>(null)
    var isLoading = mutableStateOf(false)

    private var _response = MutableStateFlow<UploadImageResponse?>(null)
    val response = _response.asStateFlow()

    fun uploadImage(context: Context){
        viewModelScope.launch {
            try {
                isLoading.value = true

                val file = uriToFile(uri.value!!, context)
                if (file != null) {

                    val bearer_token = "Bearer $access_token"

                    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)

                    val result = RetrofitInstance.api.uploadProfileImage(bearer_token, multipartBody)
                    if(result.isSuccessful){
                        _response.value = result.body()
                        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, "Error converting URI to file", Toast.LENGTH_SHORT).show()
                }

            }   catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun uriToFile(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

}