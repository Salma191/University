package ma.ensa.myapplication.ui.slideshow;

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

import ma.ensa.myapplication.ui.gallery.Role;
import ma.ensa.myapplication.ui.home.Filiere;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<List<Student>> data;
    private MutableLiveData<List<Role>> rolesData;
    private MutableLiveData<List<Filiere>> filieresData;


    public SlideshowViewModel() {
        data = new MutableLiveData<>();
        rolesData = new MutableLiveData<>();
        filieresData = new MutableLiveData<>();
    }


    public void fetchDataFromAPI(Context context) {
        String apiUrl = "http://192.168.1.21:8082/api/v1/students";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Student> fetchedData = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONArray rolesArray = response.getJSONObject(i).getJSONArray("roles");
                                JSONObject studentObject = response.getJSONObject(i);


                                List<Role> roles = new ArrayList<>();
                                for (int j = 0; j < rolesArray.length(); j++) {
                                    JSONObject roleObject = rolesArray.getJSONObject(j);
                                    String roleName = roleObject.getString("name");
                                    Role role = new Role(roleName);
                                    roles.add(role);
                                }

                                String firstName = response.getJSONObject(i).getString("firstName");
                                String lastName = response.getJSONObject(i).getString("lastName");
                                String telephone = response.getJSONObject(i).getString("telephone");

                                JSONObject filiereObject = studentObject.getJSONObject("filiere");
                                String filiereCode = filiereObject.getString("code");
                                String filiereName = filiereObject.getString("name");
                                Filiere filiere = new Filiere(filiereCode, filiereName);

                                Student student = new Student(firstName, lastName, telephone, roles, filiere);
                                fetchedData.add(student);
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
    public void fetchRolesFromAPI(Context context) {
        String apiUrl = "http://192.168.1.21:8082/api/v1/roles";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Role> fetchedRoles = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject roleObject = response.getJSONObject(i);
                                String roleName = roleObject.getString("name");
                                Role role = new Role(roleName);
                                fetchedRoles.add(role);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONException", "Error parsing JSON for roles: " + e.getMessage());
                                // Gérez l'exception ici, par exemple, ignorer l'élément en cours ou afficher un message d'erreur.
                            }
                        }
                        rolesData.setValue(fetchedRoles);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("VolleyError", "Error fetching roles data: " + error.getMessage());
                        } else {
                            Log.e("VolleyError", "Error fetching roles data: Response is null");
                        }
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void fetchFilieresFromAPI(Context context) {
        String apiUrl = "http://192.168.1.21:8082/api/v1/filieres";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Filiere> fetchedFilieres = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject filiereObject = response.getJSONObject(i);
                                String filiereCode = filiereObject.getString("code");
                                String filiereName = filiereObject.getString("name");
                                Filiere filiere = new Filiere(filiereCode, filiereName);
                                fetchedFilieres.add(filiere);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONException", "Error parsing JSON for filieres: " + e.getMessage());
                                // Gérez l'exception ici, par exemple, ignorer l'élément en cours ou afficher un message d'erreur.
                            }
                        }
                        filieresData.setValue(fetchedFilieres);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("VolleyError", "Error fetching filieres data: " + error.getMessage());
                        } else {
                            Log.e("VolleyError", "Error fetching filieres data: Response is null");
                        }
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    public void addNewStudent(String firstName, String lastName,String telephone, List<Role> roles, Filiere filiere, Context context) throws JSONException {
        String apiUrl = "http://192.168.1.21:8082/api/v1/students";

        // Créez une demande POST pour ajouter une nouvelle filière
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                new JSONObject() {{
                    put("firstName", firstName);
                    put("lastName", lastName);
                    put("telephone", telephone);
                    put("roles", roles);
                    put("filiere", filiere);
                }},
                response -> {
                    // La filière a été ajoutée avec succès à l'API
                    // Vous pouvez mettre à jour votre liste locale si nécessaire
                },
                error -> {
                    // Gérez les erreurs de la demande
                }
        );

        Student newStudent = new Student(firstName, lastName, telephone, roles, filiere);
        List<Student> currentData = data.getValue();
        currentData.add(newStudent);
        data.setValue(currentData); // Notifie les observateurs du changement de données
        // Ajoutez la demande à la file d'attente de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    public LiveData<List<Student>> getData() {
        return data;
    }
    public LiveData<List<Role>> getRoles() {
        return rolesData;
    }

    public LiveData<List<Filiere>> getFilieres() {
        return filieresData;
    }

}