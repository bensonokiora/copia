package co.ke.copia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.ke.copia.R
import co.ke.copia.databinding.ListItemBinding
import co.ke.copia.models.AllocationItem

class AllocationsAdapter(allocationItems: List<AllocationItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val allocationItems = ArrayList<AllocationItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemBinding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item, parent, false
        )
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val allocationItem = allocationItems[position]
        (holder as ViewHolder).listItemBinding.allocation = allocationItem
    }

    override fun getItemCount(): Int {
        return allocationItems.size
    }

    class ViewHolder(var listItemBinding: ListItemBinding) :
        RecyclerView.ViewHolder(listItemBinding.root)

    init {
        this.allocationItems.addAll(allocationItems!!)
    }
}
