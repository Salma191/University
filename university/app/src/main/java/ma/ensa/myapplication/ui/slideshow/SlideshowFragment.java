package ma.ensa.myapplication.ui.slideshow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.myapplication.R;
import ma.ensa.myapplication.databinding.FragmentSlideshowBinding;
import ma.ensa.myapplication.ui.gallery.Role;
import ma.ensa.myapplication.ui.home.Filiere;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private ListView listView;
    private Button button;
    private Spinner roleSpinner;
    private Spinner filiereSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.listView);
        button = root.findViewById(R.id.button);
        roleSpinner = root.findViewById(R.id.roleSpinner);
        filiereSpinner = root.findViewById(R.id.filiereSpinner);

        slideshowViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayAdapter<Student> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);

        });

        slideshowViewModel.fetchDataFromAPI(requireContext());

        slideshowViewModel.getRoles().observe(getViewLifecycleOwner(), roles -> {
            ArrayAdapter<Role> roleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roles);
            roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSpinner.setAdapter(roleAdapter);
        });

        slideshowViewModel.getFilieres().observe(getViewLifecycleOwner(), filieres -> {
            ArrayAdapter<Filiere> filiereAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, filieres);
            filiereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filiereSpinner.setAdapter(filiereAdapter);
        });

//        slideshowViewModel.fetchRolesFromAPI(requireContext());
//        slideshowViewModel.fetchFilieresFromAPI(requireContext());


        button.setOnClickListener(view -> {
            showAddDataPopup();
        });

        return root;
    }

    private void showAddDataPopup() {
        // Créer une boîte de dialogue AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Ajouter des données");

        // Créer un layout pour la boîte de dialogue (peut être un XML personnalisé)
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.student_add, null);
        builder.setView(dialogView);

        // Ajouter des champs de saisie pour les données
        EditText dataInput1 = dialogView.findViewById(R.id.firstName);
        EditText dataInput2 = dialogView.findViewById(R.id.lastName);
        EditText dataInput3 = dialogView.findViewById(R.id.telephone);
        Spinner dataInput4 = dialogView.findViewById(R.id.roleSpinner);
        Spinner dataInput5 = dialogView.findViewById(R.id.filiereSpinner);


        // Ajouter un bouton "Ajouter" pour valider les données
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String firstName = dataInput1.getText().toString();
            String lastName = dataInput2.getText().toString();
            String telephone = dataInput3.getText().toString();
            Role selectedRole = (Role) dataInput4.getSelectedItem(); // Utilisez le cast pour obtenir l'objet Role
            Filiere filiere = (Filiere) dataInput5.getSelectedItem(); // Utilisez le cast pour obtenir l'objet Filiere

            // Appelez la méthode pour ajouter la nouvelle filière à l'API
            // Maintenant, vous pouvez appeler la méthode pour ajouter le nouvel étudiant avec les rôles et la filière sélectionnés
            try {
                // Créez une liste de rôles (vous pouvez avoir plus de logique pour obtenir ces rôles)
                List<Role> roles = new ArrayList<>();
                roles.add(selectedRole);

                slideshowViewModel.addNewStudent(firstName,lastName, telephone,roles,filiere,requireContext());
            } catch (JSONException e) {
                e.printStackTrace();
                // Gérez les erreurs JSON ici si nécessaire
            }

            // Fermer la boîte de dialogue
            dialog.dismiss();
        });

        // Ajouter un bouton "Annuler" pour annuler l'ajout
        builder.setNegativeButton("Annuler", (dialog, which) -> {
            // Fermer la boîte de dialogue sans ajouter de données
            dialog.dismiss();
        });

        // Afficher la boîte de dialogue
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}