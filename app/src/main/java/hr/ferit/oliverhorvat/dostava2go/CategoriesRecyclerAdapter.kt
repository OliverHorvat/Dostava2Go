package hr.ferit.oliverhorvat.dostava2go

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoriesRecyclerAdapter(
    val items: ArrayList<Category>,
    val listener: ContentListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_category,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CategoryViewHolder -> {
                holder.bind(items[position],listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }



    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val categoryImage = itemView.findViewById<ImageView>(R.id.categoryImage)
        private val categoryName = itemView.findViewById<TextView>(R.id.categoryName)
        private val categoryButton = itemView.findViewById<Button>(R.id.categoryInvisButton)
        fun bind(category: Category,listener: ContentListener){
            Glide.with(itemView.context).load(category.imageURL).into(categoryImage)
            categoryName.text=category.name
            categoryButton.setOnClickListener {
                    listener.onItemButtonClick(category)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(category:Category)
    }
}