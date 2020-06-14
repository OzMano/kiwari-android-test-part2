package com.chataja.codingchallenges

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_split_merge.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.*
import java.nio.file.Files
import java.util.*

class SplitMergeActivity: AppCompatActivity() {

    private val directoryToWrite = Environment.getExternalStorageDirectory().
        absolutePath + File.separator + "SPLITMERGE/"

    private val chooseFileAction = 1

    private var filePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_merge)

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()

        btn_choose_file.onClick {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, chooseFileAction)
        }

        btn_split.onClick {
            if (filePath.isNotEmpty()){
                splitFile(File(filePath))
            }else{
                layout_splitmerge.snackbar("Choose file to split first!")
            }
        }

        btn_merge.onClick {
            if (filePath.isNotEmpty()){
                mergeFiles(listOfFilesToMerge(File(filePath)))
            }else{
                layout_splitmerge.snackbar("Choose file to merge first!")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            chooseFileAction -> {
                if (resultCode == -1) {
                    data?.let{
                        filePath = getPathFromUri(it.data!!)!!
                        tv_path.text = filePath
                    }
                }
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        if (SDK_INT >= KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null

                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(contentUri!!, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else {
                getDataColumn(uri, null, null)
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getDataColumn(uri: Uri, selection: String?, selectionArgs: Array<String>?): String {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return ""
    }

    private fun splitFile(f: File) {
        try {
            var partCounter = 1
            val totalPartsToSplit = 3

            val splitFile = File(directoryToWrite)
            if (!splitFile.exists()) {
                splitFile.mkdirs()
            }

            var filePartName = splitFile.absolutePath + "/" + String.format(
                "%s.%03d",
                f.name,
                partCounter
            )

            val inputStream: InputStream = FileInputStream(f)
            var outputStream: OutputStream = FileOutputStream(filePartName)

            val splitSize = inputStream.available() / totalPartsToSplit
            var streamSize = 0
            var read: Int

            while (inputStream.read().also { read = it } != -1) {
                if (splitSize == streamSize) {
                    if (partCounter != totalPartsToSplit) {
                        partCounter++

                        filePartName = splitFile.absolutePath + "/" + String.format(
                            "%s.%03d",
                            f.name,
                            partCounter
                        )

                        outputStream = FileOutputStream(filePartName)
                        streamSize = 0
                    }
                }
                outputStream.write(read)
                streamSize++
            }

            inputStream.close()
            outputStream.close()

            layout_splitmerge.snackbar("Split process done")
            filePath = ""
            tv_path.text = "File Path..."
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mergeFiles(files: List<File>) {
        val mDirectory = File(directoryToWrite)
        if (!mDirectory.exists()) {
            mDirectory.mkdir()
        }

        val fileName = File(filePath).name.substring(0, File(filePath).name.lastIndexOf('.'))

        FileOutputStream(File(mDirectory, fileName)).use { fos ->
            BufferedOutputStream(fos).use {
                for (f in files) {
                    if (SDK_INT >= O) {
                        Files.copy(f.toPath(), it)
                    }
                }

                layout_splitmerge.snackbar("Merge process done")
                filePath = ""
                tv_path.text = "File Path..."
            }
        }
    }

    private fun listOfFilesToMerge(oneOfFiles: File): List<File> {
        val tmpName = oneOfFiles.name
        val destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'))

        val regex = "$destFileName[.]\\d+".toRegex()
        val oneOf = oneOfFiles.parentFile
        val files = oneOf?.listFiles { _, name -> name.matches(regex) }

        Arrays.sort(files!!)
        return listOf<File>(*files)
    }
}