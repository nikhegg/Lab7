package utils;
import core.Globals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    Serializer() {}

    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(4096);
            ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(obj);
            byte[] response = arrayOutputStream.toByteArray();
            arrayOutputStream.close();
            outputStream.close();
            return response;
        } catch(IOException e) {
            
            return null;
        }
    }

    public Request deserialize(byte[] buffer) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
            Request obj = (Request) inputStream.readObject();
            inputStream.close();
            return obj;
        } catch(ClassNotFoundException e) {
            return new Request("", Globals.getRoutesCreated());
        } catch (IOException e) {
            return null;
        }
    }
}
