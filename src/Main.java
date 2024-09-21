public class Main {
    // Interfaz para los sensores (Observable)
    interface Sensor {
        void agregarObservador(Operador operador);
        void eliminarObservador(Operador operador);
        void notificarObservadores(String mensaje);
        void setMedicion(double valor);
    }

    // Clase abstracta para los diferentes tipos de sensores
    abstract class SensorBase implements Sensor {
        private List<Operador> observadores = new ArrayList<>();
        protected double valorMinimo;
        protected double valorMaximo;
        protected double medicionActual;
        protected String tipo;

        public SensorBase(double min, double max, String tipo) {
            this.valorMinimo = min;
            this.valorMaximo = max;
            this.tipo = tipo;
        }

        @Override
        public void agregarObservador(Operador operador) {
            observadores.add(operador);
        }

        @Override
        public void eliminarObservador(Operador operador) {
            observadores.remove(operador);
        }

        @Override
        public void notificarObservadores(String mensaje) {
            for (Operador operador : observadores) {
                operador.actualizar(this, mensaje);
            }
        }

        @Override
        public void setMedicion(double valor) {
            this.medicionActual = valor;
            if (valor < valorMinimo || valor > valorMaximo) {
                notificarObservadores("Alerta: " + tipo + " fuera de rango. Valor: " + valor);
            }
        }
    }

    // Implementaciones concretas de sensores
    class SensorTemperatura extends SensorBase {
        public SensorTemperatura() {
            super(0, 100, "Temperatura");
        }
    }

    class SensorPresion extends SensorBase {
        public SensorPresion() {
            super(0, 1000, "Presión");
        }
    }

    class SensorHumedad extends SensorBase {
        public SensorHumedad() {
            super(0, 100, "Humedad");
        }
    }

    class SensorCombustible extends SensorBase {
        public SensorCombustible() {
            super(0, 100, "Nivel de Combustible");
        }
    }

    // Interfaz para los operadores (Observer)
    interface Operador {
        void actualizar(Sensor sensor, String mensaje);
    }

    // Implementaciones concretas de operadores
    class OperadorSeguridad implements Operador {
        @Override
        public void actualizar(Sensor sensor, String mensaje) {
            System.out.println("Operador de Seguridad notificado: " + mensaje);
        }
    }

    class OperadorMantenimiento implements Operador {
        @Override
        public void actualizar(Sensor sensor, String mensaje) {
            System.out.println("Operador de Mantenimiento notificado: " + mensaje);
        }
    }

    class OperadorGerencia implements Operador {
        @Override
        public void actualizar(Sensor sensor, String mensaje) {
            if (mensaje.contains("crítico") || mensaje.contains("grave")) {
                System.out.println("Gerencia notificada: " + mensaje);
            }
        }
    }

    // Clase principal para demostrar el funcionamiento
    public class SistemaMonitoreo {
        public static void main(String[] args) {
            // Crear sensores
            Sensor sensorTemp = new SensorTemperatura();
            Sensor sensorPresion = new SensorPresion();
            Sensor sensorHumedad = new SensorHumedad();
            Sensor sensorCombustible = new SensorCombustible();

            // Crear operadores
            Operador seguridad = new OperadorSeguridad();
            Operador mantenimiento = new OperadorMantenimiento();
            Operador gerencia = new OperadorGerencia();

            // Suscribir operadores a sensores
            sensorTemp.agregarObservador(seguridad);
            sensorTemp.agregarObservador(gerencia);
            sensorPresion.agregarObservador(seguridad);
            sensorPresion.agregarObservador(mantenimiento);
            sensorCombustible.agregarObservador(mantenimiento);

            // Simular lecturas de sensores
            sensorTemp.setMedicion(120); // Fuera de rango
            sensorPresion.setMedicion(50); // Dentro del rango
            sensorPresion.setMedicion(-10); // Fuera de rango
            sensorCombustible.setMedicion(5); // Nivel bajo
        }
    }

}
}