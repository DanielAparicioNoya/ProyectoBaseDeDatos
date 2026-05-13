import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistroFrame extends JFrame {
	private JTextField txtEmail = new JTextField(20);
	private JPasswordField txtPass = new JPasswordField(20);
	private JButton btnRegistrar = new JButton("Crear Cuenta");
	private EmailService emailService = new EmailService("dapanoya@gmail.com", "typygighidntdroh");

	private Color colorPrincipal = new Color(83, 212, 151);

	public RegistroFrame() {
		setTitle("gethabits - Registro");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelPrincipal.setBackground(Color.WHITE);
		panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

		JLabel lblTitulo = new JLabel("gethabits");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setForeground(colorPrincipal);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSubtitulo = new JLabel("Crea tu cuenta ahora");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblSubtitulo.setForeground(Color.GRAY);
		lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		formPanel.setBackground(Color.WHITE);
		formPanel.setMaximumSize(new Dimension(300, 150));

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("SansSerif", Font.BOLD, 12));

		JLabel lblPass = new JLabel("Contraseña");
		lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));

		formPanel.add(lblEmail);
		formPanel.add(txtEmail);
		formPanel.add(lblPass);
		formPanel.add(txtPass);

		btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRegistrar.setBackground(colorPrincipal);
		btnRegistrar.setForeground(Color.WHITE);
		btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btnRegistrar.setOpaque(true);
		btnRegistrar.setBorderPainted(false);
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setContentAreaFilled(true);
		btnRegistrar.setMaximumSize(new Dimension(300, 45));

		panelPrincipal.add(lblTitulo);
		panelPrincipal.add(lblSubtitulo);
		panelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));
		panelPrincipal.add(formPanel);
		panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
		panelPrincipal.add(btnRegistrar);

		add(panelPrincipal);

		btnRegistrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarRegistro();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void realizarRegistro() {
		String email = txtEmail.getText();
		String pass = new String(txtPass.getPassword());

		try (Connection conn = ConexionBD.obtenerConexion()) {
			String sqlInsert = "INSERT INTO USERS (EMAIL, PASSWORD_HASH) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, email);
			ps.setString(2, pass);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int userId = rs.getInt(1);
				String sqlOTP = "SELECT OTP_CODE FROM OTP_CODES WHERE USER_ID = ? ORDER BY OTP_ID DESC LIMIT 1";
				PreparedStatement psOTP = conn.prepareStatement(sqlOTP);
				psOTP.setInt(1, userId);
				ResultSet rsOTP = psOTP.executeQuery();

				if (rsOTP.next()) {
					String otpGenerado = rsOTP.getString("OTP_CODE");

					emailService.sendEmail(email, "Tu Código de Registro", "Tu código es: " + otpGenerado);

					JOptionPane.showMessageDialog(this, "Registro exitoso. Revisa tu email.");

					new VerificarOTPFrame(userId);
					this.dispose();
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new RegistroFrame();
	}
}