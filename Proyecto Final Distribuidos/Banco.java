import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Banco {

    // ConcurrentHashMap: seguro para que muchos hilos lean y consulten a la vez
    private final Map<Integer, Cuenta> cuentas = new ConcurrentHashMap<>();

    // Agregar una cuenta al banco (lo usaremos al cargar el csv)
    public void agregarCuenta(Cuenta cuenta) {
        cuentas.put(cuenta.getId(), cuenta);
    }

    // Consultar una cuenta por id
    public Cuenta obtenerCuenta(int id) {
        return cuentas.get(id);
    }

    // Cuántas cuentas hay en total
    public int totalCuentas() {
        return cuentas.size();
    }

    // Suma de todos los saldos (nos servirá para verificar que nada se pierde)
    public double saldoTotal() {
        double suma = 0;
        for (Cuenta c : cuentas.values()) {
            suma += c.getBalance();
        }
        return suma;
    }

    // Transferencia segura para concurrencia
    public void transferir(int idOrigen, int idDestino, double monto) {
        Cuenta origen = cuentas.get(idOrigen);
        Cuenta destino = cuentas.get(idDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Cuenta origen o destino no existe");
        }

        // Para evitar deadlocks, siempre bloqueamos primero la cuenta de id menor.
        // Si no hiciéramos esto: Hilo A bloquea cuenta 5 y espera la 8,
        // mientras Hilo B bloquea cuenta 8 y espera la 5 -> deadlock.
        Cuenta primero = idOrigen < idDestino ? origen : destino;
        Cuenta segundo = idOrigen < idDestino ? destino : origen;

        primero.lock();
        try {
            segundo.lock();
            try {
                if (origen.getBalance() < monto) {
                    throw new IllegalArgumentException("Saldo insuficiente en cuenta " + idOrigen);
                }
                origen.setBalance(origen.getBalance() - monto);
                destino.setBalance(destino.getBalance() + monto);
            } finally {
                segundo.unlock();
            }
        } finally {
            primero.unlock();
        }
    }
}
