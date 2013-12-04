/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo.Peliculas;

import Modelo.Valoraciones.Valoracion;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.*;

/**
 *
 * @author jose
 */
@Entity(name="Peliculas")
public class Pelicula implements Serializable {    
    
    @Id
    @GeneratedValue
    private long ID;
    private String titulo;
    private int anio;
    private String descripcion;
    private String portada;
    private String trailer;
    @OneToMany(cascade=CascadeType.ALL)
    private Map<Long,Valoracion> valoraciones;
    
    public Pelicula(){
    }

    public Pelicula(long id, int anio, String titulo, String descripcion, String portada, String trailer, Map<Long,Valoracion> valoraciones) {        
        this.ID = id;
        this.anio = anio;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.portada = portada;
        this.trailer = trailer;
        this.valoraciones = valoraciones;
    }
    
     public Pelicula(long id, int anio, String titulo, String descripcion, String portada, String trailer) {        
        this.ID = id;
        this.anio = anio;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.portada = portada;
        this.trailer = trailer;
    }
     
    public Pelicula(Pelicula p){
        this.ID = p.getID();
        this.anio = p.getAnio();
        this.descripcion = p.getDescripcion();
        this.portada = p.getPortada();
        this.trailer = p.getTrailer();
    }

    public long getID() {
        return ID;
    }
    
    public int getAnio(){
        return anio;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPortada() {
        return portada;
    }

    public String getTrailer() {
        return trailer;
    }

    public Map<Long,Valoracion> getValoraciones() {
        return valoraciones;
    }
    
    
    
}
