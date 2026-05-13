import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		btnVerificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarCodigo();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void validarCodigo() {
		try (Connection conn = ConexionBD.obtenerConexion()) {
			CallableStatement cs = conn.prepareCall("{call VALIDATE_OTP(?, ?)}");
			cs.setInt(1, userId);
			cs.setString(2, txtOTP.getText());

			cs.execute();

			JOptionPane.showMessageDialog(this, "¡Cuenta verificada! Ya puedes iniciar sesión.");
			this.dispose();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(), "Fallo",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}