package client;

import api.IServer;

import java.io.Serializable;

/**
 * Created by cout970 on 4/30/17.
 */
public class ClientFile implements Serializable {

    private String user;
    private IServer.IAuthToken token;
    private String fileName;
    private byte[] fileContents;

    public ClientFile(String user, IServer.IAuthToken token, String fileName, byte[] fileContents) {
        this.user = user;
        this.token = token;
        this.fileName = fileName;
        this.fileContents = fileContents;
    }

    public String getUser() {
        return user;
    }

    public IServer.IAuthToken getToken() {
        return token;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileContents() {
        return fileContents;
    }

    public boolean check(IServer.IAuthToken token) {
        return this.token.equals(token);
    }
}
