package br.com.sistema.amazon;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.util.Caracter;

public class AmazonUtil {

	private int identificacao;
	private String bucketName;

	public AmazonUtil(int identificacao, String bucketName) throws MensagemException {

		if (identificacao <= 0)
			throw new MensagemException(
					Mensagens.getMensagem("Necess�rio informar a Identifica��o do cliente para manusear arquivos"));

		if (Caracter.stringIsNullOrEmpty(bucketName))
			throw new MensagemException(
					Mensagens.getMensagem("Necess�rio informar o nome do Bucket para manusear arquivos"));

		this.identificacao = identificacao;
		this.bucketName = bucketName;
	}

	private String getDiretorioS3(String identificacaoCliente, String nomeDoArquivo) {
		return identificacaoCliente + "/" + nomeDoArquivo;
	}

	public String gerarURL(String nomeArquivo) throws MensagemException {

		if (Caracter.stringIsNullOrEmpty(nomeArquivo))
			throw new MensagemException(Mensagens.getMensagem("Necessário informar o nome do arquivo"));

		return "https://" + bucketName + ".s3.amazonaws.com/" + getDiretorioS3(identificacao + "", nomeArquivo);
		/*
		 * AmazonS3 s3 = new AmazonS3Client( new
		 * ClasspathPropertiesFileCredentialsProvider()); GeneratePresignedUrlRequest
		 * generatePresignedUrlRequest = new GeneratePresignedUrlRequest( bucketName,
		 * getDiretorioS3(identificacao + "", nomeArquivo));
		 * generatePresignedUrlRequest.setMethod(HttpMethod.GET);
		 * 
		 * URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
		 * 
		 * return url;
		 */
	}

	public InputStream downloadS3(String nomeArquivo) throws IOException, MensagemException {
		if (Caracter.stringIsNullOrEmpty(nomeArquivo))

			throw new MensagemException(Mensagens.getMensagem("Necessário informar o nome do arquivo"));

		@SuppressWarnings("deprecation")
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

		S3Object object = s3
				.getObject(new GetObjectRequest(bucketName, getDiretorioS3(identificacao + "", nomeArquivo)));

		return object.getObjectContent();
	}

	public void uploadS3(String nomeArquivo, InputStream arquivo, boolean marcarArquivoComoPublico, String contentType)
			throws IOException, MensagemException {

		if (Caracter.stringIsNullOrEmpty(nomeArquivo))
			throw new MensagemException(Mensagens.getMensagem("Necessário informar o nome do arquivo"));

		@SuppressWarnings("deprecation")
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

		PutObjectRequest objeto = new PutObjectRequest(bucketName, getDiretorioS3(identificacao + "", nomeArquivo),
				arquivo, null);

		/* make public */
		if (marcarArquivoComoPublico)
			objeto.setCannedAcl(CannedAccessControlList.PublicRead);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);
		
		objeto.setMetadata(metadata);
		
		s3.putObject(objeto);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
				getDiretorioS3(identificacao + "", nomeArquivo));

		generatePresignedUrlRequest.setMethod(HttpMethod.GET);

		// URL s = s3.generatePresignedUrl(generatePresignedUrlRequest);
	}

	public void deleteS3(String nomeArquivo) throws IOException, MensagemException {

		if (Caracter.stringIsNullOrEmpty(nomeArquivo))
			throw new MensagemException(Mensagens.getMensagem("Necessário informar o nome do arquivo"));

		@SuppressWarnings("deprecation")
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());

		s3.deleteObject(new DeleteObjectRequest(bucketName, getDiretorioS3(identificacao + "", nomeArquivo)));
	}

}