import java.sql.Connection
import java.sql.DriverManager

class Conexion {
    companion object {
        // Datos necesarios para la conexión
        private const val url = "jdbc:mysql://localhost:3306/nomina"
        private const val user = "user"
        private const val passwd = "user"
        val connect: Connection = DriverManager.getConnection(url, user, passwd) //intentar gestionar posible null
    }
}