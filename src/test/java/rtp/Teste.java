package rtp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

public class Teste {
	
	public static void main(String[] args) {
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://localhost:8080/rsp/apiv1/post");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("t", "teste post a partir de um programa java que não tem nada a ver com o rsp, mas pode gerar conteudo", ContentType.create("text/plain", Consts.UTF_8));

			builder.addTextBody("ln", "FATO 123");
			builder.addTextBody("ln", "DP990020");

			
			//Path path = Paths.get("C:\\Users\\daniel-tavares\\Desktop\\465615012200657.jpg");
			//byte[] data = Files.readAllBytes(path);
	
			//builder.addBinaryBody("pi", data, ContentType.create("image/jpeg"), "teste.jpg");
			//builder.addBinaryBody("pi", data, ContentType.create("image/jpeg"), "teste.jpg");
			
			HttpEntity multipart = builder.build();
			
			uploadFile.setEntity(multipart);
			uploadFile.addHeader("Authorization", "Basic "+ new String(Base64.encodeBase64("OCR:OCR".getBytes())) );
		
			HttpResponse response = httpClient.execute(uploadFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
