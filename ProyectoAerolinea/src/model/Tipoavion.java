/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author maria
 */
@Entity
@Table(name = "tipoavion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoavion.findAll", query = "SELECT t FROM Tipoavion t")
    , @NamedQuery(name = "Tipoavion.findByIdtipoAvion", query = "SELECT t FROM Tipoavion t WHERE t.idtipoAvion = :idtipoAvion")
    , @NamedQuery(name = "Tipoavion.findByFilas", query = "SELECT t FROM Tipoavion t WHERE t.filas = :filas")
    , @NamedQuery(name = "Tipoavion.findByColumnas", query = "SELECT t FROM Tipoavion t WHERE t.columnas = :columnas")
    , @NamedQuery(name = "Tipoavion.findByCantasientos", query = "SELECT t FROM Tipoavion t WHERE t.cantasientos = :cantasientos")
    , @NamedQuery(name = "Tipoavion.findByMarca", query = "SELECT t FROM Tipoavion t WHERE t.marca = :marca")
    , @NamedQuery(name = "Tipoavion.findByModelo", query = "SELECT t FROM Tipoavion t WHERE t.modelo = :modelo")
    , @NamedQuery(name = "Tipoavion.findByCapacidadpasajeros", query = "SELECT t FROM Tipoavion t WHERE t.capacidadpasajeros = :capacidadpasajeros")})
public class Tipoavion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idtipoAvion")
    private Integer idtipoAvion;
    @Basic(optional = false)
    @Column(name = "filas")
    private int filas;
    @Basic(optional = false)
    @Column(name = "columnas")
    private int columnas;
    @Basic(optional = false)
    @Column(name = "cantasientos")
    private int cantasientos;
    @Basic(optional = false)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @Column(name = "modelo")
    private String modelo;
    @Basic(optional = false)
    @Column(name = "capacidadpasajeros")
    private String capacidadpasajeros;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAvionidtipoAvion")
    private List<Avion> avionList;

    public Tipoavion() {
    }

    public Tipoavion(Integer idtipoAvion) {
        this.idtipoAvion = idtipoAvion;
    }

    public Tipoavion(Integer idtipoAvion, int filas, int columnas, int cantasientos, String marca, String modelo, String capacidadpasajeros) {
        this.idtipoAvion = idtipoAvion;
        this.filas = filas;
        this.columnas = columnas;
        this.cantasientos = cantasientos;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidadpasajeros = capacidadpasajeros;
    }

    public Integer getIdtipoAvion() {
        return idtipoAvion;
    }

    public void setIdtipoAvion(Integer idtipoAvion) {
        this.idtipoAvion = idtipoAvion;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getCantasientos() {
        return cantasientos;
    }

    public void setCantasientos(int cantasientos) {
        this.cantasientos = cantasientos;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCapacidadpasajeros() {
        return capacidadpasajeros;
    }

    public void setCapacidadpasajeros(String capacidadpasajeros) {
        this.capacidadpasajeros = capacidadpasajeros;
    }

    @XmlTransient
    public List<Avion> getAvionList() {
        return avionList;
    }

    public void setAvionList(List<Avion> avionList) {
        this.avionList = avionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoAvion != null ? idtipoAvion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoavion)) {
            return false;
        }
        Tipoavion other = (Tipoavion) object;
        if ((this.idtipoAvion == null && other.idtipoAvion != null) || (this.idtipoAvion != null && !this.idtipoAvion.equals(other.idtipoAvion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Tipoavion[ idtipoAvion=" + idtipoAvion + " ]";
    }
    
}
