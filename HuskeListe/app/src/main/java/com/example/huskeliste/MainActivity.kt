package com.example.huskeliste

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tilpasset_liste.*
import java.io.*


class MainActivity : AppCompatActivity() {
    private val TAG:String = "HuskeListe:MainActivity"
    private lateinit var huskelisteAdapter: HuskelisteAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference



    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener{
            println("Meldinger")
            Log.d(TAG, "Login success ${it.user.toString()}")
        }
        auth.signInAnonymously().addOnFailureListener(){
            Log.e(TAG,"Login failed", it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        signInAnonymously()
        huskelisteAdapter = HuskelisteAdapter(mutableListOf())
        liste_gjenstander.adapter = huskelisteAdapter
        liste_gjenstander.layoutManager = LinearLayoutManager(this)


        var fileNameRead = "Huskelister"
        fileNameRead = "$fileNameRead.Lists"

        val pathRead = getExternalFilesDir(null)
        var myExternalFile:File = File(pathRead, fileNameRead)

        println("fileNameRead: " + fileNameRead)
        println("pathRead: " + pathRead)
        println("File: " + File(pathRead,fileNameRead).toString())
        if(File(pathRead,fileNameRead).exists()) {
            myExternalFile = File(pathRead.toString(), fileNameRead)
            var fileInputStream = FileInputStream(myExternalFile)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
                val liste = Huskeliste(stringBuilder.toString())
                huskelisteAdapter.leggTilHuskeliste(liste)
                stringBuilder.clear()
            }
            fileInputStream.close()
            println("The list is: " + stringBuilder.toString())
            Toast.makeText(applicationContext, stringBuilder.toString(), Toast.LENGTH_SHORT).show()
        }


        Legg_til_knapp.setOnClickListener {
            val Huskeliste_Tittel = liste_Tittel.text.toString()
            if(Huskeliste_Tittel.isNotEmpty()) {
                val liste = Huskeliste(Huskeliste_Tittel)
                huskelisteAdapter.leggTilHuskeliste(liste)
                liste_Tittel.text.clear()


                println("Test123")
                var fileName = "Huskelister"
                val path = getExternalFilesDir(null)
                if(fileName.isNotEmpty() && path != null)
                {
                    println(fileName)
                    println(path)
                    fileName = "$fileName.Lists"
                    File(path,fileName).delete()
                    val filPlassering = File(path,fileName)
                    FileOutputStream(File(path,fileName),true).bufferedWriter().use { writer ->
                        huskelisteAdapter.huskelister.forEach{
                            writer.write("${it.Tittel}\n")
                            //writer.write("${it.toString()}\n")
                        }

                        Log.d(TAG,"Uploaded file $filPlassering")

                        val ref = FirebaseStorage.getInstance().reference.child("Lists/${filPlassering.toUri().lastPathSegment}")
                        var uploadtask = ref.putFile(filPlassering.toUri())

                        uploadtask.addOnSuccessListener{
                            Log.d(TAG,"Succsesfully uploaded file to fb.")
                        }.addOnFailureListener{
                            Log.d(TAG,"Something went wrong when uploading to fb.")
                        }
                    }
                }

                database = FirebaseDatabase.getInstance()
                referance = database.getReference("Lists")
                val Tittel = Huskeliste_Tittel

                if(Tittel.isNotEmpty())
                {
                    referance.child("Lists").push().setValue(Tittel)
                }
                else{
                    println("Tittel field er tom")
                }
            }
        }
        try{
            Liste_Navn.setOnClickListener{
                println("TEST123")
                openActivity2()
            }
        }
        catch (e: Exception)
        {
            Log.d("", e.toString())
        }

    }
    fun openActivity2() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        println("TEST321")
    }

}