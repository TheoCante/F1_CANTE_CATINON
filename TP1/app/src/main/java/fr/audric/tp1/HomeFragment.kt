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
    val imageViewModel : ImageViewModel by activityViewModels()
    lateinit var _adapter : ItemAdapter
    var num = 0;

    //creation du fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Creation de l'adapter qui sera associe a la RecyclerView
        _adapter = ItemAdapter(ArrayList<StoredImage>(10),
            // Callback des Views dans la RecyclerView
            object : UpdateCallbacks{
                // Redefinition de onClick
                override fun onClick(image: CommonImage){
                    // On passe a la vue detaille de l'image
                    if(image is GeneratedImage) {//Si l'image est genere on envoie l'URL
                        findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.url))
                    }
                    if(image is StoredImage) {//Si l'image est stocke on envoie le nom
                        findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.imageName))
                    }
                }
        })

        // Creation de l'observer qui mets a jour l'UI
        imageViewModel.elementsLiveData.observe(viewLifecycleOwner) { list ->
            _adapter.updateElements(list)
        }

        // Recuperation du bonton d'ajout d'image
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        // On cree une image quand a l'appui sur le bouton
        buttonAdd.setOnClickListener {
            imageViewModel.genElement()
        }

        // Creation de l'observer qui signale a l'utilisateur une erreur
        imageViewModel.errors.observe(viewLifecycleOwner) {
            if(it != null) { // si l'erreur existe on l'affiche puis on la supprime
                Toast.makeText(this.context,"Probleme (${it::class.java.simpleName}", Toast.LENGTH_LONG).show()
                imageViewModel.clearError()
            }
        }

        // On donne notre adapter a la RecyclerView
        recyclerView.adapter = _adapter
    }
}