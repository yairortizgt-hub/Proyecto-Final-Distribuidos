public class PruebaCarga {

    public static void main(String[] args) throws Exception {
        Banco banco = new Banco();

        long inicio = System.currentTimeMillis();
        CargadorDatos.cargarDesdeCsv("alumnos.csv", banco);
        long fin = System.currentTimeMillis();

        System.out.println("Total de cuentas en el banco: " + banco.totalCuentas());
        System.out.println("Saldo total del banco: " + banco.saldoTotal());
        System.out.println("Tiempo de carga: " + (fin - inicio) + " ms");

        // Mostramos un par de cuentas de ejemplo para verificar visualmente
        System.out.println("Cuenta 1: " + banco.obtenerCuenta(1));
        System.out.println("Cuenta 5: " + banco.obtenerCuenta(5));
    }
}
