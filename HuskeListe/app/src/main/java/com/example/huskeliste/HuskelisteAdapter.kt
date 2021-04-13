package com.example.huskeliste

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tilpasset_liste.view.*

class HuskelisteAdapter(
    private val huskelister: MutableList<Huskeliste>
) : RecyclerView.Adapter<HuskelisteAdapter.HuskelisteViewHolder>() {

    class HuskelisteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun leggTilHuskeliste(huskeliste: Huskeliste) {
        huskelister.add(huskeliste)
        notifyItemInserted(huskelister.size - 1)
    }

    fun fjernHuskeListe() {
        huskelister.removeAll { todo ->
            todo.Markert
        }

        try {
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.d("", e.toString())
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
            }
        }
    }
}


















