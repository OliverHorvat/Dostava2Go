package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserOrdersRecyclerAdapter(
    val items: ArrayList<Order>,
    val listener: ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserOrderViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_order,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is UserOrderViewHolder -> {
                holder.bind(items[position],listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }



    class UserOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val orderName = itemView.findViewById<TextView>(R.id.orderName)
        private val orderDescription = itemView.findViewById<TextView>(R.id.orderDescription)
        private val orderPrice = itemView.findViewById<TextView>(R.id.orderPrice)
        private val orderAmount = itemView.findViewById<EditText>(R.id.orderAmountInput)
        private val addOrderButton = itemView.findViewById<Button>(R.id.addOrderButton)
        fun bind(order: Order,listener: ContentListener){
            orderName.text=order.name
            orderDescription.text=order.description
            orderPrice.text=order.price + " €"
            addOrderButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("ORDER", order.name)
                bundle.putString("AMOUNT", orderAmount.text.toString())
                bundle.putFloat("PRICE", order.price.toFloat()*orderAmount.text.toString().toFloat())
                listener.onItemButtonClick(order,bundle)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(order:Order,bundle:Bundle)
    }
}