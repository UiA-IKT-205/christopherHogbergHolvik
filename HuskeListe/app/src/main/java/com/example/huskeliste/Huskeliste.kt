package com.example.huskeliste

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties

data class Huskeliste(val Tittel: String, var Markert: Boolean = false)