package hr.algebra.nasaapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.nasaapplication.databinding.FragmentItemsBinding
import hr.algebra.nasaapplication.framework.fetchItems
import hr.algebra.nasaapplication.model.Item


class ItemsFragment : Fragment() {

    private  lateinit var b: FragmentItemsBinding
    private lateinit var items: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentItemsBinding.inflate(inflater)
        items = requireContext().fetchItems()
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemsAdapter = ItemAdapter(items, requireContext())
        b.rvItems.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
    }
}