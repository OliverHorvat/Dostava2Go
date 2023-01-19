package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserOrdersFragment : Fragment(),UserOrdersRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: UserOrdersRecyclerAdapter
    val mainBundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainBundle.putString("ORDER",this.arguments?.getString("ORDER"))
        mainBundle.putFloat("PRICE",this.requireArguments().getFloat("PRICE"))

        val view = inflater.inflate(R.layout.fragment_user_orders, container, false)
        val orderCategory = this.arguments?.getString("CATEGORY")
        val recyclerView = view.findViewById<RecyclerView>(R.id.userOrderList)
        val backButton = view.findViewById<Button>(R.id.backButton)
        db.collection(orderCategory.toString())
            .get()
            .addOnSuccessListener { result ->
                val orderList = ArrayList<Order>()
                for (data in result.documents) {
                    val order = data.toObject(Order::class.java)
                    if (order != null) {
                        orderList.add(order)
                    }
                }
                recyclerAdapter = UserOrdersRecyclerAdapter(orderList,this@UserOrdersFragment)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w(
                    "UserOrdersFragment", "Error getting documents.",
                    exception
                )
            }
        backButton.setOnClickListener{
            val categoriesFragment = CategoriesFragment()
            categoriesFragment.arguments = mainBundle
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, categoriesFragment)
            fragmentTransaction?.commit()
        }

        return view
    }
    override fun onItemButtonClick(order:Order,bundle:Bundle) {
        if (bundle.getString("AMOUNT")!="0" ||bundle.getString("AMOUNT")!=null) {
            val additonToOrder =bundle.getString("AMOUNT") +"x " + bundle.getString("ORDER")
            Toast.makeText(
                activity,
                "Added " + additonToOrder + " to your order",
                Toast.LENGTH_SHORT
            ).show()
            mainBundle.getString("ORDER")
            var tempOrder=""
            if (mainBundle.getString("ORDER")==null) {
                tempOrder = additonToOrder
            }
            else{
                tempOrder = mainBundle.getString("ORDER")!!
                if (tempOrder.indexOf("x "+bundle.getString("ORDER")!!)!=-1) {
                    val duplicateIndex = tempOrder.indexOf("x " + bundle.getString("ORDER")!!)
                    val beforeIndex = tempOrder.substring(0, duplicateIndex)
                    val afterIndex = tempOrder.substring(duplicateIndex, tempOrder.length)
                    val newLineIndex = beforeIndex.lastIndexOf("\n")
                    if (newLineIndex == -1) {
                        val amount = tempOrder.substring(0, duplicateIndex)
                            .toInt() + bundle.getString("AMOUNT")!!.toInt()
                            tempOrder = amount.toString() + afterIndex

                    } else {
                        val beforeNewLine=tempOrder.substring(0,newLineIndex)
                        val amount=tempOrder.substring(newLineIndex+1,duplicateIndex).toInt() + bundle.getString("AMOUNT")!!.toInt()
                        tempOrder = beforeNewLine + "\n" + amount.toString() + afterIndex
                    }
                }
                else{
                    tempOrder = tempOrder + "\n" + additonToOrder
                }


            }
            mainBundle.putString("ORDER",tempOrder)
            val tempPrice = mainBundle.getFloat("PRICE") + bundle.getFloat("PRICE")
            mainBundle.putFloat("PRICE",tempPrice)
        }
    }

}

