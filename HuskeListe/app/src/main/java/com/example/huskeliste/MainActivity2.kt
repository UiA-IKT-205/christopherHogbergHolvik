package com.example.huskeliste

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.liste_Tittel
import kotlinx.android.synthetic.main.activity_main.liste_gjenstander
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.tilpasset_liste.view.*

class MainActivity2 : AppCompatActivity() {
    private val TAG:String = "HuskeListe:MainActivity"
    private lateinit var huskelisteAdapter: HuskelisteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        huskelisteAdapter = HuskelisteAdapter(mutableListOf())
        liste_gjenstander.adapter = huskelisteAdapter
        liste_gjenstander.layoutManager = LinearLayoutManager(this)

        Returner.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}