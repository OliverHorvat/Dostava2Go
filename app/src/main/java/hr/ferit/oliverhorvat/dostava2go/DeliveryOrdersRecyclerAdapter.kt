package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeliveryOrdersRecyclerAdapter (
    val items: ArrayList<FinalOrder>,
    val listener: ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeliveryOrderViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_delivery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is DeliveryOrderViewHolder -> {
                holder.bind(items[position],listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    class DeliveryOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val orderTime = itemView.findViewById<TextView>(R.id.orderName)
        private val deliveryInvisButton = itemView.findViewById<Button>(R.id.deliveryInvisButton)
        fun bind(finalOrder: FinalOrder,listener: ContentListener){
            orderTime.text="Order created at: "+finalOrder.time
            deliveryInvisButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("ID", finalOrder.id)
                bundle.putString("ORDER", finalOrder.order)
                bundle.putString("PHONE", finalOrder.phone)
                bundle.putString("ADDRESS", finalOrder.address)
                bundle.putString("NOTES", finalOrder.notes)
                bundle.putString("TIME", finalOrder.time)
                listener.onItemButtonClick(finalOrder,bundle)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(order:FinalOrder,bundle: Bundle)
    }

}