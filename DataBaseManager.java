package jobtracker;

public class DataBaseManager {

	private static final String URL = "jdbc:sqlite:jobapplications.db";

	public static java.sql.Connection connect() {
		java.sql.Connection connection = null;

		try {
			connection = java.sql.DriverManager.getConnection(URL);
			System.out.println("Database connected successfully!");
		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}

		return connection;
	}
	public static void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS applications ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "company TEXT NOT NULL, "
				+ "role TEXT NOT NULL, "
				+ "status TEXT NOT NULL)";

		try (java.sql.Connection conn = connect()) {
			if (conn == null) {
				return;
			}

			try (java.sql.Statement stmt = conn.createStatement()) {
				stmt.execute(sql);
				System.out.println("Applications table created!");
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void addApplication(
			String company,
			String role,
			String status,
			String applicationDate,
			String interviewDate) {

		String sql = "INSERT INTO applications "
				+ "(company, role, status, application_date, interview_date) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (java.sql.Connection conn = connect()) {
			if (conn == null) {
				return;
			}

			try (java.sql.PreparedStatement pstmt =
					conn.prepareStatement(sql)) {

				pstmt.setString(1, company);
				pstmt.setString(2, role);
				pstmt.setString(3, status);
				pstmt.setString(4, applicationDate);
				pstmt.setString(5, interviewDate);

				pstmt.executeUpdate();

				System.out.println("Application saved successfully!");
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static java.util.List<String[]> getApplications() {
		java.util.List<String[]> applications = new java.util.ArrayList<>();

		String sql = "SELECT company, role, application_date, interview_date, status FROM applications";

		try (java.sql.Connection conn = connect()) {
			if (conn == null) {
				return applications;
			}

			try (java.sql.Statement stmt = conn.createStatement();
					java.sql.ResultSet rs = stmt.executeQuery(sql)) {

				while (rs.next()) {
					applications.add(new String[] {
							rs.getString("company"),
							rs.getString("role"),
							rs.getString("application_date"),
							rs.getString("interview_date"),
							rs.getString("status")
					});
				}
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}

		return applications;
	}


	public static void updateApplication(
			String oldCompany,
			String company,
			String role,
			String status,
			String applicationDate,
			String interviewDate) {

		String sql = "UPDATE applications "
				+ "SET company = ?, role = ?, status = ?, "
				+ "application_date = ?, interview_date = ? "
				+ "WHERE company = ?";
	//	System.out.println("Entered updateApplication method");
		try (java.sql.Connection conn = connect()) {
			if (conn == null) {
				return;
			}

			try (java.sql.PreparedStatement pstmt =
					conn.prepareStatement(sql)) {

				pstmt.setString(1, company);
				pstmt.setString(2, role);
				pstmt.setString(3, status);
				pstmt.setString(4, applicationDate);
				pstmt.setString(5, interviewDate);
				pstmt.setString(6, oldCompany);

				int rowsUpdated = pstmt.executeUpdate();

			//	System.out.println("Rows updated: " + rowsUpdated);
				System.out.println("Application updated successfully!");
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void deleteApplication(String company) {

		String sql = "DELETE FROM applications WHERE company = ?";

		try (java.sql.Connection conn = connect()) {

			if (conn == null) {
				return;
			}

			try (java.sql.PreparedStatement pstmt =
					conn.prepareStatement(sql)) {

				pstmt.setString(1, company);

				int rowsDeleted = pstmt.executeUpdate();

				System.out.println("Rows deleted: " + rowsDeleted);
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void addDateColumns() {

		try (java.sql.Connection conn = connect()) {

			if (conn == null) {
				return;
			}

			try (java.sql.Statement stmt = conn.createStatement()) {

				try {
					stmt.execute(
							"ALTER TABLE applications ADD COLUMN application_date TEXT"
							);
				} catch (java.sql.SQLException e) {
					if (!e.getMessage().toLowerCase().contains("duplicate column")) {
						throw e;
					}
				}

				try {
					stmt.execute(
							"ALTER TABLE applications ADD COLUMN interview_date TEXT"
							);
				} catch (java.sql.SQLException e) {
					if (!e.getMessage().toLowerCase().contains("duplicate column")) {
						throw e;
					}
				}

				System.out.println("Date columns are ready!");
			}

		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
