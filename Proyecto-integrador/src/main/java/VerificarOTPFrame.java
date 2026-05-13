import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerificarOTPFrame extends JFrame {
	private JTextField txtOTP = new JTextField(10);
	private JButton btnVerificar = new JButton("Validar Código");
	private int userId;

	public VerificarOTPFrame(int userId) {
		this.userId = userId;
		setTitle("Verificación de Seguridad (2FA)");
		setLayout(new FlowLayout());

		add(new JLabel("Introduce el código de 6 caracteres:"));
		add(txtOTP);
		add(btnVerificar);

		// Al pulsar el botón llamamos al procedimiento de MySQL
		btnVerificar.addActionListener(e -> validarCodigo());

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void validarCodigo() {
		try (Connection conn = ConexionBD.obtenerConexion()) {
			// Llamada al procedimiento VALIDATE_OTP de tu archivo ppp.sql
			CallableStatement cs = conn.prepareCall("{call VALIDATE_OTP(?, ?)}");
			cs.setInt(1, userId);
			cs.setString(2, txtOTP.getText());

			cs.execute();

			// Si llegamos aquí, es que no saltó la excepción 'OTP inválido o expirado'
			JOptionPane.showMessageDialog(this, "¡Cuenta verificada! Ya puedes iniciar sesión.");
			this.dispose();

			// Opcional: Abrir la pantalla de Login aquí
		} catch (SQLException ex) {
			// Captura el SIGNAL SQLSTATE '45000' que definiste en tu SQL
			JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(), "Fallo",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}