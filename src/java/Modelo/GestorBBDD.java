/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

import Modelo.Peliculas.ConjuntoPeliculas;
import Modelo.Peliculas.Pelicula;
import Modelo.Usuarios.ConjuntoUsuarios;
import Modelo.Usuarios.Usuario;
import Modelo.Valoraciones.ConjuntoValoraciones;
import Modelo.Valoraciones.Valoracion;
import Persistencia.GestorPersistencia;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author jose
 */
public class GestorBBDD {
    
    GestorPersistencia gpersistencia = null;
    
    public GestorBBDD (){
        try{
            GestorPersistencia.newConexion();
            gpersistencia=GestorPersistencia.instancia();
        }catch(Exception e){
            System.out.println("ERROR al abrir base de datos: "+e.getMessage());
        }
    }
    
    public GestorPersistencia getGestorPersistencia(){
        return gpersistencia;
    }
    
    //GESTIÓN DE PELÍCULAS
    public static ConjuntoPeliculas selectPeliculas(GestorPersistencia gp){
        Query consulta = gp.getEntityManager().createQuery("SELECT p FROM Peliculas p");
        ConjuntoPeliculas peliculas = new ConjuntoPeliculas((ArrayList)consulta.getResultList());
        return peliculas;
    }
    
    public ConjuntoPeliculas selectIntervaloPeliculas(int ini, int fin){
        Query consulta = gpersistencia.getEntityManager().createQuery("SELECT * FROM Pelicula ORDER BY ID DESC LIMIT "+fin);
        ConjuntoPeliculas peliculas = new ConjuntoPeliculas((ArrayList)consulta.getResultList());
        return peliculas;
    }
   
    
    public static Pelicula selectPeliculaById(GestorPersistencia gp, long idPelicula){
        Pelicula peli;
        Query consulta=GestorPersistencia.instancia().getEntityManager().createQuery("SELECT p FROM Peliculas p WHERE p.ID="+idPelicula);
        peli=(Pelicula) consulta.getSingleResult();
        return peli;
    }
    
    public int getNumeroPeliculas(GestorPersistencia gp){
        Query consulta = gp.getEntityManager().createQuery("SELECT COUNT(*) FROM Pelicula");
        return (Integer) consulta.getSingleResult();
    }
    
    public static ArrayList<Pelicula> getPeliculasIntervalo(GestorPersistencia gp, int min, int max){
        ArrayList<Pelicula> pelis;
        Query consulta=GestorPersistencia.instancia().getEntityManager().createQuery("SELECT p FROM Peliculas p WHERE (p.ID>"+min+" AND p.ID<="+max+")");
        pelis=(ArrayList<Pelicula>)consulta.getResultList();
        return pelis;
    }
     //GESTIÓN DE USUARIOS
    public static Usuario selectUsuarioById(GestorPersistencia gp, Long idUsuario){
         Query consulta =gp.getEntityManager().createQuery("SELECT u FROM Usuarios u WHERE u.ID = "+idUsuario);
         Usuario u = (Usuario)consulta.getSingleResult();
         return u;
    }
    
    public ConjuntoUsuarios selectUsuarios(GestorPersistencia gp){
        Query consulta = gp.getEntityManager().createQuery("SELECT * FROM Usuario");
        ConjuntoUsuarios usuarios = new ConjuntoUsuarios((ArrayList)consulta.getResultList());
        return usuarios;
    }
    

    public static Usuario selecUsuarioByNick(GestorPersistencia gp, String nickuser){
        Query consulta = gp.getEntityManager().createQuery("SELECT u FROM Usuarios u WHERE u.nick = :nickuser").setParameter("nickuser", nickuser);
        Usuario usuario;
        usuario=(Usuario)consulta.getSingleResult();
        
        return usuario;
    }
 
    //GESTIÓN DE VALORACIONES
    public Valoracion selectValoracionById(GestorPersistencia gp, Valoracion idValoracion){
        return gp.getEntityManager().find(Valoracion.class, idValoracion);
    }
    
    public ConjuntoValoraciones getValoracionesByPelicula(GestorPersistencia gp, int idPelicula){
        ConjuntoValoraciones valoraciones = null;
        try{
            Query consulta = gp.getEntityManager().createQuery("SELECT * FROM Valoracion WHERE idPelicula = "+idPelicula);
            valoraciones = new ConjuntoValoraciones((ArrayList)consulta.getResultList());
        }catch(Exception e){
            System.out.println("ERROR: al devolver las valoraciones de la película. "+e.getMessage());
        }
        return valoraciones;
    }
    
    public static ArrayList<Valoracion>selectValoracionesByUsuario(GestorPersistencia gp, long idUsuario){
        ArrayList<Valoracion> valoraciones;
        Query consulta=gp.getEntityManager().createQuery("SELECT v FROM Valoraciones v WHERE v.idUsuario = :idUsuario").setParameter("idUsuario", idUsuario);
        valoraciones=(ArrayList<Valoracion>)consulta.getResultList();
        return valoraciones;
    }
    
    public float getNotaPelicula(GestorPersistencia gp, int idPelicula){
        ConjuntoValoraciones valoraciones;
        valoraciones = getValoracionesByPelicula(gp, idPelicula);
        float nota = 0;
        
        for(int i = 0;i<valoraciones.getListValoraciones().size();i++){
           nota = nota + (float)valoraciones.getListValoraciones().get(i).getNota();
        }
        
        return (float)nota/(float)valoraciones.getListValoraciones().size();
    }
    //GESTION DE RECOMENDACIONES
    public static ArrayList<Pelicula> getRecomendaciones(Usuario usu,GestorPersistencia gp){
        
        SerializarModeloSimilitud deserializar = new SerializarModeloSimilitud();                
        
        SerializarModeloSimilitud ms;
        Algoritmos al = new Algoritmos();
        ArrayList<Recomendacion> recom_list = new ArrayList();
        ArrayList<Pelicula> recom_pelis = new ArrayList();
        
        
        try {
            
            HashMap<Long, TreeSet<ItemSim>> modeloS = deserializar.deserializar("C:\\Users\\Marci\\Documents\\NetBeansProjects\\Byottafilms\\src\\java\\Recursos\\30-Coseno").getModeloSimilitud();
            recom_list = al.getRecomendaciones(usu, modeloS, gp);
            
            for(int i=0;i<recom_list.size();i++){
                recom_pelis.add(recom_list.get(i).getPelicula());
            }
            
        } catch (IOException ex) {
            
        } catch (ClassNotFoundException ex) {
            
        }
        return recom_pelis;
    }

}
