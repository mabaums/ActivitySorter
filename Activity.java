import java.util.ArrayList;
import java.util.List;

public class Activity {

    private String name;
    private String meeting_spot;
    private int cap;
    private List<Student> students = new ArrayList<>();

    public Activity(String name, String meeting_spot, int cap) {
        this.name = name;
        this.meeting_spot = meeting_spot;
        this.cap = cap;
    }
    public String getMeeting_spot() {
        return this.meeting_spot;
    }
    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean open() {
        return students.size() < this.cap;
    }
    public String getName() {
        return name;
    }
    public List<Student> getStudents() {
        return students;
    }
    @Override
    public String toString() {
        return String.format("%s meeting at %s with capacity %d", name, meeting_spot, cap);
    }
}