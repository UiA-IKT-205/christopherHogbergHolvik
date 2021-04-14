package com.example.huskeliste

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.tilpasset_liste.view.*
import java.io.File
import java.io.FileOutputStream


class HuskelisteAdapter(
    val huskelister: MutableList<Huskeliste>
) : RecyclerView.Adapter<HuskelisteAdapter.HuskelisteViewHolder>() {

    class HuskelisteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val context: Context? = null


    fun leggTilHuskeliste(huskeliste: Huskeliste) {
        huskelister.add(huskeliste)
        notifyItemInserted(huskelister.size - 1)


        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference = firebaseDatabase.getReference()
        databaseReference.child("Lists").push().setValue("huskeliste.Tittel")
    }

    fun fjernHuskeListe() {
        huskelister.removeAll { todo ->
            todo.Markert
        }

        try {
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HuskelisteViewHolder {
        return HuskelisteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tilpasset_liste, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return huskelister.size
    }

    override fun onBindViewHolder(holder: HuskelisteViewHolder, position: Int) {
        val Gjeldende_liste = huskelister[position]
        holder.itemView.apply {
            Liste_Navn.text = Gjeldende_liste.Tittel
            ferdig_Checkbox.isChecked = Gjeldende_liste.Markert
            ferdig_Checkbox.setOnCheckedChangeListener { _, isChecked ->
                Gjeldende_liste.Markert = !Gjeldende_liste.Markert
                fjernHuskeListe()

                var fileName = "Huskelister"
                val path = context?.getExternalFilesDir(null)
                if(fileName.isNotEmpty() && path != null)
                {
                    println("Fungerer det?")
                    println(fileName)
                    println(path)
                    fileName = "$fileName.Lists"
                    File(path,fileName).delete()
                    FileOutputStream(File(path,fileName),true).bufferedWriter().use { writer ->
                        huskelister.forEach{
                            writer.write("${it.Tittel}\n")
                            println(huskelister)
                        }
                    }
                }

            }
            Liste_Navn.setOnClickListener{
                val context = holder.itemView.context
                val intent = Intent(context, MainActivity2::class.java)
                context.startActivity(intent)
            }
        }
    }
    fun openActivity2(holder: HuskelisteViewHolder) {
        val context = holder.itemView.context
        val intent = Intent(context, MainActivity2::class.java)
        context.startActivity(intent)
        println("TEST321")
    }
}


















