import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ph1_gui extends Application {
	private TableView<Student> student_TV;
	private TableView<Major> major_TV;
	private SLL student_LL = new SLL();
	private MLL major_LL = new MLL();
	private int index = 0;
	ComboBox<String> std_Mjr_CBX = new ComboBox<>();
	private List<Student> allStudents;
	private int total_rejected = 0;
	private int lowGrades_Rej = 0;
	private int totalRej_File = 0;
	private int Major_NotFound = 0;
	private int Low_Grades = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		allStudents = new ArrayList<>();

		primaryStage.setTitle("Student Admission System");

		student_TV = new TableView<>();
		setupStudentTable();
		major_TV = new TableView<>();
		setupMajorTable();

		std_Mjr_CBX.getItems().addAll("Student", "Major");
		std_Mjr_CBX.setValue("Student");

		Button insertB = new Button("Insert");
		insertB.setMinWidth(100);
		insertB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");

		insertB.setOnAction(e -> insert(std_Mjr_CBX.getValue()));

		Button deleteB = new Button("Delete");
		deleteB.setMinWidth(100);
		deleteB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		deleteB.setOnAction(e -> delete(std_Mjr_CBX.getValue()));

		Button updateB = new Button("Update");
		updateB.setMinWidth(100);
		updateB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		updateB.setOnAction(e -> update(std_Mjr_CBX.getValue()));

		Button searchB = new Button("Search");
		searchB.setMinWidth(100);
		searchB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		searchB.setOnAction(e -> search(std_Mjr_CBX.getValue()));

		Button statsB = new Button("Show Statistics");
		statsB.setMinWidth(100);
		statsB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		statsB.setOnAction(e -> stats());

		Button prevB = new Button("Previous");
		prevB.setMinWidth(100);
		prevB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		prevB.setOnAction(e -> go_prev());

		Button nextB = new Button("Next");
		nextB.setMinWidth(100);
		nextB.setStyle(
				"-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
		nextB.setOnAction(e -> go_next());

		HBox buttons_hbox = new HBox(10, prevB, nextB, insertB, deleteB, updateB, searchB, statsB);
		buttons_hbox.setAlignment(Pos.CENTER);
		buttons_hbox.setPadding(new Insets(10));

		VBox layout = new VBox(10, new Label("Select Entity:"), std_Mjr_CBX, new Label("Students Table"), student_TV,
				new Label("Majors Table"), major_TV, buttons_hbox);
		layout.setPadding(new Insets(20));

		layout.setStyle("-fx-background-color: #ffffff; " + "-fx-border-color: #cccccc; " + "-fx-border-width: 2;");

		MenuBar menuBar = new MenuBar();
		Menu fileM = new Menu("File");

		MenuItem loadStd = new MenuItem("Load Students");
		loadStd.setOnAction(e -> load_S_File(primaryStage));

		MenuItem loadMjr = new MenuItem("Load Majors");
		loadMjr.setOnAction(e -> load_Mjr_File(primaryStage));

		MenuItem save = new MenuItem("Save Data");
		save.setOnAction(e -> save_toFile());

		fileM.getItems().addAll(loadStd, loadMjr, save);
		menuBar.getMenus().add(fileM);

		BorderPane mainLayout = new BorderPane();
		mainLayout.setTop(menuBar);
		mainLayout.setCenter(layout);

		Scene mainScene = new Scene(mainLayout, 900, 600);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	private void setupStudentTable() {
		TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSid()));

		TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

		TableColumn<Student, Double> tawjihiColumn = new TableColumn<>("Tawjihi Grade");
		tawjihiColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getT_grade()));

		TableColumn<Student, Double> placementTestColumn = new TableColumn<>("Placement Test Grade");
		placementTestColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPt_grade()));

		TableColumn<Student, Double> admissionMarkColumn = new TableColumn<>("Admission Mark");
		admissionMarkColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAdm_mark()));
		student_TV.getColumns().addAll(idColumn, nameColumn, tawjihiColumn, placementTestColumn, admissionMarkColumn);
	}

	private void setupMajorTable() {
		TableColumn<Major, String> nameColumn = new TableColumn<>("Major Name");
		nameColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

		TableColumn<Major, Double> acceptanceGradeColumn = new TableColumn<>("Acceptance Grade");
		acceptanceGradeColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAcptGrade()));

		TableColumn<Major, Double> tawjihiWeightColumn = new TableColumn<>("Tawjihi Weight");
		tawjihiWeightColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTawjihiWeight()));

		TableColumn<Major, Double> ptWeightColumn = new TableColumn<>("Placement Test Weight");
		ptWeightColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPtWeight()));
		major_TV.getColumns().addAll(nameColumn, acceptanceGradeColumn, tawjihiWeightColumn, ptWeightColumn);
	}

	private void go_next() {
		String sel_choice = std_Mjr_CBX.getValue();

		if (sel_choice.equals("Major")) {
			index = (index + 1) % major_TV.getItems().size();
			major_TV.getSelectionModel().select(index);
			major_TV.scrollTo(index);

			Major sel_Major = major_TV.getSelectionModel().getSelectedItem();
			if (sel_Major != null) {
				Accepted_Stds(sel_Major);
				major_TV.refresh();
			}
		} else if (sel_choice.equals("Student") && !student_TV.getItems().isEmpty()) {
			student_TV.getItems().setAll(allStudents);
			index = (index + 1) % student_TV.getItems().size();
			student_TV.getSelectionModel().select(index);
			student_TV.scrollTo(index);
			student_TV.refresh();
		}
	}

	private void go_prev() {
		String sel_choice = std_Mjr_CBX.getValue();
		if (sel_choice.equals("Major")) {
			index = (index - 1 + major_TV.getItems().size()) % major_TV.getItems().size();
			major_TV.getSelectionModel().select(index);
			major_TV.scrollTo(index);

			Major sel_Major = major_TV.getSelectionModel().getSelectedItem();
			if (sel_Major != null) {
				Accepted_Stds(sel_Major);
			}
		} else if (sel_choice.equals("Student")) {
			student_TV.getItems().setAll(allStudents);
		}
	}

	private void Accepted_Stds(Major major) {
		if (allStudents.isEmpty()) {
			showAlert(AlertType.ERROR, "Data Error", "Student data is missing.");
			return;
		}

		List<Student> acceptedStudents = new ArrayList<>();
		for (Student student : allStudents) {
			if (student.getSt_major().trim().equalsIgnoreCase(major.getName().trim())
					&& student.getAdm_mark() >= major.getAcptGrade()) {
				acceptedStudents.add(student);
			}
		}

		if (acceptedStudents.isEmpty()) {
			showAlert(AlertType.INFORMATION, "Accepted Students", "No students accepted for: " + major.getName());
			student_TV.getItems().clear();
		} else {
			student_TV.getItems().setAll(acceptedStudents);
		}
		student_TV.refresh();
	}

	private void load_S_File(Stage primaryStage) {
		FileChooser fch = new FileChooser();
		fch.setTitle("Open Students File");
		fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File file = fch.showOpenDialog(primaryStage);

		if (file != null) {
			int rej_Count = 0;
			int mnf_Count = 0;
			int low_G_Count = 0;

			int[] lowGrade_Rej = new int[10];
			String[] m_Names = new String[10];

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				int line_Num = 1;

				line = br.readLine();

				while ((line = br.readLine()) != null) {
					line_Num++;
					String[] data = line.split("\\|");

					if (data.length != 5) {
						showAlert(AlertType.ERROR, "Load Students",
								"Incorrect format on line " + line_Num + ": " + line);
						continue;
					}
					try {
						int id = Integer.parseInt(data[0].trim());
						if (student_LL.search(id) != null) {
							showAlert(AlertType.ERROR, "Load Students",
									"Duplicate Student ID on line " + line_Num + ": " + id);
							continue;
						}

						String name = data[1].trim();
						if (name.isEmpty()) {
							showAlert(AlertType.ERROR, "Load Students",
									"Missing name for Student ID on line " + line_Num + ": " + id);
							continue;
						}

						double tawjihi = Double.parseDouble(data[2].trim());
						double placement = Double.parseDouble(data[3].trim());
						if (tawjihi < 0 || tawjihi > 100 || placement < 0 || placement > 100) {
							showAlert(AlertType.ERROR, "Load Students",
									"Grades out of bounds on line " + line_Num + " for Student ID: " + id);
							continue;
						}

						String majorName = data[4].trim();
						Major major = major_LL.search(majorName);
						if (major == null) {
							mnf_Count++;
							rej_Count++;
							continue;
						}

						double adm_Mark = (tawjihi * major.getTawjihiWeight()) + (placement * major.getPtWeight());
						if (adm_Mark < major.getAcptGrade()) {
							low_G_Count++;
							rej_Count++;
							for (int i = 0; i < m_Names.length; i++) {
								if (m_Names[i] == null) {
									m_Names[i] = majorName;
									lowGrade_Rej[i] = 1;
									break;
								} else if (m_Names[i].equals(majorName)) {
									lowGrade_Rej[i]++;
									break;
								}
							}
							continue;
						}
						Student student = new Student(id, name, tawjihi, placement, majorName);
						student.setAdm_mark(adm_Mark);
						student_LL.insert(student);
						student_TV.getItems().add(student);
						allStudents.add(student);
						major.addStudent(student);

					} catch (NumberFormatException e) {
						showAlert(AlertType.ERROR, "Load Students",
								"Invalid number format on line " + line_Num + ": " + line);
					}
				}
				totalRej_File += rej_Count;
				Major_NotFound += mnf_Count;
				Low_Grades += low_G_Count;
				StringBuilder low_Grades = new StringBuilder();
				for (int i = 0; i < m_Names.length; i++) {
					if (m_Names[i] != null) {
						low_Grades.append(
								String.format("%s: %d rejected due to low grades\n", m_Names[i], lowGrade_Rej[i]));
					}
				}

				showAlert(AlertType.INFORMATION, "Load Students", String.format(
						"Total Rejected: %d\n - Major Not Found: %d\n - Low Grades: %d\n\nLow Grade Rejections by Major:\n%s",
						rej_Count, mnf_Count, low_G_Count, low_Grades.toString()));

			} catch (IOException e) {
				showAlert(AlertType.ERROR, "Load Students", "Error reading student data.");
			}
		}
	}

	private void load_Mjr_File(Stage primaryStage) {
		FileChooser fch = new FileChooser();
		fch.setTitle("Open Majors File");
		fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File file = fch.showOpenDialog(primaryStage);

		if (file != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				int lineNumber = 1;
				line = reader.readLine();

				while ((line = reader.readLine()) != null) {
					lineNumber++;

					String[] data = line.split("\\|");

					if (data.length != 4) {
						showAlert(AlertType.ERROR, "Load Majors",
								"Incorrect format on line " + lineNumber + ": " + line);
						continue;
					}

					try {
						double tawjihiWeight = Double.parseDouble(data[0].trim());
						double ptWeight = Double.parseDouble(data[1].trim());
						double acptGrade = Double.parseDouble(data[2].trim());

						String name = data[3].trim();
						if (name.isEmpty()) {
							showAlert(AlertType.ERROR, "Load Majors", "Major name missing on line " + lineNumber);
							continue;
						}

						if (acptGrade < 0 || acptGrade > 100) {
							showAlert(AlertType.ERROR, "Load Majors",
									"Acceptance grade out of range on line " + lineNumber + " for Major: " + name);
							continue;
						}

						if (Math.abs(tawjihiWeight + ptWeight - 1) > 0.001) {
							showAlert(AlertType.ERROR, "Load Majors",
									"Weights do not sum to 1 on line " + lineNumber + " for Major: " + name);
							continue;
						}

						Major major = new Major(name, acptGrade, tawjihiWeight, ptWeight);
						major_LL.insert(major);
						major_TV.getItems().add(major);

					} catch (NumberFormatException e) {
						showAlert(AlertType.ERROR, "Load Majors",
								"Invalid number format on line " + lineNumber + ": " + line);
					}
				}
				showAlert(AlertType.INFORMATION, "Load Majors", "Majors loaded successfully.");
			} catch (IOException e) {
				showAlert(AlertType.ERROR, "Load Majors", "Error reading major data.");
			}
		}
	}

	private void insert(String entityType) {
		if (entityType.equals("Student")) {
			TextInputDialog id_Dialog = new TextInputDialog();
			id_Dialog.setHeaderText("Enter Student ID (integer):");
			Optional<String> idResult = id_Dialog.showAndWait();
			int id;

			if (!idResult.isPresent() || !isValidInteger(idResult.get())) {
				showAlert(AlertType.ERROR, "Insert Student", "Invalid Student ID.");
				return;
			}
			id = Integer.parseInt(idResult.get().trim());
			if (student_LL.search(id) != null) {
				showAlert(AlertType.WARNING, "Insert Student", "Student ID already exists.");
				return;
			}

			TextInputDialog name_Dialog = new TextInputDialog();
			name_Dialog.setHeaderText("Enter Student Name:");
			Optional<String> nameResult = name_Dialog.showAndWait();
			if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
				showAlert(AlertType.ERROR, "Insert Student", "Invalid Student Name.");
				return;
			}
			String name = nameResult.get().trim();

			TextInputDialog tawjihi_Dialog = new TextInputDialog();
			tawjihi_Dialog.setHeaderText("Enter Tawjihi Grade (0.0 - 100.0):");
			Optional<String> tawjihi_Result = tawjihi_Dialog.showAndWait();
			if (!tawjihi_Result.isPresent() || !isValidDouble(tawjihi_Result.get(), 0, 100)) {
				showAlert(AlertType.ERROR, "Insert Student", "Invalid Tawjihi Grade.");
				return;
			}
			double tawjihi = Double.parseDouble(tawjihi_Result.get().trim());

			TextInputDialog plac_Dialog = new TextInputDialog();
			plac_Dialog.setHeaderText("Enter Placement Test Grade (0.0 - 100.0):");
			Optional<String> placementResult = plac_Dialog.showAndWait();
			if (!placementResult.isPresent() || !isValidDouble(placementResult.get(), 0, 100)) {
				showAlert(AlertType.ERROR, "Insert Student", "Invalid Placement Test Grade.");
				return;
			}
			double placement = Double.parseDouble(placementResult.get().trim());

			List<String> avail_Majors = new ArrayList<>();
			for (Major major : major_TV.getItems()) {
				double admMark = (tawjihi * major.getTawjihiWeight()) + (placement * major.getPtWeight());
				if (admMark >= major.getAcptGrade()) {
					avail_Majors.add(major.getName());
				}
			}

			if (avail_Majors.isEmpty()) {
				total_rejected++;
				lowGrades_Rej++;
				showAlert(AlertType.INFORMATION, "No Eligible Majors", "This student does not qualify for any major.");
				return;
			}

			ChoiceDialog<String> majorDialog = new ChoiceDialog<>(avail_Majors.get(0), avail_Majors);
			majorDialog.setHeaderText("Select Major:");
			Optional<String> majorResult = majorDialog.showAndWait();
			if (!majorResult.isPresent() || majorResult.get().trim().isEmpty()) {
				showAlert(AlertType.ERROR, "Insert Student", "Invalid Major selection.");
				return;
			}
			String majorName = majorResult.get().trim();
			Major chosenMajor = major_LL.search(majorName);
			double admMark = (tawjihi * chosenMajor.getTawjihiWeight()) + (placement * chosenMajor.getPtWeight());
			Student student = new Student(id, name, tawjihi, placement, majorName);
			student.setAdm_mark(admMark);
			student_LL.insert(student);
			student_TV.getItems().add(student);
			allStudents.add(student);
			chosenMajor.addStudent(student);
			showAlert(AlertType.INFORMATION, "Insert Student", "Student inserted successfully.");

		} else if (entityType.equals("Major")) {
			TextInputDialog nameDialog = new TextInputDialog();
			nameDialog.setHeaderText("Enter Major Name:");
			Optional<String> nameResult = nameDialog.showAndWait();
			if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
				showAlert(AlertType.ERROR, "Insert Major", "Invalid Major Name.");
				return;
			}
			String majorName = nameResult.get().trim();
			TextInputDialog acptGrade_Dialog = new TextInputDialog();
			acptGrade_Dialog.setHeaderText("Enter Acceptance Grade (0.0 - 100.0):");
			Optional<String> acptGrade_Result = acptGrade_Dialog.showAndWait();
			if (!acptGrade_Result.isPresent() || !isValidDouble(acptGrade_Result.get(), 0, 100)) {
				showAlert(AlertType.ERROR, "Insert Major", "Invalid Acceptance Grade.");
				return;
			}
			double acptGrade = Double.parseDouble(acptGrade_Result.get().trim());
			TextInputDialog tawjihi_W_Dialog = new TextInputDialog();
			tawjihi_W_Dialog.setHeaderText("Enter Tawjihi Weight (0.0 - 1.0):");
			Optional<String> tawjihi_W_Res = tawjihi_W_Dialog.showAndWait();
			if (!tawjihi_W_Res.isPresent() || !isValidDouble(tawjihi_W_Res.get(), 0, 1)) {
				showAlert(AlertType.ERROR, "Insert Major", "Invalid Tawjihi Weight.");
				return;
			}
			double tawjihi_Weight = Double.parseDouble(tawjihi_W_Res.get().trim());
			double ptWeight = 1.0 - tawjihi_Weight;
			Major major = new Major(majorName, acptGrade, tawjihi_Weight, ptWeight);
			major_LL.insert(major);
			major_TV.getItems().add(major);
			showAlert(AlertType.INFORMATION, "Insert Major", "Major inserted successfully.");
		}
	}

	private boolean isValidInteger(String input) {
		try {
			Integer.parseInt(input.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isValidDouble(String input, double min, double max) {
		try {
			double num = Double.parseDouble(input.trim());
			return num >= min && num <= max;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void delete(String entityType) {
		if (entityType.equals("Student")) {
			TextInputDialog idDialog = new TextInputDialog();
			idDialog.setHeaderText("Enter the Student ID (integer) to delete:");
			Optional<String> idResult = idDialog.showAndWait();

			if (idResult.isPresent()) {
				try {
					int studentId = Integer.parseInt(idResult.get().trim());
					Student sel_Std = student_LL.search(studentId);

					if (sel_Std != null) {
						Major std_Major = major_LL.search(sel_Std.getSt_major());
						if (std_Major != null) {
							std_Major.getStudents().delete(studentId);
						}

						student_LL.delete(studentId);
						student_TV.getItems().remove(sel_Std);
						allStudents = new ArrayList<>(student_TV.getItems());
						showAlert(AlertType.INFORMATION, "Delete Student", "Student deleted successfully.");
						student_TV.refresh();
					} else {
						showAlert(AlertType.WARNING, "Delete Student", "Student not found.");
					}
				} catch (NumberFormatException e) {
					showAlert(AlertType.ERROR, "Delete Student", "Invalid Student ID format. Please enter an integer.");
				}
			}

		} else if (entityType.equals("Major")) {
			List<String> avail_Majors = new ArrayList<>();
			for (Major major : major_TV.getItems()) {
				avail_Majors.add(major.getName());
			}

			ChoiceDialog<String> majorDialog = new ChoiceDialog<>(avail_Majors.get(0), avail_Majors);
			majorDialog.setHeaderText("Select the Major to delete:");
			Optional<String> majorResult = majorDialog.showAndWait();

			if (majorResult.isPresent()) {
				String majorName = majorResult.get().trim();
				Major selectedMajor = major_LL.search(majorName);

				if (selectedMajor != null) {
					if (selectedMajor.getStudents().getHead() != null) {
						showAlert(AlertType.WARNING, "Delete Major",
								"Cannot delete major with enrolled students. Please remove students first.");
						return;
					}
					major_LL.delete(majorName);
					major_TV.getItems().remove(selectedMajor);
					showAlert(AlertType.INFORMATION, "Delete Major", "Major deleted successfully.");
					major_TV.refresh();
				} else {
					showAlert(AlertType.WARNING, "Delete Major", "Major not found.");
				}
			}
		}
	}

	private void update(String entityType) {
		if (entityType.equals("Student")) {
			TextInputDialog idDialog = new TextInputDialog();
			idDialog.setHeaderText("Enter the Student ID (integer)");
			Optional<String> idResult = idDialog.showAndWait();

			if (idResult.isPresent()) {
				try {
					int std_Id = Integer.parseInt(idResult.get().trim());
					Student sel_Std = student_LL.search(std_Id);

					if (sel_Std != null) {
						TextInputDialog nameDialog = new TextInputDialog(sel_Std.getName());
						nameDialog.setHeaderText("Update Student Name");
						Optional<String> nameResult = nameDialog.showAndWait();
						nameResult.ifPresent(newValue -> sel_Std.setName(newValue.trim()));

						TextInputDialog tawjihiDialog = new TextInputDialog(String.valueOf(sel_Std.getT_grade()));
						tawjihiDialog.setHeaderText("Update Tawjihi Grade");
						Optional<String> tawjihiResult = tawjihiDialog.showAndWait();
						tawjihiResult.ifPresent(newValue -> {
							try {
								sel_Std.setT_grade(Double.parseDouble(newValue.trim()));
							} catch (NumberFormatException e) {
								showAlert(AlertType.ERROR, "Update Student", "Invalid input for Tawjihi Grade.");
							}
						});

						TextInputDialog palce_Dialog = new TextInputDialog(String.valueOf(sel_Std.getPt_grade()));
						palce_Dialog.setHeaderText("Update Placement Grade");
						Optional<String> place_Res = palce_Dialog.showAndWait();
						place_Res.ifPresent(newValue -> {
							try {
								sel_Std.setPt_grade(Double.parseDouble(newValue.trim()));
							} catch (NumberFormatException e) {
								showAlert(AlertType.ERROR, "Update Student", "Invalid input for Placement Grade.");
							}
						});

						List<String> can_inter_mjrs = new ArrayList<>();
						double tawjihi = sel_Std.getT_grade();
						double placement = sel_Std.getPt_grade();
						for (Major major : major_TV.getItems()) {
							double admMark = (tawjihi * major.getTawjihiWeight()) + (placement * major.getPtWeight());
							if (admMark >= major.getAcptGrade()) {
								can_inter_mjrs.add(major.getName());
							}
						}

						if (can_inter_mjrs.isEmpty()) {
							showAlert(AlertType.INFORMATION, "No Eligible Majors",
									"Student does not meet the criteria for any major.");
							return;
						}

						ChoiceDialog<String> mjr_Dialog = new ChoiceDialog<>(sel_Std.getSt_major(), can_inter_mjrs);
						mjr_Dialog.setHeaderText("Select Eligible Major");
						Optional<String> mjr_Res = mjr_Dialog.showAndWait();
						mjr_Res.ifPresent(newMajor -> {
							Major prev_Mjr = major_LL.search(sel_Std.getSt_major());
							if (prev_Mjr != null) {
								prev_Mjr.getStudents().delete(std_Id);
							}
							sel_Std.setSt_major(newMajor.trim());
							Std_in_Mjr(sel_Std);
						});

						student_TV.refresh();
						showAlert(AlertType.INFORMATION, "Update Student", "Student updated.");
					} else {
						showAlert(AlertType.WARNING, "Update Student", "Student not found.");
					}
				} catch (NumberFormatException e) {
					showAlert(AlertType.ERROR, "Update Student", "Invalid Student ID format. Please enter an integer.");
				}
			}
		} else if (entityType.equals("Major")) {
			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.setTitle("Select Major to Update");
			dialog.setHeaderText("Choose the major you want to update.");

			ComboBox<String> mjr_cb = new ComboBox<>();
			for (Major major : major_TV.getItems()) {
				mjr_cb.getItems().add(major.getName());
			}

			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			dialog.getDialogPane().setContent(mjr_cb);

			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				String majorName = mjr_cb.getValue();
				if (majorName != null && !majorName.trim().isEmpty()) {
					Major sel_Mjr = major_LL.search(majorName.trim());

					if (sel_Mjr != null) {
						TextInputDialog nameDialog = new TextInputDialog(sel_Mjr.getName());
						nameDialog.setHeaderText("Update Major Name");
						Optional<String> name_Res = nameDialog.showAndWait();
						name_Res.ifPresent(newName -> {
							if (!newName.trim().equals(sel_Mjr.getName()) && isUnique(newName.trim())) {
								sel_Mjr.setName(newName.trim());
							} else {
								showAlert(AlertType.ERROR, "Update Major",
										"The new major name already exists or is invalid.");
							}
						});

						TextInputDialog acpt_Grade_Dialog = new TextInputDialog(String.valueOf(sel_Mjr.getAcptGrade()));
						acpt_Grade_Dialog.setHeaderText("Update Acceptance Grade");
						Optional<String> acpt_Grade_Res = acpt_Grade_Dialog.showAndWait();
						acpt_Grade_Res.ifPresent(newValue -> {
							try {
								sel_Mjr.setAcptGrade(Double.parseDouble(newValue.trim()));
							} catch (NumberFormatException e) {
								showAlert(AlertType.ERROR, "Update Major", "Invalid input for Acceptance Grade.");
							}
						});

						TextInputDialog tawjihi_W_Dialog = new TextInputDialog(
								String.valueOf(sel_Mjr.getTawjihiWeight()));
						tawjihi_W_Dialog.setHeaderText("Update Tawjihi Weight");
						Optional<String> tawjihi_W_Res = tawjihi_W_Dialog.showAndWait();
						tawjihi_W_Res.ifPresent(newValue -> {
							try {
								double tawjihiWeight = Double.parseDouble(newValue.trim());
								sel_Mjr.setTawjihiWeight(tawjihiWeight);
								sel_Mjr.setPtWeight(1.0 - tawjihiWeight);
							} catch (NumberFormatException e) {
								showAlert(AlertType.ERROR, "Update Major", "Invalid input for Tawjihi Weight.");
							}
						});

						S_Node curr = sel_Mjr.getStudents().getHead();
						if (curr != null) {
							do {
								Student student = curr.getStudent();
								double newAdmMark = (student.getT_grade() * sel_Mjr.getTawjihiWeight())
										+ (student.getPt_grade() * sel_Mjr.getPtWeight());
								student.setAdm_mark(newAdmMark);
								curr = curr.getNext();
							} while (curr != sel_Mjr.getStudents().getHead());
						}

						major_TV.refresh();
						showAlert(AlertType.INFORMATION, "Update Major", "Major updated successfully.");
					} else {
						showAlert(AlertType.WARNING, "Update Major", "Major not found.");
					}
				} else {
					showAlert(AlertType.WARNING, "Update Major", "Please select a valid major.");
				}
			}
		}
	}

	private boolean isUnique(String name) {
		for (Major major : major_TV.getItems()) {
			if (major.getName().equalsIgnoreCase(name)) {
				return false;
			}
		}
		return true;
	}

	private void search(String entityType) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText(entityType.equals("Student") ? "Enter Student ID" : "Enter Major Name");
		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			String input = result.get().trim();

			if (entityType.equals("Student")) {
				try {
					int id = Integer.parseInt(input);
					Student student = student_LL.search(id);

					if (student != null) {
						student_TV.getSelectionModel().select(student);
						showAlert(AlertType.INFORMATION, "Search Student", "Student found:\nName: " + student.getName()
								+ "\nMajor: " + student.getSt_major() + "\nAdmission Mark: " + student.getAdm_mark());
					} else {
						showAlert(AlertType.WARNING, "Search Student", "Student not found.");
					}
				} catch (NumberFormatException e) {
					showAlert(AlertType.ERROR, "Search Student", "Invalid input. Please enter a valid Student ID.");
				}
			} else if (entityType.equals("Major")) {
				Major major = major_LL.search(input);

				if (major != null) {
					major_TV.getSelectionModel().select(major);
					StringBuilder std_info = new StringBuilder();
					S_Node curr = major.getStudents().getHead();
					int count = 0;

					if (curr != null) {
						do {
							Student student = curr.getStudent();
							std_info.append("Student ID: ").append(student.getSid()).append(", Name: ")
									.append(student.getName()).append(", Admission Mark: ")
									.append(student.getAdm_mark()).append("\n");
							count++;
							curr = curr.getNext();
						} while (curr != major.getStudents().getHead());
					}

					showAlert(AlertType.INFORMATION, "Search Major", "Major found: " + major.getName()
							+ "\nTotal Students: " + count + "\n\n" + std_info.toString());
				} else {
					showAlert(AlertType.WARNING, "Search Major", "Major not found.");
				}
			}
		}
	}

	private void stats() {
		int totalStudents = allStudents.size();
		int tot_Accepted = 0;
		int tot_Rej = totalRej_File + total_rejected;
		int notFoundCount = Major_NotFound;
		int lowGradesCount = Low_Grades + lowGrades_Rej;

		List<String> rejection_info = new ArrayList<>();
		StringBuilder majorStats = new StringBuilder("Major-Specific Statistics:\n");

		for (Major major : major_TV.getItems()) {
			int majorAccepted = 0;
			int majorTotal = 0;
			int majorRejected = 0;

			for (Student student : allStudents) {
				if (student.getSt_major().equals(major.getName())) {
					majorTotal++;

					if (student.getAdm_mark() >= major.getAcptGrade()) {
						majorAccepted++;
					} else {
						majorRejected++;
					}
				}
			}
			for (Student student : allStudents) {
				if (student.getSt_major().equals(major.getName()) && student.getAdm_mark() < major.getAcptGrade()) {
					majorRejected++;
				}
			}

			tot_Accepted += majorAccepted;
			tot_Rej += majorRejected;

			double accept_Rate = (majorTotal > 0) ? ((double) majorAccepted / majorTotal) * 100 : 0;
			rejection_info.add(String.format("\t%s: %d rejected\n", major.getName(), majorRejected));
			majorStats.append(String.format(
					"\nMajor: %s\n - Total Students: %d\n - Accepted: %d\n - Rejected: %d\n - Acceptance Rate: %.1f%%\n",
					major.getName(), majorTotal, majorAccepted, majorRejected, accept_Rate));
		}

		int tot_Eval = tot_Accepted + tot_Rej;
		double allAccept_Rate = (tot_Eval > 0) ? ((double) tot_Accepted / tot_Eval) * 100 : 0;
		String rej_Sum = String.join("", rejection_info);

		String stats = String.format(
				"Overall Statistics:\n" + "-------------------\n" + "Total Students: %d\nTotal Evaluated: %d\n"
						+ "Accepted: %d\nRejected: %d\n" + "Overall Acceptance Rate: %.1f%%\n\n"
						+ "Rejection Reasons:\n" + "-------------------\n" + "Major Not Found: %d\nLow Grades: %d\n\n"
						+ "Rejected Students by Major:\n" + "---------------------------\n%s\n%s",
				totalStudents, tot_Eval, tot_Accepted, tot_Rej, allAccept_Rate, notFoundCount, lowGradesCount, rej_Sum,
				majorStats.toString());

		TextArea textArea = new TextArea(stats);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefSize(600, 400);

		ScrollPane scrollPane = new ScrollPane(textArea);
		scrollPane.setFitToWidth(true);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Statistics Report");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setContent(scrollPane);

		ButtonType saveButton = new ButtonType("Save to File");
		alert.getButtonTypes().setAll(ButtonType.OK, saveButton);

		alert.showAndWait().ifPresent(response -> {
			if (response == saveButton) {
				save_Stat_File(stats);
			}
		});
	}

	private void save_Stat_File(String stats) {
		FileChooser fch = new FileChooser();
		fch.setTitle("Save Statistics");
		fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		File file = fch.showSaveDialog(null);
		if (file != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				writer.println(stats);
				showAlert(AlertType.INFORMATION, "Success", "Statistics saved successfully!");
			} catch (IOException e) {
				showAlert(AlertType.ERROR, "Error", "Failed to save statistics: " + e.getMessage());
			}
		}
	}

	private void save_toFile() {
		FileChooser std_fch = new FileChooser();
		std_fch.setTitle("Save Student Data");
		std_fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File studentFile = std_fch.showSaveDialog(null);

		if (studentFile != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(studentFile))) {
				writer.println("Student ID|Name|Tawjihi Grade|Placement Test Grade|Chosen Major");

				for (Student student : student_TV.getItems()) {
					String std_info = String.format("%d | %s | %.2f | %.2f | %s", student.getSid(), student.getName(),
							student.getT_grade(), student.getPt_grade(), student.getSt_major());
					writer.println(std_info);
				}
			} catch (IOException e) {
				showAlert(AlertType.ERROR, "Save Data", "Error saving student data.");
			}
		}

		FileChooser mjr_fch = new FileChooser();
		mjr_fch.setTitle("Save Major Data");
		mjr_fch.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File majorFile = mjr_fch.showSaveDialog(null);

		if (majorFile != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(majorFile))) {
				writer.println("Major Name|Acceptance Grade|Tawjihi Weight|Placement Test Weight");

				for (Major major : major_TV.getItems()) {
					String mjr_info = String.format("%s | %.2f | %.2f | %.2f", major.getName(), major.getAcptGrade(),
							major.getTawjihiWeight(), major.getPtWeight());
					writer.println(mjr_info);
				}
				showAlert(AlertType.INFORMATION, "Save Data", "Data saved successfully.");
			} catch (IOException e) {
				showAlert(AlertType.ERROR, "Save Data", "Error saving major data.");
			}
		}
	}

	private void Std_in_Mjr(Student student) {
		Major mjr = null;
		double max_adm_grade = 0.0;

		for (Major major : major_TV.getItems()) {
			double adm_grade = (student.getT_grade() * major.getTawjihiWeight())
					+ (student.getPt_grade() * major.getPtWeight());
			student.setAdm_mark(adm_grade);

			if (adm_grade >= major.getAcptGrade() && adm_grade > max_adm_grade) {
				max_adm_grade = adm_grade;
				mjr = major;
			}
		}

		if (mjr != null) {
			student.setSt_major(mjr.getName());
			mjr.addStudent(student);
		} else {
			student.setSt_major("Not Accepted");
			diff_Mjr(student);
		}

		student_TV.refresh();
		major_TV.refresh();
	}

	private void diff_Mjr(Student student) {
		double adm_grade = student.getAdm_mark();
		List<Major> diffmajors = new ArrayList<>();
		for (Major major : major_TV.getItems()) {
			if (adm_grade >= major.getAcptGrade()) {
				diffmajors.add(major);
			}
		}
		if (!diffmajors.isEmpty()) {
			ChoiceDialog<Major> different_majors = new ChoiceDialog<>(diffmajors.get(0), diffmajors);
			different_majors.setTitle("Alternative Major Recommendations");
			different_majors
					.setHeaderText("You were not accepted into your choice major. Please select an alternative major:");

			StringBuilder options = new StringBuilder("Recommended Majors:\n");
			for (Major major : diffmajors) {
				options.append(major.getName()).append(" (Acceptance Grade: ").append(major.getAcptGrade())
						.append(")\n");
			}
			different_majors.setContentText(options.toString());

			Optional<Major> result = different_majors.showAndWait();
			if (result.isPresent()) {
				Major sel_Mjr = result.get();
				student.setSt_major(sel_Mjr.getName());
				sel_Mjr.addStudent(student);
				showAlert(AlertType.INFORMATION, "Major Registration",
						"You have been registered in " + sel_Mjr.getName());
			} else {
				showAlert(AlertType.WARNING, "No Major Selected", "You did not select any alternative major.");
			}
		} else {
			showAlert(AlertType.INFORMATION, "No Alternatives",
					"Unfortunately, there are no alternative majors available based on your admission mark.");
		}
		student_TV.refresh();
		major_TV.refresh();
	}

	private void showAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
