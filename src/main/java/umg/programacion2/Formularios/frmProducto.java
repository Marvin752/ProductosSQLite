package umg.programacion2.Formularios;

import umg.programacion2.DataBase.Model.ProductoModel;
import umg.programacion2.DataBase.Service.ProductoService;
import umg.programacion2.Reportes.PdfReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmProducto extends JFrame {
    private JPanel panel1;
    private JLabel lblTitulo;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblOrigen;
    private JTextField textFieldCodigo;
    private JTextField textFieldNombre;
    private JComboBox<String> comboBoxOrigen;
    private JButton buttonBuscar;
    private JButton buttonGuardar;
    private JButton buttonActualizar;
    private JButton buttonBorrar;
    private JButton buttonPDF;
    private JLabel lblPrecio;
    private JLabel lblExistencia;
    private JTextField textFieldExistencia;
    private JTextField textFieldPrecio;
    private JLabel lblReportes; // Asegúrate de que este JLabel exista en el formulario
    private JComboBox<String> comboBoxReportes; // Especifica el tipo genérico para evitar advertencias
    private JCheckBox checkBoxAgrupar;

    private ProductoService productoService;

    public frmProducto() {
        // Instanciar el servicio de Producto
        productoService = new ProductoService();

        // Configuración del JFrame
        setContentPane(panel1);
        setTitle("Gestión de Productos");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir las opciones en un array
        String[] opcionesOrigen = {
                "Estados Unidos", "Corea del Sur", "Vietnam", "Colombia", "Italia",
                "Japón", "Suiza", "México", "España", "Argentina",
                "Noruega", "Suecia", "Francia", "Irlanda", "China",
                "Alemania", "Escocia"
        };

        // Agregar opciones al comboBox (Origen)
        for (String opcion : opcionesOrigen) {
            comboBoxOrigen.addItem(opcion);
        }

        // Definir las opciones en un array
        String[] opcionesReportes = {
                "Reporte General",
                "Existencias menores a 20 unidades",
                "Origen del producto",
                "Precios mayores a 2000",
                "Ordenar de precio mayor a menor",
                "Ordenar de precio menor a mayor"
        };

        // Agregar las opciones al comboBox en un bucle
        for (String opcion : opcionesReportes) {
            comboBoxReportes.addItem(opcion);
        }

        // Acción del botón Guardar
        buttonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Guardar producto
                String descripcion = textFieldNombre.getText();
                String origen = comboBoxOrigen.getSelectedItem().toString();
                int existencia = Integer.parseInt(textFieldExistencia.getText());
                double precio = Double.parseDouble(textFieldPrecio.getText());

                ProductoModel producto = new ProductoModel();
                producto.setDescripcion(descripcion);
                producto.setOrigen(origen);
                producto.setExistencia(existencia);
                producto.setPrecio(precio);

                try {
                    productoService.agregarProducto(producto.getDescripcion(), producto.getOrigen(), producto.getPrecio(), producto.getExistencia());
                    JOptionPane.showMessageDialog(null, "Producto guardado exitosamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + ex.getMessage());
                }
            }
        });

        // Acción del botón Buscar
        buttonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Buscar producto por ID
                try {
                    int idProducto = Integer.parseInt(textFieldCodigo.getText());
                    ProductoModel producto = productoService.obtenerProductoPorId(idProducto);

                    if (producto != null) {
                        textFieldNombre.setText(producto.getDescripcion());
                        comboBoxOrigen.setSelectedItem(producto.getOrigen());
                        textFieldExistencia.setText(String.valueOf(producto.getExistencia()));
                        textFieldPrecio.setText(String.valueOf(producto.getPrecio()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Código inválido");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + ex.getMessage());
                }
            }
        });

        // Acción del botón Actualizar
        buttonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar producto
                try {
                    int idProducto = Integer.parseInt(textFieldCodigo.getText());
                    String descripcion = textFieldNombre.getText();
                    String origen = comboBoxOrigen.getSelectedItem().toString();
                    int existencia = Integer.parseInt(textFieldExistencia.getText());
                    double precio = Double.parseDouble(textFieldPrecio.getText());

                    ProductoModel producto = new ProductoModel();
                    producto.setIdProducto(idProducto);
                    producto.setDescripcion(descripcion);
                    producto.setOrigen(origen);
                    producto.setExistencia(existencia);
                    producto.setPrecio(precio);

                    productoService.actualizarProducto(producto.getIdProducto(), producto.getDescripcion(), producto.getOrigen(), producto.getPrecio(), producto.getExistencia());
                    JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Código o valores inválidos");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage());
                }
            }
        });

        // Acción del botón Borrar
        buttonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Borrar producto
                try {
                    int idProducto = Integer.parseInt(textFieldCodigo.getText());
                    ProductoModel producto = productoService.obtenerProductoPorId(idProducto);
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este producto?", "Confirmación", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (producto.getPrecio() == 0) {
                            productoService.eliminarProducto(idProducto);
                            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente");
                            limpiarCampos();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: no se puede eliminar un producto si su precio no es de Q0.00");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Código inválido");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
                }
            }
        });

        // Acción del botón Generar PDF
        buttonPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean agrupados = checkBoxAgrupar.isSelected();
                    int reporteSeleccionado = comboBoxReportes.getSelectedIndex();
                    List<ProductoModel> productos;

                    // Si agrupar está seleccionado pero no es el reporte "Reporte General" (índice 0)
                    if (agrupados && reporteSeleccionado != 0) {
                        JOptionPane.showMessageDialog(null, "La función 'Agrupar' solo funciona con 'Reporte General'.");
                        return; // Detener la ejecución si la condición no se cumple
                    }

                    switch (reporteSeleccionado) {
                        case 0:
                            // Acción para "Reporte General"
                            if (agrupados) {
                                productos = productoService.obtenerTodosLosProductos();
                            } else {
                                productos = productoService.obtenerTodosLosProductosID();
                            }
                            break;
                        case 1:
                            // Acción para "Existencias menores a 20 unidades"
                            productos = productoService.obtenerGenericos("existencia < 20");
                            break;
                        case 2:
                            // Acción para productos de un país específico
                            String pais = comboBoxOrigen.getSelectedItem().toString();
                            productos = productoService.obtenerGenericos("origen = '" + pais + "'");
                            break;
                        case 3:
                            // Acción para "Precios mayores a 2000"
                            productos = productoService.obtenerGenericos("precio > 2000");
                            break;
                        case 4:
                            // Acción para "Ordenar de precio mayor a menor"
                            productos = productoService.obtenerGenericos("1=1 ORDER BY precio DESC");
                            break;
                        case 5:
                            // Acción para "Ordenar de precio menor a mayor"
                            productos = productoService.obtenerGenericos("1=1 ORDER BY precio ASC");
                            break;
                        default:
                            productos = null;
                    }

                    // Generar el reporte si hay productos
                    if (productos != null) {
                        new PdfReport().generateProductReport(productos, "C:\\PdfProgra\\reporte.pdf", agrupados);
                        JOptionPane.showMessageDialog(null, "Reporte generado en C:\\PdfProgra");
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay productos para generar el reporte.");
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + exception.getMessage());
                }
            }
        });

    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        textFieldCodigo.setText("");
        textFieldNombre.setText("");
        textFieldExistencia.setText("");
        textFieldPrecio.setText("");
        comboBoxOrigen.setSelectedIndex(0);
        comboBoxReportes.setSelectedIndex(0); // Resetear comboBox de reportes
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmProducto frame = new frmProducto();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}
