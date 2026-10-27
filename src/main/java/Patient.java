
public class Patient extends Person {

    private String gender;
    private String address;
    private String bloodGroup;

    // Constructor
    public Patient(
            int id,
            String name,
            int age,
            String phone,
            String gender,
            String address,
            String bloodGroup) {

        super(id, name, age, phone);

        this.gender = gender;
        this.address = address;
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Gender: " + gender);
        System.out.println("Address: " + address);
        System.out.println("Blood Group: " + bloodGroup);
    }
}
   