package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DeliveryOrderDetailsFragment : Fragment() {
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_order_details, container, false)
        val backButton = view.findViewById<Button>(R.id.deliveryBackButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteOrderButton)
        val orderData = view.findViewById<TextView>(R.id.deliveryOrderText)
        orderData.movementMethod= ScrollingMovementMethod()
        val address = view.findViewById<TextView>(R.id.addressText)
        address.movementMethod= ScrollingMovementMethod()
        val phone = view.findViewById<TextView>(R.id.phoneText)
        phone.movementMethod= ScrollingMovementMethod()
        val notes = view.findViewById<TextView>(R.id.notesText)
        notes.movementMethod= ScrollingMovementMethod()
        orderData.text = this.requireArguments().getString("ORDER")
        address.text = this.requireArguments().getString("ADDRESS")
        phone.text = this.requireArguments().getString("PHONE")
        notes.text = this.requireArguments().getString("NOTES")

        backButton.setOnClickListener{
            val deliveryOrdersFragment = DeliveryOrdersFragment()
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(
                R.id.fragmentContainerView,
                deliveryOrdersFragment
            )
            fragmentTransaction?.commit()
        }

        deleteButton.setOnClickListener{
            db.collection("orders")
                .document(this.requireArguments().getString("ID").toString()).delete()
            Toast.makeText(
                activity,
                "Order delivered and removed from active orders.",
                Toast.LENGTH_SHORT
            ).show()
            val deliveryOrdersFragment = DeliveryOrdersFragment()
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(
                R.id.fragmentContainerView,
                deliveryOrdersFragment
            )
            fragmentTransaction?.commit()
        }


        return view
    }

}