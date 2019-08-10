package jp.yama.myapplication3

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateVMFactory


class AFragment : Fragment() {

    companion object {
        fun newInstance() = AFragment()
    }

    private lateinit var viewModel: AViewModel

    private lateinit var text: TextView
    private lateinit var input: EditText
    private lateinit var next: Button
    private lateinit var check: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.a_fragment, container, false)
        text = view.findViewById(R.id.text)
        input = view.findViewById(R.id.input)
        next = view.findViewById(R.id.nextBtn)
        check = view.findViewById(R.id.checkBox2)
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
        viewModel = ViewModelProviders.of(this, factory).get(AViewModel::class.java)

        text.text = viewModel.handle.get("text")
        input.text = viewModel.handle.get("text")
        input.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.handle.set("text", p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        viewModel.handle.getLiveData<String>("text").observe(this, Observer {
            text.text = it
        })
        next.setOnClickListener {
            val bundle = Bundle().apply {
                this.putString("text", viewModel.handle.get("text"))
                this.putBoolean("check", viewModel.handle.get<Boolean>("check") ?: false)
            }
            val fragment = BFragment.newInstance()
            fragment.arguments = bundle
            fragmentManager?.let {
                it.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, fragment)
                    .commit()
            }
        }
        viewModel.handle.get<Boolean>("check")?.let {
            check.isChecked = it
        }
    }

}
