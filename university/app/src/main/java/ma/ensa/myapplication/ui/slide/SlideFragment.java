package ma.ensa.myapplication.ui.slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.myapplication.R;
import ma.ensa.myapplication.ui.slide.SlideViewModel; // Import your SlideViewModel class
import ma.ensa.myapplication.ui.slideshow.Student;

public class SlideFragment extends Fragment {
    private SlideViewModel viewModel; // Reference to your ViewModel

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide, container, false);

        // Find the Spinner and Button by their IDs
        Spinner spinner = rootView.findViewById(R.id.studByFil);
        Button searchButton = rootView.findViewById(R.id.button2);

        // Create an ArrayAdapter and populate the Spinner with Filiere names
        int filiereNames = 0;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, filiereNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Set an OnClickListener for the "Search" Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected Filiere from the Spinner
                String selectedFiliere = spinner.getSelectedItem().toString();

                // Filter the list of students based on the selected Filiere
                List<Student> filteredStudents = filterStudentsByFiliere(selectedFiliere);

                // Update your ViewModel with the filtered student list
                viewModel.updateData(filteredStudents);
            }
        });

        return rootView;
    }

    private List<Student> filterStudentsByFiliere(String selectedFiliere) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : viewModel.getData().getValue()) { // Access data from the ViewModel
            if (student.getFiliere().equals(selectedFiliere)) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }
}
