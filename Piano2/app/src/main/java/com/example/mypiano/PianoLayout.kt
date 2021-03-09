package com.example.mypiano

import Note
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mypiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.fullToneKey
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2", "A2", "B2")
    private val semiTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")

    private var score:MutableList<Note> = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()


        fullTones.forEach{ OrgNoteValue ->
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(OrgNoteValue)
            var PlayFullNote:Long = 0
            var FullNoteTimeStart = ""

            fullTonePianoKey.onKeyDown = { note ->
                PlayFullNote = System.nanoTime()
                val currentDateTime = LocalDateTime.now()
                FullNoteTimeStart = currentDateTime.format(DateTimeFormatter.ISO_TIME)
                println("White key down $note")
            }
            fullTonePianoKey.onKeyUp = {
                val StopFullNote = System.nanoTime()
                var totalFullTone: Double = 0.0
                var timeFullTone: Long = 0
                timeFullTone = StopFullNote - PlayFullNote
                totalFullTone = timeFullTone.toDouble() / 1000000000
                val note = Note(it, FullNoteTimeStart, totalFullTone)
                score.add(note)
                println("White key up $it start: $FullNoteTimeStart for $totalFullTone seconds")
            }

            ft.add(view.fullToneKey.id, fullTonePianoKey, OrgNoteValue)
        }

        semiTones.forEach{ OrgNoteValue ->
            val semiTonePianoKey = SemiTonePianoKeyFragment.newInstance(OrgNoteValue)
            var startPlayHalf:Long = 0
            var SemiNoteTimeStart:String = ""

            semiTonePianoKey.onKeyDown = {note ->
                startPlayHalf = System.nanoTime()
                val currentDateTime = LocalDateTime.now()
                SemiNoteTimeStart = currentDateTime.format(DateTimeFormatter.ISO_TIME)
                println("Semi key down $note")
            }
            semiTonePianoKey.onKeyUp = {
                val endPlayHalf = System.nanoTime()
                var totalHalfTone:Double = 0.0
                var timeHalfTone:Long = 0

                timeHalfTone = endPlayHalf - startPlayHalf
                totalHalfTone = timeHalfTone.toDouble() / 1000000000
                val note = Note(it,SemiNoteTimeStart,totalHalfTone)

                score.add(note)
                println("Black key up $it start: $SemiNoteTimeStart for $totalHalfTone seconds")
            }

            ft.add(view.semiToneKey.id,semiTonePianoKey,OrgNoteValue)
        }

        ft.commit()
        view.saveScoreBt.setOnClickListener {
            var fileName = view.fileNameTextEdit.text.toString()
            fileName = "$fileName.musikk"
            val path = this.activity?.getExternalFilesDir(null)
            val file = File(path, fileName)
            val fileExists = file.exists()
            if (score.count() > 0 && fileName.isNotEmpty() && path != null){
                if(fileExists){
                    Toast.makeText(activity,"$fileName this name is taken. Please enter different file name.", Toast.LENGTH_SHORT).show()
                } else{
                    FileOutputStream(File(path,fileName),true).bufferedWriter().use { writer ->
                        score.forEach {
                            writer.write("${it.toString()}\n")
                        }
                    }
                }
            }
        }
        return view
    }
}