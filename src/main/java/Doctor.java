
public class Doctor extends Person {

    private String specialization;
    private boolean available;

    public Doctor(int id, String name, int age, String phone,
                  String specialization, boolean available) {
        super(id, name, age, phone);
        this.specialization = specialization;
        this.available = available;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Specialization: " + specialization);
        System.out.println("Available: " + (available ? "Yes" : "No"));
    }
}
