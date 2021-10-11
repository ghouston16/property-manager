package org.wit.property_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.property_manager.databinding.CardPropertyBinding
import org.wit.property_manager.models.PropertyModel

class PropertyAdapter constructor(private var properties: List<PropertyModel>) :
    RecyclerView.Adapter<PropertyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPropertyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val property = properties[holder.adapterPosition]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    class MainHolder(private val binding : CardPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: PropertyModel) {
            binding.propertyTitle.text = property.title
            binding.description.text = property.description
        }
    }
}