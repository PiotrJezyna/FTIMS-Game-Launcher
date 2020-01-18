package fgl.drive;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import fgl.userPanel.User;
import fgl.product.Game;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DriveDao {

    private static final String APPLICATION_NAME = "FtimsGameLaucher";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final java.io.File CREDENTIALS_PATH =
            new java.io.File( System.getProperty( "user.dir" ), "credentials" );
    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    private static final List<String> SCOPES = Collections.singletonList( DriveScopes.DRIVE );

    private static FileDataStoreFactory dataStoreFactory;

    private static HttpTransport httpTransport;

    private static Drive driveService;

    private static String gamesFolderID = "1SHMY6ACYActf-07-SUD3pU10WC8yeKJ4";
    private static String usersFolderID = "1mbGOIGO1FW1jo2UI9cqgNYmE27DnK2G0";

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory( CREDENTIALS_PATH );
        } catch ( GeneralSecurityException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public DriveDao() {
        String userDir = System.getProperty( "user.dir" );

        java.io.File folder = new java.io.File( userDir, "Data" );
        if ( !folder.exists() ) {
            folder.mkdir();
        }

        folder = new java.io.File( userDir, "Data/Avatars" );
        if ( !folder.exists() ) {
            folder.mkdir();
        }

        folder = new java.io.File( userDir, "Data/Games" );
        if ( !folder.exists() ) {
            folder.mkdir();
        }

        folder = new java.io.File( userDir, "Data/Temp" );
        if ( !folder.exists() ) {
            folder.mkdir();
        }

        try {
            getDriveService();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public Credential getCredentials() throws IOException {

        java.io.File clientSecretFilePath = new java.io.File( CREDENTIALS_PATH, CLIENT_SECRET_FILE_NAME );

        if ( !clientSecretFilePath.exists() ) {
            throw new FileNotFoundException( "Please copy " + CLIENT_SECRET_FILE_NAME  +
                    " to folder: " + CREDENTIALS_PATH.getAbsolutePath() );
        }

        InputStream in = new FileInputStream( clientSecretFilePath );

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, new InputStreamReader( in ) );

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder( httpTransport, JSON_FACTORY,
                clientSecrets, SCOPES ).setDataStoreFactory( dataStoreFactory ).setAccessType( "offline" ).build();
        Credential credential = new AuthorizationCodeInstalledApp( flow, new LocalServerReceiver() ).authorize( "user" );

        return credential;
    }

    public void getDriveService() throws IOException {

        if ( driveService != null ) {
            return;
        }

        Credential credential = getCredentials();

        driveService = new Drive.Builder( httpTransport, JSON_FACTORY, credential ).setApplicationName( APPLICATION_NAME ).build();
    }

    public boolean uploadGame( Game game, String path ) throws IOException {

        File folder = createFolderOnDisk( game.getTitle(), gamesFolderID );

        return createFileOnDisk( game.getTitle() + "Zip.zip", "aplication/zip", path, folder.getId() ) != null;
    }

    public boolean updateGame( Game game, String path ) throws IOException {

        File folder = findFileOnDisk( game.getTitle() );
        File file = findFileOnDisk( game.getTitle() + "Zip.zip" );

        if ( file != null ) {
            driveService.files().delete( file.getId() ).execute();
        }

        return createFileOnDisk( game.getTitle() + "Zip.zip", "aplication/zip", path, folder.getId() ) != null;
    }

    public boolean downloadGame( Game game ) throws IOException {

        File file = findFileOnDisk( game.getTitle() + "Zip.zip" );

        ByteArrayOutputStream downloadedData = new ByteArrayOutputStream();
        driveService.files().get( file.getId() ).executeMediaAndDownloadTo( downloadedData );

        try ( FileOutputStream newFile = new FileOutputStream( "Data/Temp/" + game.getTitle() + ".zip" ) ) {
            downloadedData.writeTo( newFile );
        }

        java.io.File archive = new java.io.File( "Data/Temp/" + game.getTitle() + ".zip" );
        String destination = "Data/Games/";

        try {
            ZipFile zipFile = new ZipFile( archive );
            zipFile.extractAll( destination );
        } catch ( ZipException e ) {
            e.printStackTrace();
            return false;
        }

        archive.delete();

        return true;
    }

    public boolean uploadAvatar( User user, String path ) throws IOException {

        File folder;
        if ( ( folder = findFileOnDisk( user.getUsername() ) ) == null ) {
            folder = createFolderOnDisk( user.getUsername(), usersFolderID );
        } else {
            File file;
            if ( ( file = findFileOnDisk( user.getUsername() + "Avatar.png" ) ) != null ) {
                driveService.files().delete( file.getId() ).execute();
            }
        }

        return createFileOnDisk( user.getUsername() + "Avatar.png", "image/png", path, folder.getId() ) != null;
    }

    public java.io.File downloadAvatar( User user ) throws IOException {

        File file;
        if ( ( file = findFileOnDisk( user.getUsername() + "Avatar.png" ) ) == null ) {
            file = findFileOnDisk( "DefaultAvatar.png" );
        }

        ByteArrayOutputStream downloadedData = new ByteArrayOutputStream();
        driveService.files().get( file.getId() ).executeMediaAndDownloadTo( downloadedData );

        try ( FileOutputStream newFile = new FileOutputStream( "Data/Avatars/" + user.getUsername() + "Avatar.png" ) ) {
            downloadedData.writeTo( newFile );
        }

        java.io.File avatar = new java.io.File( "Data/Avatars/" + user.getUsername() + "Avatar.png" );

        return avatar;
    }

    private File findFileOnDisk( String name ) throws IOException {
        String query = " name = '" + name + "' ";

        FileList result = driveService.files().list().setQ( query ).setFields( "nextPageToken, files(id, name)" ).execute();

        List<File> files = result.getFiles();
        if ( files == null || files.isEmpty() ) {
            return null;
        }

        return files.get( 0 );
    }

    private File createFolderOnDisk( String name, String parentID ) throws IOException {
        File folderMetadata = new File();

        folderMetadata.setName( name );
        folderMetadata.setMimeType( "application/vnd.google-apps.folder" );

        List<String> folderParents = Arrays.asList( parentID );
        folderMetadata.setParents( folderParents );

        return driveService.files().create( folderMetadata ).setFields( "id, name" ).execute();
    }

    private File createFileOnDisk( String name, String type, String path, String parentID ) throws IOException {

        java.io.File uploadFile = new java.io.File( path );

        AbstractInputStreamContent uploadStreamContent = new FileContent( type, uploadFile );

        File fileMetadata = new File();
        fileMetadata.setName( name );

        List<String> fileParents = Arrays.asList( parentID );
        fileMetadata.setParents( fileParents );

        File file = driveService.files().create( fileMetadata, uploadStreamContent ).setFields( "id, webContentLink, webViewLink, parents, md5Checksum" ).execute();

        if ( calculateChecksum( uploadFile ).equals( file.getMd5Checksum() ) ) {
            System.out.println( "Equals" );
        } else {
            System.out.println( "Something went wrong, try again" );
            driveService.files().delete( file.getId() ).execute();
            return null;
        }

        return file;
    }

    private String calculateChecksum( java.io.File file ) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
            DigestInputStream digestInputStream = new DigestInputStream( new FileInputStream( file ), messageDigest );

            byte[] buffer = new byte[2048];
            while ( digestInputStream.read( buffer ) > -1 ) {
            }

            digestInputStream.close();
            byte[] md5 = messageDigest.digest();

            StringBuilder md5String = new StringBuilder();
            for ( byte b : md5 ) {
                md5String.append( String.format( "%02X", b ) );
            }

            return md5String.toString().toLowerCase();
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return null;
    }
}
