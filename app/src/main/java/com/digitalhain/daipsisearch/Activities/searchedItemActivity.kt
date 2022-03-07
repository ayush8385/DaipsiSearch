package com.digitalhain.daipsisearch.Activities

//import pl.droidsonroids.gif.GifImageView

import android.Manifest
import android.R.attr
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.livesearch.ApiClient
import com.ayush.livesearch.ApiInterface
import com.digitalhain.daipsisearch.R
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.Activities.Room.QuestionEntity
import com.digitalhain.daipsisearch.Activities.Room.QuestionViewModel
import com.digitalhain.daipsisearch.Activities.utils.Preferences
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hellohasan.android_firebase_notification.notification.Configuration
import android.R.attr.bitmap
import android.net.Uri

import android.provider.MediaStore
import android.app.ProgressDialog

import android.os.AsyncTask

import android.graphics.Bitmap
import android.util.Base64
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat
import net.gotev.uploadservice.data.UploadNotificationConfig
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class searchedItemActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var ll_center: LinearLayout
    lateinit var recyclerAdapter:SearchAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var textse:TextView
    lateinit var noDataText:TextView
    lateinit var url:String
    lateinit var wait:TextView
    lateinit var searchView: SearchView
    lateinit var gif: GifImageView
    lateinit var apiInterface:ApiInterface
    lateinit var subjectArray:ArrayList<Subject>
    lateinit var sharedPreferences:Preferences

    lateinit var progressDialog: ProgressDialog

    lateinit var GetImageNameEditText: String

    var ImageName = "image_name"

    var ImagePath = "image_path"

    lateinit var bitmap: Bitmap
    lateinit var uri: Uri
    var PdfNameHolder: String? = null

    var PdfID:String? = null


    lateinit var askQuestion:ExtendedFloatingActionButton
    lateinit var addImg:FloatingActionButton
    lateinit var addPdf:FloatingActionButton

    lateinit var imgtxt:TextView
    lateinit var pdftext:TextView

    var fabsvisible:Boolean=false

    val handler = Handler()
    val filteredlist:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()
    var str=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_item)

        ll_center = findViewById(R.id.ll_center)
        textse=findViewById(R.id.text_ser)
        noDataText=findViewById(R.id.noDataText)
        searchView=findViewById(R.id.search_bar)
        gif=findViewById(R.id.gif)
        wait=findViewById(R.id.wait_text)

        askQuestion=findViewById(R.id.ask_que)
        addImg=findViewById(R.id.add_img)
        addPdf=findViewById(R.id.add_pdf)

        imgtxt=findViewById(R.id.add_imgtxt)
        pdftext=findViewById(R.id.add_pdftxt)

        addImg.visibility=View.GONE
        addPdf.visibility=View.GONE
        imgtxt.visibility=View.GONE
        pdftext.visibility=View.GONE

        fabsvisible=false

        askQuestion.shrink()

        askQuestion.setOnClickListener {
            if(!fabsvisible){
                addImg.visibility=View.VISIBLE
                addPdf.visibility=View.VISIBLE
                imgtxt.visibility=View.VISIBLE
                pdftext.visibility=View.VISIBLE

                askQuestion.extend()

                fabsvisible=true
            }
            else{
                addImg.visibility=View.GONE
                addPdf.visibility=View.GONE
                imgtxt.visibility=View.GONE
                pdftext.visibility=View.GONE

                fabsvisible=false

                askQuestion.shrink()
            }
        }
        addPdf.setOnClickListener {
            val intent = Intent()

            intent.type = "application/pdf"

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 2)

        }

        addImg.setOnClickListener {
            val intent = Intent()

            intent.type = "image/*"

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1)

        }

        val sub=intent.getStringExtra("subject")

        supportActionBar!!.title=sub

        sharedPreferences= Preferences.getInstance(applicationContext)!!


        recyclerView=findViewById(R.id.recyclermain)
        layoutManager=LinearLayoutManager(this@searchedItemActivity)
        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(true)


        if(sub=="Engineering"){
            str="engineering"
        }
        else if(sub=="Medical"){
            str="medical"
        }
        else if(sub=="Commerce"){
            str="commerce"
        }
        else{
            str="govtexams"
        }




        gif.visibility=View.GONE
        wait.visibility=View.GONE
        searchView.visibility=View.VISIBLE

        searchElement()

        fetchQuestion("","")

    }

    override fun onActivityResult(RC: Int, RQC: Int, I: Intent?) {
        super.onActivityResult(RC, RQC, I)
        if (RC == 1 && RQC == RESULT_OK && I != null && I.data != null) {
            uri = I.data!!
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                ImageUploadToServerFunction()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (RC==2 && RQC == RESULT_OK && I != null && I.getData() != null) {

            uri = I.getData()!!
//            AllowRunTimePermission()
            PdfUploadFunction()

            //SelectButton.setText("PDF is Selected");
        }
    }

    private fun searchElement() {

        recyclerAdapter= SearchAdapter(this@searchedItemActivity)
        recyclerView.adapter=recyclerAdapter

        searchView.queryHint="Search your question..."
        searchView.setIconifiedByDefault(false)
        val searchIcon:ImageView = searchView.findViewById(R.id.search_mag_icon);
        searchIcon.visibility= View.GONE
        searchIcon.setImageDrawable(null)
        val closeIcon:ImageView = searchView.findViewById(R.id.search_close_btn);
        closeIcon.setColorFilter(Color.BLACK)
        val theTextArea = searchView.findViewById<View>(R.id.search_src_text) as SearchAutoComplete
        theTextArea.setTextColor(Color.BLACK)
        theTextArea.setHintTextColor(Color.DKGRAY)//or any color that you want

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                if(query.length>1){
                    fetchQuest(query,str)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    if(newText.length>1){
                        fetchQuestion(newText.replace("\\s".toRegex(), "").replace("\\?".toRegex(), ""),str)
                    }
                    else{
                        filteredlist.clear()
                        recyclerAdapter.filterList(filteredlist)
                    }
                }, 500)
                return false

            }

        })

    }

    fun fetchQuest(key:String, course:String){

        apiInterface= ApiClient().getApiClient().create(ApiInterface::class.java)
        val call: Call<List<Subject>> =apiInterface.getQuestions(key.replace("\\s".toRegex(), "").replace("\\?".toRegex(), ""),course)

        call.enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: retrofit2.Response<List<Subject>>) {
                textse.visibility=View.VISIBLE
                subjectArray= (response.body() as ArrayList<Subject>?)!!

                recyclerAdapter.filterList(subjectArray as ArrayList<Subject>)

                var m=0
                for(quest in subjectArray){
                    if(quest.ques!!.toLowerCase().contains(key.toLowerCase())){
                        m=1
                        break
                    }
                }

                if(m==0){
                    askQuestion(str,key)
                }
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                gif.visibility=View.GONE
                wait.visibility=View.GONE
                searchView.visibility=View.VISIBLE

                textse.visibility=View.VISIBLE
                Toast.makeText(this@searchedItemActivity,"Error on :"+t.toString(),Toast.LENGTH_LONG).show()
            }

        })
    }

    fun askQuestion(str: String, newText: String) {

        url="https://daipsi.com/Android_App_Daipsi/"+str+".php/"
        val queue= Volley.newRequestQueue(this)

            val jsonObjectRequest=object : StringRequest(Method.POST,url,Response.Listener {
                try{
                    if(it.equals("success")){

//                        sharedPreferences.addQuestion(applicationContext,Subject(str,newText))

                        QuestionViewModel(application).inserQuestion(QuestionEntity(str,newText))


                        Toast.makeText(this,"Your Answer will be available in next 60 mins", Toast.LENGTH_LONG).show()
                        Log.d("repsonse...",it)
                    }
                    else{
                        Toast.makeText(this,"Error While Saving Question", Toast.LENGTH_LONG).show()
                    }
                }
                catch (e:Exception){
                    Toast.makeText(applicationContext,"Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
            }){
                override fun getParams(): MutableMap<String, String> {
                    val params=HashMap<String,String>()
                    params.put("question",newText)
                    params.put("course",str)
                    params.put("asked_by", getSharedPreferences(Configuration.SHARED_PREF,
                        MODE_PRIVATE).getString("fcm_token","")!!
                    )
                    return params
                }
            }
            queue.add(jsonObjectRequest)
    }

    fun fetchQuestion(key:String, course:String){

        apiInterface= ApiClient().getApiClient().create(ApiInterface::class.java)
        val call: Call<List<Subject>> =apiInterface.getQuestions(key,course)

        call.enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: retrofit2.Response<List<Subject>>) {
                textse.visibility=View.VISIBLE
                subjectArray= (response.body() as ArrayList<Subject>?)!!


                recyclerAdapter.filterList(subjectArray)


            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                gif.visibility=View.GONE
                wait.visibility=View.GONE
                searchView.visibility=View.VISIBLE

                textse.visibility=View.VISIBLE
                Toast.makeText(this@searchedItemActivity,"Error on :"+t.toString(),Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finishAffinity()
        super.onBackPressed()
    }


    fun ImageUploadToServerFunction() {
        val byteArrayOutputStreamObject: ByteArrayOutputStream
        byteArrayOutputStreamObject = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject)
        val byteArrayVar: ByteArray = byteArrayOutputStreamObject.toByteArray()
        val ConvertImage: String = Base64.encodeToString(byteArrayVar, Base64.DEFAULT)


        class AsyncTaskUploadClass :
            AsyncTask<Void?, Void?, String?>() {
            override fun onPreExecute() {
                super.onPreExecute()
                progressDialog = ProgressDialog.show(
                    this@searchedItemActivity,
                    "Image is Uploading",
                    "Please Wait",
                    false,
                    false
                )
            }

            override fun onPostExecute(string1: String?) {
                super.onPostExecute(string1)

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss()

                // Printing uploading success message coming from server on android app.
                Toast.makeText(this@searchedItemActivity, string1, Toast.LENGTH_LONG).show()

                // Setting image as transparent after done uploading.
            }


            override fun doInBackground(vararg params: Void?): String? {
                val url="https://daipsi.com/Android_App_Daipsi/uploadimg.php"
                val imageProcessClass = ImageProcessClass()
                val HashMapParams =
                    HashMap<String, String>()
                HashMapParams[ImageName] = "GetImageNameEditText"
                HashMapParams[ImagePath] = ConvertImage
                return imageProcessClass.ImageHttpRequest(url, HashMapParams)
            }
        }

        val AsyncTaskUploadClassOBJ = AsyncTaskUploadClass()
        AsyncTaskUploadClassOBJ.execute()
    }

    class ImageProcessClass {
        fun ImageHttpRequest(requestURL: String?, PData: HashMap<String, String>): String {
            var stringBuilder = StringBuilder()
            try {
                val url: URL
                val httpURLConnectionObject: HttpURLConnection
                val OutPutStream: OutputStream
                val bufferedWriterObject: BufferedWriter
                val bufferedReaderObject: BufferedReader
                val RC: Int
                url = URL(requestURL)
                httpURLConnectionObject = url.openConnection() as HttpURLConnection
                httpURLConnectionObject.setReadTimeout(19000)
                httpURLConnectionObject.setConnectTimeout(19000)
                httpURLConnectionObject.setRequestMethod("POST")
                httpURLConnectionObject.setDoInput(true)
                httpURLConnectionObject.setDoOutput(true)
                OutPutStream = httpURLConnectionObject.getOutputStream()
                bufferedWriterObject = BufferedWriter(
                    OutputStreamWriter(OutPutStream, "UTF-8")
                )
                bufferedWriterObject.write(bufferedWriterDataFN(PData))
                bufferedWriterObject.flush()
                bufferedWriterObject.close()
                OutPutStream.close()
                RC = httpURLConnectionObject.getResponseCode()
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject =
                        BufferedReader(InputStreamReader(httpURLConnectionObject.getInputStream()))
                    stringBuilder = StringBuilder()
                    var RC2: String?
                    while (bufferedReaderObject.readLine().also { RC2 = it } != null) {
                        stringBuilder.append(RC2)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return stringBuilder.toString()
        }

        @Throws(UnsupportedEncodingException::class)
        private fun bufferedWriterDataFN(HashMapParams: HashMap<String, String>): String {
            var check=true
            val stringBuilderObject: StringBuilder
            stringBuilderObject = StringBuilder()
            for ((key, value) in HashMapParams.entries) {
                if (check) check = false else stringBuilderObject.append("&")
                stringBuilderObject.append(URLEncoder.encode(key, "UTF-8"))
                stringBuilderObject.append("=")
                stringBuilderObject.append(URLEncoder.encode(value, "UTF-8"))
            }
            return stringBuilderObject.toString()
        }
    }


    fun PdfUploadFunction() {
       // PdfNameHolder = PdfNameEditText.getText().toString().trim()
        val PdfPathHolder = FilePath.getPath(this,uri)
        if (PdfPathHolder == null) {
            Toast.makeText(
                this,
                "Please move your PDF file to internal storage & try again.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            try {
                val url="https://daipsi.com/Android_App_Daipsi/uploadpdf.php"
                PdfID = UUID.randomUUID().toString()

                MultipartUploadRequest(this, url)
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name", "PdfNameHolder")
                   // .setNotificationConfig(UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload()
            } catch (exception: java.lang.Exception) {
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun AllowRunTimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            PdfUploadFunction()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Canceled", Toast.LENGTH_LONG).show()
            }
        }
    }


}