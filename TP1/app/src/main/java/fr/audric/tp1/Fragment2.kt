package fr.audric.tp1

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray

import org.json.JSONObject

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import fr.audric.tp1.Fragment2.ItemAdapter
import androidx.recyclerview.widget.LinearLayoutManager


class Fragment2 : Fragment(R.layout.fragment2) {


    val stringsViewModel : StringsViewModel by viewModels()
    var _adapter : ItemAdapter? = null
    var num = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _adapter = ItemAdapter(stringsViewModel)
        val button = view.findViewById<Button>(R.id.buttonIntent)
        button?.setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        buttonAdd?.setOnClickListener {
            _adapter!!.addElement("Rebonjour" + num++)
            _adapter!!.notifyDataSetChanged()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = _adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nameView: TextView
        fun update(name: String?) {
            _nameView.text = name
        }

        init {
            _nameView = itemView.findViewById(R.id.recycle_item_text)
        }
    }

    class ItemAdapter(var stringsViewModel : StringsViewModel) : RecyclerView.Adapter<ItemViewHolder>() {
        fun addElement(newElement: String) {
            stringsViewModel.addElement(newElement)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val context: Context = parent.context
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.list_elt, parent, false)

            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
            viewHolder.update((stringsViewModel.getElements().value)?.get(position))
        }

        override fun getItemCount(): Int {
            if((stringsViewModel.getElements().value) == null){
                return 0
            }
            return (stringsViewModel.getElements().value)?.size!!
        }
    }
}