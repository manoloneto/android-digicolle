package co.hillstech.digicollection.ui.digiDex

import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.hillstech.digicollection.R
import co.hillstech.digicollection.models.Monster
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_digidex_item.view.*
import kotlinx.android.synthetic.main.view_digivice_list_items.view.viewImage



class DigiDexAdapter(private val items: List<Monster>,
                     private val onMonsterClick: () -> Unit) : RecyclerView.Adapter<DigiDexHolder>() {

    override fun onBindViewHolder(holder: DigiDexHolder, position: Int) {
        val item = items[position]
        holder.bindView(item, onMonsterClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigiDexHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_digidex_item, parent, false)
        return DigiDexHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class DigiDexHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(item: Monster, onMonsterClick: () -> Unit) = with(itemView) {
        viewSpecies.text = item.species

        val matrix = ColorMatrix()

        if (item.scanned) {
            viewImage.alpha = 1f
            matrix.setSaturation(1f)
            layoutMonster.setOnClickListener { onMonsterClick() }
        } else {
            viewImage.alpha = 0.1f
            matrix.setSaturation(0f)
            layoutMonster.setOnClickListener { }
        }

        viewImage.colorFilter = ColorMatrixColorFilter(matrix)

        Picasso.get().load(item.image)
                .placeholder(R.drawable.placeholder)
                .into(viewImage)

    }

}