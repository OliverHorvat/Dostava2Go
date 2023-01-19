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
import kotlin.math.roundToInt

class CategoriesFragment : Fragment(),CategoriesRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: CategoriesRecyclerAdapter
    val mainBundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this.arguments?.getString("ORDER") == null){
            mainBundle.putString("ORDER",null)
            mainBundle.putFloat("PRICE",(0).toFloat())
        }
        else{
            mainBundle.putString("ORDER",this.arguments?.getString("ORDER"))
            mainBundle.putFloat("PRICE",(this.requireArguments().getFloat("PRICE")*100).roundToInt()/100.toFloat())
        }
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.categoryList)
        val orderButton = view.findViewById<Button>(R.id.orderButton)
        val clearOrderButton = view.findViewById<Button>(R.id.clearOrderButton)
        val orderDetailsButton = view.findViewById<Button>(R.id.orderDetailsButton)
        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                val categoryList = ArrayList<Category>()
                for (data in result.documents) {
                    val category = data.toObject(Category::class.java)
                    if (category != null) {
                        categoryList.add(category)
                    }
                }
                recyclerAdapter = CategoriesRecyclerAdapter(categoryList,this@CategoriesFragment)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w(
                    "CategoriesFragment", "Error getting documents.",
                    exception
                )
            }


        orderDetailsButton.setOnClickListener{
            if (mainBundle.getString("ORDER")!=null) {
                Toast.makeText(
                    activity,
                    mainBundle.getString("ORDER") + "\n" +"Total price: " + mainBundle.getFloat("PRICE")
                        .toString() + " €",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(
                    activity,
                    "Order is currently empty, please add item(s) to you order!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        clearOrderButton.setOnClickListener{
            mainBundle.putString("ORDER",null)
            mainBundle.putFloat("PRICE",(0).toFloat())
            Toast.makeText(
                activity,
                "Your order has been cleared.",
                Toast.LENGTH_SHORT
            ).show()
        }

        orderButton.setOnClickListener{
            if (mainBundle.getString("ORDER")!=null) {
                val confirmOrderFragment = ConfirmOrderFragment()
                confirmOrderFragment.arguments = mainBundle
                val fragmentTransaction: FragmentTransaction? =
                    activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainerView, confirmOrderFragment)
                fragmentTransaction?.commit()
            }
            else{
                Toast.makeText(
                    activity,
                    "Order is currently empty, please add item(s) to you order!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }
    override fun onItemButtonClick(category: Category) {

        val userOrdersFragment = UserOrdersFragment()

        mainBundle.putString("CATEGORY", category.name)
        userOrdersFragment.arguments = mainBundle
        val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainerView, userOrdersFragment)
        fragmentTransaction?.commit()


    }

}