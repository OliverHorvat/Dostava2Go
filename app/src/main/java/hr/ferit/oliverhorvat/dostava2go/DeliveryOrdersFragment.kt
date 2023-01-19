package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DeliveryOrdersFragment : Fragment(), DeliveryOrdersRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: DeliveryOrdersRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_orders, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.deliveryOrderList)


        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                val orderList = ArrayList<FinalOrder>()
                for (data in result.documents) {
                    val order = data.toObject(FinalOrder::class.java)
                    if (order != null) {
                        order.id=data.id
                        orderList.add(order)
                    }
                }
                recyclerAdapter = DeliveryOrdersRecyclerAdapter(orderList,this@DeliveryOrdersFragment)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w(
                    "DeliveryOrdersFragment", "Error getting documents.",
                    exception
                )
            }



        return view
    }

    override fun onItemButtonClick(order:FinalOrder,bundle:Bundle) {
        val deliveryOrderDetailsFragment = DeliveryOrderDetailsFragment()
        deliveryOrderDetailsFragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(
            R.id.fragmentContainerView,
            deliveryOrderDetailsFragment
        )
        fragmentTransaction?.commit()
        }

}