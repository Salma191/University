package ma.ensa.myapplication.ui.slideshow;

import java.util.List;

import ma.ensa.myapplication.ui.gallery.Role;
import ma.ensa.myapplication.ui.home.Filiere;

public class Student {
    private String firtName;
    private String lastName;
    private String telephone;
    private List<Role> roles;
    private Filiere filiere;

    public Student(String firtName, String lastName, String telephone, List<Role> roles,Filiere filiere) {
        this.firtName = firtName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.roles = roles;
        this.filiere = filiere;
    }

    public Student(String firtName, String lastName, String telephone) {
        this.firtName = firtName;
        this.lastName = lastName;
        this.telephone = telephone;
    }

    public String getFirtName() {
        return firtName;
    }

    public void setFirtName(String firtName) {
        this.firtName = firtName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    @Override
    public String toString() {
        return "\nFirst Name: " + firtName + "\n\nLast Name: " + lastName + "\n\nTelephone: " + telephone +"\n\nFiliere: " + filiere.getName() + "\n\nRoles: " + roles + "\n";
    }
}
