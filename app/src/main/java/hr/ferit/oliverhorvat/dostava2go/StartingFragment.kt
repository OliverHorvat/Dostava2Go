package hr.ferit.oliverhorvat.dostava2go

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction


class StartingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_starting, container, false)
        val userLoginButton = view.findViewById<Button>(R.id.userLoginButton)
        val deliveryLoginButton = view.findViewById<Button>(R.id.deliveryLoginButton)

        userLoginButton.setOnClickListener(){
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString("LOGINOPTION", "USER")
            loginFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, loginFragment)
            fragmentTransaction?.commit()
        }

        deliveryLoginButton.setOnClickListener(){
            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString("LOGINOPTION", "DELIVERY")
            loginFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, loginFragment)
            fragmentTransaction?.commit()
        }
        return view
    }

}