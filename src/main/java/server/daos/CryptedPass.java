package server.daos;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by pedro on 3/04/17.
 */
public class CryptedPass {


    @Id
    private String usuario;
    private String encryptedPass;

    public CryptedPass() {}

    public CryptedPass(String usuario, String encryptedPass) {
        this.usuario = usuario;
        this.encryptedPass = encryptedPass;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
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

        if (usuario != null ? !usuario.equals(that.usuario) : that.usuario != null) return false;
        return encryptedPass != null ? encryptedPass.equals(that.encryptedPass) : that.encryptedPass == null;
    }

    @Override
    public int hashCode() {
        int result = usuario != null ? usuario.hashCode() : 0;
        result = 31 * result + (encryptedPass != null ? encryptedPass.hashCode() : 0);
        return result;
    }
}
