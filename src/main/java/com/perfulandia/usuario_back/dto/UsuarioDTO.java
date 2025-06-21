package com.perfulandia.usuario_back.dto;

/**
 * Data Transfer Object (DTO) para representar un usuario del sistema.
 * Se utiliza para transferir datos entre capas sin exponer directamente la entidad.
 */
public class UsuarioDTO {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * RUT del usuario (Rol Único Tributario).
     */
    private String rut;

    /**
     * Correo electrónico del usuario.
     */
    private String correo;

    /**
     * Dirección de residencia del usuario.
     */
    private String direccion;

    /**
     * Estado de activación del usuario.
     */
    private boolean activo;

    /**
     * Obtiene el ID del usuario.
     *
     * @return ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id nuevo ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido nuevo apellido del usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el RUT del usuario.
     *
     * @return RUT del usuario
     */
    public String getRut() {
        return rut;
    }

    /**
     * Establece el RUT del usuario.
     *
     * @param rut nuevo RUT del usuario
     */
    public void setRut(String rut) {
        this.rut = rut;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param correo nuevo correo del usuario
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la dirección del usuario.
     *
     * @return dirección del usuario
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     *
     * @param direccion nueva dirección del usuario
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Indica si el usuario está activo.
     *
     * @return true si está activo, false si está desactivado
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de activación del usuario.
     *
     * @param activo true para activo, false para desactivado
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
