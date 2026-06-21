import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PruebaConcurrencia {

    public static void main(String[] args) throws InterruptedException {
        Banco banco = new Banco();

        // 1. Creamos cuentas de muestra
        int numCuentas = 50;
        for (int i = 1; i <= numCuentas; i++) {
            banco.agregarCuenta(new Cuenta(i, "PERSONA " + i, 1000.0));
        }

        double saldoAntes = banco.saldoTotal();
        System.out.println("Cuentas creadas: " + banco.totalCuentas());
        System.out.println("Saldo total ANTES de la prueba: " + saldoAntes);

        // 2. Lanzamos muchos hilos haciendo transferencias al azar
        int numHilos = 20;
        int transferenciasPorHilo = 5000;
        ExecutorService pool = Executors.newFixedThreadPool(numHilos);
        Random random = new Random();

        for (int h = 0; h < numHilos; h++) {
            pool.submit(() -> {
                for (int t = 0; t < transferenciasPorHilo; t++) {
                    int origen = random.nextInt(numCuentas) + 1;
                    int destino = random.nextInt(numCuentas) + 1;

                    if (origen == destino) continue; // no transferirse a sí mismo

                    double monto = 1 + random.nextInt(10); // entre 1 y 10

                    try {
                        banco.transferir(origen, destino, monto);
                    } catch (IllegalArgumentException e) {
                        // saldo insuficiente: es normal que pase a veces, lo ignoramos
                    }
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        // 3. Verificamos el resultado
        double saldoDespues = banco.saldoTotal();
        System.out.println("Saldo total DESPUES de la prueba: " + saldoDespues);

        if (Math.abs(saldoAntes - saldoDespues) < 0.0001) {
            System.out.println("✅ EXITO: el saldo total se mantuvo constante.");
        } else {
            System.out.println("❌ ERROR: el saldo total cambió. Diferencia: " + (saldoDespues - saldoAntes));
        }

        System.out.println("Total de transferencias intentadas: " + (numHilos * transferenciasPorHilo));
    }
}
