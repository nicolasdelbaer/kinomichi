package be.nidel.kinomichi;

public class Participant {

    private String firstName;
    private String lastName;
    private String phone = "n/a";
    private String email = "n/a";
    private String clubName = "n/a";
    private ParticipantType type = ParticipantType.Attendee;

    private Participant() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public ParticipantType getType() {
        return type;
    }

    public void setType(ParticipantType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Participant{" +
                firstName + ", " +
                lastName + ", " +
                phone + ", " +
                email + ", " +
                clubName + ", " +
                type +
                '}';
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public static class Builder{
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private String clubName;
        private ParticipantType type;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setClubName(String clubName) {
            this.clubName = clubName;
            return this;
        }

        public Builder setType(ParticipantType type) {
            this.type = type;
            return this;
        }

        public Participant build(){
            Participant p = new Participant();
            p.firstName = firstName;
            p.lastName = lastName;
            p.phone = phone;
            p.email = email;
            p.clubName = clubName;
            p.type = type;
            return p;
        }
    }
}
