package tp1.server.javas;

import tp1.api.service.util.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class JavaFiles {

    private static Logger Log = Logger.getLogger(JavaFiles.class.getName());

    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        try {
            File file = new File(fileId);
            if(!file.exists()) file.createNewFile();

            FileOutputStream oFile = new FileOutputStream(file);
            oFile.write(data);
            oFile.close();

            Log.info("PATHHHH ONDE O  FICHEIRO ESTA GUARDADOOOOO " + file.getCanonicalPath());

            return Result.ok();
        } catch (IOException e) {
            System.out.println("An error occurred writing in the file");
            e.printStackTrace();
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }


    public Result<Void> deleteFile(String fileId, String token) {
        File file = new File(fileId);
        if (!file.isFile()) return Result.error(Result.ErrorCode.NOT_FOUND);

        if (file.delete())
            return Result.ok();
        else {
            // pode haver outros resultados
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
    }


    public Result<byte[]> getFile(String fileId, String token){


        File file = new File(fileId);

        if (!file.isFile()) return Result.error(Result.ErrorCode.NOT_FOUND);
        byte[] bFile = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bFile);
            fileInputStream.close();

            return Result.ok(bFile);

        } catch (IOException e) {
            System.out.println("An error occurred getting the file");
            //e.printStackTrace();
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }
    }
}