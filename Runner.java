import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.activation.ActivationID;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);

        welcome(frame);
        File file = chooseFile(frame);
        FileParser fileParser = new FileParser(file);
        fileParser.parse_data();
        List<String> parsed_data = fileParser.getParsed_data();
        List<Activity> activities = make_activities(parsed_data);
        List<RCGroup> rcGroups = get_students(parsed_data);

        JScrollPane studentPanel = makeStudentList(frame, rcGroups);
        JScrollPane activityPanel = makeActivityList(frame, activities);
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        container.add(studentPanel);
        container.add(activityPanel);
        JButton sort = new JButton("Sort students");
        JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "1. Download the excel file as a .csv off the google drive. \n" +
                        "2. Choose the file using the file chooser menu. (Choose the file that is a csv.)\n" +
                        "3. Double click on the RC Groups that are going to have Priority and RC Group Nights. (Use the buttons so it is reflected correctly at the top)" +
                        "4. Close out all the windows except the one with all the RC Groups, hit sort. Wait until " +
                        "the program is done then exit out. The file should be named Activities.docx. Format it, to your liking and print it.", "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Activity> sortedAct = sortStudents(activities, rcGroups);
                FileWriter fileWriter = new FileWriter();

                try {
                    fileWriter.makeRoster(sortedAct);
                } catch (Exception f ) {
                    System.out.print("An error occured, YIKES");
                    f.printStackTrace();
                }
            }
        });
        container.add(sort);
        container.add(instructions);
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
    }
    public static JScrollPane makeActivityList(JFrame frame, List<Activity> activities) {
        ListModel listModel = new DefaultListModel<>();
        for (Activity a : activities) {
            ((DefaultListModel) listModel).addElement(a.getName());
        }
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(list);
        return listScroller;
    }
    public static JScrollPane makeStudentList(JFrame frame, List<RCGroup> rcGroups) {
        ListModel listModel = new DefaultListModel<>();
        for (RCGroup r : rcGroups) {
            if (r.isPriority()) {
                ((DefaultListModel) listModel).addElement("RC:" + r.getRC() + " (Priority)");
            } else {
                ((DefaultListModel) listModel).addElement("RC:" + r.getRC());
            }

        }
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    RCGroup(rcGroups.get(list.getSelectedIndex()));
                }
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        return listScroller;

    }
    public static void RCGroup(RCGroup rcGroup) {
        JFrame newFrame = new JFrame();
        newFrame.setSize(300, 300);
        List<Student> students = rcGroup.getStudents();
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        ListModel listModel = new DefaultListModel();
        ((DefaultListModel) listModel).addElement("----THIS GROUP HAS PRIORITY: " + rcGroup.isPriority() + "----");
        ((DefaultListModel) listModel).addElement("----THIS GROUP HAS GROUP NIGHT: " + rcGroup.isGroupNight() + "----");
        for (Student s : students) {
            ((DefaultListModel) listModel).addElement(s.get_name());
        }
        JList list = new JList(listModel);
        JScrollPane listScroller = new JScrollPane(list);
        container.add(listScroller);
        JButton changeGN = new JButton("Change Group Night");
        changeGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rcGroup.changeGroupNight();
                ((DefaultListModel) listModel).setElementAt("----THIS GROUP HAS GROUP NIGHT: " + rcGroup.isGroupNight() + "----",1);
            }
        });
        JButton makePriority = new JButton("Change Priority");
        makePriority.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rcGroup.changePriority();
                ((DefaultListModel) listModel).setElementAt("----THIS GROUP HAS PRIORITY: " + rcGroup.isPriority()
                        + "----", 0);
            }
        });
        container.add(makePriority);
        container.add(changeGN);
        newFrame.add(container);
        newFrame.setVisible(true);


    }
    public static File chooseFile(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }

    }
    public static void welcome(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "This is the evening activity sorter. You will determine which kids" +
                " have priority and it will sort them first, given their choices.\n Within the kids who" +
                " have priority they will be randomly chosen." + "\n\n------INSTRUCTIONS-------\n1. Download the excel file as a .csv off the google drive. \n" +
                "2. Choose the file using the file chooser menu. (Choose the file that is a csv.)\n" +
                "3. Double click on the RC Groups that are going to have Priority (You can ignore the ones that" +
                " are having RC Group nights they will not be added to the activity lists.\n" +
                "4. Close out all the windows except the one with all the RC Groups, hit sort. Wait until " +
                "the program is done then exit out. The file should be named Activities.docx. Format it, to your liking and print it.", "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }
    public static List<Activity> sortStudents(List<Activity> activities, List<RCGroup> rcGroups) {
        List<RCGroup> priority = new ArrayList<>();
        List<RCGroup> notPriority = new ArrayList<>();

        for (int i = 0; i < rcGroups.size(); i++) {
            if (rcGroups.get(i).isPriority() && !rcGroups.get(i).isGroupNight()) {
                priority.add(rcGroups.get(i));
            } else if (!rcGroups.get(i).isPriority() && !rcGroups.get(i).isGroupNight()) {
                notPriority.add(rcGroups.get(i));
            }
        }
        Random rand = new Random();
        while (priority.size() > 0) {
            int randnum = rand.nextInt(priority.size());
            RCGroup rcGroup = priority.get(randnum);
            for (int i = 0; i < rcGroup.getStudents().size(); i++) {
                Student student = rcGroup.getStudents().get((i + randnum) % rcGroup.getStudents().size());
                String[] choiceNames = {student.getChoice1(),student.getChoice2(), student.getChoice3()};
                boolean placed = false;
                for (String name: choiceNames) {
                    for (Activity a : activities) {
                        if (name.equals(a.getName()) && a.open() && !placed) {
                            a.addStudent(student);
                            placed = true;
                        }
                    }
                }
                if (!placed) {
                    for (Activity a : activities) {
                        if (a.open() && !placed) {
                            a.addStudent(student);
                            placed = true;
                        }
                    }
                }
            }
            priority.remove(rcGroup);
        }
        while (notPriority.size() > 0) {
            int randnum = rand.nextInt(notPriority.size());
            RCGroup rcGroup = notPriority.get(randnum);
            for (int i = 0; i < rcGroup.getStudents().size(); i++) {
                Student student = rcGroup.getStudents().get((i + randnum) % rcGroup.getStudents().size());
                String[] choiceNames = {student.getChoice1(),student.getChoice2(), student.getChoice3()};
                boolean placed = false;
                for (String name: choiceNames) {
                    for (Activity a : activities) {
                        if (name.equals(a.getName()) && a.open() && !placed) {
                            a.addStudent(student);
                            placed = true;
                        }
                    }
                }
                if (!placed) {
                    for (Activity a : activities) {
                        if (a.open() && !placed) {
                            a.addStudent(student);
                            placed = true;
                        }
                    }
                }
            }
            notPriority.remove(rcGroup);
        }
        return activities;
    }
    public static List<Activity> make_activities(List<String> parsed_data) {
        String[] ActivityNames = parsed_data.get(0).split(",");
        String[] ActivityMeetingPlaces = parsed_data.get(1).split(",");
        String[] ActivityCapacities = parsed_data.get(2).split(",");
        List<Activity> activities = new ArrayList<>();

        for (int i = 1; i < ActivityNames.length; i++) {
            Activity activity = new Activity(ActivityNames[i], ActivityMeetingPlaces[i],
                    Integer.parseInt(ActivityCapacities[i]));
            activities.add(activity);
        }

        return activities;
    }

    public static List<RCGroup> get_students(List<String> parsed_data) {
        List<RCGroup> rcGroups = new ArrayList<>();
        RCGroup rcGroup = new RCGroup("", new ArrayList<>(), false, false);
        for (int i = 5; i < parsed_data.size(); i++) {
            String[] StudentInfo = parsed_data.get(i).split(",");
            if (StudentInfo.length > 2 && StudentInfo[2].equals("RC")) {
                rcGroup = new RCGroup(StudentInfo[1], new ArrayList<>(), false, false);
                rcGroups.add(rcGroup);
            } else if (StudentInfo.length > 4) {
                if (!StudentInfo[2].equals("RC") && !StudentInfo[3].equals("")) {
                    Student student = new Student(StudentInfo[1],StudentInfo[0], StudentInfo[2], StudentInfo[3],
                            StudentInfo[4]);
                    rcGroup.addStudent(student);
                }
            }
        }
        return rcGroups;
    }
}
