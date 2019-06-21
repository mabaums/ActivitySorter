import java.util.List;
import java.util.ArrayList;

public class RCGroup {
    private String RC;
    private List<Student> students = new ArrayList<>();
    private boolean Priority;
    private boolean groupNight;

    public RCGroup(String rc, List<Student> students, boolean Priority, boolean groupNight) {
        this.RC = rc;
        this.students = students;
        this.Priority = Priority;
        this.groupNight = groupNight;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }
    public boolean isGroupNight() {
        return groupNight;
    }
    public void removeStudent(Student student) {
        this.students.remove(student);
    }
    public void changeGroupNight() {
        this.groupNight = !groupNight;
    }
    public List<Student> getStudents() {
        return this.students;
    }

    public boolean isPriority() {
        return this.Priority;
    }
    public void givePriority() {
        this.Priority = true;
    }
    public void removePriority() {
        this.Priority = false;
    }
    public void changePriority() {
        this.Priority = !Priority;
    }
    public String getRC(){
        return this.RC;
    }

}
