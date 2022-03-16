package fr.audric.tp1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// Fragment d'accueil de l'application
class HomeFragment : Fragment(R.layout.home_fragment) {
    // Récupère le ViewModel
    private val imageViewModel : ImageViewModel by activityViewModels()

    // Création du fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupération du layoutManager
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Création d'un LayoutManager pour la recyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context)


        // Création de l'adapter qui sera associé à la RecyclerView
        val customAdapter = ItemAdapter(ArrayList<StoredImage>(10),
            // Callback des Views dans la RecyclerView
            object : UpdateCallbacks{
                // Redéfinition de onClick
                override fun onClick(image: CommonImage){
                    // On passe à la vue détaillée de l'image
                    if(image is GeneratedImage) { // Si l'image est générée on envoie l'URL
                        findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.url))
                    }
                    if(image is StoredImage) { // Si l'image est é on envoie le nom
                        findNavController().navigate(HomeFragmentDirections.actionFragment2ToHomeFragment(image.imageName))
                    }
                }
        })

        // Création de l'observer qui met à jour l'UI
        imageViewModel.elementsLiveData.observe(viewLifecycleOwner) { list ->
            customAdapter.updateElements(list)
        }

        // Récupération du bouton d'ajout d'image
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        // On crée une image lors d'un appui sur le bouton
        buttonAdd.setOnClickListener {
            imageViewModel.genElement()
        }

        // Création de l'observer qui signale à l'utilisateur une erreur
        imageViewModel.errors.observe(viewLifecycleOwner) {
            if(it != null) { // Si l'erreur existe on l'affiche puis on la supprime
                Toast.makeText(this.context,"Problème (${it::class.java.simpleName}", Toast.LENGTH_LONG).show()
                imageViewModel.clearError()
            }
        }

        // On donne notre adapter à la RecyclerView
        recyclerView.adapter = customAdapter
    }
}