package jobtracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
public class JOBTRACK {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Job Application Tracker");
			DataBaseManager.createTable();
			DataBaseManager.addDateColumns();
			frame.setSize(1100, 700);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout(0, 0));

			// Header
			JPanel header = new JPanel();
			header.setBackground(new Color(45, 55, 120));
			header.setPreferredSize(new Dimension(900, 80));

			JLabel title = new JLabel("JOB APPLICATION TRACKER");
			title.setForeground(Color.WHITE);
			title.setFont(new Font("Arial", Font.BOLD, 26));
			header.add(title);

			frame.add(header, BorderLayout.NORTH);

			// Dashboard cards
			JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
			//			int appliedCount=0;
			//			int interviewCount=0;
			//			int offerCount=0;
			cards.setBackground(new Color(240, 245, 255));
			cards.setBorder(
					BorderFactory.createEmptyBorder(30, 20, 30, 20)
					);

			JPanel appliedCard=createCard("APPLIED", "0",
					new Color(190, 220, 255));
			cards.add(appliedCard);
			JPanel interviewCard=createCard("INTERVIEWS", "0",
					new Color(210, 200, 255));
			cards.add(interviewCard);
			JPanel offerCard=createCard("OFFERS", "0",
					new Color(190, 245, 210));
			cards.add(offerCard);
			String[] columns = {"Company", "Job Role", "Application Date","Interview Date","Status"};
			javax.swing.table.DefaultTableModel tableModel =
					new javax.swing.table.DefaultTableModel(
							new Object[][] {},
							columns
							);

			JTable jobTable = new JTable(tableModel);
			jobTable.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
			jobTable.setRowHeight(28);
			jobTable.getTableHeader().setFont(
			        new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14)
			);
			jobTable.setBackground(java.awt.Color.WHITE);
			jobTable.setForeground(java.awt.Color.DARK_GRAY);
			jobTable.setGridColor(new java.awt.Color(230, 230, 230));
			jobTable.setSelectionBackground(new java.awt.Color(210, 230, 255));
			jobTable.setSelectionForeground(java.awt.Color.BLACK);
			for(String[] app:DataBaseManager.getApplications()) {
				tableModel.addRow(app);
			}
			updateDashboard(
					tableModel,
					appliedCard,
					interviewCard,
					offerCard
					);
			JTextField searchField=new JTextField(20);
			searchField.getDocument().addDocumentListener(
					new javax.swing.event.DocumentListener() {

						private void searchApplications() {
							String searchText =
									searchField.getText().trim().toLowerCase();

							javax.swing.table.TableRowSorter<
							javax.swing.table.DefaultTableModel
							> sorter = new javax.swing.table.TableRowSorter<>(tableModel);

							jobTable.setRowSorter(sorter);

							if (searchText.isEmpty()) {
								sorter.setRowFilter(null);
							} else {
								sorter.setRowFilter(
										javax.swing.RowFilter.regexFilter(
												"(?i)" + java.util.regex.Pattern.quote(searchText)
												)
										);
							}
						}

						@Override
						public void insertUpdate(
								javax.swing.event.DocumentEvent e) {
							searchApplications();
						}

						@Override
						public void removeUpdate(
								javax.swing.event.DocumentEvent e) {
							searchApplications();
						}

						@Override
						public void changedUpdate(
								javax.swing.event.DocumentEvent e) {
							searchApplications();
						}
					}
					);
			searchField.setFont(new Font("Arial",Font.PLAIN,16));
			searchField.setToolTipText("Search company or job role");
			jobTable.setRowHeight(30);
			jobTable.setFont(new Font("Arial", Font.PLAIN, 14));
			jobTable.getTableHeader().setFont(
					new Font("Arial", Font.BOLD, 14)
					);

			JScrollPane tableScroll = new JScrollPane(jobTable);
			JPanel searchPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
			searchPanel.add(new JLabel("Search Company or Role"));
			searchPanel.add(searchField);

			JPanel centerPanel = new JPanel(new BorderLayout());
			centerPanel.setBackground(new Color(240, 245, 255));
			//	centerPanel.add(cards, BorderLayout.NORTH);
			JPanel topPanel = new JPanel(new BorderLayout());

			topPanel.add(cards, BorderLayout.NORTH);
			topPanel.add(searchPanel, BorderLayout.SOUTH);
			centerPanel.add(tableScroll, BorderLayout.CENTER);
			centerPanel.add(topPanel, BorderLayout.NORTH);

			frame.add(centerPanel, BorderLayout.CENTER);

			// Bottom button
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(new Color(240, 245, 255));

			JButton addJobButton =
					new JButton("+ Add Job Application");
			JButton deleteJobButton=new JButton("Delete Application");
			JButton editJobButton=new JButton("Edit Application");
			editJobButton.setFont(new Font("Arial",Font.BOLD,16));
			editJobButton.setBackground(new Color(45,55,120));
			editJobButton.setForeground(Color.WHITE);
			editJobButton.setFocusPainted(false);

			deleteJobButton.setFont(new Font("Arial",Font.BOLD,16));
			deleteJobButton.setBackground(new Color(45,55,120));
			deleteJobButton.setForeground(Color.WHITE);
			deleteJobButton.setFocusPainted(false);
			addJobButton.setFont(new Font("Arial", Font.BOLD, 16));
			addJobButton.setBackground(new Color(45, 55, 120));
			addJobButton.setForeground(Color.WHITE);
			addJobButton.setFocusPainted(false);

			buttonPanel.add(addJobButton);
			buttonPanel.add(deleteJobButton);
			buttonPanel.add(editJobButton);
			editJobButton.addActionListener(e -> {
				int selectedRow = jobTable.getSelectedRow();

				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(
							frame,
							"Please select an application to edit."
							);
				}
				else {

					String oldCompany = tableModel.getValueAt(selectedRow, 0).toString();
					String company = tableModel.getValueAt(selectedRow, 0).toString();
					String role = tableModel.getValueAt(selectedRow, 1).toString();

					String applicationDate = String.valueOf(
							tableModel.getValueAt(selectedRow, 2) == null
							? ""
									: tableModel.getValueAt(selectedRow, 2)
							);

					String interviewDate = String.valueOf(
							tableModel.getValueAt(selectedRow, 3) == null
							? ""
									: tableModel.getValueAt(selectedRow, 3)
							);

					String status = String.valueOf(tableModel.getValueAt(selectedRow, 4));

					JTextField companyField = new JTextField(company);
					JTextField roleField = new JTextField(role);
					JTextField applicationDateField = new JTextField(applicationDate);
					JTextField interviewDateField = new JTextField(interviewDate);

					String[] statuses = {
							"Applied", "Interview", "Selected", "Rejected"
					};

					JComboBox<String> statusBox = new JComboBox<>(statuses);
					statusBox.setSelectedItem(status);

					JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

					form.add(new JLabel("Company Name:"));
					form.add(companyField);

					form.add(new JLabel("Job Role:"));
					form.add(roleField);

					form.add(new JLabel("Application Date (YYYY-MM-DD):"));
					form.add(applicationDateField);

					form.add(new JLabel("Interview Date (YYYY-MM-DD):"));
					form.add(interviewDateField);

					form.add(new JLabel("Application Status:"));
					form.add(statusBox);

					int result = JOptionPane.showConfirmDialog(
							frame, form, "Edit Job Application",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE
							);
					if (result == JOptionPane.OK_OPTION) {

						String updatedCompany = companyField.getText().trim();
						String updatedRole = roleField.getText().trim();
						String updatedApplicationDate = applicationDateField.getText().trim();
						String updatedInterviewDate = interviewDateField.getText().trim();
						String updatedStatus = (String) statusBox.getSelectedItem();
						if (!updatedApplicationDate.isEmpty()
								&& !updatedApplicationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
							JOptionPane.showMessageDialog(
									frame,
									"Application date must use YYYY-MM-DD format.");
							return;
						}

						if (!updatedInterviewDate.isEmpty()
								&& !updatedInterviewDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
							JOptionPane.showMessageDialog(
									frame,
									"Interview date must use YYYY-MM-DD format.");
							return;
						}

						if (!isValidDate(updatedApplicationDate)) {
							JOptionPane.showMessageDialog(
									frame,
									"Please enter a real application date in YYYY-MM-DD format.");
							return;
						}

						if (!isValidDate(updatedInterviewDate)) {
							JOptionPane.showMessageDialog(
									frame,
									"Please enter a real interview date in YYYY-MM-DD format.");
							return;
						}

						if (updatedCompany.isEmpty() || updatedRole.isEmpty()) {
						    JOptionPane.showMessageDialog(
						            frame,
						            "Please enter both company name and job role."
						    );
						} else {
						    tableModel.setValueAt(updatedCompany, selectedRow, 0);
						    tableModel.setValueAt(updatedRole, selectedRow, 1);
						    tableModel.setValueAt(updatedApplicationDate, selectedRow, 2);
						    tableModel.setValueAt(updatedInterviewDate, selectedRow, 3);
						    tableModel.setValueAt(updatedStatus, selectedRow, 4);

//						    System.out.println("Calling database update...");

						    DataBaseManager.updateApplication(
						            oldCompany,
						            updatedCompany,
						            updatedRole,
						            updatedStatus,
						            updatedApplicationDate,
						            updatedInterviewDate
						    );

						    updateDashboard(
						            tableModel,
						            appliedCard,
						            interviewCard,
						            offerCard
						    );
						}}}}
						);
			deleteJobButton.addActionListener(e -> {
				int selectedRow = jobTable.getSelectedRow();

				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(
							frame,
							"Please select an application to delete."
							);
				} else {
					String company = tableModel.getValueAt(selectedRow, 0).toString();

					DataBaseManager.deleteApplication(company);

					tableModel.removeRow(selectedRow);
					updateDashboard(
							tableModel,
							appliedCard,
							interviewCard,
							offerCard
							);
				}
			});
			addJobButton.addActionListener(e -> {
				JTextField companyField = new JTextField();
				JTextField roleField = new JTextField();
				JTextField applicationDateField = new JTextField();
				JTextField interviewDateField = new JTextField();

				String[] statuses = {
						"Applied", "Interview", "Selected", "Rejected"
				};

				JComboBox<String> statusBox =
						new JComboBox<>(statuses);

				JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

				form.add(new JLabel("Company Name:"));
				form.add(companyField);

				form.add(new JLabel("Job Role:"));
				form.add(roleField);
				form.add(new JLabel("Application Date (YYYY-MM-DD):"));
				form.add(applicationDateField);

				form.add(new JLabel("Interview Date (YYYY-MM-DD):"));
				form.add(interviewDateField);

				form.add(new JLabel("Application Status:"));
				form.add(statusBox);

				int result = JOptionPane.showConfirmDialog(
						frame,
						form,
						"Add Job Application",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE
						);

				if (result == JOptionPane.OK_OPTION) {
					String company = companyField.getText().trim();
					String role = roleField.getText().trim();
					String applicationDate = applicationDateField.getText().trim();
					String interviewDate = interviewDateField.getText().trim();
					String status = (String) statusBox.getSelectedItem();


					if (!applicationDate.isEmpty()
							&& !applicationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
						JOptionPane.showMessageDialog(
								frame,
								"Application date must use YYYY-MM-DD format."
								);
						return;
					}

					if (!isValidDate(applicationDate)) {
						JOptionPane.showMessageDialog(
								frame,
								"Please enter a real application date in YYYY-MM-DD format."
								);
						return;
					}

					if (!isValidDate(interviewDate)) {
						JOptionPane.showMessageDialog(
								frame,
								"Please enter a real interview date in YYYY-MM-DD format."
								);
						return;
					}

					if (!interviewDate.isEmpty()
							&& !interviewDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
						JOptionPane.showMessageDialog(
								frame,
								"Interview date must use YYYY-MM-DD format."
								);
						return;
					}

					if (company.isEmpty() || role.isEmpty()) {
						JOptionPane.showMessageDialog(
								frame,
								"Please enter both company name and job role."
								);
					} else {
						tableModel.addRow(new Object[] {
								company,role,applicationDate,interviewDate,status
						});	
						DataBaseManager.addApplication(
								company,
								role,
								status,
								applicationDate,
								interviewDate
								);
						updateDashboard(tableModel,appliedCard,interviewCard,offerCard);
					}
				}
			});

			buttonPanel.setPreferredSize(new Dimension(900, 75));

			frame.add(buttonPanel, BorderLayout.SOUTH);

			frame.setVisible(true);
			});

		}


		private static JPanel createCard(
				String heading, String number, Color color) {

			JPanel card = new JPanel(new GridLayout(2, 1));
			card.setBackground(color);

			JLabel title = new JLabel(heading, JLabel.CENTER);
			title.setFont(new Font("Arial", Font.BOLD, 18));

			JLabel value = new JLabel(number, JLabel.CENTER);
			value.setName(heading);
			value.setFont(new Font("Arial", Font.BOLD, 30));

			card.add(title);
			card.add(value);
			card.putClientProperty("valueLabel",value);

			return card;

		}
		private static JLabel getCardValue(JPanel card) {
			return (JLabel) card.getClientProperty("valueLabel");
		}
		private static void updateDashboard(
				javax.swing.table.DefaultTableModel tableModel,
				JPanel appliedCard,
				JPanel interviewCard,
				JPanel offerCard) {

			int applied = 0;
			int interviews = 0;
			int offers = 0;

			for (int i = 0; i < tableModel.getRowCount(); i++) {
				Object statusValue = tableModel.getValueAt(i, 4);

				if (statusValue == null) {
					continue;
				}

				String status = statusValue.toString();

				if (status.equals("Applied")) {
					applied++;
				} else if (status.equals("Interview")) {
					interviews++;
				} else if (status.equals("Selected")) {
					offers++;
				}
			}

			getCardValue(appliedCard).setText(String.valueOf(applied));
			getCardValue(interviewCard).setText(String.valueOf(interviews));
			getCardValue(offerCard).setText(String.valueOf(offers));
		}

		private static boolean isValidDate(String date) {
			if (date == null || date.isEmpty()) {
				return true;
			}

			try {
				LocalDate.parse(date);
				return true;
			} catch (java.time.format.DateTimeParseException e) {
				return false;
			}
		}

	}