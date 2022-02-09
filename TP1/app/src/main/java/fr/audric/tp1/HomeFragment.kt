package fr.audric.tp1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File


class HomeFragment : Fragment(R.layout.home_fragment) {
    val stringsViewModel : StringsViewModel by activityViewModels()
    lateinit var _adapter : ItemAdapter
    var num = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Create the observer which updates the UI.
        _adapter = ItemAdapter(ArrayList<StoredImage>(10), object : UpdateCallbacks{
            override fun onClick(image: CommonImage){
                if(image is GeneratedImage) {
                    findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.url))
                }
                if(image is StoredImage) {
                    findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.imageName))
                }
            }
        })

        stringsViewModel.elementsLiveData.observe(viewLifecycleOwner) { list ->
            _adapter.updateElements(list)
        }

        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            stringsViewModel.genElement()
        }

        stringsViewModel.errors.observe(viewLifecycleOwner) {
            if(it != null) {
                Toast.makeText(this.context,"Pas de co (${it::class.java.simpleName}", Toast.LENGTH_LONG).show()
                stringsViewModel.clearError()
            }
        }
        recyclerView.adapter = _adapter
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nameView: TextView
        var _imageView: ImageView
        var _button: Button

        init {
            _nameView = itemView.findViewById(R.id.recycle_item_text)
            _button = itemView.findViewById(R.id.recycle_item_button)
            _imageView = itemView.findViewById(R.id.recycle_item_image)
        }

        fun update(image: CommonImage,classeFunction : UpdateCallbacks) {
//            _nameView.text = image.imageName
            _button.setOnClickListener {
                classeFunction.onClick(image)
            }
            if(image is StoredImage) {
                val imageFile =  File(_imageView.context.filesDir, image.imageName)
                _imageView.load(imageFile)
            } else if(image is GeneratedImage) {
                _imageView.load(R.drawable.unknown)
            }
        }
    }

    class ItemAdapter(var images : List<CommonImage>, val classeFunction : UpdateCallbacks) : RecyclerView.Adapter<ItemViewHolder>() {
        fun updateElements(newImages: List<CommonImage>) {
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
            val image = images[position]
            viewHolder.itemView.setOnClickListener(){
                classeFunction.onClick(image)
            }
            viewHolder.update(image,classeFunction)
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }

    interface UpdateCallbacks{
        fun onClick(image : CommonImage)
    }
}