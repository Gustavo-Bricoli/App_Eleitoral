package com.example.app_eleitoral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

private val formatodata = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val formatohora = DateTimeFormatter.ofPattern("HH:mm")
class EleitorAdapter(private val eleitores: List<SurveyResponse>) :
    RecyclerView.Adapter<EleitorAdapter.EleitorViewHolder>(){

    class EleitorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nome)
        val telefone: TextView = itemView.findViewById(R.id.telefone)
        val candidato: TextView = itemView.findViewById(R.id.candidato)
        val localizacao: TextView = itemView.findViewById(R.id.localizacao)
        val problemas: TextView = itemView.findViewById(R.id.problemas)
        val dataehorario: TextView = itemView.findViewById(R.id.dataehorario)
    }

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EleitorViewHolder{
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.itens_eleitores, parent, false)
    return EleitorViewHolder(view)
}

override fun onBindViewHolder(holder: EleitorViewHolder, position: Int){
    val eleitor = eleitores[position]

    holder.nome.text = eleitor.nome
    holder.telefone.text = eleitor.telefone
    holder.candidato.text = eleitor.candidato
    holder.localizacao.text = eleitor.localizacao
    holder.problemas.text = eleitor.problemas.joinToString(", ")
    val estruturadata = eleitor.data.format(formatodata)
    val estruturahora = eleitor.data.format(formatohora)
    holder.dataehorario.text = "${estruturadata} às ${estruturahora}"
    }
override fun getItemCount(): Int = eleitores.size
}