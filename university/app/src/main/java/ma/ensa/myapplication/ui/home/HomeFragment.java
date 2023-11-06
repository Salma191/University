package ma.ensa.myapplication.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;

import ma.ensa.myapplication.R;
import ma.ensa.myapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ListView listView;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.listView);
        button = root.findViewById(R.id.button);

        homeViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            ArrayAdapter<Filiere> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);
        });
        homeViewModel.fetchDataFromAPI(requireContext());


        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            // Récupérer l'élément sélectionné dans la liste
            Filiere selectedFiliere = (Filiere) adapterView.getItemAtPosition(position);

            // Créer une boîte de dialogue pour afficher les détails de l'élément sélectionné
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Détails de la Filiere");
            builder.setMessage("Code: " + selectedFiliere.getCode() + "\nName: " + selectedFiliere.getName());

            // Ajouter un bouton "Modifier"
            builder.setPositiveButton("Modifier", (dialog, which) -> {
                // Code à exécuter lorsque le bouton "Modifier" est cliqué
                // Ouvrir une nouvelle boîte de dialogue pour permettre à l'utilisateur de saisir les nouveaux détails de la filière

                // Créer une boîte de dialogue AlertDialog pour la modification
                AlertDialog.Builder modifyDialogBuilder = new AlertDialog.Builder(requireContext());
                modifyDialogBuilder.setTitle("Modifier la filière");

                // Créer un layout pour la boîte de dialogue (peut être un XML personnalisé)
                View modifyDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.filiere_add, null);
                modifyDialogBuilder.setView(modifyDialogView);

                // Ajouter des champs de saisie pour les nouvelles données
                EditText newCodeInput = modifyDialogView.findViewById(R.id.code);
                EditText newNameInput = modifyDialogView.findViewById(R.id.name);

                // Remplir les champs de saisie avec les détails de la filière actuelle (selectedFiliere)
                newCodeInput.setText(selectedFiliere.getCode());
                newNameInput.setText(selectedFiliere.getName());

                // Ajouter un bouton "Modifier" pour valider les modifications
                modifyDialogBuilder.setPositiveButton("Modifier", (modifyDialog, modifyWhich) -> {
                    String newCode = newCodeInput.getText().toString();
                    String newName = newNameInput.getText().toString();

                    Log.d("DEBUG", "Nouveau code : " + newCode); // Message de débogage
                    Log.d("DEBUG", "Nouveau nom : " + newName); // Message de débogage

                    // Appeler la méthode pour mettre à jour la filière en utilisant les nouvelles informations
                    try {
                        homeViewModel.updateFiliere(selectedFiliere.getId(), newCode, newName, requireContext());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // Fermer la boîte de dialogue de modification
                    modifyDialog.dismiss();
                });

                // Ajouter un bouton "Annuler" pour annuler la modification
                modifyDialogBuilder.setNegativeButton("Annuler", (modifyDialog, modifyWhich) -> {
                    // Fermer la boîte de dialogue de modification sans apporter de modifications
                    modifyDialog.dismiss();
                });

                // Afficher la boîte de dialogue de modification
                modifyDialogBuilder.show();

                // Fermer la boîte de dialogue de détails
                dialog.dismiss();
            });


            // Ajouter un bouton "Supprimer"
            builder.setNeutralButton("Supprimer", (dialog, which) -> {
                int filiereIdToDelete = selectedFiliere.getId();
                Log.d("DEBUG", "ID de la filière à supprimer : " + filiereIdToDelete);

                // Demander une confirmation de suppression (facultatif)
                new AlertDialog.Builder(requireContext())
                        .setTitle("Confirmation de suppression")
                        .setMessage("Êtes-vous sûr de vouloir supprimer cette filière ?")
                        .setPositiveButton("Oui", (confirmationDialog, confirmationWhich) -> {
                            Log.d("DEBUG", "ID confirmé pour la suppression : " + filiereIdToDelete);
                            // Appeler la méthode deleteFiliere pour supprimer la filière par son ID
                            homeViewModel.deleteFiliere(filiereIdToDelete, requireContext());
                            // Fermer la boîte de dialogue de confirmation
                            confirmationDialog.dismiss();
                            // Fermer la boîte de dialogue de détails
                            dialog.dismiss();
                        })
                        .setNegativeButton("Annuler", (confirmationDialog, confirmationWhich) -> {
                            // Ne rien faire, annuler la suppression
                            confirmationDialog.dismiss();
                        })
                        .show();
            });


//            // Ajouter un bouton "Fermer" pour fermer la boîte de dialogue
//            builder.setPositiveButton("Fermer", (dialog, which) -> {
//                dialog.dismiss();
//            });

            // Afficher la boîte de dialogue
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

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
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.filiere_add, null);
        builder.setView(dialogView);

        // Ajouter des champs de saisie pour les données
        EditText dataInput1 = dialogView.findViewById(R.id.code);
        EditText dataInput2 = dialogView.findViewById(R.id.name);

        // Ajouter un bouton "Ajouter" pour valider les données
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String newData1 = dataInput1.getText().toString();
            String newData2 = dataInput2.getText().toString();

            // Appelez la méthode pour ajouter la nouvelle filière à l'API
            try {
                homeViewModel.addNewFiliere(newData1,newData2,requireContext());
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