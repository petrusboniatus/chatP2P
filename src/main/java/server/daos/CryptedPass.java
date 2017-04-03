package server.daos;

import org.mongodb.morphia.annotations.Reference;

/**
 * Created by pedro on 3/04/17.
 */
public class CryptedPass {


    @Reference
    private Profile usuario;
    private String encryptedPass;

    public CryptedPass(Profile usuario, String encryptedPass) {
        this.usuario = usuario;
        this.encryptedPass = encryptedPass;
    }

    public Profile getUsuario() {
        return usuario;
    }

    public void setUsuario(Profile usuario) {
        this.usuario = usuario;
    }

    public String getEncryptedPass() {
        return encryptedPass;
    }

    public void setEncryptedPass(String encryptedPass) {
        this.encryptedPass = encryptedPass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CryptedPass that = (CryptedPass) o;

        return usuario != null ? usuario.equals(that.usuario) : that.usuario == null;
    }

    @Override
    public int hashCode() {
        return usuario != null ? usuario.hashCode() : 0;
    }
}
