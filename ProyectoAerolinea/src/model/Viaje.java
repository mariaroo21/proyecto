/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maria
 */
@Entity
@Table(name = "viaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viaje.findAll", query = "SELECT v FROM Viaje v")
    , @NamedQuery(name = "Viaje.findByIdViaje", query = "SELECT v FROM Viaje v WHERE v.idViaje = :idViaje")
    , @NamedQuery(name = "Viaje.findByFechasalida", query = "SELECT v FROM Viaje v WHERE v.fechasalida = :fechasalida")
    , @NamedQuery(name = "Viaje.findByFechallegada", query = "SELECT v FROM Viaje v WHERE v.fechallegada = :fechallegada")
    , @NamedQuery(name = "Viaje.findByCuposdispo", query = "SELECT v FROM Viaje v WHERE v.cuposdispo = :cuposdispo")
    , @NamedQuery(name = "Viaje.findByIdayvuelta", query = "SELECT v FROM Viaje v WHERE v.idayvuelta = :idayvuelta")})
public class Viaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idViaje")
    private Integer idViaje;
    @Basic(optional = false)
    @Column(name = "fechasalida")
    private String fechasalida;
    @Basic(optional = false)
    @Column(name = "fechallegada")
    private String fechallegada;
    @Basic(optional = false)
    @Column(name = "cuposdispo")
    private float cuposdispo;
    @Basic(optional = false)
    @Column(name = "idayvuelta")
    private String idayvuelta;
    @JoinColumn(name = "Vuelo_idVuelo", referencedColumnName = "idVuelo")
    @ManyToOne(optional = false)
    private Vuelo vueloidVuelo;

    public Viaje() {
    }

    public Viaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public Viaje(Integer idViaje, String fechasalida, String fechallegada, float cuposdispo, String idayvuelta) {
        this.idViaje = idViaje;
        this.fechasalida = fechasalida;
        this.fechallegada = fechallegada;
        this.cuposdispo = cuposdispo;
        this.idayvuelta = idayvuelta;
    }

    public Integer getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public String getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(String fechasalida) {
        this.fechasalida = fechasalida;
    }

    public String getFechallegada() {
        return fechallegada;
    }

    public void setFechallegada(String fechallegada) {
        this.fechallegada = fechallegada;
    }

    public float getCuposdispo() {
        return cuposdispo;
    }

    public void setCuposdispo(float cuposdispo) {
        this.cuposdispo = cuposdispo;
    }

    public String getIdayvuelta() {
        return idayvuelta;
    }

    public void setIdayvuelta(String idayvuelta) {
        this.idayvuelta = idayvuelta;
    }

    public Vuelo getVueloidVuelo() {
        return vueloidVuelo;
    }

    public void setVueloidVuelo(Vuelo vueloidVuelo) {
        this.vueloidVuelo = vueloidVuelo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idViaje != null ? idViaje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Viaje)) {
            return false;
        }
        Viaje other = (Viaje) object;
        if ((this.idViaje == null && other.idViaje != null) || (this.idViaje != null && !this.idViaje.equals(other.idViaje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Viaje[ idViaje=" + idViaje + " ]";
    }
    
}
