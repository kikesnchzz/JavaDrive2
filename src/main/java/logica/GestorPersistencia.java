package logica;

import Excepciones.FormatoIncorrectoException;
import Excepciones.NumeroIncorrectoException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static app.Main.clientes;
import static app.Main.flota;
import static logica.GestorInformes.*;

public class GestorPersistencia {
    public static void cargarDatos() throws IllegalArgumentException {
        flota.clear();
        clientes.clear();

        try {
            leerDocumento("vehiculos.txt");
            leerDocumento("clientes.txt");
        }catch (FileNotFoundException e){
            System.out.println("Error: El Archivo no se encuentra");
        }catch (IOException e) {
            System.out.println("Error: no se puede leer");
        } catch (NumeroIncorrectoException e) {
            System.out.println("Error, en el número incorrecto de plazas de coche");
        }
        // Ya no necesitas el finally porque leerDocumento cierra cada archivo
    }

    /**
     * Método para guardar todos los datos de los documentos txt
     */
    public static void guardarDatos() {
        try {
            pw = new PrintWriter(new FileWriter("vehiculos.txt"));

            for (Vehiculo v : flota) {
                pw.println(mostrarVehiculos(v));
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error al guardar datos de los vehículos");
        }
        try {
            pw = new PrintWriter(new FileWriter("clientes.txt"));

            for (Cliente c : clientes) {
                pw.println(c.toString());
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error al guardar datos de los clientes");
        }
    }
    private static Properties props = new Properties();

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
    public static boolean probarConexion() {
        System.out.println("Intentando conectar a MariaDB...");
        try (Connection conn = obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión establecida con éxito");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión");
        }
        return false;
    }
    public static boolean insertarRegistro(Reserva r) {

        String sql = "INSERT INTO Reserva (fecha_inicio, fecha_fin, matricula, dni) VALUES (?, ?, ?, ?)";

        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(r.getFechaInicio()));
                pstmt.setDate(2, Date.valueOf(r.getFechaFin()));
                pstmt.setString(3, r.getVehiculo().getMatricula());
                pstmt.setString(4, r.getCliente().getDni());

            // Ejecutamos la inserción
            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                return true;
            }


        } catch (SQLException e) {
            System.out.println("Error en la base de datos");
        }
        return false;
    }
    public static boolean insertarFurgoneta(Furgoneta f) {

        String sql = "INSERT INTO Furgoneta (tipoFurgoneta, capacidad, matricula) VALUES (?, ?, ?)";

        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, f.isEsDeCarga() ? "carga" : "pasajeros");
            pstmt.setInt(2, f.getCapacidad());
            pstmt.setString(3, f.getMatricula());

            // Ejecutamos la inserción
            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                return true;
            }


        } catch (SQLException e) {
            System.out.println("Error en la base de datos");
        }
        return false;
    }


    public boolean insertarCoche(Coche coche) {
        try {

            String sql = "INSERT INTO Coche (matricula, tipoCoche, nPlazas) VALUES (?, ?, ?)";

            try (Connection conn = obtenerConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, coche.getMatricula());
                ps.setString(2, coche.getTipo().toString().toLowerCase());
                ps.setInt(3, coche.getPlazas());

                ps.executeUpdate();
            }

            System.out.println("✔ Coche insertado: " + coche.getMatricula());
            return true;
        } catch (SQLException e) {
            System.err.println("✘ Error al insertar coche (rollback): " + e.getMessage());
            {
                return false;
            }
        }
    }

    public boolean insertarVehiculo(Vehiculo v) {
        String sql = "INSERT INTO Vehiculo (matricula, marca, modelo, disponibilidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.isDisponible() ? "disponible" : "no disponible");
            ps.executeUpdate();
            System.out.println("✔ Vehículo insertado: " + v.getMatricula());
            return true;
        } catch (SQLException e) {
            System.err.println("✘ Error al insertar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (dni, nombre, telefono) VALUES (?, ?, ?)";
        try (Connection conn = obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.executeUpdate();
            System.out.println("✔ Cliente insertado: " + cliente.getDni());
            return true;
        } catch (SQLException e) {
            System.err.println("✘ Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }


    public static boolean eliminarReserva(String matricula, String dni, LocalDate fechaInicio) {

        String sql = "DELETE FROM Reserva WHERE matricula = ? AND dni = ? AND fecha_inicio = ?";

        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricula);
            pstmt.setString(2, dni);
            pstmt.setDate(3, Date.valueOf(fechaInicio));

            // Ejecutamos la inserción
            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos");
        }
        return false;
    }


    public boolean eliminarFurgoneta(String matricula) {
        String sql = "DELETE FROM Furgoneta WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar de tabla Furgoneta: " + e.getMessage());
            return false;
        }
        return eliminarVehiculo(matricula);
    }

    public boolean eliminarCoche(String matricula) {
        String sql = "DELETE FROM Coche WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar de tabla Coche: " + e.getMessage());
            return false;
        }
        return eliminarVehiculo(matricula);
    }

    public boolean eliminarVehiculo(String matricula) {
        String sql = "DELETE FROM Vehiculo WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Vehículo eliminado: " + matricula);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCliente(String dni) {
        String sql = "DELETE FROM Cliente WHERE dni = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Cliente eliminado: " + dni);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }


    public static int modificarReserva(String matricula, String dni, LocalDate fechaInicio, LocalDate nuevaFechaFin) {

        String sql = "UPDATE Reserva SET fecha_fin = ? WHERE matricula = ? AND dni = ? AND fecha_inicio = ?";


        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(nuevaFechaFin));
            pstmt.setString(2, matricula);
            pstmt.setString(3, dni);
            pstmt.setDate(4, Date.valueOf(fechaInicio));

            // Ejecutamos la inserción
            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                return filasActualizadas;
            }

        } catch (SQLException e) {
            System.out.println("Error en la base de datos");
        }
        return 0;
    }

    public boolean modificarFurgoneta(Furgoneta f) {
        modificarVehiculo(f);

        String sql = "UPDATE Furgoneta SET tipoFurgoneta = ?, capacidad = ? WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, f.isEsDeCarga() ? "carga" : "pasajeros");
            pstmt.setInt(2, f.getCapacidad());
            pstmt.setString(3, f.getMatricula());
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Furgoneta modificada: " + f.getMatricula());
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al modificar furgoneta: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarCoche(Coche coche) {
        // Actualizar también tabla padre
        modificarVehiculo(coche);

        String sql = "UPDATE Coche SET tipoCoche = ?, nPlazas = ? WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, coche.getTipo().toString().toLowerCase());
            pstmt.setInt(2, coche.getPlazas());
            pstmt.setString(3, coche.getMatricula());
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Coche modificado: " + coche.getMatricula());
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al modificar coche: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarVehiculo(Vehiculo v) {
        String sql = "UPDATE Vehiculo SET marca = ?, modelo = ?, disponibilidad = ? WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, v.getMarca());
            pstmt.setString(2, v.getModelo());
            pstmt.setString(3, v.isDisponible() ? "disponible" : "no disponible");
            pstmt.setString(4, v.getMatricula());
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Vehículo modificado: " + v.getMatricula());
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al modificar vehículo: " + e.getMessage());
            return false;
        }
    }



    public boolean modificarCliente(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre = ?, telefono = ? WHERE dni = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getDni());
            int filas = pstmt.executeUpdate();
            System.out.println("✔ Cliente modificado: " + cliente.getDni());
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("✘ Error al modificar cliente: " + e.getMessage());
            return false;
        }
    }


    public static Reserva buscarReserva(String matricula, String dni) {

        String sql =
                "SELECT r.fecha_inicio, r.fecha_fin, " +
                        "c.dni, c.nombre, c.telefono, " +
                        "v.matricula, v.marca, v.modelo, v.disponibilidad " +
                        "FROM Reserva r " +
                        "JOIN Cliente c ON r.dni = c.dni " +
                        "JOIN Vehiculo v ON r.matricula = v.matricula " +
                        "WHERE r.matricula = ? AND r.dni = ?";

        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricula);
            pstmt.setString(2, dni);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    Cliente cliente = new Cliente(
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("telefono")
                    );

                    Vehiculo vehiculo =
                            buscarVehiculoCompleto(rs.getString("matricula"));

                    return new Reserva(
                            cliente,
                            vehiculo,
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_fin").toLocalDate()
                    );
                }
            }
            } catch (SQLException e) {
                System.out.println("Error al buscar reserva");
            } catch (FormatoIncorrectoException e) {
                System.out.println("Error en el formato de datos");
            }

        return null;
    }
    public static Furgoneta buscarFurgonetaPorMatricula(String matricula) {
        String sql = "SELECT v.matricula, v.marca, v.modelo, v.disponibilidad, " +
                " f.tipoFurgoneta, f.capacidad " +
                " FROM Vehiculo v JOIN Furgoneta f ON v.matricula = f.matricula " +
                " WHERE v.matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean esCarga = rs.getString("tipoFurgoneta").equals("carga");
                return new Furgoneta(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("disponibilidad").equals("disponible"),
                        esCarga,
                        Integer.parseInt(rs.getString("capacidad"))
                );
            }
        } catch (SQLException e) {
            System.err.println("✘ Error al buscar furgoneta: " + e.getMessage());
        }
        return null;
    }

    public static Coche buscarCochePorMatricula(String matricula) {
        String sql = "SELECT v.matricula, v.marca, v.modelo, v.disponibilidad, " +
                "c.tipoCoche, c.nPlazas " +
                "FROM Vehiculo v JOIN Coche c ON v.matricula = c.matricula " +
                "WHERE v.matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tipoCoche tipo = tipoCoche.valueOf(
                        capitalizar(rs.getString("tipoCoche"))
                );
                return new Coche(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("disponibilidad").equals("disponible"),
                        tipo,
                        rs.getInt("nPlazas")
                );
            }
        } catch (SQLException | NumeroIncorrectoException e) {
            System.err.println("✘ Error al buscar coche: " + e.getMessage());
        }
        return null;
    }


    public ResultSet buscarVehiculoPorMatricula(String matricula) {
        String sql = "SELECT * FROM Vehiculo WHERE matricula = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricula);
            ResultSet rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.err.println("✘ Error al buscar vehículo: " + e.getMessage());
            return null;
        }
    }


    public Cliente buscarClientePorDni(String dni) {
        String sql = "SELECT * FROM Cliente WHERE dni = ?";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException | FormatoIncorrectoException e) {
            System.err.println("✘ Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public List<Reserva> consultarTodasReservas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.fecha_inicio, r.fecha_fin, " +
                "c.dni, c.nombre, c.telefono, " +
                "v.matricula " +
                "FROM Reserva r " +
                "JOIN Cliente c ON r.dni = c.dni " +
                "JOIN Vehiculo v ON r.matricula = v.matricula";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("telefono")
                    );
                    Vehiculo vehiculo = buscarVehiculoCompleto(rs.getString("matricula"));
                    lista.add(new Reserva(
                            cliente,
                            vehiculo,
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_fin").toLocalDate()
                    ));
                }
            }
        } catch (SQLException | FormatoIncorrectoException e) {
            System.err.println("✘ Error al consultar reservas: " + e.getMessage());
        }
        return lista;
    }

    public List<Furgoneta> consultarTodasFurgonetas() {
        List<Furgoneta> lista = new ArrayList<>();
        String sql = "SELECT v.matricula, v.marca, v.modelo, v.disponibilidad, " +
                "f.tipoFurgoneta, f.capacidad " +
                "FROM Vehiculo v JOIN Furgoneta f ON v.matricula = f.matricula";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean esCarga = rs.getString("tipoFurgoneta").equals("carga");
                lista.add(new Furgoneta(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("disponibilidad").equals("disponible"),
                        esCarga,
                        Integer.parseInt(rs.getString("capacidad"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("✘ Error al consultar furgonetas: " + e.getMessage());
        }
        return lista;
    }

    public List<Coche> consultarTodosCoches() {
        List<Coche> lista = new ArrayList<>();
        String sql = "SELECT v.matricula, v.marca, v.modelo, v.disponibilidad, " +
                "c.tipoCoche, c.nPlazas " +
                "FROM Vehiculo v JOIN Coche c ON v.matricula = c.matricula";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tipoCoche tipo = tipoCoche.valueOf(
                        capitalizar(rs.getString("tipoCoche"))
                );
                lista.add(new Coche(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("disponibilidad").equals("disponible"),
                        tipo,
                        rs.getInt("nPlazas")
                ));
            }
        } catch (SQLException | NumeroIncorrectoException e) {
            System.err.println("✘ Error al consultar coches: " + e.getMessage());
        }
        return lista;
    }

    public List<String[]> consultarTodosVehiculos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("disponibilidad")
                });
            }
        } catch (SQLException e) {
            System.err.println("✘ Error al consultar vehículos: " + e.getMessage());
        }
        return lista;
    }

    public List<Cliente> consultarTodosClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = obtenerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                ));
            }
        } catch (SQLException | FormatoIncorrectoException e) {
            System.err.println("✘ Error al consultar clientes: " + e.getMessage());
        }
        return lista;
    }

    private static Vehiculo buscarVehiculoCompleto(String matricula) {
        Vehiculo v = buscarCochePorMatricula(matricula);
        if (v == null) {
            v = buscarFurgonetaPorMatricula(matricula);
        }
        return v;
    }
    private static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }


























}
