package application;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class SocketFactory {

	

	static SSLSocketFactory getSocketFactory(final String caFile,final String crtFile, final String keyFile, final String pwd)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		// load CA certificate
		X509Certificate caCert = null;

		FileInputStream fileInputstream = new FileInputStream(caFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputstream);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		while (bufferedInputStream.available() > 0) {
			caCert = (X509Certificate) cf.generateCertificate(bufferedInputStream);
		}

		// load client certificate
		bufferedInputStream = new BufferedInputStream(new FileInputStream(crtFile));
		X509Certificate clientCert = null;
		while (bufferedInputStream.available() > 0) {
			clientCert = (X509Certificate) cf.generateCertificate(bufferedInputStream);
		}

		// load client private key
		PEMParser pemParser = new PEMParser(new FileReader(keyFile));
		Object object = pemParser.readObject();
		PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
				.build(pwd.toCharArray());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
				.setProvider("BC");
		KeyPair key;
		if (object instanceof PEMEncryptedKeyPair) {
			System.out.println("Encrypted key - we will use provided password");
			key = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
		} else {
			System.out.println("Unencrypted key - no password needed");
			key = converter.getKeyPair((PEMKeyPair) object);
		}
		pemParser.close();

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", clientCert);
		ks.setKeyEntry("private-key", key.getPrivate(), pwd.toCharArray(),
				new java.security.cert.Certificate[] { clientCert });
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, pwd.toCharArray());

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("tlsv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}

}