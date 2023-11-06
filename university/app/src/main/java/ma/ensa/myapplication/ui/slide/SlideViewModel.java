package ma.ensa.myapplication.ui.slide;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


import ma.ensa.myapplication.ui.home.Filiere;
import ma.ensa.myapplication.ui.slideshow.Student;

public class SlideViewModel extends ViewModel {
    private MutableLiveData<List<Student>> data;
    private MutableLiveData<List<Filiere>> filieresData;

    public SlideViewModel() {
        data = new MutableLiveData<>();
        filieresData = new MutableLiveData<>();
    }


    public void updateData(List<Student> newData) {
        data.setValue(newData);
    }

    public void updateFilieresData(List<Filiere> newFilieresData) {
        filieresData.setValue(newFilieresData);
    }

    public MutableLiveData<List<Student>> getData() {
        return data;
    }

    public MutableLiveData<List<Filiere>> getFilieresData() {
        return filieresData;
    }

}
