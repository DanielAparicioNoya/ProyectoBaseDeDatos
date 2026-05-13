
public class OTPMailer {

	public static void main(String[] args) {
		try {
			EmailService email = new EmailService("dapanoya@gmail.com", "typy gigh idnt droh");

			String otp = "123456"; // este viene de PL/SQL

			email.sendEmail("francisco.magarino@u-tad.com", "Tu código de verificación",
					"Tu código OTP es: " + otp + "\nExpira en 5 minutos.");

			System.out.println("Correo enviado correctamente");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
