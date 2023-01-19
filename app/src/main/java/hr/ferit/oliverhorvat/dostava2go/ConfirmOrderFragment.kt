package hr.ferit.oliverhorvat.dostava2go

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ConfirmOrderFragment : Fragment() {
    private val db = Firebase.firestore
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_order, container, false)
        val orderData = view.findViewById<TextView>(R.id.orderData)
        orderData.movementMethod=ScrollingMovementMethod()
        orderData.text= this.requireArguments().getString("ORDER")+"\n"+"Total price: " + this.requireArguments().getFloat("PRICE").toString()+" €"
        val addressInput=view.findViewById<EditText>(R.id.addressInput)
        val phoneInput=view.findViewById<EditText>(R.id.phoneInput)
        val notesInput=view.findViewById<EditText>(R.id.notesInput)
        val confirmOrderButton=view.findViewById<Button>(R.id.confirmOrderButton)
        val backButton=view.findViewById<Button>(R.id.finalBackButton)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm")
        confirmOrderButton.setOnClickListener{
            if (addressInput.text.toString()!="" && phoneInput.text.toString()!=""){
                val id = db.collection("orders").document().id
                val order = FinalOrder(orderData.text.toString(),addressInput.text.toString(),phoneInput.text.toString(),notesInput.text.toString(), LocalDateTime.now().format(formatter),id)
                db.collection("orders")
                    .document(id)
                    .set(order)
                Toast.makeText(
                    activity,
                    "Order is sent, if there are any problems regarding the order you will be notified via SMS message.",
                    Toast.LENGTH_LONG
                ).show()
                val categoriesFragment = CategoriesFragment()
                val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainerView, categoriesFragment)
                fragmentTransaction?.commit()
            }
            else{
                Toast.makeText(
                    activity,
                    "Address and Phone number fields must not be empty!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        backButton.setOnClickListener{
            val categoriesFragment = CategoriesFragment()
            categoriesFragment.arguments = this.arguments
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, categoriesFragment)
            fragmentTransaction?.commit()
        }

        return view
    }
}