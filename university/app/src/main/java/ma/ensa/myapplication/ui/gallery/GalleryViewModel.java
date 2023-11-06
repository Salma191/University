package ma.ensa.myapplication.ui.gallery;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GalleryViewModel extends ViewModel {

    private MutableLiveData<List<Role>> data;

    public GalleryViewModel() {
        data = new MutableLiveData<>();
    }

    public void fetchDataFromAPI(Context context) {
        String apiUrl = "http://192.168.1.21:8082/api/v1/roles";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Role> fetchedData = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String name = response.getJSONObject(i).getString("name");
                                Role role = new Role(name);
                                fetchedData.add(role);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONException", "Error parsing JSON: " + e.getMessage());
                                // Vous pouvez gérer l'exception ici, par exemple, ignorer l'élément en cours ou afficher un message d'erreur.
                            }
                        }
                        data.setValue(fetchedData);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("VolleyError", "Error fetching data: " + error.getMessage());
                        } else {
                            Log.e("VolleyError", "Error fetching data: Response is null");
                        }
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void addNewRole( String name, Context context) throws JSONException {
        String apiUrl = "http://192.168.1.21:8082/api/v1/roles";

        // Créez une demande POST pour ajouter une nouvelle filière
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                new JSONObject() {{
                    put("name", name);
                }},
                response -> {
                    // La filière a été ajoutée avec succès à l'API
                    // Vous pouvez mettre à jour votre liste locale si nécessaire
                },
                error -> {
                    // Gérez les erreurs de la demande
                }
        );

        Role newRole = new Role(name);
        List<Role> currentData = data.getValue();
        currentData.add(newRole);
        data.setValue(currentData); // Notifie les observateurs du changement de données
        // Ajoutez la demande à la file d'attente de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    public void updateRole(int id, String newName, Context context) throws JSONException {
        String apiUrl = "http://192.168.1.21:8082/api/v1/roles/"+id;

        // Créez une demande PUT pour mettre à jour la filière par ID
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                apiUrl,
                new JSONObject() {{
                    put("name", newName);
                }},
                response -> {
                    // La filière a été mise à jour avec succès dans l'API
                    // Mettez à jour votre liste locale si nécessaire
                    updateLocalData(id, newName);
                },
                error -> {
                    // Gérez les erreurs de la demande
                }
        );

        // Ajoutez la demande à la file d'attente de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void updateLocalData(int id, String newName) {
        List<Role> currentData = data.getValue();

        // Recherchez la filière à mettre à jour dans la liste locale en fonction de l'ID
        for (Role role : currentData) {
            if (role.getId() == id) {
                role.setName(newName);
                break; // Sortez de la boucle une fois la filière mise à jour
            }
        }

        data.setValue(currentData); // Mettez à jour la liste avec les données mises à jour
    }


    public void deleteRole(int id, Context context) {
        String apiUrl = "http://192.168.1.21:8082/api/v1/roles/"+id;

        // Créez une demande DELETE pour supprimer le role par ID
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                apiUrl,
                null,
                response -> {
                    // La filière a été supprimée avec succès de l'API
                    // Vous pouvez mettre à jour votre liste locale si nécessaire
                },
                error -> {
                    // Gérez les erreurs de la demande
                }
        );

        List<Role> currentData = data.getValue();

        // Créez une copie de la liste pour éviter de modifier la liste pendant la boucle
        List<Role> updatedData = new ArrayList<>(currentData);

        // Recherchez et supprimez la filière de la liste locale en fonction de l'ID
        for (Role role : updatedData) {
            Log.d("DEBUG", "ID à supprimer : " + id);
            Log.d("DEBUG", "ID de la filière actuelle : " + role.getId());
            if (role.getId() == id) {
                Log.d("DEBUG", "Suppression de la filière avec ID : " + role.getId());
                updatedData.remove(role);
                break; // Sortez de la boucle une fois la filière supprimée
            }
        }

        data.setValue(updatedData); // Mettez à jour la liste avec la nouvelle liste sans l'élément supprimé

        // Ajoutez la demande à la file d'attente de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    public LiveData<List<Role>> getData() {
        return data;
    }
}
