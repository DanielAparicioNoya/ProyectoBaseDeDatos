import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistroFrame extends JFrame {
	private JTextField txtEmail = new JTextField(20);
	private JPasswordField txtPass = new JPasswordField(20);
	private JButton btnRegistrar = new JButton("Crear Cuenta");

	// Tu configuración de correo que ya probamos con éxito
	private EmailService emailService = new EmailService("dapanoya@gmail.com", "typygighidntdroh");

	public RegistroFrame() {
		setTitle("Registro - Proyecto Integrador");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3, 2, 10, 10));

		add(new JLabel(" Email:"));
		add(txtEmail);
		add(new JLabel(" Contraseña:"));
		add(txtPass);
		add(new JLabel(""));
		add(btnRegistrar);

		btnRegistrar.addActionListener(e -> realizarRegistro());

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void realizarRegistro() {
		String email = txtEmail.getText();
		String pass = new String(txtPass.getPassword());

		try (Connection conn = ConexionBD.obtenerConexion()) {
			// Insertar usuario (esto activa tu TRIGGER en MySQL)
			String sqlInsert = "INSERT INTO USERS (EMAIL, PASSWORD_HASH) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, email);
			ps.setString(2, pass);
			ps.executeUpdate();

			// Obtener el ID generado para buscar su OTP
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int userId = rs.getInt(1);

				// Consultar el OTP que generó el TRIGGER 'TRG_AFTER_USER_INSERT'
				String sqlOTP = "SELECT OTP_CODE FROM OTP_CODES WHERE USER_ID = ? ORDER BY OTP_ID DESC LIMIT 1";
				PreparedStatement psOTP = conn.prepareStatement(sqlOTP);
				psOTP.setInt(1, userId);
				ResultSet rsOTP = psOTP.executeQuery();

				if (rsOTP.next()) {
					String otpGenerado = rsOTP.getString("OTP_CODE");

					// Enviar el correo real con el código de la BD
					emailService.sendEmail(email, "Tu Código de Registro", "Tu código es: " + otpGenerado);

					JOptionPane.showMessageDialog(this, "Registro exitoso. Revisa tu email.");

					// Abrir la pantalla de verificación (la crearemos a continuación)
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
		new RegistroFrame();
	}
}