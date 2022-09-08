package com.example.Reto5.controlador;

import com.example.Reto5.modelo.Producto;
import com.example.Reto5.modelo.RepositorioProducto;
import com.example.Reto5.vista.actualizar;
import com.example.Reto5.vista.inventario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;

public class ControladorProducto implements ActionListener{
    @Autowired
    RepositorioProducto repositorio;
    inventario vista;
    actualizar ventana;
    
    public ControladorProducto(RepositorioProducto repositorio, inventario vista, actualizar ventana){
        this.repositorio = repositorio;
        this.vista = vista;
        this.ventana = ventana;
        mapearEventos();
        listarProductos();
    }
    
    private void mapearEventos(){
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnInforme().addActionListener(this);
        ventana.getBtnActualizarPro().addActionListener(this);
    }
    
    private void listarProductos(){
        List<Producto> lista = (List<Producto>) repositorio.findAll();                
        JTable table = vista.getTblProductos();
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();       
        model.setRowCount(0);       
        
        for (Producto pro : lista) {            
            model.addRow(new Object[]{pro.getCodigo(),
                                      pro.getNombre(),
                                      pro.getPrecio(),
                                      pro.getInventario()});
        }     
    }

    public void agregar(){
        if (vista.getTxtNombre().getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista,"Por favor llenar el Nombre del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
        }
        else if (vista.getTxtPrecio().getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista,"Por favor llenar el Precio del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
        }
        else if (vista.getTxtInventario().getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista,"Por favor llenar el Inventario del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
        }
        else
        {            
            Producto producto = new Producto(null,
                                             vista.getTxtNombre().getText(),
                                             Double.parseDouble(vista.getTxtPrecio().getText()),
                                             Integer.parseInt(vista.getTxtInventario().getText()));
            repositorio.save(producto);
            JOptionPane.showMessageDialog(vista, "¡Producto insertado con exito!", "CONFIRMACION", JOptionPane.INFORMATION_MESSAGE);

            limpiarTextos();            
            listarProductos();
        }
    }
    
    public void eliminar(){
        JTable tablaProductos = vista.getTblProductos();
        int row = tablaProductos.getSelectedRow();
        if (row != -1){
            Integer codigo = (Integer) tablaProductos.getModel().getValueAt(row, 0);
            int Opcion = JOptionPane.showConfirmDialog(vista, "¿Esta seguro de eliminar el producto " + tablaProductos.getModel().getValueAt(row, 1) + "?", "", 0);
            if (Opcion != 1) {
                repositorio.deleteById(codigo);            
                JOptionPane.showMessageDialog(vista, "¡Producto eliminado con exito!", "CONFIRMACION", JOptionPane.INFORMATION_MESSAGE);                
            }
        }
        else
        {
            JOptionPane.showMessageDialog(vista,"Para eliminar un producto seleccionelo de la tabla. ","Producto no seleccionado",JOptionPane.ERROR_MESSAGE);            
        }
        listarProductos();
    }
    
    public void llenarCampos(){
        JTable tablaProductos = vista.getTblProductos();
        
        if (tablaProductos.getSelectedRow() != -1) {
            String nombre = (String) tablaProductos.getModel().getValueAt(tablaProductos.getSelectedRow(), 1);
            Double Precio = (Double) tablaProductos.getModel().getValueAt(tablaProductos.getSelectedRow(), 2);
            Integer Inventario = (Integer) tablaProductos.getModel().getValueAt(tablaProductos.getSelectedRow(), 3);
        
            ventana.getTxtNombreAct().setText(nombre);
            ventana.getTxtPrecioAct().setText(String.valueOf(Precio));
            ventana.getTxtInventarioAct().setText(String.valueOf(Inventario));          
            
            ventana.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(vista,"Para actualizar un producto seleccionelo primero de la tabla. ","Producto no seleccionado",JOptionPane.ERROR_MESSAGE);                        
        }
        
    }
    
    public void actualizar(){
        JTable tablaProductos = vista.getTblProductos();
        Integer codigo = (Integer) tablaProductos.getModel().getValueAt(tablaProductos.getSelectedRow(), 0);
        if (codigo == null) {
            JOptionPane.showMessageDialog(null,"Por favor seleccione un producto para actualizar sobre la tabla. ","Error",JOptionPane.ERROR_MESSAGE);            
        }
        else
        {            
            if (ventana.getTxtNombreAct().getText().isEmpty()) {
                JOptionPane.showMessageDialog(ventana,"Por favor llenar el Nombre del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
            }
            else if (ventana.getTxtPrecioAct().getText().isEmpty()) {
                JOptionPane.showMessageDialog(ventana,"Por favor llenar el Precio del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
            }
            else if (ventana.getTxtInventarioAct().getText().isEmpty()) {
                JOptionPane.showMessageDialog(ventana,"Por favor llenar el Inventario del Producto","Datos Incompletos",JOptionPane.ERROR_MESSAGE);
            }
            else
            {            
                Producto producto = new Producto(codigo, 
                                                 ventana.getTxtNombreAct().getText(),
                                                 Double.parseDouble(ventana.getTxtPrecioAct().getText()),
                                                 Integer.parseInt(ventana.getTxtInventarioAct().getText()));
                repositorio.save(producto);
                JOptionPane.showMessageDialog(ventana, "¡Producto actualizado con exito!", "CONFIRMACION", JOptionPane.INFORMATION_MESSAGE);

                limpiarTextos();
                ventana.setVisible(false);
                listarProductos();
            }
        }        
    }
    
    public void limpiarTextos(){
        vista.getTxtNombre().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtInventario().setText("");
        ventana.getTxtNombreAct().setText("");
        ventana.getTxtPrecioAct().setText("");
        ventana.getTxtInventarioAct().setText("");
        
    }
    public void generarInforme()
    {
        List<Producto> lista = (List<Producto>) repositorio.findAll();
        Double suma = 0.0;
        String nombreMayor = "";
        String nombreMenor = "";
        Double precioMayor = 0.0;
        Double precioMenor = 0.0;
        Double valorInventario = 0.0;
        
        for (Producto prod :lista) {
            if (precioMenor == 0.0) {
                precioMenor = prod.getPrecio();
                nombreMenor = prod.getNombre();
            }
            if (prod.getPrecio() > precioMayor) {
                precioMayor = prod.getPrecio();
                nombreMayor = prod.getNombre();
            }
            if (prod.getPrecio() < precioMenor) {
                precioMenor = prod.getPrecio();
                nombreMenor = prod.getNombre();
            }
            suma += prod.getPrecio();
            valorInventario += (prod.getPrecio()*prod.getInventario());
                        
        }
        Double promedio = suma / lista.size();
        
        JOptionPane.showMessageDialog(vista, "Producto precio mayor:          " + nombreMayor +
                                             "\nProducto precio menor:          " + nombreMenor +
                                             "\nPromedio precio productos:      " + String.format("%.1f",promedio) +             
                                             "\nValor Total Invetario:           " + String.format("%.1f",valorInventario)
                , "Informe Inventario", JOptionPane.INFORMATION_MESSAGE);
    }   
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnAgregar()) {
            agregar();
        }
        if (e.getSource() == vista.getBtnActualizar()) {
            llenarCampos();
        }
        if (e.getSource() == vista.getBtnBorrar()) {
            eliminar();
        }
        if (e.getSource() == vista.getBtnInforme()) {
            generarInforme();
        }
        if (e.getSource() == ventana.getBtnActualizarPro()) {       
            actualizar();
        }

    }
    
}
