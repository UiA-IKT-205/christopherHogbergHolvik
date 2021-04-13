package com.example.huskeliste

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG:String = "HuskeListe:MainActivity"
    private lateinit var huskelisteAdapter: HuskelisteAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference


    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener{
            println("Meldinger")
            Log.d(TAG, "Login success ${it.user.toString()}")
        }
        auth.signInAnonymously().addOnFailureListener(){
            Log.e(TAG,"Login failed", it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        signInAnonymously()

        huskelisteAdapter = HuskelisteAdapter(mutableListOf())
        liste_gjenstander.adapter = huskelisteAdapter
        liste_gjenstander.layoutManager = LinearLayoutManager(this)

        Legg_til_knapp.setOnClickListener {
            val Huskeliste_Tittel = liste_Tittel.text.toString()
            if(Huskeliste_Tittel.isNotEmpty()) {
                val liste = Huskeliste(Huskeliste_Tittel)
                huskelisteAdapter.leggTilHuskeliste(liste)
                liste_Tittel.text.clear()
            }
        }
        /*slett_Ferdige_knapp.setOnClickListener {
            huskelisteAdapter.fjernHuskeListe()
        }

         */
    }


}