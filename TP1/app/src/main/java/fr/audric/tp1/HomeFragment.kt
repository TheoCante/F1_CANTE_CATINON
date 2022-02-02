package fr.audric.tp1

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager


class HomeFragment : Fragment(R.layout.home_fragment) {
    val stringsViewModel : StringsViewModel by activityViewModels()
    var _adapter : ItemAdapter? = null
    var num = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the observer which updates the UI.
        _adapter = ItemAdapter(ArrayList<Image>(10))

        stringsViewModel.elements.observe(this) { list ->
            _adapter!!.updateElements(list)
        }

        val button = view.findViewById<Button>(R.id.buttonIntent)
        button?.setOnClickListener {
            //val intent = Intent("android.media.action.IMAGE_CAPTURE")
            //startActivity(intent)
        }
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        buttonAdd?.setOnClickListener {
               stringsViewModel.genElement()
        }

        stringsViewModel.errors.observe(viewLifecycleOwner) {
            if(it != null) {
                Toast.makeText(this.context,"Pas de co (${it::class.java.simpleName}", Toast.LENGTH_LONG).show()
                stringsViewModel.clearError()
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = _adapter
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nameView: TextView
        var _button: Button
        fun update(image: Image?,position:Int) {
            _nameView.text = image?.imageName
            _button.setOnClickListener {
                val action = HomeFragmentDirections.actionFragment2ToHomeFragment()
                action.elementPosition = position
                itemView.findNavController().navigate(action)
            }
        }

        init {
            _nameView = itemView.findViewById(R.id.recycle_item_text)
            _button = itemView.findViewById(R.id.recycle_item_button)
        }
    }

    class ItemAdapter(var images : List<Image>) : RecyclerView.Adapter<ItemViewHolder>() {
        fun updateElements(newImages: List<Image>) {
            images = newImages
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val context: Context = parent.context
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.list_elt, parent, false)

            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
            viewHolder.update(images[position],position)
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }
}