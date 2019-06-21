public class Student {
    private String firstname;
    private String lastname;
    private String choice1;
    private String choice2;
    private String choice3;
    private String givenChoice;
    private boolean priority;

    public Student(String firstname, String lastname, String c1, String c2, String c3) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.choice1 = c1;
        this.choice2 = c2;
        this.choice3 = c3;
        this.priority = priority;
    }

    public boolean isPriority() {
        return priority;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }
    public String get_name() {
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", firstname, lastname);
    }
}
