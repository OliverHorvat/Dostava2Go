package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginOption = this.arguments?.getString("LOGINOPTION")
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            var flag=1
            val username=view.findViewById<EditText>(R.id.usernameInput).text.toString()
            val password=view.findViewById<EditText>(R.id.passwordInput).text.toString()
            if (username.isEmpty()||password.isEmpty()){
                Toast.makeText(activity, "Username and password fields must not be empty!", Toast.LENGTH_SHORT).show()
                flag=0
            }
            if (loginOption == "USER" && flag==1) {
                db.collection("users")
                    .document(username)
                    .get()
                    .addOnSuccessListener { fetched ->
                        val user = fetched.toObject(Login::class.java)
                        if (user?.username !=null && user?.password !=null) {
                            if (user!!.username == username && user.password == password) {
                                val userOrderFragment = CategoriesFragment()
                                val fragmentTransaction: FragmentTransaction? =
                                    activity?.supportFragmentManager?.beginTransaction()
                                fragmentTransaction?.replace(
                                    R.id.fragmentContainerView,
                                    userOrderFragment
                                )
                                fragmentTransaction?.commit()
                            } else {
                                Toast.makeText(activity, "Incorrect password!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        else {
                            Toast.makeText(activity, "User not found, try checking your spelling!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "LoginFragment", "Error getting document.",
                            exception
                        )
                    }
            }
            if (loginOption == "DELIVERY") {
                db.collection("deliverypersons")
                    .document(username)
                    .get()
                    .addOnSuccessListener { fetched ->
                        val user = fetched.toObject(Login::class.java)
                        if (user?.username !=null && user?.password !=null) {
                            if (user!!.username == username && user.password == password) {
                                val deliveryOrdersFragment = DeliveryOrdersFragment()
                                val fragmentTransaction: FragmentTransaction? =
                                    activity?.supportFragmentManager?.beginTransaction()
                                fragmentTransaction?.replace(
                                    R.id.fragmentContainerView,
                                    deliveryOrdersFragment
                                )
                                fragmentTransaction?.commit()
                            } else {
                                Toast.makeText(activity, "Incorrect password!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        else {
                            Toast.makeText(activity, "Delivery person not found, try checking your spelling!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "LoginFragment", "Error getting document.",
                            exception
                        )
                    }
            }
        }
        return view
    }
}