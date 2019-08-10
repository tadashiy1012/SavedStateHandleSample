package jp.yama.myapplication3

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.SavedStateVMFactory


class BFragment : Fragment() {

    companion object {
        fun newInstance() = BFragment()
    }

    private lateinit var viewModel: BViewModel
    private lateinit var text: TextView
    private lateinit var check: CheckBox
    private lateinit var back: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.b_fragment, container, false)
        text = view.findViewById(R.id.textb)
        check = view.findViewById(R.id.checkBox)
        back = view.findViewById(R.id.backBtn)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = SavedStateVMFactory(this, Bundle().apply {
            arguments?.getBoolean("check")?.let {
                this.putBoolean("check", it)
            }
            arguments?.getString("text")?.let {
                this.putString("text", it)
            }
        })
        viewModel = ViewModelProviders.of(this, factory).get(BViewModel::class.java)
        text.text = viewModel.handle.get("text")
        check.isChecked = viewModel.handle.get<Boolean>("check") ?: false
        back.setOnClickListener {
            val bundle = Bundle().apply {
                this.putBoolean("check", viewModel.handle.get<Boolean>("check") ?: false)
                this.putString("text", viewModel.handle.get("text"))
            }
            val fragment = AFragment.newInstance()
            fragment.arguments = bundle
            fragmentManager?.let {
                it.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, fragment)
                    .commit()
            }
        }
        check.setOnCheckedChangeListener { _, bool ->
            viewModel.handle.set("check", bool)
        }
    }

}
