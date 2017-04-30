package server.remote;

import api.IServer;
import server.utils.RandomString;

/**
 * Created by pedro on 4/04/17.
 */
public class AuthToken implements IServer.IAuthToken{

    private String nombreUsuario;
    private String randValue;


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRandValue() {
        return randValue;
    }


    AuthToken(IServer.IProfile perfil, String randValue ){
        nombreUsuario = perfil.getName();
        this.randValue = randValue;
    }

    AuthToken(String nombreUsuario, String randValue) {
        this.nombreUsuario = nombreUsuario;
        this.randValue = randValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken = (AuthToken) o;

        if (nombreUsuario != null ? !nombreUsuario.equals(authToken.nombreUsuario) : authToken.nombreUsuario != null)
            return false;
        return randValue != null ? randValue.equals(authToken.randValue) : authToken.randValue == null;
    }

    @Override
    public int hashCode() {
        int result = nombreUsuario != null ? nombreUsuario.hashCode() : 0;
        result = 31 * result + (randValue != null ? randValue.hashCode() : 0);
        return result;
    }
}
