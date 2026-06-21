import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CargadorDatos {

    // Lee el archivo csv y carga cada línea como una cuenta en el banco.
    // Formato esperado por línea: id,nombre,apellido1,apellido2,saldo
    public static void cargarDesdeCsv(String rutaArchivo, Banco banco) throws IOException {
        int lineasLeidas = 0;
        int erroresIgnorados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(",");
                if (partes.length != 5) {
                    erroresIgnorados++;
                    continue; // línea con formato inesperado, la saltamos
                }

                try {
                    int id = Integer.parseInt(partes[0]);
                    String nombreCompleto = partes[1] + " " + partes[2] + " " + partes[3];
                    double saldo = Double.parseDouble(partes[4]);

                    banco.agregarCuenta(new Cuenta(id, nombreCompleto, saldo));
                    lineasLeidas++;
                } catch (NumberFormatException e) {
                    erroresIgnorados++;
                }
            }
        }

        System.out.println("Cuentas cargadas: " + lineasLeidas);
        if (erroresIgnorados > 0) {
            System.out.println("Lineas con error ignoradas: " + erroresIgnorados);
        }
    }
}
