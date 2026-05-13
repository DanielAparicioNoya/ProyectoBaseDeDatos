import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VerificarOTPFrame extends JFrame {
    private JTextField txtOTP = new JTextField(10);
    private JButton btnVerificar = new JButton("Validar Código");
    private int userId;
    private Color colorPrincipal = new Color(83, 212, 151);

    public VerificarOTPFrame(int userId) {
        this.userId = userId;
        setTitle("gethabits - Verificación");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("gethabits");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInstruccion = new JLabel("Introduce el código de 6 caracteres:");
        lblInstruccion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInstruccion.setForeground(Color.DARK_GRAY);
        lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtOTP.setFont(new Font("Monospaced", Font.BOLD, 24));
        txtOTP.setHorizontalAlignment(JTextField.CENTER);
        txtOTP.setMaximumSize(new Dimension(200, 40));

        btnVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerificar.setBackground(colorPrincipal);
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVerificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerificar.setOpaque(true);
        btnVerificar.setBorderPainted(false);
        btnVerificar.setFocusPainted(false);
        btnVerificar.setMaximumSize(new Dimension(200, 45));

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(lblInstruccion);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(txtOTP);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(btnVerificar);

        add(panelPrincipal);

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