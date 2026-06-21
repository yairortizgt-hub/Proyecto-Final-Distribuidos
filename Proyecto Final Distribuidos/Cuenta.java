import java.util.concurrent.locks.ReentrantLock;

public class Cuenta {

    private final int id;
    private final String propietario;
    private double balance;
    private final ReentrantLock candado = new ReentrantLock();

    public Cuenta(int id, String propietario, double balance) {
        this.id = id;
        this.propietario = propietario;
        this.balance = balance;
    }

    public void lock() {
        candado.lock();
    }

    public void unlock() {
        candado.unlock();
    }

    public int getId() {
        return id;
    }

    public String getPropietario() {
        return propietario;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Cuenta{id=" + id + ", propietario='" + propietario + "', balance=" + balance + "}";
    }
}
