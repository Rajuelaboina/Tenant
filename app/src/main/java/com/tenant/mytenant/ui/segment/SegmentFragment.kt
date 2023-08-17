package com.tenant.mytenant.ui.segment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tenant.mytenant.ui.power.PowerWaterFragment
import com.tenant.mytenant.R
import com.tenant.mytenant.ui.fragment.UserPaymentFragment
import com.tenant.mytenant.databinding.FragmentSegmentBinding
import com.tenant.mytenant.ui.rentpayment.Payment
import com.tenant.mytenant.ui.register.UserRegistration


class SegmentFragment : Fragment() {
    private  var _binding: FragmentSegmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: SegmentViewModel
    private  lateinit var  userRegistration: UserRegistration
    private  lateinit var  payment: Payment
    private val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_segment, container, false)
        _binding = FragmentSegmentBinding.inflate(inflater,container,false)
        //viewModel = ViewModelProvider(this,SegmentViewModelFactory(this))[SegmentViewModel::class.java]
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRegistration = arguments?.getSerializable("OK") as UserRegistration
        payment = arguments?.getSerializable("PAYMENT") as Payment

        bundle.putSerializable("OK",userRegistration)
        bundle.putSerializable("PAYMENT",payment)

        // first tiem enbled the radio button display the data
        if ( binding.paymentRadioButton.isChecked){
            loadPaymentRadioData()
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.paymentRadioButton ->{
                    loadPaymentRadioData()
                }
                R.id.currentBillRadioButton ->{
                    binding.paymentRadioButton.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.black
                    ))
                    binding.currentBillRadioButton.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.white
                    ))
                    val fragment = PowerWaterFragment()
                    fragment.arguments = bundle
                    childFragmentManager.beginTransaction().replace(R.id.content,fragment).addToBackStack(null).commit()

                }
            }
        }
    }

    private fun loadPaymentRadioData() {
        val fragment = UserPaymentFragment()
        fragment.arguments = bundle
        childFragmentManager.beginTransaction().replace(R.id.content,fragment).addToBackStack(null).commit()
        binding.paymentRadioButton.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.white
        ))
        binding.currentBillRadioButton.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.black
        ))

    }


}